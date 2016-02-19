package com.kspt.it.dao.products;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class ProductsAggregationByStoreAndDateResultEntry {

  private final int year;

  private final int month;

  private final int day;

  private final int storeId;

  private final int productId;

  private final double minCheckValue;

  private final double avgCheckValue;

  private final double maxCheckValue;

  private final double allChecksValueSum;

  private final double minProductQuantity;

  private final double avgProductQuantity;

  private final double maxProductQuantity;

  private final double allProductsQuantitySum;

  private final int itemsCount;

  public ProductsAggregationByStoreAndDateResultEntry(
      final int year,
      final int month,
      final int day,
      final int storeId,
      final int productId,
      final double minCheckValue,
      final double avgCheckValue,
      final double maxCheckValue,
      final double allChecksValueSum,
      final double minProductQuantity,
      final double avgProductQuantity,
      final double maxProductQuantity,
      final double allProductsQuantitySum,
      final int itemsCount) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.storeId = storeId;
    this.productId = productId;
    this.minCheckValue = minCheckValue;
    this.avgCheckValue = avgCheckValue;
    this.maxCheckValue = maxCheckValue;
    this.allChecksValueSum = allChecksValueSum;
    this.minProductQuantity = minProductQuantity;
    this.avgProductQuantity = avgProductQuantity;
    this.maxProductQuantity = maxProductQuantity;
    this.allProductsQuantitySum = allProductsQuantitySum;
    this.itemsCount = itemsCount;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public int getDay() {
    return day;
  }

  public int getStoreId() {
    return storeId;
  }

  public int getProductId() {
    return productId;
  }

  public double getMinCheckValue() {
    return minCheckValue;
  }

  public double getAvgCheckValue() {
    return avgCheckValue;
  }

  public double getMaxCheckValue() {
    return maxCheckValue;
  }

  public double getAllChecksValueSum() {
    return allChecksValueSum;
  }

  public double getMinProductQuantity() {
    return minProductQuantity;
  }

  public double getAvgProductQuantity() {
    return avgProductQuantity;
  }

  public double getMaxProductQuantity() {
    return maxProductQuantity;
  }

  public double getAllProductsQuantitySum() {
    return allProductsQuantitySum;
  }

  public int getItemsCount() {
    return itemsCount;
  }
}
