package com.kspt.it.services.meta.synthetic;

import com.google.common.collect.Range;
import com.kspt.it.services.meta.MetaRetrievingApi;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SyntheticMetaRetrievingApi implements MetaRetrievingApi {
  @Override
  public Range<Long> getDataCollectionOrigin() {
    final ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
    return Range.closed(
        utcNow.minusDays(30).toEpochSecond() * 1000,
        utcNow.toEpochSecond() * 1000);
  }
}
