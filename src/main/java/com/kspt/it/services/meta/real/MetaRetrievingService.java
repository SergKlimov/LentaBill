package com.kspt.it.services.meta.real;

import com.google.common.collect.Range;
import com.kspt.it.dao.meta.checks.ChecksMetaDAO;
import com.kspt.it.dao.meta.stores.StoresMetaDAO;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.StoreMeta;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import java.util.List;

public class MetaRetrievingService implements MetaRetrievingApi {

  private final ChecksMetaDAO checksDao;

  private final StoresMetaDAO storesDao;

  @Inject
  public MetaRetrievingService(final ChecksMetaDAO checksDao, final StoresMetaDAO storesDao) {
    this.checksDao = checksDao;
    this.storesDao = storesDao;
  }

  @Override
  public Range<Long> getDataCollectionOrigin() {
    return Range.closed(
        checksDao.getFirstCheckOrigin().getOrigin(),
        checksDao.getLastCheckOrigin().getOrigin());
  }

  @Override
  public List<StoreMeta> getStoresMeta() {
    return storesDao.getStoresEntries().stream()
        .map(se -> new StoreMeta(se.getId(), se.getAlias()))
        .collect(toList());
  }
}
