package com.kspt.it.dao.aggregation.products;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class CompactProductsAggregationByStoreResultEntry {

  private final Integer storeId;

  private final Integer productId;

  private final Double value;

  public CompactProductsAggregationByStoreResultEntry(
      final Integer storeId,
      final Integer productId,
      final Double value) {
    this.storeId = storeId;
    this.productId = productId;
    this.value = value;
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
