package com.kspt.it.services.meta.real;

import com.google.common.collect.Range;
import com.kspt.it.dao.meta.ChecksMetaDAO;
import com.kspt.it.dao.meta.ProductsDAO;
import com.kspt.it.dao.meta.StoresMetaDAO;
import com.kspt.it.persist.data.ProductEntry;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.Product;
import com.kspt.it.services.meta.Store;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import java.util.List;

public class MetaRetrievingService implements MetaRetrievingApi {

  private final ChecksMetaDAO checksDao;

  private final StoresMetaDAO storesDao;

  private final ProductsDAO productsDao;

  @Inject
  public MetaRetrievingService(
      final ChecksMetaDAO checksDao,
      final StoresMetaDAO storesDao,
      final ProductsDAO productsDao) {
    this.checksDao = checksDao;
    this.storesDao = storesDao;
    this.productsDao = productsDao;
  }

  @Override
  public Range<Long> getDataCollectionOrigin() {
    return Range.closed(
        checksDao.getFirstCheckOrigin().getOrigin(),
        checksDao.getLastCheckOrigin().getOrigin());
  }

  @Override
  public List<Store> getStoresMeta() {
    return storesDao.getStoresEntries().stream()
        .map(se -> new Store(se.getId(), se.getAlias()))
        .collect(toList());
  }

  @Override
  public List<Product> getAllProducts() {
    return productsDao.all().stream()
        .map(pe -> new Product(pe.getId(), pe.getName()))
        .collect(toList());
  }

  @Override
  public Product findProductInfo(final int id) {
    final ProductEntry found = productsDao.find(id);
    return new Product(found.getId(), found.getName());
  }
}
