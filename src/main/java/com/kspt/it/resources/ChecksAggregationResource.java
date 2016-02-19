package com.kspt.it.resources;

import com.kspt.it.services.checks.ChecksAggregationApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Path("/api/checks/aggregation")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "checks_aggregation", description = "Aggregation for checks")
public class ChecksAggregationResource {

  @Inject
  private ChecksAggregationApi service;

  @GET
  @Path("/byDateAndStore")
  @ApiOperation(value = "Aggregate checks info by date and sore", notes = "Anything Else?")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 500, message = "Something wrong in Server")
  })
  public List<ChecksAggregationResultRepresentation> aggregateByDateAndStore() {
    final List<ChecksAggregationResultRepresentation> list = service.aggregateByDateAndStore()
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
    return list;
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ChecksAggregationResultRepresentation {

  private long timestamp;

  private int storeId;

  private double minCheckValue;

  private double avgCheckValue;

  private double maxCheckValue;

  private double allChecksValueSum;

  private int checksCount;

  public ChecksAggregationResultRepresentation() {
  }

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