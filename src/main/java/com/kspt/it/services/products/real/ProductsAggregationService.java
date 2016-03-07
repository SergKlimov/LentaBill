package com.kspt.it.services.products.real;

import com.google.inject.Inject;
import com.kspt.it.dao.aggregation.products.ProductsAggregationDAO;
import com.kspt.it.services.checks.CompactChecksAggregationResult;
import com.kspt.it.services.forecast.real.ForecastStatisticsExtrapolationService;
import com.kspt.it.services.products.*;
import javafx.util.Pair;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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

  @Override
  public List<CompactProductAggregationResult> forecastForProducts(final String aggregationFunction) {
    return buildForecastForAllProducts(aggregateUsing(aggregationFunction));
  }

  private List<CompactProductAggregationResult> buildForecastForAllProducts(
          final List<CompactChecksAggregationResult> entries) {
    final Map<Integer, List<CompactChecksAggregationResult>> aggregationsForProduct = entries
            .stream()
            .collect(groupingBy(CompactChecksAggregationResult::getStoreId));
    return aggregationsForProduct.entrySet().stream()
            .map(e -> buildForecastForProduct(e.getKey(), e.getValue()))
            .flatMap(List::stream)
            .collect(toList());
  }

  private List<CompactProductAggregationResult> buildForecastForProduct(
          final int productId,
          final List<CompactChecksAggregationResult> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
            .mapToObj(i -> new CompactProductAggregationResult(
                    forecast.get(i).getValue(),
                    productId,
                    forecast.get(i).getKey()))
            .collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor(
          final List<CompactChecksAggregationResult> entries,
          final int forecastHorizon) {
    final List<Pair<Double, Long>> valueToOrigin = entries.stream()
            .map(are -> new Pair<>(
                    are.getValue(),
                    are.getOrigin()))
            .collect(toList());
    return ForecastStatisticsExtrapolationService
            .extrapolateStatistics(valueToOrigin, forecastHorizon);
  }

  @Override
  public List<CompactChecksAggregationResult> aggregateUsing(final String aggregationFunction) {
    return dao.aggregateUsing(aggregationFunction).stream()
            .map(care -> new CompactChecksAggregationResult(
                    LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                            .atStartOfDay()
                            .toInstant(ZoneOffset.UTC)
                            .toEpochMilli(),
                    care.getStoreId(),
                    care.getValue()))
            .collect(toList());
  }
}
