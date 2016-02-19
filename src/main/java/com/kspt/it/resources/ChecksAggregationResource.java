package com.kspt.it.resources;

import com.kspt.it.services.checks.ChecksAggregationService;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Path("api/checks/aggregation")
@Produces(MediaType.APPLICATION_XML)
public class ChecksAggregationResource {

  @Inject
  private ChecksAggregationService service;

  @GET
  @Path("/byDateAndStore")
  public List<ChecksAggregationResultRepresentation> aggregateByDateAndStore() {
    return service.aggregateByDateAndStore()
        .stream()
        .map(ar -> new ChecksAggregationResultRepresentation(
            ar.getTimestamp(),
            ar.getStoreId(),
            ar.getMinCheckValue(),
            ar.getAvgCheckValue(),
            ar.getMaxCheckValue(),
            ar.getAllChecksValueSum(),
            ar.getChecksCount())
        ).collect(toList());
  }
}

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
class ChecksAggregationResultRepresentation {
  private final long timestamp;

  private final int storeId;

  private final double minCheckValue;

  private final double avgCheckValue;

  private final double maxCheckValue;

  private final double allChecksValueSum;

  private final int checksCount;

  public ChecksAggregationResultRepresentation(
      final long timestamp,
      final int storeId,
      final Double minCheckValue,
      final Double avgCheckValue,
      final Double maxCheckValue,
      final Double allChecksValueSum,
      final Integer checksCount) {
    this.timestamp = timestamp;
    this.storeId = storeId;
    this.minCheckValue = minCheckValue;
    this.avgCheckValue = avgCheckValue;
    this.maxCheckValue = maxCheckValue;
    this.allChecksValueSum = allChecksValueSum;
    this.checksCount = checksCount;
  }
}
