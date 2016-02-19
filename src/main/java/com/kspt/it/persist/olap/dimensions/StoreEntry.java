package com.kspt.it.persist.olap.dimensions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "stores")
public class StoreEntry {

  @Id
  private final Integer id;

  @Column(nullable = false)
  private final String alias;

  public StoreEntry(final Integer id, final String alias) {
    this.id = id;
    this.alias = alias;
  }

  public Integer getId() {
    return id;
  }

  public String getAlias() {
    return alias;
  }
}
