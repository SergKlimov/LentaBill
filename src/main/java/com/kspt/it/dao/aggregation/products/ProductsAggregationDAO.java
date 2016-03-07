package com.kspt.it.dao.aggregation.products;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import javax.inject.Inject;
import java.util.List;

public class ProductsAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ProductsAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ProductsAggregationByDateResultEntry> aggregateByDateAndProduct() {
    final String query = "SELECT "
        + "date_dimensions.year AS year, "
        + "date_dimensions.month AS month, "
        + "date_dimensions.day AS day, "
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
        + "JOIN date_dimensions "
        + "ON product_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "product_facts.product_id, "
        + "date_dimensions.year, "
        + "date_dimensions.month, "
        + "date_dimensions.day ";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(ProductsAggregationByDateResultEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<ProductsAggregationByStoreResultEntry> aggregateByStoreAndProduct() {
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
    return ebean.find(ProductsAggregationByStoreResultEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<ProductsAggregationByStoreAndDateResultEntry> aggregateByStoreAndDateAndProduct() {
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
    return ebean.find(ProductsAggregationByStoreAndDateResultEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactProductsAggregationByDateResultEntry> aggregateValueByDateUsing(
      final String aggregationFunction) {
    return aggregateByDateUsing(
        aggregationFunction,
        "product_facts.value * product_facts.quantity");
  }

  private List<CompactProductsAggregationByDateResultEntry> aggregateByDateUsing(
      final String aggregationFunction,
      final String aggregationSubject) {
    final String query = "SELECT "
        + "date_dimensions.year AS year, "
        + "date_dimensions.month AS month, "
        + "date_dimensions.day AS day, "
        + "product_facts.product_id AS productId, "
        + aggregationFunction + "(" + aggregationSubject + ") AS value "
        + "FROM "
        + "product_facts "
        + "JOIN date_dimensions "
        + "ON product_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "product_facts.product_id, "
        + "date_dimensions.year, "
        + "date_dimensions.month, "
        + "date_dimensions.day ";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CompactProductsAggregationByDateResultEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactProductsAggregationByDateResultEntry> aggregateQuantityByDateUsing(
      final String aggregationFunction) {
    return aggregateByDateUsing(aggregationFunction, "product_facts.quantity");
  }

  public List<CompactProductsAggregationByStoreResultEntry> aggregateValueByStoreUsing(
      final String aggregationFunction) {
    return aggregateByStoreUsing(
        aggregationFunction,
        "product_facts.value * product_facts.quantity");
  }

  private List<CompactProductsAggregationByStoreResultEntry> aggregateByStoreUsing(
      final String aggregationFunction,
      final String aggregationSubject) {
    final String query = "SELECT "
        + "supplier_dimensions.store_id AS storeId, "
        + "product_facts.product_id AS productId, "
        + aggregationFunction + "(" + aggregationSubject + ") AS value "
        + "FROM "
        + "product_facts "
        + "JOIN supplier_dimensions "
        + "ON product_facts.supplier_id = supplier_dimensions.id "
        + "GROUP BY "
        + "product_facts.product_id, "
        + "supplier_dimensions.store_id";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CompactProductsAggregationByStoreResultEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactProductsAggregationByStoreResultEntry> aggregateQuantityByStoreUsing(
      final String aggregationFunction) {
    return aggregateByStoreUsing(aggregationFunction, "product_facts.quantity");
  }
}
