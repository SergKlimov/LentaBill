package com.kspt.it.resources;

import com.kspt.it.services.aggregation.ByStoreAndProductAggregationApi;
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
@Api(value = "by store and product aggregation")
public class ByStoreAndProductAggregationResource {

  @Inject
  private ByStoreAndProductAggregationApi service;

  @GET
  @Path("/aggregation/byStore")
  @ApiOperation(value = "Aggregate products info by sore", notes = "Anything Else?")
  public List<ByStoreAndProductAggregationRepresentation> aggregateByStore() {
    final List<ByStoreAndProductAggregationRepresentation> list = service
        .aggregateAllStatisticsAtOnce().stream()
        .map(ar -> new ByStoreAndProductAggregationRepresentation(
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
  @Path("/{productId}/aggregate/value/byStore/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate value of a particular product aggregated by store using arbitrary "
          + "aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<CompactByStoreAndProductAggregationRepresentation> aggregateValues(
      final @PathParam("productId") Integer productId,
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<CompactByStoreAndProductAggregationRepresentation> list = service
        .aggregateOneValueStatisticForProduct(productId, aggregationFunction).stream()
        .map(car -> new CompactByStoreAndProductAggregationRepresentation(
            car.getStoreId(),
            car.getValue()))
        .collect(toList());
    return list;
  }

  @GET
  @Path("/{productId}/aggregate/quantity/byStore/{aggregationFunction}")
  @ApiOperation(
      value = "Aggregate quantity of a particular product aggregated by store using arbitrary "
          + "aggregation function.",
      notes = "Available functions are: min, avg, max, sum, count.")
  public List<CompactByStoreAndProductAggregationRepresentation> aggregateQuantity(
      final @PathParam("productId") Integer productId,
      final @PathParam("aggregationFunction") String aggregationFunction) {
    final List<CompactByStoreAndProductAggregationRepresentation> list = service
        .aggregateOneQuantityStatisticForProduct(productId, aggregationFunction).stream()
        .map(car -> new CompactByStoreAndProductAggregationRepresentation(
            car.getStoreId(),
            car.getValue()))
        .collect(toList());
    return list;
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ByStoreAndProductAggregationRepresentation {

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

  public ByStoreAndProductAggregationRepresentation() {
  }

  public ByStoreAndProductAggregationRepresentation(
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
class CompactByStoreAndProductAggregationRepresentation {

  private Integer storeId;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double value;

  public CompactByStoreAndProductAggregationRepresentation(
      final Integer storeId,
      final Double value) {
    this.storeId = storeId;
    this.value = value;
  }

  public CompactByStoreAndProductAggregationRepresentation() {
  }
}