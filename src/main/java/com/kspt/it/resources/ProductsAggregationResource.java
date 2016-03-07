package com.kspt.it.resources;

import com.kspt.it.services.products.CompactProductsAggregationByDateResult;
import com.kspt.it.services.products.ProductsAggregationApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import static java.util.stream.Collectors.toList;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Path("api/products/")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "products_aggregation", description = "Aggregation for products")
public class ProductsAggregationResource {

  @Inject
  private ProductsAggregationApi service;

  @GET
  @Path("/aggregation/byDate")
  @ApiOperation(value = "Aggregate products info by date", notes = "Anything Else?")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 500, message = "Something wrong in Server")
  })
  public List<ProductsAggregationByDateResultRepresentation> aggregateByDate() {
    final List<ProductsAggregationByDateResultRepresentation> list = service
            .aggregateByDateAndProduct().stream()
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
                    ar.getItemsCount())
            ).collect(toList());
    return list;
  }

  @GET
  @Path("/aggregation/byStore")
  @ApiOperation(value = "Aggregate products info by sore", notes = "Anything Else?")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 500, message = "Something wrong in Server")
  })
  public List<ProductsAggregationByStoreResultRepresentation> aggregateByStore() {
    final List<ProductsAggregationByStoreResultRepresentation> list = service
            .aggregateByStoreAndProduct().stream()
            .map(ar -> new ProductsAggregationByStoreResultRepresentation(
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
  @Path("/aggregation/byDateAndStore")
  @ApiOperation(value = "Aggregate products info by date and sore", notes = "Anything Else?")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "OK"),
          @ApiResponse(code = 500, message = "Something wrong in Server")
  })
  public List<ProductsAggregationByStoreAndDateResultRepresentation> aggregateByDateAndStore() {
    final List<ProductsAggregationByStoreAndDateResultRepresentation> list = service
            .aggregateByStoreAndDateAndProduct().stream()
            .map(ar -> new ProductsAggregationByStoreAndDateResultRepresentation(
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
  @Path("/forecast/byDateAndProduct/{aggregationFunction}")
  @ApiOperation(
          value = "Build forecast for checks info aggregated by date and sore using arbitrary function",
          notes = "Available functions are: min, avg, max, sum, count.")
  public List<CompactProductsAggregationResultRepresentationByDate> forecastAggregationByDateAndProductByDate(
          final @QueryParam("aggregationFunction") String aggregationFunction) {
    final List<CompactProductsAggregationResultRepresentationByDate> list = service
            .forecastForProductsByDate(aggregationFunction)
            .stream()
            .map(ar -> new CompactProductsAggregationResultRepresentationByDate(
                    ar.getOrigin(),
                    ar.getProductId(),
                    ar.getValue())
            ).collect(toList());
    return list;
  }
}
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class CompactProductsAggregationResultRepresentationByDate {
  private Long timestamp;

  private Integer productId;

  @XmlJavaTypeAdapter(XMLDoubleAdapter.class)
  private Double value;

  public CompactProductsAggregationResultRepresentationByDate(
          final Long timestamp,
          final Integer productId,
          final Double value) {
    this.timestamp = timestamp;
    this.productId = productId;
    this.value = value;
  }

  public CompactProductsAggregationResultRepresentationByDate() {
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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ProductsAggregationByStoreAndDateResultRepresentation {

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

  public ProductsAggregationByStoreAndDateResultRepresentation() {
  }

  public ProductsAggregationByStoreAndDateResultRepresentation(
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
class ProductsAggregationByStoreResultRepresentation {

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

  public ProductsAggregationByStoreResultRepresentation() {
  }

  public ProductsAggregationByStoreResultRepresentation(
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