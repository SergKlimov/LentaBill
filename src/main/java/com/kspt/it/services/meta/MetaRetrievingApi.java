package com.kspt.it.services.meta;

import com.google.common.collect.Range;

public interface MetaRetrievingApi {

  Range<Long> getDataCollectionOrigin();
}
