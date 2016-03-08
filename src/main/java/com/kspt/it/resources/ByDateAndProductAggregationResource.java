package com.kspt.it.resources;

import com.kspt.it.services.aggregation.ByDateAndProductAggregationApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Path("/products/")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "by date and product aggregation")
public class ByDateAndProductAggregationResource {

  @Inject
  private ByDateAndProductAggregationApi service;

  @GET
  @Path("/aggregation/byDate")
  @ApiOperation(value = "Calculate all statistics aggregated by date and product for all products "
      + "at once.")
  public List<ProductsAggregationByDateResultRepresentation> aggregateByDate() {
    final List<ProductsAggregationByDateResultRepresentation> list = service
        .aggregateAllStatisticsAtOnce().stream()
        .map(ar -> new ProductsAggregationByDateResultRepresentation(
            ar.getTimestamp(),
            ar.getProductId(),
            ar.getMinCheckValue(),
            ar.getAvgCheckValue(),
            ar.getMaxCheckValue(),
            ar.getAllChecksValueSum(),
            ar.getMinProductQuantity(),
            ar.getAvgProductQuantity(),
            ar.getMaxProductQuantity(),
            ar.getAllProductsQuantitySum(),
            ar.getItemsCount()))
        .collect(toList());
    return list;
  }

  @GET
  @Path("{productId}/forecast/value/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Forecast value of a particular product aggregated by date using arbitrary "
          + "aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints forecastAggregatedValues(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .forecastOneValueStatisticForProduct(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue())
        ).collect(toList());
    return new TimeDomainPoints(list);
  }

  @GET
  @Path("{productId}/forecast/quantity/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Forecast quantity of a particular product aggregated by date using arbitrary "
          + "aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints forecastAggregatedQuantity(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .forecastOneQuantityStatisticForProduct(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }

  @GET
  @Path("{productId}/aggregate/values/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate values of a particular product by date using arbitrary aggregation "
          + "function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints aggregateValues(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .aggregateOneValueStatisticForProduct(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }

  @GET
  @Path("{productId}/aggregate/quantity/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate quantity of a particular product by date using arbitrary aggregation "
          + "function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public TimeDomainPoints aggregateQuantity(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .aggregateOneQuantityStatisticForProduct(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return new TimeDomainPoints(list);
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ProductsAggregationByDateResultRepresentation {

  private long timestamp;

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

  public ProductsAggregationByDateResultRepresentation() {
  }

  public ProductsAggregationByDateResultRepresentation(
      final long timestamp,
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

