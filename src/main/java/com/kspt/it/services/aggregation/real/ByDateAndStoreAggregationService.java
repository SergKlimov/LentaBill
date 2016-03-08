package com.kspt.it.services.aggregation.real;

import com.kspt.it.dao.aggregation.ByDateAndStoreAggregationDAO;
import com.kspt.it.services.aggregation.ByDateAndStoreAggregationApi;
import com.kspt.it.services.forecast.ForecastStatisticsExtrapolationService;
import static java.util.stream.Collectors.*;
import javafx.util.Pair;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ByDateAndStoreAggregationService implements ByDateAndStoreAggregationApi {

  private final ByDateAndStoreAggregationDAO dao;

  @Inject
  public ByDateAndStoreAggregationService(final ByDateAndStoreAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ByDateAndStoreAggregation> aggregateAllStatisticsAtOnceForDateRange(final long since, final int limit) {

    return dao.aggregate(since, limit).stream()
        .map(are -> new ByDateAndStoreAggregation(
            LocalDate.of(are.getYear(), are.getMonth(), are.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            are.getStoreId(),
            are.getMinCheckValue(),
            are.getAvgCheckValue(),
            are.getMaxCheckValue(),
            are.getAllChecksValueSum(),
            are.getChecksCount())
        ).collect(toList());
  }

  @Override
  public List<CompactByDateAndStoreAggregation> forecastOneValueStatistic(final String aggregationFunction) {
    return buildForecastForAllStores(aggregateOneValueStatistic(aggregationFunction));
  }

  private List<CompactByDateAndStoreAggregation> buildForecastForAllStores(
      final List<CompactByDateAndStoreAggregation> entries) {
    final Map<Integer, List<CompactByDateAndStoreAggregation>> aggregationsForStore = entries
        .stream()
        .collect(groupingBy(CompactByDateAndStoreAggregation::getStoreId));
    return aggregationsForStore.entrySet().stream()
        .map(e -> buildForecastForStore(e.getKey(), e.getValue()))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<CompactByDateAndStoreAggregation> buildForecastForStore(
      final int storeId,
      final List<CompactByDateAndStoreAggregation> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new CompactByDateAndStoreAggregation(
            forecast.get(i).getValue(),
            storeId,
            forecast.get(i).getKey()))
        .collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor(
      final List<CompactByDateAndStoreAggregation> entries,
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
  public List<CompactByDateAndStoreAggregation> aggregateOneValueStatistic(final String aggregationFunction) {
    return dao.aggregateOneValueStatistic(aggregationFunction).stream()
        .map(care -> new CompactByDateAndStoreAggregation(
            LocalDate.of(care.getYear(), care.getMonth(), care.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli(),
            care.getStoreId(),
            care.getValue()))
        .collect(toList());
  }
}