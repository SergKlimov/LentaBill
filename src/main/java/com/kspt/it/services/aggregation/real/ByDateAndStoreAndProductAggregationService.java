package com.kspt.it.services.aggregation.real;

import com.kspt.it.Tuple2;
import com.kspt.it.Tuple3;
import com.kspt.it.dao.aggregation.ByDateAndStoreAndProductAggregationDAO;
import com.kspt.it.services.aggregation.ByDateAndStoreAndProductAggregationApi;
import com.kspt.it.services.forecast.ForecastStatisticsExtrapolationService;
import static java.util.stream.Collectors.*;
import javafx.util.Pair;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ByDateAndStoreAndProductAggregationService
    implements ByDateAndStoreAndProductAggregationApi {

  private final ByDateAndStoreAndProductAggregationDAO dao;

  @Inject
  public ByDateAndStoreAndProductAggregationService(
      final ByDateAndStoreAndProductAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ByDateAndStoreAndProductAggregation> aggregateByStoreAndDateAndProduct() {
    return dao.aggregateByStoreAndDateAndProduct().stream()
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
  public List<CompactByDateAndStoreAndProductAggregation> forecastForProductsByStoreAndDate(
      final String aggregationFunction) {
    return buildForecastForAllProductsByStoreAndDate(
        aggregateUsingByStoreAndDate(aggregationFunction));
  }

  private List<CompactByDateAndStoreAndProductAggregation> buildForecastForAllProductsByStoreAndDate(
      final List<CompactByDateAndStoreAndProductAggregation> entries) {
    final Map<Integer, Map<Integer, List<CompactByDateAndStoreAndProductAggregation>>> map
        = entries.stream()
        .collect(groupingBy(CompactByDateAndStoreAndProductAggregation::getStoreId))
        .entrySet()
        .stream()
        .map(e -> new Tuple2<>(
            e.getKey(),
            e.getValue().stream()
                .collect(groupingBy(CompactByDateAndStoreAndProductAggregation::getProductId))
        )).collect(toMap(t2 -> t2._1, t2 -> t2._2));
    return map.entrySet().stream()
        .map(e -> new Tuple2<>(e.getKey(), e.getValue()))
        .flatMap(t2 -> t2._2.entrySet().stream()
            .map(e -> new Tuple3<>(t2._1, e.getKey(), e.getValue())))
        .map(t3 -> buildForecastForProductByStoreAndDate(t3._2, t3._1, t3._3))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<CompactByDateAndStoreAndProductAggregation> buildForecastForProductByStoreAndDate(
      final int productId, final int storeId,
      final List<CompactByDateAndStoreAndProductAggregation> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new CompactByDateAndStoreAndProductAggregation(
            forecast.get(i).getValue(),
            storeId,
            productId,
            forecast.get(i).getKey()))
        .collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor(
      final List<CompactByDateAndStoreAndProductAggregation> entries,
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
  public List<CompactByDateAndStoreAndProductAggregation> aggregateUsingByStoreAndDate(
      String aggregationFunction) {
    return null;
  }
}
