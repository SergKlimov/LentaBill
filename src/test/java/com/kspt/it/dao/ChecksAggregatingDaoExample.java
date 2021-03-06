package com.kspt.it.dao;

import com.avaje.ebean.EbeanServer;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.kspt.it.GuiceModule;
import com.kspt.it.Tuple2;
import com.kspt.it.dao.aggregation.ByDateAndStoreAggregationDAO;
import com.kspt.it.dao.aggregation.ByDateAndStoreAggregationDAO.ByDateAndStoreAggregationEntry;
import com.kspt.it.persist.data.CashMachineEntry;
import com.kspt.it.persist.data.CheckEntry;
import com.kspt.it.persist.data.ProductEntry;
import com.kspt.it.persist.data.StoreEntry;
import com.kspt.it.persist.olap.dimensions.DateDimensions;
import com.kspt.it.persist.olap.dimensions.SupplierDimensions;
import com.kspt.it.persist.olap.facts.CheckFactEntry;
import com.kspt.it.persist.olap.facts.ProductFactEntry;
import static java.lang.System.currentTimeMillis;
import static java.time.ZoneId.systemDefault;
import org.junit.Before;
import org.junit.Test;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ChecksAggregatingDaoExample {
  private EbeanServer ebean;

  @Before
  public void setUp() {
    ebean = Guice.createInjector(new GuiceModule()).getInstance(EbeanServer.class);
    createSupplierDimensions();
    createProducts();
    createChecks();
    createDateDimensions();
    createFacts();
  }

  private void createFacts() {
    System.out.println("creating facts");
    createCheckFacts();
    createProductFacts();
  }

  private void createProductFacts() {
    ebean.find(CheckEntry.class).findList().stream()
        .flatMap(ch -> ebean.find(ProductEntry.class).findList().stream()
            .map(p -> {
              final SupplierDimensions supplier = ebean.find(SupplierDimensions.class)
                  .where()
                  .eq("cash_machine_id", ch.getCashMachine().getId())
                  .findUnique();
              final Instant i = Instant.ofEpochMilli(ch.getOrigin());
              final LocalDateTime ldt = LocalDateTime.ofInstant(i, systemDefault());
              final DateDimensions date = ebean.find(DateDimensions.class)
                  .where()
                  .eq("year", ldt.getYear())
                  .eq("month", ldt.getMonth())
                  .eq("day", ldt.getDayOfMonth())
                  .eq("hour", ldt.getHour())
                  .eq("minute", ldt.getMinute())
                  .eq("second", ldt.getSecond())
                  .findUnique();
              final Double v = ch.getValue();
              return new ProductFactEntry(null, date, supplier, p, v, v / (2 * p.getValue()));
            })).forEach(ebean::save);
  }

  private void createCheckFacts() {
    ebean.find(CheckEntry.class).findList().stream()
        .map(ch -> {
          final SupplierDimensions supplier = ebean.find(SupplierDimensions.class)
              .where()
              .eq("cash_machine_id", ch.getCashMachine().getId())
              .findUnique();
          final Instant i = Instant.ofEpochMilli(ch.getOrigin());
          final LocalDateTime ldt = LocalDateTime.ofInstant(i, systemDefault());
          final DateDimensions date = ebean.find(DateDimensions.class)
              .where()
              .eq("year", ldt.getYear())
              .eq("month", ldt.getMonth())
              .eq("day", ldt.getDayOfMonth())
              .eq("hour", ldt.getHour())
              .eq("minute", ldt.getMinute())
              .eq("second", ldt.getSecond())
              .findUnique();
          return new CheckFactEntry(null, date, supplier, ch.getValue());
        }).forEach(ebean::save);
  }

  private void createDateDimensions() {
    ebean.find(CheckEntry.class).findList().stream()
        .map(CheckEntry::getOrigin)
        .map(Instant::ofEpochMilli)
        .map(i -> LocalDateTime.ofInstant(i, systemDefault()))
        .map(ldt -> new DateDimensions(
            null,
            ldt.getYear(),
            ldt.getMonthValue(),
            ldt.getDayOfMonth(),
            ldt.getHour(),
            ldt.getMinute(),
            ldt.getSecond(),
            ldt.toInstant(ZoneOffset.UTC).toEpochMilli())).forEach(ebean::save);
  }

  private void createChecks() {
    System.out.println("creating checks");
    final List<CashMachineEntry> crs = ebean.find(CashMachineEntry.class).findList();
    IntStream.range(0, crs.size())
        .mapToObj(i -> new Tuple2<>(i, crs.get(i)))
        .map(t2 -> new CheckEntry(null, currentTimeMillis() + t2._1 * 1000, t2._2, 100.0))
        .forEach(ebean::save);
  }

  private void createProducts() {
    System.out.println("creating products");
    final ProductEntry p1 = new ProductEntry(null, "p1", 1.0);
    final ProductEntry p2 = new ProductEntry(null, "p2", 5.0);
    ebean.save(p1);
    ebean.save(p2);
  }

  private void createSupplierDimensions() {
    createStores();
    createCashMachines();
    fillSupplierDimensions();
  }

  private void fillSupplierDimensions() {
    ebean.find(CashMachineEntry.class).findList().stream()
        .map(cm -> new SupplierDimensions(null, cm.getStore(), cm))
        .forEach(ebean::save);
  }

  private void createStores() {
    System.out.println("creating stores");
    final StoreEntry s1 = new StoreEntry(null, "store1");
    final StoreEntry s2 = new StoreEntry(null, "store2");
    ebean.save(s1);
    ebean.save(s2);
  }

  private void createCashMachines() {
    ebean.find(StoreEntry.class).findList().stream()
        .flatMap(s -> Lists
            .newArrayList(
                new CashMachineEntry(null, UUID.randomUUID().toString(), s),
                new CashMachineEntry(null, UUID.randomUUID().toString(), s)
            ).stream()
        ).forEach(ebean::save);
  }



  @Test
  public void byDateAndStore() {
    final ByDateAndStoreAggregationDAO dao = new ByDateAndStoreAggregationDAO(ebean);
    final List<ByDateAndStoreAggregationEntry> r = dao
        .aggregate(currentTimeMillis() - 24 * 60 * 60 * 1000, 2);
    final int a = 1;
  }
}