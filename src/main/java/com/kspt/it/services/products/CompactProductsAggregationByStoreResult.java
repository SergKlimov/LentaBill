package com.kspt.it.services.products;

public class CompactProductsAggregationByStoreResult {
    private final int storeId;

    private final int productId;

    private final double value;

    public CompactProductsAggregationByStoreResult(final int storeId, final int productId, final double value) {
        this.storeId = storeId;
        this.productId = productId;
        this.value = value;
    }

    public long getStoreId() {
        return storeId;
    }

    public int getProductId() {
        return productId;
    }

    public double getValue() {
        return value;
    }
}