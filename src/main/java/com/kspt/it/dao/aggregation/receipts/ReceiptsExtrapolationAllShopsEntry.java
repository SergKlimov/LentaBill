package com.kspt.it.dao.aggregation.receipts;

import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class ReceiptsExtrapolationAllShopsEntry {
    private final int year;

    private final int month;

    private final int day;

    private final double totalSum;

    public ReceiptsExtrapolationAllShopsEntry(final int year,
                                              final int month,
                                              final int day,
                                              final double totalSum) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.totalSum = totalSum;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public double getTotalSum() {
        return totalSum;
    }
}
