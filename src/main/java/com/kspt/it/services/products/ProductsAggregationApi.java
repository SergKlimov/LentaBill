package com.kspt.it.services.products;

import com.kspt.it.services.checks.CompactChecksAggregationResult;

import java.util.List;

public interface ProductsAggregationApi {
  List<ProductsAggregationByDateResult> aggregateByDateAndProduct();

  List<ProductsAggregationByStoreResult> aggregateByStoreAndProduct();

  List<ProductsAggregationByStoreAndDateResult> aggregateByStoreAndDateAndProduct();

  List<CompactProductsAggregationByDateResult> forecastForProductsByDate(final String aggregationFunction);

  List<CompactProductsAggregationByDateResult> aggregateUsingByDate(final String aggregationFunction);
}
