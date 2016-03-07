package com.kspt.it.dao.meta.stores;

import com.avaje.ebean.EbeanServer;
import com.kspt.it.persist.data.StoreEntry;
import javax.inject.Inject;
import java.util.List;

public class StoresMetaDAO {

  private final EbeanServer ebean;

  @Inject
  public StoresMetaDAO(final EbeanServer ebean) {
    this.ebean = ebean;
  }

  public List<StoreEntry> getStoresEntries() {
    return ebean.find(StoreEntry.class).findList();
  }
}
