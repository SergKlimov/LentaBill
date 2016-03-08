package com.kspt.it.resources;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.kspt.it.services.meta.MetaRetrievingApi;
import com.kspt.it.services.meta.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import static java.util.Objects.nonNull;
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
import java.util.List;

@Path("/meta")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "meta_retrieving", description = "Metadata retrieving endpoint")
public class MetaRetrievingResource {

  @Inject
  private MetaRetrievingApi api;

  @GET
  @Path("/dataCollectionDomain")
  @ApiOperation(value = "Retrieves time domain of processed collection.")
  public DataCollectionDomainRepresentation getDataCollectionDomain() {
    final Range<Long> domain = api.getDataCollectionOrigin();
    return new DataCollectionDomainRepresentation(domain.lowerEndpoint(), domain.upperEndpoint());
  }

  @GET
  @Path("/storesMeta")
  @ApiOperation(value = "Retrieves stores info.")
  public List<StoreRepresentation> getStoresMeta() {
    final List<StoreRepresentation> list = api.getStoresMeta().stream()
        .map(sm -> new StoreRepresentation(sm.getId(), sm.getName()))
        .collect(toList());
    return list;
  }

  @GET
  @Path("/products/all")
  @ApiOperation(value = "Retrieves all products info.")
  public List<ProductRepresentation> getAllProductsInfo() {
    final List<ProductRepresentation> list = api.getAllProducts().stream()
        .map(p -> new ProductRepresentation(p.getId(), p.getName()))
        .collect(toList());
    return list;
  }

  @GET
  @Path("/products/{id}")
  @ApiOperation(value = "Retrieves products info for a particular id.")
  public ProductRepresentation getProductInfo(
      final @PathParam("id") Integer id) {
    final Product found = api.findProductInfo(id);
    Preconditions.checkState(nonNull(found), "Product for id %s not found", id);
    return new ProductRepresentation(found.getId(), found.getName());
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class DataCollectionDomainRepresentation {
  private long lowerBound;

  private long upperBound;

  public DataCollectionDomainRepresentation() {
  }

  public DataCollectionDomainRepresentation(final long lowerBound, final long upperBound) {
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class StoreRepresentation {
  private int id;

  private String name;

  public StoreRepresentation() {
  }

  public StoreRepresentation(final int id, final String name) {
    this.id = id;
    this.name = name;
  }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ProductRepresentation {
  private int id;

  private String name;

  public ProductRepresentation() {
  }

  public ProductRepresentation(final int id, final String name) {
    this.id = id;
    this.name = name;
  }
}
