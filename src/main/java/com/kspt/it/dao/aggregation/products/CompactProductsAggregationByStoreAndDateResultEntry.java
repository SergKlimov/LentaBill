package com.kspt.it.dao.aggregation.products;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class CompactProductsAggregationByStoreAndDateResultEntry {

  private final Integer year;

  private final Integer month;

  private final Integer day;

  private final Integer storeId;

  private final Integer productId;

  private final Double value;

  public CompactProductsAggregationByStoreAndDateResultEntry(
      final Integer year,
      final Integer month,
      final Integer day,
      final Integer storeId,
      final Integer productId,
      final Double value) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.storeId = storeId;
    this.productId = productId;
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

  public Integer getProductId() {
    return productId;
  }

  public Double getValue() {
    return value;
  }
}
