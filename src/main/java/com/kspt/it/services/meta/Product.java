package com.kspt.it.services.meta;

public class Product {

  private final int id;

  private final String name;

  public Product(final int id, final String name) {
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
