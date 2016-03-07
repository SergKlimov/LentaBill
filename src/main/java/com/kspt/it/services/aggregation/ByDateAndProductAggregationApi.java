package com.kspt.it.services.aggregation;

import java.util.List;

public interface ByDateAndProductAggregationApi {
  List<ByDateAndProductAggregation> aggregateByDateAndProduct();

  List<CompactByDateAndProductAggregation> forecastAggregatedValues(
      final int productId,
      final String aggregationFunction);

  List<CompactByDateAndProductAggregation> forecastAggregatedQuantity(
      final int productId,
      final String aggregationFunction);

  List<CompactByDateAndProductAggregation> aggregateValues(
      final int productId,
      final String aggregationFunction);

  List<CompactByDateAndProductAggregation> aggregateQuantityUsing(
      int productId,
      String aggregationFunction);

  class ByDateAndProductAggregation {

    private final long timestamp;

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

    public ByDateAndProductAggregation(
        final long timestamp,
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
      this.timestamp = timestamp;
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

    public long getTimestamp() {
      return timestamp;
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

  class CompactByDateAndProductAggregation {
      private final long origin;

      private final int productId;

      private final double value;

      public CompactByDateAndProductAggregation(final long origin, final int productId, final double value) {
          this.origin = origin;
          this.productId = productId;
          this.value = value;
      }

      public long getOrigin() {
          return origin;
      }

      public int getProductId() {
          return productId;
      }

      public double getValue() {
          return value;
      }
  }
}
