package com.kspt.it.persist.data;

import com.kspt.it.persist.olap.dimensions.StoreEntry;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cash_machines")
public class CashMachineEntry {

  @Id
  private final Integer id;

  @Column(nullable = false)
  private final String code;

  @ManyToOne
  private final StoreEntry store;

  public CashMachineEntry(final Integer id, final String code, final StoreEntry store) {
    this.id = id;
    this.code = code;
    this.store = store;
  }

  public Integer getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public StoreEntry getStore() {
    return store;
  }
}
