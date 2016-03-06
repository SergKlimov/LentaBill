package com.kspt.it.dao.aggregation.checks;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class CompactChecksAggregationResultEntry {
  private final Integer year;

  private final Integer month;

  private final Integer day;

  private final Integer storeId;

  private final Double value;

  public CompactChecksAggregationResultEntry(
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
