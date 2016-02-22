package com.kspt.it.services.receipts;

public class ReceiptsExtrapolationAllShopsResult {
    private final long timestamp;
    private final double totalSum;

    public ReceiptsExtrapolationAllShopsResult(final long timestamp, final double totalSum) {
        this.timestamp = timestamp;
        this.totalSum = totalSum;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getTotalSum() {
        return totalSum;
    }
}
