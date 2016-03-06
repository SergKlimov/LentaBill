package com.kspt.it.services.checks.real;

import com.kspt.it.dao.aggregation.checks.ChecksAggregationDAO;
import com.kspt.it.dao.aggregation.checks.ChecksAggregationResultEntry;
import com.kspt.it.services.checks.ChecksAggregationApi;
import com.kspt.it.services.checks.ChecksAggregationResult;
import com.kspt.it.services.forecast.real.ForecastStatisticsExtrapolationService;
import static java.util.stream.Collectors.*;
import javafx.util.Pair;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
  public List<ChecksAggregationResult> forecastAggregationByDateAndStore() {
    return buildForecastForAllStores(dao.aggregateByDateAndStore());
  }

  private List<ChecksAggregationResult> buildForecastForAllStores(
      final List<ChecksAggregationResultEntry> entries) {
    final Map<Integer, List<ChecksAggregationResultEntry>> aggregationsForStore = entries.stream()
        .collect(groupingBy(ChecksAggregationResultEntry::getStoreId));
    return aggregationsForStore.entrySet().stream()
        .map(e -> buildForecastForStore(e.getKey(), e.getValue()))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<ChecksAggregationResult> buildForecastForStore(
      final int storeId,
      final List<ChecksAggregationResultEntry> entries) {
    final int forecastHorizon = 15;
    final List<Pair<Double, Long>> f1 = buildForecastFor(
        entries, ChecksAggregationResultEntry::getMinCheckValue, forecastHorizon);
    final List<Pair<Double, Long>> f2 = buildForecastFor(
        entries, ChecksAggregationResultEntry::getAvgCheckValue, forecastHorizon);
    final List<Pair<Double, Long>> f3 = buildForecastFor(
        entries, ChecksAggregationResultEntry::getMaxCheckValue, forecastHorizon);
    final List<Pair<Double, Long>> f4 = buildForecastFor(
        entries, ChecksAggregationResultEntry::getAllChecksValueSum, forecastHorizon);
    final List<Pair<Double, Long>> f5 = buildForecastFor(
        entries, are -> are.getChecksCount().doubleValue(), forecastHorizon);
    return IntStream.range(0, forecastHorizon)
        .mapToObj(i -> new ChecksAggregationResult(
            f1.get(i).getValue(),
            storeId,
            f1.get(i).getKey(),
            f2.get(i).getKey(),
            f3.get(i).getKey(),
            f4.get(i).getKey(),
            f5.get(i).getKey().intValue()
        )).collect(toList());
  }

  private List<Pair<Double, Long>> buildForecastFor(
      final List<ChecksAggregationResultEntry> entries,
      final Function<ChecksAggregationResultEntry, Double> valueMapper,
      final int forecastHorizon) {
    final List<Pair<Double, Long>> collect = entries.stream()
        .map(are -> new Pair<>(
            valueMapper.apply(are),
            LocalDate.of(are.getYear(), are.getMonth(), are.getDay())
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()))
        .sorted((o1, o2) -> Long.compare(o1.getValue(), o2.getValue()))
        .collect(toList());
    return ForecastStatisticsExtrapolationService.extrapolateStatistics(collect, forecastHorizon);
  }
}
