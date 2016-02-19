package com.kspt.it.persist.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "checks")
public class CheckEntry {

  @Id
  private final Integer id;

  @Column(nullable = false)
  private final Long origin;

  @ManyToOne
  private final CashMachineEntry cashMachine;

  @Column(nullable = false)
  private final Double value;

  public CheckEntry(
      final Integer id,
      final Long origin,
      final CashMachineEntry cashMachine,
      final Double value) {
    this.id = id;
    this.origin = origin;
    this.cashMachine = cashMachine;
    this.value = value;
  }

  public Integer getId() {
    return id;
  }

  public Long getOrigin() {
    return origin;
  }

  public CashMachineEntry getCashMachine() {
    return cashMachine;
  }

  public Double getValue() {
    return value;
  }
}
