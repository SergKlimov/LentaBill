package com.kspt.it.persist.olap.facts;

import com.kspt.it.persist.data.ProductEntry;
import com.kspt.it.persist.olap.dimensions.DateDimensions;
import com.kspt.it.persist.olap.dimensions.SupplierDimensions;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_facts")
public class ProductFactEntry {

  @Id
  private final Integer id;

  @ManyToOne
  private final DateDimensions date;

  @ManyToOne
  private final SupplierDimensions supplier;

  @ManyToOne
  private final ProductEntry product;

  @Column(nullable = false)
  private final Double value;

  @Column(nullable = false)
  private final Double quantity;

  public ProductFactEntry(
      final Integer id,
      final DateDimensions date,
      final SupplierDimensions supplier,
      final ProductEntry product,
      final Double value,
      final Double quantity) {
    this.id = id;
    this.date = date;
    this.supplier = supplier;
    this.product = product;
    this.value = value;
    this.quantity = quantity;
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

  public ProductEntry getProduct() {
    return product;
  }

  public Double getValue() {
    return value;
  }

  public Double getQuantity() {
    return quantity;
  }
}
