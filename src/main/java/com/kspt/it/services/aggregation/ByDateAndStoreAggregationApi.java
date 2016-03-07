package com.kspt.it.services.aggregation;

import java.util.List;

public interface ByDateAndStoreAggregationApi {
  List<ByDateAndStoreAggregation> aggregateByDateAndStore(final long since, final int limit);

  List<CompactByDateAndStoreAggregation> forecastForStores(final String aggregationFunction);

  List<CompactByDateAndStoreAggregation> aggregateUsing(final String aggregationFunction);

  class ByDateAndStoreAggregation {

    private final long timestamp;

    private final int storeId;

    private final Double minCheckValue;

    private final Double avgCheckValue;

    private final Double maxCheckValue;

    private final Double allChecksValueSum;

    private final Integer checksCount;

    public ByDateAndStoreAggregation(
        final long timestamp,
        final int storeId,
        final Double minCheckValue,
        final Double avgCheckValue,
        final Double maxCheckValue,
        final Double allChecksValueSum,
        final Integer checksCount) {
      this.timestamp = timestamp;
      this.storeId = storeId;
      this.minCheckValue = minCheckValue;
      this.avgCheckValue = avgCheckValue;
      this.maxCheckValue = maxCheckValue;
      this.allChecksValueSum = allChecksValueSum;
      this.checksCount = checksCount;
    }

    public long getTimestamp() {
      return timestamp;
    }

    public int getStoreId() {
      return storeId;
    }

    public Double getMinCheckValue() {
      return minCheckValue;
    }

    public Double getAvgCheckValue() {
      return avgCheckValue;
    }

    public Double getMaxCheckValue() {
      return maxCheckValue;
    }

    public Double getAllChecksValueSum() {
      return allChecksValueSum;
    }

    public Integer getChecksCount() {
      return checksCount;
    }
  }

  class CompactByDateAndStoreAggregation {

    private final long origin;

    private final int storeId;

    private final double value;

    public CompactByDateAndStoreAggregation(final long origin, final int storeId, final double value) {
      this.origin = origin;
      this.storeId = storeId;
      this.value = value;
    }

    public long getOrigin() {
      return origin;
    }

    public int getStoreId() {
      return storeId;
    }

    public double getValue() {
      return value;
    }
  }
}
