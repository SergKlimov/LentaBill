package com.kspt.it.services.aggregation.synthetic;

import com.kspt.it.Tuple2;
import com.kspt.it.services.aggregation.ByStoreAndProductAggregationApi;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class SyntheticByStoreAndProductAggregationApi implements ByStoreAndProductAggregationApi {

  private final int daysCount;

  private final int storesCount;

  private final int productsCount;

  public SyntheticByStoreAndProductAggregationApi(
      final int daysCount,
      final int storesCount,
      final int productsCount) {
    this.daysCount = daysCount;
    this.storesCount = storesCount;
    this.productsCount = productsCount;
  }

  @Override
  public List<ByStoreAndProductAggregation> aggregateByStoreAndProduct() {
    return range(0, storesCount)
        .boxed()
        .flatMap(i -> range(0, productsCount).mapToObj(j -> new Tuple2<>(i, j)))
        .map(this::generateProductsAggregationByStoreResult)
        .collect(toList());
  }

  @Override
  public List<CompactByStoreAndProductAggregation> aggregateValues(
      String aggregationFunction) {
    return range(0, storesCount)
        .boxed()
        .flatMap(s -> range(0, productsCount).mapToObj(p -> new Tuple2<>(s, p)))
        .map(t2 -> new CompactByStoreAndProductAggregation(t2._1, t2._2, 100 * Math.random()))
        .collect(toList());
  }

  @NotNull
  private ByStoreAndProductAggregation generateProductsAggregationByStoreResult(
      final Tuple2<Integer, Integer> t2) {
    final double minCheckValue = 10 * Math.random();
    final double maxCheckValue = minCheckValue + 10 * Math.random();
    final double minProductQuantity = 1000 * Math.random();
    final double maxProductQuantity = minProductQuantity + 10 * Math.random();
    return new ByStoreAndProductAggregation(
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
}
