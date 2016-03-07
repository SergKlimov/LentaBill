package com.kspt.it.dao.aggregation.products;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.kspt.it.dao.aggregation.checks.CompactChecksAggregationResultEntry;

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

  public List<CompactProductsAggregationByDateResultEntry> aggregateUsingByDate(
          final String aggregationFunction) {
    //TODO
    // Написать правильный запрос к БД
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
    return ebean.find(CompactProductsAggregationByDateResultEntry.class)
            .setRawSql(sql)
            .findList();
  }
}
