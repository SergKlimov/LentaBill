package com.kspt.it.dao.aggregation.checks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChecksAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ChecksAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ChecksAggregationResultEntry> aggregateByDateAndStore(
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
    return ebean.find(ChecksAggregationResultEntry.class)
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

  public List<CompactChecksAggregationResultEntry> aggregateUsing(
      final String aggregationFunction) {
    final String aggregationFunctionArgument =
        aggregationFunction.equalsIgnoreCase("count") ? "*" : "check_facts.value";
    final String query = "SELECT "
        + "dates.year AS year, "
        + "dates.month AS month, "
        + "dates.day AS day, "
        + "supplier_dimensions.store_id AS storeId, "
        + aggregationFunction + "(" + aggregationFunctionArgument + ") AS value "
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
    return ebean.find(CompactChecksAggregationResultEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<ChecksAggregationResultEntry> aggregateByDateAndStore() {
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
        + "RIGHT JOIN date_dimensions "
        + "ON check_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "supplier_dimensions.store_id, "
        + "date_dimensions.year, "
        + "date_dimensions.month, "
        + "date_dimensions.day";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(ChecksAggregationResultEntry.class)
        .setRawSql(sql)
        .findList();
  }
}

