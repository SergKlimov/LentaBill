package com.kspt.it.services.products.synthetic;

import com.kspt.it.Tuple2;
import com.kspt.it.services.checks.CompactChecksAggregationResult;
import com.kspt.it.services.products.*;

import static java.util.stream.Collectors.toList;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.IntStream;

public class SyntheticProductsAggregationApi implements ProductsAggregationApi {

  private final int daysCount;

  private final int storesCount;

  private final int productsCount;

  public SyntheticProductsAggregationApi(
      final int daysCount,
      final int storesCount,
      final int productsCount) {
    this.daysCount = daysCount;
    this.storesCount = storesCount;
    this.productsCount = productsCount;
  }

  @Override
  public List<ProductsAggregationByDateResult> aggregateByDateAndProduct() {
    return IntStream.range(0, daysCount)
        .mapToObj(i -> LocalDate.now().minusDays(i))
        .flatMap(d -> IntStream.range(0, productsCount).mapToObj(i -> new Tuple2<>(d, i)))
        .map(this::generateProductsAggregationByDateResult)
        .collect(toList());
  }

  @Override
  public List<ProductsAggregationByStoreResult> aggregateByStoreAndProduct() {
    return IntStream.range(0, storesCount)
        .boxed()
        .flatMap(i -> IntStream.range(0, productsCount).mapToObj(j -> new Tuple2<>(i, j)))
        .map(this::generateProductsAggregationByStoreResult)
        .collect(toList());
  }

  @Override
  public List<ProductsAggregationByStoreAndDateResult> aggregateByStoreAndDateAndProduct() {
    return IntStream.range(0, daysCount)
        .mapToObj(i -> LocalDate.now().minusDays(i))
        .flatMap(d -> IntStream.range(0, storesCount).mapToObj(i -> new Tuple2<>(d, i)))
        .flatMap(t2 -> IntStream.range(0, productsCount).mapToObj(i -> new Tuple2<>(t2, i)))
        .map(this::generateProductsAggregationByStoreAndDateResult)
        .collect(toList());
  }

  @Override
  public List<CompactProductsAggregationByDateResult> forecastForProductsByDate(final String aggregationFunction) {
    final int forecastHorizon = 15;
    return IntStream.range(0, forecastHorizon)
            .mapToObj(i -> java.time.LocalDate.now().plusDays(i))
            .flatMap(d -> IntStream.range(0, 3).mapToObj(i -> new Tuple2<>(d, i)))
            .map(t2 -> new CompactProductsAggregationByDateResult(
                    t2._1.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
                    t2._2,
                    100 * Math.random()))
            .collect(toList());
  }
  @NotNull
  private ProductsAggregationByStoreAndDateResult generateProductsAggregationByStoreAndDateResult(
      final Tuple2<Tuple2<LocalDate, Integer>, Integer> t2) {
    final double minCheckValue = 10 * Math.random();
    final double maxCheckValue = minCheckValue + 10 * Math.random();
    final double minProductQuantity = 1000 * Math.random();
    final double maxProductQuantity = minProductQuantity + 10 * Math.random();
    return new ProductsAggregationByStoreAndDateResult(
        t2._1._1.toDateTimeAtStartOfDay().getMillis(),
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

  @NotNull
  private ProductsAggregationByDateResult generateProductsAggregationByDateResult(
      final Tuple2<LocalDate, Integer> t2) {
    final double minCheckValue = 10 * Math.random();
    final double maxCheckValue = minCheckValue + 10 * Math.random();
    final double minProductQuantity = 1000 * Math.random();
    final double maxProductQuantity = minProductQuantity + 10 * Math.random();
    return new ProductsAggregationByDateResult(
        t2._1.toDateTimeAtStartOfDay().getMillis(),
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

  @NotNull
  private ProductsAggregationByStoreResult generateProductsAggregationByStoreResult(
      final Tuple2<Integer, Integer> t2) {
    final double minCheckValue = 10 * Math.random();
    final double maxCheckValue = minCheckValue + 10 * Math.random();
    final double minProductQuantity = 1000 * Math.random();
    final double maxProductQuantity = minProductQuantity + 10 * Math.random();
    return new ProductsAggregationByStoreResult(
        t2._1,
        t2._2,
        minCheckValue,
        (minCheckValue + maxCheckValue) / 2,
        maxCheckValue,
        22 * maxCheckValue,
        minProductQuantity,
        (minProductQuantity + maxProductQuantity) / 2,
        maxProductQuantity,
        13 * maxProductQuantity,
        (int) (1000 * Math.random()));
  }

  @Override
  public List<CompactProductsAggregationByDateResult> aggregateUsingByDate(final String aggregationFunction) {
    final int sampleSize = 30;
    final java.time.LocalDate startOfReport = java.time.LocalDate.now().minusDays(sampleSize);
    return IntStream.range(0, sampleSize)
            .mapToObj(startOfReport::plusDays)
            .flatMap(d -> IntStream.range(0, storesCount).mapToObj(i -> new Tuple2<>(d, i)))
            .map(t2 -> new CompactProductsAggregationByDateResult(
                    t2._1.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
                    t2._2,
                    100 * Math.random()))
            .collect(toList());
  }
}
