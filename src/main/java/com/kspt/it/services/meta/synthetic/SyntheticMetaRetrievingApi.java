package com.kspt.it.services.meta.synthetic;

import com.kspt.it.services.meta.DataCollectionOrigin;
import com.kspt.it.services.meta.MetaRetrievingApi;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SyntheticMetaRetrievingApi implements MetaRetrievingApi {
  @Override
  public DataCollectionOrigin getDataCollectionOrigin() {
    final long currentUTCTimeMs = ZonedDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli();
    return new DataCollectionOrigin(currentUTCTimeMs);
  }
}
