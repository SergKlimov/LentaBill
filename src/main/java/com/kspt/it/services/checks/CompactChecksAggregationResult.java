package com.kspt.it.services.checks;

public class CompactChecksAggregationResult {

  private final long origin;

  private final int storeId;

  private final double value;

  public CompactChecksAggregationResult(final long origin, final int storeId, final double value) {
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
