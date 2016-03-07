package com.kspt.it.persist.olap.dimensions;

import com.kspt.it.persist.data.CashMachineEntry;
import com.kspt.it.persist.data.StoreEntry;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supplier_dimensions")
public class SupplierDimensions {

  @Id
  private final Integer id;

  @ManyToOne
  private final StoreEntry store;

  @OneToOne
  private final CashMachineEntry cashMachine;

  public SupplierDimensions(
      final Integer id,
      final StoreEntry store,
      final CashMachineEntry cashMachine) {
    this.id = id;
    this.store = store;
    this.cashMachine = cashMachine;
  }

  public Integer getId() {
    return id;
  }

  public StoreEntry getStore() {
    return store;
  }

  public CashMachineEntry getCashMachine() {
    return cashMachine;
  }
}
