package com.kspt.it.services.meta.real;

import com.google.common.collect.Range;
import com.kspt.it.dao.meta.ChecksMetaDAO;
import com.kspt.it.services.meta.MetaRetrievingApi;

public class MetaRetrievingService implements MetaRetrievingApi {

  private final ChecksMetaDAO dao;

  public MetaRetrievingService(final ChecksMetaDAO dao) {
    this.dao = dao;
  }

  @Override
  public Range<Long> getDataCollectionOrigin() {
    return Range.closed(
        dao.getFirstCheckOrigin().getOrigin(),
        dao.getLastCheckOrigin().getOrigin());
  }
}
