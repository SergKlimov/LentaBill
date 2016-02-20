package com.kspt.it.services.checks.synthetic;

import com.kspt.it.Tuple2;
import com.kspt.it.services.checks.ChecksAggregationApi;
import com.kspt.it.services.checks.ChecksAggregationResult;
import static java.util.stream.Collectors.toList;
import org.jetbrains.annotations.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.IntStream;

public class SyntheticChecksAggregationApi implements ChecksAggregationApi {

  private final int storesCount;

  public SyntheticChecksAggregationApi(final int storesCount) {
    this.storesCount = storesCount;
  }

  @Override
  public List<ChecksAggregationResult> aggregateByDateAndStore(final long since, final int limit) {
    return IntStream.range(0, limit)
        .mapToObj(i -> LocalDateTime
            .ofInstant(
                Instant.ofEpochMilli(since),
                ZoneId.systemDefault())
            .toLocalDate()
            .plusDays(i))
        .flatMap(d -> IntStream.range(0, storesCount).mapToObj(i -> new Tuple2<>(d, i)))
        .map(this::generateChecksAggregationResult)
        .collect(toList());
  }

  @NotNull
  private ChecksAggregationResult generateChecksAggregationResult(
      final Tuple2<LocalDate, Integer> t2) {
    final double minCheckValue = 100 * Math.random();
    final double maxCheckValue = minCheckValue + 100 * Math.random();
    return new ChecksAggregationResult(
        t2._1.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000,
        t2._2,
        minCheckValue,
        (minCheckValue + maxCheckValue) / 2,
        maxCheckValue,
        12 * maxCheckValue,
        (int) (10000 * Math.random()));
  }
}
