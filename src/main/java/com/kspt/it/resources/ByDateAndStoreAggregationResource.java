package com.kspt.it.resources;

import com.kspt.it.services.aggregation.ByDateAndStoreAggregationApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Path("/checks/")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "by date and store aggregation")
public class ByDateAndStoreAggregationResource {

  @Inject
  private ByDateAndStoreAggregationApi service;

  @GET
  @Path("/aggregation/byDateAndStore")
  @ApiOperation(value = "Aggregate checks info by date and sore", notes = "Anything Else?")
  public List<ByDateAndStoreAggregationRepresentation> aggregateByDateAndStore(
      final @QueryParam("since") long since,
      final @QueryParam("limit") int limit) {
    final List<ByDateAndStoreAggregationRepresentation> list = service
        .aggregateAllStatisticsAtOnceForDateRange(since, limit)
        .stream()
        .map(ar -> new ByDateAndStoreAggregationRepresentation(
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

  @GET
  @Path("/aggregation/byDateAndStore/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate checks info by date and sore using arbitrary function as a parameter.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public CompactByDateAndStoreAggregationRepresentations aggregateByDateAndStore(
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<CompactByDateAndStoreAggregationRepresentation> list = service
        .aggregateOneValueStatistic(aggregationFunction)
        .stream()
        .map(ar -> new CompactByDateAndStoreAggregationRepresentation(
            ar.getOrigin(),
            ar.getStoreId(),
            ar.getValue())
        ).collect(toList());
    return new CompactByDateAndStoreAggregationRepresentations(list);
  }

  @GET
  @Path("/forecast/byDateAndStore/{aggregationFunction}")
  @ApiOperation(
      value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public CompactByDateAndStoreAggregationRepresentations forecastAggregationByDateAndStore(
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<CompactByDateAndStoreAggregationRepresentation> list = service
        .forecastOneValueStatistic(aggregationFunction)
        .stream()
        .map(ar -> new CompactByDateAndStoreAggregationRepresentation(
            ar.getOrigin(),
            ar.getStoreId(),
            ar.getValue())
        ).collect(toList());
    return new CompactByDateAndStoreAggregationRepresentations(list);
  }
}

@XmlAccessorType(XmlAccessType.FIELD)
class CompactByDateAndStoreAggregationRepresentation {
  @XmlElement(name = "ts")
  private Long timestamp;

  @XmlElement(name = "sid")
  private Integer storeId;

  @XmlElement(name = "v")
  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double value;

  public CompactByDateAndStoreAggregationRepresentation(
      final Long timestamp,
      final Integer storeId,
      final Double value) {
    this.timestamp = timestamp;
    this.storeId = storeId;
    this.value = value;
  }

  public CompactByDateAndStoreAggregationRepresentation() {
  }
}

@XmlRootElement(name = "results")
@XmlAccessorType(XmlAccessType.FIELD)
class CompactByDateAndStoreAggregationRepresentations {
  @XmlElement(name = "r")
  @XmlElementWrapper(name = "list")
  private List<CompactByDateAndStoreAggregationRepresentation> results;

  public CompactByDateAndStoreAggregationRepresentations(
      final List<CompactByDateAndStoreAggregationRepresentation> results) {
    this.results = results;
  }

  public CompactByDateAndStoreAggregationRepresentations() {
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ByDateAndStoreAggregationRepresentation {

  private Long timestamp;

  private Integer storeId;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double minCheckValue;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double avgCheckValue;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double maxCheckValue;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double allChecksValueSum;

  private Integer checksCount;

  public ByDateAndStoreAggregationRepresentation() {
  }

  public ByDateAndStoreAggregationRepresentation(
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