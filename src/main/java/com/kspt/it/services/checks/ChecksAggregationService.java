package com.kspt.it.services.checks;

import com.kspt.it.dao.checks.ChecksAggregationDAO;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class ChecksAggregationService {

  private final ChecksAggregationDAO dao;

  @Inject
  public ChecksAggregationService(final ChecksAggregationDAO dao) {
    this.dao = dao;
  }

  public List<ChecksAggregationResult> aggregateByDateAndStore() {
    return dao.aggregateByDateAndStore().stream()
        .map(are -> new ChecksAggregationResult(
            LocalDate.of(are.getYear(), are.getMonth(), are.getDay())
                .atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC),
            are.getStoreId(),
            are.getMinCheckValue(),
            are.getAvgCheckValue(),
            are.getMaxCheckValue(),
            are.getAllChecksValueSum(),
            are.getChecksCount())
        ).collect(toList());
  }
}
