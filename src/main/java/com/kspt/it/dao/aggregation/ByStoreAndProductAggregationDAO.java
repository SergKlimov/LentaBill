package com.kspt.it.dao.aggregation;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;
import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.List;

public class ByStoreAndProductAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ByStoreAndProductAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ByStoreAndProductAggregationEntry> aggregateByStoreAndProduct() {
    final String query = "SELECT "
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
        + "GROUP BY "
        + "product_facts.product_id, "
        + "supplier_dimensions.store_id";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(ByStoreAndProductAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactByStoreAndProductAggregationEntry> aggregateValues(
      final int productId,
      final String aggregationFunction) {
    return aggregateByStoreUsing(
        productId,
        aggregationFunction,
        "product_facts.value * product_facts.quantity");
  }

  private List<CompactByStoreAndProductAggregationEntry> aggregateByStoreUsing(
      final int productId,
      final String aggregationFunction,
      final String aggregationSubject) {
    final String query = "SELECT "
        + "supplier_dimensions.store_id AS storeId, "
        + "product_facts.product_id AS productId, "
        + aggregationFunction + "(" + aggregationSubject + ") AS value "
        + "FROM "
        + "(SELECT * FROM product_facts WHERE product_id = " + productId + ") "
        + "LEFT JOIN supplier_dimensions "
        + "ON product_facts.supplier_id = supplier_dimensions.id "
        + "GROUP BY "
        + "product_facts.product_id, "
        + "supplier_dimensions.store_id";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CompactByStoreAndProductAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactByStoreAndProductAggregationEntry> aggregateQuantity(
      final int productId,
      final String aggregationFunction) {
    return aggregateByStoreUsing(productId, aggregationFunction, "product_facts.quantity");
  }

  @Entity
  @Sql
  public static class ByStoreAndProductAggregationEntry {

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

    public ByStoreAndProductAggregationEntry(
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
  public static class CompactByStoreAndProductAggregationEntry {

    private final Integer storeId;

    private final Integer productId;

    private final Double value;

    public CompactByStoreAndProductAggregationEntry(
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
}
