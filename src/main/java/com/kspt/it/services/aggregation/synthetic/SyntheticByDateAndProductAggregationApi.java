package com.kspt.it.services.aggregation.synthetic;

import com.kspt.it.Tuple2;
import com.kspt.it.services.aggregation.ByDateAndProductAggregationApi;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class SyntheticByDateAndProductAggregationApi implements ByDateAndProductAggregationApi {

  private final int daysCount;

  private final int storesCount;

  private final int productsCount;

  public SyntheticByDateAndProductAggregationApi(
      final int daysCount,
      final int storesCount,
      final int productsCount) {
    this.daysCount = daysCount;
    this.storesCount = storesCount;
    this.productsCount = productsCount;
  }

  @Override
  public List<ByDateAndProductAggregation> aggregateByDateAndProduct() {
    return range(0, daysCount)
        .mapToObj(i -> LocalDate.now().minusDays(i))
        .flatMap(d -> range(0, productsCount).mapToObj(i -> new Tuple2<>(d, i)))
        .map(this::generateProductsAggregationByDateResult)
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndProductAggregation> forecastAggregatedValues(
      final int productId,
      final String aggregationFunction) {
    return listOfCompactAggregations(productId, 15);
  }

  private List<CompactByDateAndProductAggregation> listOfCompactAggregations(
      final int productId,
      final int size) {
    return range(0, size)
        .mapToObj(i -> LocalDate.now().plusDays(i))
        .map(d -> new CompactByDateAndProductAggregation(
            d.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            productId,
            100 * Math.random()))
        .collect(toList());
  }

  @Override
  public List<CompactByDateAndProductAggregation> forecastAggregatedQuantity(
      final int productId,
      final String aggregationFunction) {
    return listOfCompactAggregations(productId, 15);
  }

  @Override
  public List<CompactByDateAndProductAggregation> aggregateValues(
      final int productId,
      final String aggregationFunction) {
    return listOfCompactAggregations(productId, 30);
  }

  @Override
  public List<CompactByDateAndProductAggregation> aggregateQuantityUsing(
      int productId,
      String aggregationFunction) {
    return listOfCompactAggregations(productId, 30);
  }

  private ByDateAndProductAggregation generateProductsAggregationByDateResult(
      final Tuple2<LocalDate, Integer> t2) {
    final double minCheckValue = 10 * Math.random();
    final double maxCheckValue = minCheckValue + 10 * Math.random();
    final double minProductQuantity = 1000 * Math.random();
    final double maxProductQuantity = minProductQuantity + 10 * Math.random();
    return new ByDateAndProductAggregation(
        t2._1.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
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
