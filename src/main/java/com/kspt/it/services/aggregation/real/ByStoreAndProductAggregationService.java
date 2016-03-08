package com.kspt.it.services.aggregation.real;

import com.kspt.it.dao.aggregation.ByStoreAndProductAggregationDAO;
import com.kspt.it.services.aggregation.ByStoreAndProductAggregationApi;
import static java.util.stream.Collectors.toList;
import java.util.List;

public class ByStoreAndProductAggregationService implements ByStoreAndProductAggregationApi {

  private final ByStoreAndProductAggregationDAO dao;

  public ByStoreAndProductAggregationService(final ByStoreAndProductAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ByStoreAndProductAggregation> aggregateByStoreAndProduct() {
    return dao.aggregateByStoreAndProduct().stream()
        .map(are -> new ByStoreAndProductAggregation(
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
  public List<CompactByStoreAndProductAggregation> aggregateValues(
      final int productId,
      final String aggregationFunction) {
    return dao.aggregateValues(productId, aggregationFunction).stream()
        .map(care -> new CompactByStoreAndProductAggregation(
            care.getStoreId(),
            care.getProductId(),
            care.getValue()))
        .collect(toList());
  }

  @Override
  public List<CompactByStoreAndProductAggregation> aggregateQuantity(
      final int productId,
      final String aggregationFunction) {
    return dao.aggregateQuantity(productId, aggregationFunction).stream()
        .map(care -> new CompactByStoreAndProductAggregation(
            care.getStoreId(),
            care.getProductId(),
            care.getValue()))
        .collect(toList());
  }
}
