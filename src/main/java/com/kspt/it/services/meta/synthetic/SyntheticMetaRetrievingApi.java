package com.kspt.it.services.meta.synthetic;

import com.google.common.collect.Range;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.Product;
import com.kspt.it.services.meta.Store;
import static java.util.stream.Collectors.toList;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class SyntheticMetaRetrievingApi implements MetaRetrievingApi {

  private final int storesCount;

  private final int productsCount;

  public SyntheticMetaRetrievingApi(final int storesCount, final int productsCount) {
    this.storesCount = storesCount;
    this.productsCount = productsCount;
  }

  @Override
  public Range<Long> getDataCollectionOrigin() {
    final ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);
    return Range.closed(
        utcNow.minusDays(29).toEpochSecond() * 1000,
        utcNow.toEpochSecond() * 1000);
  }

  @Override
  public List<Store> getStoresMeta() {
    return IntStream.range(0, storesCount)
        .mapToObj(i -> new Store(i, "store-" + i))
        .collect(toList());
  }

  @Override
  public List<Product> getAllProducts() {
    return IntStream.range(0, productsCount)
        .mapToObj(this::findProductInfo)
        .collect(toList());
  }

  @Override
  public Product findProductInfo(final int id) {
    return new Product(id, String.format("store-%d", id));
  }
}
