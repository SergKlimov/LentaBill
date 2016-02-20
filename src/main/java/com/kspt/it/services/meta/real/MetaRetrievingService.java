package com.kspt.it.services.meta.real;

import com.kspt.it.dao.meta.ChecksMetaDAO;
import com.kspt.it.services.meta.DataCollectionOrigin;
import com.kspt.it.services.meta.MetaRetrievingApi;

public class MetaRetrievingService implements MetaRetrievingApi {

  private final ChecksMetaDAO dao;

  public MetaRetrievingService(final ChecksMetaDAO dao) {
    this.dao = dao;
  }

  @Override
  public DataCollectionOrigin getDataCollectionOrigin() {
    return new DataCollectionOrigin(dao.getFirstCheckOrigin().getOrigin());
  }
}
