package com.kspt.it.services.checks;

public class ChecksAggregationResult {

  private final long timestamp;

  private final int storeId;

  private final Double minCheckValue;

  private final Double avgCheckValue;

  private final Double maxCheckValue;

  private final Double allChecksValueSum;

  private final Integer checksCount;

  public ChecksAggregationResult(
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
