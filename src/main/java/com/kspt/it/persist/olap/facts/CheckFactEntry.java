package com.kspt.it.persist.olap.facts;

import com.kspt.it.persist.olap.dimensions.DateDimensions;
import com.kspt.it.persist.olap.dimensions.SupplierDimensions;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "check_facts")
public class CheckFactEntry {
  @Id
  private final Integer id;

  @ManyToOne
  private final DateDimensions date;

  @ManyToOne
  private final SupplierDimensions supplier;

  @Column(nullable = false)
  private final Double value;

  public CheckFactEntry(
      final Integer id,
      final DateDimensions date,
      final SupplierDimensions supplier,
      final Double value) {
    this.id = id;
    this.date = date;
    this.supplier = supplier;
    this.value = value;
  }

  public Integer getId() {
    return id;
  }

  public DateDimensions getDate() {
    return date;
  }

  public SupplierDimensions getSupplier() {
    return supplier;
  }

  public Double getValue() {
    return value;
  }
}