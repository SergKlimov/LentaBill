package com.kspt.it.dao.checks;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class ChecksAggregationResultEntry {
  private final Integer year;

  private final Integer month;

  private final Integer day;

  private final Integer storeId;

  private final Double minCheckValue;

  private final Double avgCheckValue;

  private final Double maxCheckValue;

  private final Double allChecksValueSum;

  private final Integer checksCount;

  public ChecksAggregationResultEntry(
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
