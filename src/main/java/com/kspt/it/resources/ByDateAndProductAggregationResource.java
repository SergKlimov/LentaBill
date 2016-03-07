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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Path("/products/")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "by date and product aggregation")
public class ByDateAndProductAggregationResource {

  @Inject
  private ByDateAndProductAggregationApi service;

  @GET
  @Path("/aggregation/byDate")
  @ApiOperation(value = "Aggregate products info by date")
  public List<ByDateAndProductAggregationRepresentation> aggregateByDate() {
    final List<ByDateAndProductAggregationRepresentation> list = service
        .aggregateByDateAndProduct().stream()
        .map(ar -> new ByDateAndProductAggregationRepresentation(
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
      value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<TimeDomainPoint> forecastAggregatedValues(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .forecastAggregatedValues(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue())
        ).collect(toList());
    return list;
  }

  @GET
  @Path("{productId}/forecast/quantity/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<TimeDomainPoint> forecastAggregatedQuantity(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .forecastAggregatedQuantity(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return list;
  }

  @GET
  @Path("{productId}/aggregate/values/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<TimeDomainPoint> aggregateValues(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .aggregateValues(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return list;
  }

  @GET
  @Path("{productId}/aggregate/quantity/byDate/{aggregationFunction}")
  @ApiOperation(
      value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<TimeDomainPoint> aggregateQuantity(
      final @PathParam("productId") @NotNull Integer productId,
      final @PathParam("aggregationFunction") @NotNull String aggregationFunction) {
    final List<TimeDomainPoint> list = service
        .aggregateQuantityUsing(productId, aggregationFunction)
        .stream()
        .map(ar -> new TimeDomainPoint(ar.getOrigin(), ar.getValue()))
        .collect(toList());
    return list;
  }

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.FIELD)
  static class ByDateAndProductAggregationRepresentation {

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

    public ByDateAndProductAggregationRepresentation() {
    }

    public ByDateAndProductAggregationRepresentation(
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

  @XmlRootElement
  @XmlAccessorType(XmlAccessType.FIELD)
  static class TimeDomainPoint {
    private Long ts;

    @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
    private Double v;

    public TimeDomainPoint(final Long ts, final Double v) {
      this.ts = ts;
      this.v = v;
    }

    public TimeDomainPoint() {
    }
  }
}
