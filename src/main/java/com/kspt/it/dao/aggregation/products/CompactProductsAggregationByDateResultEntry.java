package com.kspt.it.dao.aggregation.products;
import com.avaje.ebean.annotation.Sql;
import javax.persistence.Entity;

@Entity
@Sql
public class CompactProductsAggregationByDateResultEntry {
    private final Integer year;

    private final Integer month;

    private final Integer day;

    private final Integer productId;

    private final Double value;

    public CompactProductsAggregationByDateResultEntry(
            final Integer year,
            final Integer month,
            final Integer day,
            final Integer productId,
            final Double value) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.productId = productId;
        this.value = value;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getProductId() {
        return productId;
    }

    public Double getValue() {
        return value;
    }
}
