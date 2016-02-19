package com.kspt.it.services.checks;

import java.util.List;

public interface ChecksAggregationApi {
  List<ChecksAggregationResult> aggregateByDateAndStore();
}
