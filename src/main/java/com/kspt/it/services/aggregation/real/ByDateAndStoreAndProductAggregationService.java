package com.kspt.it.services.aggregation.real;

import com.kspt.it.dao.aggregation.ByDateAndStoreAndProductAggregationDAO;
import com.kspt.it.services.aggregation.ByDateAndStoreAndProductAggregationApi;
import com.kspt.it.services.forecast.ForecastStatisticsExtrapolationService;
import static java.util.stream.Collectors.toList;
import javafx.util.Pair;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class ByDateAndStoreAndProductAggregationService
    implements ByDateAndStoreAndProductAggregationApi {

  private final ByDateAndStoreAndProductAggregationDAO dao;

  @Inject
  public ByDateAndStoreAndProductAggregationService(
      final ByDateAndStoreAndProductAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ByDateAndStoreAndProductAggregation> aggregateAllStatisticsAtOnce() {
    return dao.aggregateAllStatisticAtOnce().stream()
        .map(are -> new ByDateAndStoreAndProductAggregation(
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
  public List<CompactByDateAndStoreAndProductAggregation> forecastOneValueStatisticForStoreAndProduct(
      final int storeId,
      final int productId,
      final String aggregationFunction) {
    final int forecastHorizon = 15;
    final List<CompactByDateAndStoreAndProductAggregation> aggregated = aggregateOneValueStatisticForStoreAndProduct(
        storeId, productId, aggregationFunction);
    final List<Pair<Double, Long>> toForecast = aggregated.stream()
        .map(care -> new Pair<>(care.getValue(), care.getOrigin()))
        .collect(toList());
    return ForecastStatisticsExtrapolationService
        .extrapolateStatistics(toForecast, forecastHorizon)
        .stream()
        .map(p -> new CompactByDateAndStoreAndProductAggregation(
            p.getValue(),
            storeId,
            productId,
            p.getKey()))
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndStoreAndProductAggregation> aggregateOneValueStatisticForStoreAndProduct(
      final int storeId,
      final int productId,
      final String aggregationFunction) {
    return dao.aggregateOneValueStatisticForStoreAndProduct(storeId, productId, aggregationFunction)
        .stream()
        .map(care -> new CompactByDateAndStoreAndProductAggregation(
            LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            care.getStoreId(),
            care.getProductId(),
            care.getValue()
        )).collect(toList());
  }

  @Override
  public List<CompactByDateAndStoreAndProductAggregation> forecastOneQuantityStatisticForStoreAndProduct(
      final int storeId,
      final int productId,
      final String aggregationFunction) {
    final int forecastHorizon = 15;
    final List<CompactByDateAndStoreAndProductAggregation> aggregated = aggregateOneQuantityStatisticForStoreAndProduct(
        storeId, productId, aggregationFunction);
    final List<Pair<Double, Long>> toForecast = aggregated.stream()
        .map(care -> new Pair<>(care.getValue(), care.getOrigin()))
        .collect(toList());
    return ForecastStatisticsExtrapolationService
        .extrapolateStatistics(toForecast, forecastHorizon)
        .stream()
        .map(p -> new CompactByDateAndStoreAndProductAggregation(
            p.getValue(),
            storeId,
            productId,
            p.getKey()))
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndStoreAndProductAggregation> aggregateOneQuantityStatisticForStoreAndProduct(
      final int storeId,
      final int productId,
      final String aggregationFunction) {
    return dao.aggregateOneQuantityStatisticForStoreAndProduct(storeId, productId, aggregationFunction)
        .stream()
        .map(care -> new CompactByDateAndStoreAndProductAggregation(
            LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            care.getStoreId(),
            care.getProductId(),
            care.getValue()
        )).collect(toList());
  }
}
