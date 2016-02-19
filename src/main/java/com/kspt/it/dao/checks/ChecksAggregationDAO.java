package com.kspt.it.dao.checks;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import javax.inject.Inject;
import java.util.List;

public class ChecksAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ChecksAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ChecksAggregationResultEntry> aggregateByDateAndStore() {
    final String query = "SELECT "
        + "date_dimensions.year AS year, "
        + "date_dimensions.month AS month, "
        + "date_dimensions.day AS day, "
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
        + "JOIN date_dimensions "
        + "ON check_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "supplier_dimensions.store_id, "
        + "date_dimensions.year, "
        + "date_dimensions.month, "
        + "date_dimensions.day ";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(ChecksAggregationResultEntry.class)
        .setRawSql(sql)
        .findList();
  }
}

