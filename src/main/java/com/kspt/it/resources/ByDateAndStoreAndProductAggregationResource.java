package com.kspt.it.resources;

import com.kspt.it.services.aggregation.ByDateAndStoreAndProductAggregationApi;
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
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Path("/products/")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "by date and store and product aggregation")
public class ByDateAndStoreAndProductAggregationResource {

  @Inject
  private ByDateAndStoreAndProductAggregationApi service;

  @GET
  @Path("/aggregation/byDateAndStore")
  @ApiOperation(value = "Calculate all statistics aggregated by date and store and product for "
      + "all products at once.")
  public List<ByDateAndStoreAndProductAggregationRepresentation> aggregateByDateAndStore() {
    final List<ByDateAndStoreAndProductAggregationRepresentation> list = service
        .aggregateAllStatisticsAtOnce().stream()
        .map(ar -> new ByDateAndStoreAndProductAggregationRepresentation(
            ar.getTimestamp(),
            ar.getStoreId(),
            ar.getProductId(),
            ar.getMinCheckValue(),
            ar.getAvgCheckValue(),
            ar.getMaxCheckValue(),
            ar.getAllChecksValueSum(),
            ar.getMinProductQuantity(),
            ar.getAvgProductQuantity(),
            ar.getMaxProductQuantity(),
            ar.getAllProductsQuantitySum(),
            ar.getItemsCount())
        ).collect(toList());
    return list;
  }

  @GET
  @Path("/{productId}/aggregate/value/byStoreAndDate/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate value of a particular product within a particular store aggregated by "
          + "date using arbitrary aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints aggregateValues(
      final @QueryParam("storeId") Integer storeId,
      final @PathParam("productId") Integer productId,
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .aggregateOneValueStatisticForStoreAndProduct(storeId, productId, aggregationFunction).stream()
        .map(car -> new TimeDomainPoint(car.getOrigin(), car.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }

  @GET
  @Path("/{productId}/aggregate/quantity/byStoreAndDate/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate quantity of a particular product within a particular store aggregated by "
          + "date using arbitrary aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints aggregateQuantity(
      final @QueryParam("storeId") Integer storeId,
      final @PathParam("productId") Integer productId,
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .aggregateOneQuantityStatisticForStoreAndProduct(storeId, productId, aggregationFunction).stream()
        .map(car -> new TimeDomainPoint(car.getOrigin(), car.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }

  @GET
  @Path("/{productId}/forecast/value/byStoreAndDate/{aggregationFunction}")
  @ApiOperation(
      value = "Forecast value of a particular product within a particular store aggregated by "
          + "date using arbitrary aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints forecastValues(
      final @PathParam("productId") Integer productId,
      final @QueryParam("storeId") Integer storeId,
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .forecastOneValueStatisticForStoreAndProduct(storeId, productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }

  @GET
  @Path("/{productId}/forecast/quantity/byStoreAndDate/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate quantity of a particular product within a particular store aggregated by "
          + "date using arbitrary aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints forecastQuantity(
      final @PathParam("productId") Integer productId,
      final @QueryParam("storeId") Integer storeId,
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .forecastOneQuantityStatisticForStoreAndProduct(storeId, productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ByDateAndStoreAndProductAggregationRepresentation {

  private long timestamp;

  private int storeId;

  private int productId;

  private double minCheckValue;

  private double avgCheckValue;

  private double maxCheckValue;

  private double allChecksValueSum;

  private double minProductQuantity;

  private double avgProductQuantity;

  private double maxProductQuantity;

  private double allProductsQuantitySum;

  private int itemsCount;

  public ByDateAndStoreAndProductAggregationRepresentation() {
  }

  public ByDateAndStoreAndProductAggregationRepresentation(
      final long timestamp,
      final int storeId,
      final int productId,
      final double minCheckValue,
      final double avgCheckValue,
      final double maxCheckValue,
      final double allChecksValueSum,
      final double minProductQuantity,
      final double avgProductQuantity,
      final double maxProductQuantity,
      final double allProductsQuantitySum,
      final int itemsCount) {
    this.timestamp = timestamp;
    this.storeId = storeId;
    this.productId = productId;
    this.minCheckValue = minCheckValue;
    this.avgCheckValue = avgCheckValue;
    this.maxCheckValue = maxCheckValue;
    this.allChecksValueSum = allChecksValueSum;
    this.minProductQuantity = minProductQuantity;
    this.avgProductQuantity = avgProductQuantity;
    this.maxProductQuantity = maxProductQuantity;
    this.allProductsQuantitySum = allProductsQuantitySum;
    this.itemsCount = itemsCount;
  }
}