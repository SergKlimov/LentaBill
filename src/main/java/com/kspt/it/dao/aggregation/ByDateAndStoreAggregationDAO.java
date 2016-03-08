package com.kspt.it.dao.aggregation;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;
import static java.lang.String.format;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ByDateAndStoreAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ByDateAndStoreAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ByDateAndStoreAggregationEntry> aggregate(
      final long since,
      final int limit) {
    final String query = "SELECT "
        + "dates.year AS year, "
        + "dates.month AS month, "
        + "dates.day AS day, "
        + "supplier_dimensions.store_id AS storeId, "
        + "MIN(check_facts.value) AS minCheckValue, "
        + "AVG(check_facts.value) AS avgCheckValue, "
        + "MAX(check_facts.value) AS maxCheckValue, "
        + "SUM(check_facts.value) AS allChecksValueSum, "
        + "COUNT(*) AS checksCount "
        + "FROM "
        + "check_facts "
        + "JOIN supplier_dimensions "
        + "ON check_facts.supplier_id = supplier_dimensions.id "
        + "RIGHT JOIN (" + datesQuery(since, limit) + ") AS dates "
        + "ON check_facts.date_id = dates.id "
        + "GROUP BY "
        + "supplier_dimensions.store_id, "
        + "dates.year, "
        + "dates.month, "
        + "dates.day "
        + "ORDER BY MIN(dates.as_millisecond) "
        + "LIMIT " + limit;
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(ByDateAndStoreAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  @NotNull
  private String datesQuery(final long since, final int limit) {
    return "SELECT id, year, month, day, as_millisecond FROM "
        + "date_dimensions "
        + "WHERE "
        + "as_millisecond > " + since + " AND "
        + "as_millisecond < " + (since + TimeUnit.DAYS.toMillis(limit));
  }

  public List<CompactByDateAndStoreAggregationEntry> aggregateOneValueStatistic(
      final String aggregationFunction) {
    final String aggregationSubject =
        aggregationFunction.equalsIgnoreCase("count") ? "*" : "check_facts.value";
    final String query = "SELECT "
        + "dates.year AS year, "
        + "dates.month AS month, "
        + "dates.day AS day, "
        + "supplier_dimensions.store_id AS storeId, "
        + format("%s(%s) AS value ", aggregationFunction, aggregationSubject)
        + "FROM "
        + "check_facts "
        + "JOIN supplier_dimensions "
        + "ON check_facts.supplier_id = supplier_dimensions.id "
        + "RIGHT JOIN date_dimensions "
        + "ON check_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "supplier_dimensions.store_id, "
        + "date_dimensions.year, "
        + "date_dimensions.month, "
        + "date_dimensions.day";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CompactByDateAndStoreAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  @Entity
  @Sql
  public static class ByDateAndStoreAggregationEntry {
    private final Integer year;

    private final Integer month;

    private final Integer day;

    private final Integer storeId;

    private final Double minCheckValue;

    private final Double avgCheckValue;

    private final Double maxCheckValue;

    private final Double allChecksValueSum;

    private final Integer checksCount;

    public ByDateAndStoreAggregationEntry(
        final Integer year,
        final Integer month,
        final Integer day,
        final Integer storeId,
        final Double minCheckValue,
        final Double avgCheckValue,
        final Double maxCheckValue,
        final Double allChecksValueSum,
        final Integer checksCount) {
      this.year = year;
      this.month = month;
      this.day = day;
      this.storeId = storeId;
      this.minCheckValue = minCheckValue;
      this.avgCheckValue = avgCheckValue;
      this.maxCheckValue = maxCheckValue;
      this.allChecksValueSum = allChecksValueSum;
      this.checksCount = checksCount;
    }

    public Integer getYear() {
      return year;
    }

    public Integer getMonth() {
      return month;
    }

    public Integer getDay() {
      return day;
    }

    public Integer getStoreId() {
      return storeId;
    }

    public Double getMinCheckValue() {
      return minCheckValue;
    }

    public Double getAvgCheckValue() {
      return avgCheckValue;
    }

    public Double getMaxCheckValue() {
      return maxCheckValue;
    }

    public Double getAllChecksValueSum() {
      return allChecksValueSum;
    }

    public Integer getChecksCount() {
      return checksCount;
    }
  }

  @Entity
  @Sql
  public static class CompactByDateAndStoreAggregationEntry {
    private final Integer year;

    private final Integer month;

    private final Integer day;

    private final Integer storeId;

    private final Double value;

    public CompactByDateAndStoreAggregationEntry(
        final Integer year,
        final Integer month,
        final Integer day,
        final Integer storeId,
        final Double value) {
      this.year = year;
      this.month = month;
      this.day = day;
      this.storeId = storeId;
      this.value = value;
    }

    public Integer getYear() {
      return year;
    }

    public Integer getMonth() {
      return month;
    }

    public Integer getDay() {
      return day;
    }

    public Integer getStoreId() {
      return storeId;
    }

    public Double getValue() {
      return value;
    }
  }
}

