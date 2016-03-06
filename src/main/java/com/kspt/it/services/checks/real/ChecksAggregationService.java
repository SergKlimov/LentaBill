package com.kspt.it.services.checks.real;

import com.kspt.it.dao.aggregation.checks.ChecksAggregationDAO;
import com.kspt.it.services.checks.ChecksAggregationApi;
import com.kspt.it.services.checks.ChecksAggregationResult;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

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
        }
