package com.kspt.it.dao.meta;

import com.avaje.ebean.EbeanServer;
import com.google.common.base.Preconditions;
import com.kspt.it.persist.data.ProductEntry;
import static java.util.Objects.nonNull;
import javax.inject.Inject;
import java.util.List;

public class ProductsDAO {

  private final EbeanServer ebean;

  @Inject
  public ProductsDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<ProductEntry> all() {
    return ebean.find(ProductEntry.class).findList();
  }

  public ProductEntry find(final Integer id) {
    Preconditions.checkState(nonNull(id), "Cannot find product entry for null id.");
    return ebean.find(ProductEntry.class, id);
  }
}
