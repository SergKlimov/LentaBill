package com.kspt.it.services.products;

import java.util.List;

public interface ProductsAggregationApi {
  List<ProductsAggregationByDateResult> aggregateByDateAndProduct();

  List<ProductsAggregationByStoreResult> aggregateByStoreAndProduct();

  List<ProductsAggregationByStoreAndDateResult> aggregateByStoreAndDateAndProduct();
}
