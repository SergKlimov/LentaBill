package com.kspt.it.services.meta.synthetic;

import com.google.common.collect.Range;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.StoreMeta;
import static java.util.stream.Collectors.toList;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class SyntheticMetaRetrievingApi implements MetaRetrievingApi {

  private final int storesCount;

  public SyntheticMetaRetrievingApi(final int storesCount) {
    this.storesCount = storesCount;
  }

  @Override
  public Range<Long> getDataCollectionOrigin() {
    final ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
    return Range.closed(
        utcNow.minusDays(29).toEpochSecond() * 1000,
        utcNow.toEpochSecond() * 1000);
  }

  @Override
  public List<StoreMeta> getStoresMeta() {
    return IntStream.range(0, storesCount)
        .mapToObj(i -> new StoreMeta(i, "store-" + i))
        .collect(toList());
  }
}
