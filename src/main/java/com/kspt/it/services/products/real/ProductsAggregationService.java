package com.kspt.it.services.products.real;

import com.google.inject.Inject;
import com.kspt.it.Tuple2;
import com.kspt.it.Tuple3;
import com.kspt.it.dao.aggregation.products.ProductsAggregationDAO;
import com.kspt.it.services.forecast.real.ForecastStatisticsExtrapolationService;
import com.kspt.it.services.products.CompactProductsAggregationByDateResult;
import com.kspt.it.services.products.CompactProductsAggregationByStoreAndDateResult;
import com.kspt.it.services.products.CompactProductsAggregationByStoreResult;
import com.kspt.it.services.products.ProductsAggregationApi;
import com.kspt.it.services.products.ProductsAggregationByDateResult;
import com.kspt.it.services.products.ProductsAggregationByStoreAndDateResult;
import com.kspt.it.services.products.ProductsAggregationByStoreResult;
import static java.util.stream.Collectors.*;
import javafx.util.Pair;
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
    return dao.aggregateByStoreAndProduct().stream()
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
  public List<CompactProductsAggregationByDateResult> forecastForProductsByDate(
      final String aggregationFunction) {
    return buildForecastForAllProductsByDate(aggregateUsingByDate(aggregationFunction));
  }

  private List<CompactProductsAggregationByDateResult> buildForecastForAllProductsByDate(
      final List<CompactProductsAggregationByDateResult> entries) {
    final Map<Integer, List<CompactProductsAggregationByDateResult>> aggregationsForProduct
        = entries
        .stream()
        .collect(groupingBy(CompactProductsAggregationByDateResult::getProductId));
    return aggregationsForProduct.entrySet().stream()
        .map(e -> buildForecastForProductByDate(e.getKey(), e.getValue()))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<CompactProductsAggregationByDateResult> buildForecastForProductByDate(
      final int productId,
      final List<CompactProductsAggregationByDateResult> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new CompactProductsAggregationByDateResult(
            forecast.get(i).getValue(),
            productId,
            forecast.get(i).getKey()))
        .collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor(
      final List<CompactProductsAggregationByDateResult> entries,
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
  public List<CompactProductsAggregationByDateResult> aggregateUsingByDate(
      final String aggregationFunction) {
    return dao.aggregateValueByDateUsing(aggregationFunction).stream()
        .map(care -> new CompactProductsAggregationByDateResult(
            LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            care.getProductId(),
            care.getValue()))
        .collect(toList());
  }

  @Override
  public List<CompactProductsAggregationByStoreAndDateResult> forecastForProductsByStoreAndDate(
      String aggregationFunction) {
    return buildForecastForAllProductsByStoreAndDate(
        aggregateUsingByStoreAndDate(aggregationFunction));
  }

  private List<CompactProductsAggregationByStoreAndDateResult> buildForecastForAllProductsByStoreAndDate(
      final List<CompactProductsAggregationByStoreAndDateResult> entries) {
    final Map<Integer, Map<Integer, List<CompactProductsAggregationByStoreAndDateResult>>> map
        = entries.stream()
        .collect(groupingBy(CompactProductsAggregationByStoreAndDateResult::getStoreId))
        .entrySet()
        .stream()
        .map(e -> new Tuple2<>(
            e.getKey(),
            e.getValue().stream()
                .collect(groupingBy(CompactProductsAggregationByStoreAndDateResult::getProductId))
        )).collect(toMap(t2 -> t2._1, t2 -> t2._2));
    return map.entrySet().stream()
        .map(e -> new Tuple2<>(e.getKey(), e.getValue()))
        .flatMap(t2 -> t2._2.entrySet().stream()
            .map(e -> new Tuple3<>(t2._1, e.getKey(), e.getValue())))
        .map(t3 -> buildForecastForProductByStoreAndDate(t3._2, t3._1, t3._3))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<CompactProductsAggregationByStoreAndDateResult> buildForecastForProductByStoreAndDate(
      final int productId, final int storeId,
      final List<CompactProductsAggregationByStoreAndDateResult> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor2(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new CompactProductsAggregationByStoreAndDateResult(
            forecast.get(i).getValue(),
            storeId,
            productId,
            forecast.get(i).getKey()))
        .collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor2(
      final List<CompactProductsAggregationByStoreAndDateResult> entries,
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
  public List<CompactProductsAggregationByStoreAndDateResult> aggregateUsingByStoreAndDate(
      String aggregationFunction) {
    return null;
  }

  @Override
  public List<CompactProductsAggregationByStoreResult> aggregateUsingByStore(
      String aggregationFunction) {
    return null;
  }
}
