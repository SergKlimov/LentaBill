package com.kspt.it.dao.aggregation;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.annotation.Sql;
import static java.lang.String.format;
import javax.inject.Inject;
import javax.persistence.Entity;
import java.util.List;

public class ByDateAndProductAggregationDAO {

  private final EbeanServer ebean;

  @Inject
  public ByDateAndProductAggregationDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ByDateAndProductAggregationEntry> aggregateAllStatisticsAtOnce() {
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
    return ebean.find(ByDateAndProductAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactByDateAndProductAggregationEntry> aggregateOneValueStatisticForProduct(
      final int productId,
      final String aggregationFunction) {
    final String aggregationSubject = aggregationFunction.equalsIgnoreCase("count") ?
        "*" : "product_facts.value * product_facts.quantity";
    return aggregateOneStatisticForProduct(productId, aggregationFunction, aggregationSubject);
  }

  private List<CompactByDateAndProductAggregationEntry> aggregateOneStatisticForProduct(
      final int productId,
      final String aggregationFunction,
      final String aggregationSubject) {
    final String query = "SELECT "
        + "date_dimensions.year AS year, "
        + "date_dimensions.month AS month, "
        + "date_dimensions.day AS day, "
        + "product_facts.product_id AS productId, "
        + format("%s(%s) AS value ", aggregationFunction, aggregationSubject)
        + "FROM "
        + "(SELECT * FROM product_facts WHERE product_facts.id = " + productId + ") "
        + "LEFT JOIN date_dimensions "
        + "ON product_facts.date_id = date_dimensions.id "
        + "GROUP BY "
        + "product_facts.product_id, "
        + "date_dimensions.year, "
        + "date_dimensions.month, "
        + "date_dimensions.day ";
    final RawSql sql = RawSqlBuilder.parse(query).create();
    return ebean.find(CompactByDateAndProductAggregationEntry.class)
        .setRawSql(sql)
        .findList();
  }

  public List<CompactByDateAndProductAggregationEntry> aggregateOneQuantityStatisticForProduct(
      final int productId,
      final String aggregationFunction) {
    final String aggregationSubject = aggregationFunction.equalsIgnoreCase("count") ?
        "*" : "product_facts.quantity";
    return aggregateOneStatisticForProduct(productId, aggregationFunction, aggregationSubject);
  }

  @Entity
  @Sql
  public static class ByDateAndProductAggregationEntry {

    private final int year;

    private final int month;

    private final int day;

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

    public ByDateAndProductAggregationEntry(
        final int year,
        final int month,
        final int day,
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
  public static class CompactByDateAndProductAggregationEntry {
      private final Integer year;

      private final Integer month;

      private final Integer day;

      private final Integer productId;

      private final Double value;

      public CompactByDateAndProductAggregationEntry(
              final Integer year,
              final Integer month,
              final Integer day,
              final Integer productId,
              final Double value) {
          this.year = year;
          this.month = month;
          this.day = day;
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

      public Integer getProductId() {
          return productId;
      }

      public Double getValue() {
          return value;
      }
  }
}
