package com.kspt.it.resources;

import com.kspt.it.services.products.ProductsAggregationService;
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

@Path("api/products/aggregation")
@Produces(MediaType.APPLICATION_XML)
public class ProductsAggregationResource {

  @Inject
  private ProductsAggregationService service;

  @GET
  @Path("/byDate")
  public List<ProductsAggregationByDateResultRepresentation> aggregateByDate() {
    return service.aggregateByDateAndProduct().stream()
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
  }

  @GET
  @Path("/byStore")
  public List<ProductsAggregationByStoreResultRepresentation> aggregateByStore() {
    return service.aggregateByStoreAndProduct().stream()
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
  }

  @GET
  @Path("/byDateAndStore")
  public List<ProductsAggregationByStoreAndDateResultRepresentation> aggregateByDateAndStore() {
    return service.aggregateByStoreAndDateAndProduct().stream()
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
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ProductsAggregationByDateResultRepresentation {

  private final long timestamp;

  private final int productId;

  private final double minCheckValue;

  private final double avgCheckValue;

  private final double maxCheckValue;

  private final double allChecksValueSum;

  private final double minProductQuantity;

  private final double avgProductQuantity;

  private final double maxProductQuantity;

  private final double allProductsQuantitySum;

  private final int itemsCount;

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

  private final long timestamp;

  private final int storeId;

  private final int productId;

  private final double minCheckValue;

  private final double avgCheckValue;

  private final double maxCheckValue;

  private final double allChecksValueSum;

  private final double minProductQuantity;

  private final double avgProductQuantity;

  private final double maxProductQuantity;

  private final double allProductsQuantitySum;

  private final int itemsCount;

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

  private final int storeId;

  private final int productId;

  private final double minCheckValue;

  private final double avgCheckValue;

  private final double maxCheckValue;

  private final double allChecksValueSum;

  private final double minProductQuantity;

  private final double avgProductQuantity;

  private final double maxProductQuantity;

  private final double allProductsQuantitySum;

  private final int itemsCount;

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