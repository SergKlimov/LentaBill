package com.kspt.it.services.meta;

public class Store {
  private final int id;

  private final String name;

  public Store(final int id, final String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
