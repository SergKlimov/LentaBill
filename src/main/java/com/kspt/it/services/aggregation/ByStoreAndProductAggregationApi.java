package com.kspt.it.services.aggregation;

import java.util.List;

public interface ByStoreAndProductAggregationApi {

  List<ByStoreAndProductAggregation> aggregateByStoreAndProduct();

  List<CompactByStoreAndProductAggregation> aggregateValues(
      int productId,
      String aggregationFunction);

  List<CompactByStoreAndProductAggregation> aggregateQuantity(
      int productId,
      String aggregationFunction);

  class ByStoreAndProductAggregation {

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

    public ByStoreAndProductAggregation(
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

  class CompactByStoreAndProductAggregation {
      private final int storeId;

      private final int productId;

      private final double value;

      public CompactByStoreAndProductAggregation(final int storeId, final int productId, final double value) {
          this.storeId = storeId;
          this.productId = productId;
          this.value = value;
      }

      public int getStoreId() {
          return storeId;
      }

      public int getProductId() {
          return productId;
      }

      public double getValue() {
          return value;
      }
  }
}
