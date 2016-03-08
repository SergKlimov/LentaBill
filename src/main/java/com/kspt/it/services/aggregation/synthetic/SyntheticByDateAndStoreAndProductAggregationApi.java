package com.kspt.it.services.aggregation.synthetic;

import com.kspt.it.Tuple2;
import com.kspt.it.Tuple3;
import com.kspt.it.services.aggregation.ByDateAndStoreAndProductAggregationApi;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class SyntheticByDateAndStoreAndProductAggregationApi
    implements ByDateAndStoreAndProductAggregationApi {

  private final int daysCount;

  private final int storesCount;

  private final int productsCount;

  public SyntheticByDateAndStoreAndProductAggregationApi(
      final int daysCount,
      final int storesCount,
      final int productsCount) {
    this.daysCount = daysCount;
    this.storesCount = storesCount;
    this.productsCount = productsCount;
  }

  @Override
  public List<ByDateAndStoreAndProductAggregation> aggregateByStoreAndDateAndProduct() {
    return range(0, daysCount)
        .mapToObj(i -> LocalDate.now().minusDays(i))
        .flatMap(d -> range(0, storesCount).mapToObj(i -> new Tuple2<>(d, i)))
        .flatMap(t2 -> range(0, productsCount).mapToObj(i -> new Tuple2<>(t2, i)))
        .map(this::generateProductsAggregationByStoreAndDateResult)
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndStoreAndProductAggregation> forecastValues(
      final String aggregationFunction) {
    final int forecastHorizon = 15;
    return range(0, forecastHorizon)
        .mapToObj(i -> java.time.LocalDate.now().plusDays(i))
        .flatMap(d -> range(0, storesCount).mapToObj(i -> new Tuple2<>(d, i)))
        .flatMap(t2 -> range(0, productsCount).mapToObj(p -> new Tuple3<>(t2._1, t2._2, p)))
        .map(t3 -> new CompactByDateAndStoreAndProductAggregation(
            t3._1.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            t3._2,
            t3._3,
            100 * Math.random()))
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndStoreAndProductAggregation> aggregateUsingByStoreAndDate(
      String aggregationFunction) {
    return range(0, daysCount)
        .mapToObj(i -> LocalDate.now().minusDays(i))
        .flatMap(d -> range(0, storesCount).mapToObj(s -> new Tuple2<>(d, s)))
        .flatMap(t2 -> range(0, productsCount).mapToObj(p -> new Tuple3<>(t2._1, t2._2, p)))
        .map(t3 -> new CompactByDateAndStoreAndProductAggregation(
            t3._1.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            t3._2,
            t3._3,
            100 * Math.random()))
        .collect(toList());
  }

  @NotNull
  private ByDateAndStoreAndProductAggregation generateProductsAggregationByStoreAndDateResult(
      final Tuple2<Tuple2<LocalDate, Integer>, Integer> t2) {
    final double minCheckValue = 10 * Math.random();
    final double maxCheckValue = minCheckValue + 10 * Math.random();
    final double minProductQuantity = 1000 * Math.random();
    final double maxProductQuantity = minProductQuantity + 10 * Math.random();
    return new ByDateAndStoreAndProductAggregation(
        t2._1._1.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
        t2._1._2,
        t2._2,
        minCheckValue,
        (minCheckValue + maxCheckValue) / 2,
        maxCheckValue,
        22 * maxCheckValue,
        minProductQuantity,
        (minProductQuantity + maxProductQuantity) / 2,
        maxProductQuantity,
        13 * maxProductQuantity,
        (int) (1000 * Math.random())
    );
  }
}
