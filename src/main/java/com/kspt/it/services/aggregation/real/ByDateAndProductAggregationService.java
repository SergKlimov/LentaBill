package com.kspt.it.services.aggregation.real;

import com.kspt.it.dao.aggregation.ByDateAndProductAggregationDAO;
import com.kspt.it.services.aggregation.ByDateAndProductAggregationApi;
import com.kspt.it.services.forecast.ForecastStatisticsExtrapolationService;
import static java.util.stream.Collectors.*;
import javafx.util.Pair;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ByDateAndProductAggregationService implements ByDateAndProductAggregationApi {

  private final ByDateAndProductAggregationDAO dao;

  @Inject
  public ByDateAndProductAggregationService(final ByDateAndProductAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ByDateAndProductAggregation> aggregateByDateAndProduct() {
    return dao.aggregateByDateAndProduct().stream()
        .map(are -> new ByDateAndProductAggregation(
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
  public List<CompactByDateAndProductAggregation> forecastAggregatedValues(
      final int productId,
      final String aggregationFunction) {
    return buildForecastForAllProductsByDate(aggregateValues(productId, aggregationFunction));
  }

  private List<CompactByDateAndProductAggregation> buildForecastForAllProductsByDate(
      final List<CompactByDateAndProductAggregation> entries) {
    final Map<Integer, List<CompactByDateAndProductAggregation>> aggregationsForProduct
        = entries
        .stream()
        .collect(groupingBy(CompactByDateAndProductAggregation::getProductId));
    return aggregationsForProduct.entrySet().stream()
        .map(e -> buildForecastForProductByDate(e.getKey(), e.getValue()))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<CompactByDateAndProductAggregation> buildForecastForProductByDate(
      final int productId,
      final List<CompactByDateAndProductAggregation> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new CompactByDateAndProductAggregation(
            forecast.get(i).getValue(),
            productId,
            forecast.get(i).getKey()))
        .collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor(
      final List<CompactByDateAndProductAggregation> entries,
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
  public List<CompactByDateAndProductAggregation> aggregateValues(
      final int productId,
      final String aggregationFunction) {
    return dao.aggregateValueByDateUsing(productId, aggregationFunction).stream()
        .map(care -> new CompactByDateAndProductAggregation(
            LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            care.getProductId(),
            care.getValue()))
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndProductAggregation> forecastAggregatedQuantity(
      final int productId,
      final String aggregationFunction) {
    return buildForecastForAllProductsByDate(aggregateValues(productId, aggregationFunction));
  }

  @Override
  public List<CompactByDateAndProductAggregation> aggregateQuantityUsing(
      final int productId,
      final String aggregationFunction) {
    return dao.aggregateQuantityByDateUsing(productId, aggregationFunction).stream()
        .map(care -> new CompactByDateAndProductAggregation(
            LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            care.getProductId(),
            care.getValue()))
        .collect(toList());
  }
}
