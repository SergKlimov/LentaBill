package com.kspt.it.services.meta;

import com.google.common.collect.Range;
import java.util.List;

public interface MetaRetrievingApi {

  Range<Long> getDataCollectionOrigin();

  List<StoreMeta> getStoresMeta();


}
