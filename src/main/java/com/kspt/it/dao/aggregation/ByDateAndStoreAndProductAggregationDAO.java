package com.kspt.it.dao.aggregation;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;
import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.List;

public class ByDateAndStoreAndProductAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ByDateAndStoreAndProductAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ByDateAndStoreAndProductAggregationEntry> aggregateByStoreAndDateAndProduct() {
    final String query = "SELECT "
        + "date_dimensions.year AS year, "
        + "date_dimensions.month AS month, "
        + "date_dimensions.day AS day, "
        + "supplier_dimensions.store_id AS storeId, "
        + "product_facts.product_id AS productId, "
        + "MIN(product_facts.value * product_facts.quantity) AS minCheckValue, "
        + "AVG(product_facts.value * product_facts.quantity) AS avgCheckValue, "
        + "MAX(product_facts.value * product_facts.quantity) AS maxCheckValue, "
        + "SUM(product_facts.value * product_facts.quantity) AS allChecksValueSum, "
        + "MIN(product_facts.quantity) AS minProductQuantity, "
        + "AVG(product_facts.quantity) AS avgProductQuantity, "
        + "MAX(product_facts.quantity) AS maxProductQuantity, "
        + "SUM(product_facts.quantity) AS allProductsQuantitySum, "
        + "COUNT(*) AS itemsCount "
        + "FROM "
        + "product_facts "
        + "JOIN supplier_dimensions "
        + "ON product_facts.supplier_id = supplier_dimensions.id "
        + "JOIN date_dimensions "
        + "ON product_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "date_dimensions.year,"
        + "date_dimensions.month,"
        + "date_dimensions.day, "
        + "product_facts.product_id, "
        + "supplier_dimensions.store_id";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(ByDateAndStoreAndProductAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactByDateAndStoreAndProductAggregationEntry> aggregateValueByDateAndStoreUsing(
      final int storeId,
      final int productId,
      final String aggregationFunction) {
    return aggregateByDateAndStoreUsing(
        storeId,
        productId,
        aggregationFunction,
        "product_facts.value * product_facts.quantity");
  }

  private List<CompactByDateAndStoreAndProductAggregationEntry> aggregateByDateAndStoreUsing(
      final int storeId,
      final int productId,
      final String aggregationFunction,
      final String aggregationSubject) {
    final String query = "SELECT "
        + "date_dimensions.year AS year, "
        + "date_dimensions.month AS month, "
        + "date_dimensions.day AS day, "
        + "supplier_dimensions.store_id AS storeId, "
        + "product_facts.product_id AS productId, "
        + aggregationFunction + "(" + aggregationSubject + ") AS value "
        + "FROM "
        + "(SELECT * FROM product_facts WHERE product_id = " + productId + ") "
        + "RIGHT JOIN (SELECT * FROM supplier_dimensions WHERE store_id = " + storeId + ") "
        + "ON product_facts.supplier_id = supplier_dimensions.id "
        + "JOIN date_dimensions "
        + "ON product_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "date_dimensions.year,"
        + "date_dimensions.month,"
        + "date_dimensions.day, "
        + "product_facts.product_id, "
        + "supplier_dimensions.store_id";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CompactByDateAndStoreAndProductAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactByDateAndStoreAndProductAggregationEntry> aggregateQuantityByDateAndStoreUsing(
      final int storeId,
      final int productId,
      final String aggregationFunction) {
    return aggregateByDateAndStoreUsing(
        storeId,
        productId,
        aggregationFunction,
        "product_facts.quantity");
  }

  @Entity
  @Sql
  public static class ByDateAndStoreAndProductAggregationEntry {

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

    public ByDateAndStoreAndProductAggregationEntry(
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

  @Entity
  @Sql
  public static class CompactByDateAndStoreAndProductAggregationEntry {

    private final Integer year;

    private final Integer month;

    private final Integer day;

    private final Integer storeId;

    private final Integer productId;

    private final Double value;

    public CompactByDateAndStoreAndProductAggregationEntry(
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
}
