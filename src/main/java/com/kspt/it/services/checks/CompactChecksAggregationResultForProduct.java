package com.kspt.it.services.checks;

public class CompactChecksAggregationResultForProduct {

    private final long origin;

    private final int productId;

    private final double value;

    public CompactChecksAggregationResultForProduct(final long origin, final int productId, final double value) {
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
