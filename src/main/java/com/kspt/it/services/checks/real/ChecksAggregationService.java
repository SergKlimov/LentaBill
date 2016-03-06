package com.kspt.it.services.checks.real;

import com.kspt.it.dao.aggregation.checks.ChecksAggregationDAO;
import com.kspt.it.services.checks.ChecksAggregationApi;
import com.kspt.it.services.checks.ChecksAggregationResult;
import com.kspt.it.services.checks.CompactChecksAggregationResult;
import com.kspt.it.services.forecast.real.ForecastStatisticsExtrapolationService;
import static java.util.stream.Collectors.*;
import javafx.util.Pair;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ChecksAggregationService implements ChecksAggregationApi {

  private final ChecksAggregationDAO dao;

  @Inject
  public ChecksAggregationService(final ChecksAggregationDAO dao) {
    this.dao = dao;
  }

  @Override
  public List<ChecksAggregationResult> aggregateByDateAndStore(final long since, final int limit) {

    return dao.aggregateByDateAndStore(since, limit).stream()
        .map(are -> new ChecksAggregationResult(
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
  public List<CompactChecksAggregationResult> forecastFor(final String aggregationFunction) {
    return buildForecastForAllStores(aggregateUsing(aggregationFunction));
  }

  private List<CompactChecksAggregationResult> buildForecastForAllStores(
      final List<CompactChecksAggregationResult> entries) {
    final Map<Integer, List<CompactChecksAggregationResult>> aggregationsForStore = entries
        .stream()
        .collect(groupingBy(CompactChecksAggregationResult::getStoreId));
    return aggregationsForStore.entrySet().stream()
        .map(e -> buildForecastForStore(e.getKey(), e.getValue()))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<CompactChecksAggregationResult> buildForecastForStore(
      final int storeId,
      final List<CompactChecksAggregationResult> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> forecast = buildForecastFor(entries, forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new CompactChecksAggregationResult(
            forecast.get(i).getValue(),
            storeId,
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