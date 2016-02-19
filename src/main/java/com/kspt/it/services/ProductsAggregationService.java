package com.kspt.it.services;

import com.google.inject.Inject;
import com.kspt.it.dao.products.ProductsAggregationDAO;
import com.kspt.it.services.products.ProductsAggregationApi;
import com.kspt.it.services.products.ProductsAggregationByDateResult;
import com.kspt.it.services.products.ProductsAggregationByStoreAndDateResult;
import com.kspt.it.services.products.ProductsAggregationByStoreResult;
import static java.util.stream.Collectors.toList;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class ProductsAggregationService implements ProductsAggregationApi {

  private final ProductsAggregationDAO dao;

  @Inject
  public ProductsAggregationService(final ProductsAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ProductsAggregationByDateResult> aggregateByDateAndProduct() {
    return dao.aggregateByDateAndProduct().stream()
        .map(are -> new ProductsAggregationByDateResult(
            LocalDate.of(are.getYear(), are.getMonth(), are.getDay())
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC),
            are.getProductId(),
            are.getMinCheckValue(),
            are.getAvgCheckValue(),
            are.getMaxCheckValue(),
            are.getAllChecksValueSum(),
            are.getMinProductQuantity(),
            are.getAvgProductQuantity(),
            are.getMaxProductQuantity(),
            are.getAllProductsQuantitySum(),
            are.getItemsCount())
        ).collect(toList());
  }

  @Override
  public List<ProductsAggregationByStoreResult> aggregateByStoreAndProduct() {
    return dao.aggregateByStoreAndDateAndProduct().stream()
        .map(are -> new ProductsAggregationByStoreResult(
            are.getStoreId(),
            are.getProductId(),
            are.getMinCheckValue(),
            are.getAvgCheckValue(),
            are.getMaxCheckValue(),
            are.getAllChecksValueSum(),
            are.getMinProductQuantity(),
            are.getAvgProductQuantity(),
            are.getMaxProductQuantity(),
            are.getAllProductsQuantitySum(),
            are.getItemsCount())
        ).collect(toList());
  }

  @Override
  public List<ProductsAggregationByStoreAndDateResult> aggregateByStoreAndDateAndProduct() {
    return dao.aggregateByStoreAndDateAndProduct().stream()
        .map(are -> new ProductsAggregationByStoreAndDateResult(
            LocalDate.of(are.getYear(), are.getMonth(), are.getDay())
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC),
            are.getStoreId(),
            are.getProductId(),
            are.getMinCheckValue(),
            are.getAvgCheckValue(),
            are.getMaxCheckValue(),
            are.getAllChecksValueSum(),
            are.getMinProductQuantity(),
            are.getAvgProductQuantity(),
            are.getMaxProductQuantity(),
            are.getAllProductsQuantitySum(),
            are.getItemsCount())
        ).collect(toList());
  }
}
