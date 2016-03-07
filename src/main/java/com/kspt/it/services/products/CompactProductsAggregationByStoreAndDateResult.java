package com.kspt.it.services.products;

public class CompactProductsAggregationByStoreAndDateResult {
    private final long origin;

    private final int storeId;

    private final int productId;

    private final double value;

    public CompactProductsAggregationByStoreAndDateResult(final long origin, final int storeId, final int productId, final double value) {
        this.origin = origin;
        this.storeId = storeId;
        this.productId = productId;
        this.value = value;
    }

    public long getOrigin() {
        return origin;
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
