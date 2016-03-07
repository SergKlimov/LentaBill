package com.kspt.it.services.products;

public class CompactProductAggregationResult {
    private final long origin;

    private final int productId;

    private final double value;

    public CompactProductAggregationResult(final long origin, final int productId, final double value) {
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
