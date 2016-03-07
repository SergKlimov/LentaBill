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
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Path("/products/")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "by date and store and product aggregation")
public class ByDateAndStoreAndProductAggregationResource {

  @Inject
  private ByDateAndStoreAndProductAggregationApi service;

  @GET
  @Path("/aggregation/byDateAndStore")
  @ApiOperation(value = "Aggregate products info by date and sore")
  public List<ByDateAndStoreAndProductAggregationRepresentation> aggregateByDateAndStore() {
    final List<ByDateAndStoreAndProductAggregationRepresentation> list = service
        .aggregateByStoreAndDateAndProduct().stream()
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
  @Path("/forecast/byStoreAndDate/{aggregationFunction}")
  @ApiOperation(
      value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<CompactByDateAndStoreAndProductAggregationRepresentation> forecastAggregationByStoreAndDate(
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<CompactByDateAndStoreAndProductAggregationRepresentation> list = service
        .forecastForProductsByStoreAndDate(aggregationFunction)
        .stream()
        .map(ar -> new CompactByDateAndStoreAndProductAggregationRepresentation(
            ar.getOrigin(),
            ar.getStoreId(),
            ar.getProductId(),
            ar.getValue())
        ).collect(toList());
    return list;
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class CompactByDateAndStoreAndProductAggregationRepresentation {
  private Long timestamp;

  private Integer storeId;

  private Integer productId;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double value;

  public CompactByDateAndStoreAndProductAggregationRepresentation(
      final Long timestamp,
      final Integer storeId,
      final Integer productId,
      final Double value) {
    this.timestamp = timestamp;
    this.storeId = storeId;
    this.productId = productId;
    this.value = value;
  }

  public CompactByDateAndStoreAndProductAggregationRepresentation() {
  }
}