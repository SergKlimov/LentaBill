package com.kspt.it.persist.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntry {

  @Id
  private final Integer id;

  @Column(nullable = false)
  private final String name;

  @Column(nullable = false)
  private final Double value;

  public ProductEntry(final Integer id, final String name, final Double value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getValue() {
    return value;
  }
}
