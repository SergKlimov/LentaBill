package com.kspt.it.services.checks;

import java.util.List;

public interface ChecksAggregationApi {
  List<ChecksAggregationResult> aggregateByDateAndStore(final long since, final int limit);

  List<CompactChecksAggregationResult> forecastForStores(final String aggregationFunction);

  List<CompactChecksAggregationResult> aggregateUsing(final String aggregationFunction);
}
