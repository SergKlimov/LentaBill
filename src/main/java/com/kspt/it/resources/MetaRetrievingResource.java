package com.kspt.it.resources;

import com.google.common.collect.Range;
import com.kspt.it.services.meta.MetaRetrievingApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/api/meta")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "meta_retrieving", description = "Metadata retrieving endpoint")
public class MetaRetrievingResource {

  @Inject
  private MetaRetrievingApi api;

  @GET
  @Path("/dataCollectionOrigin")
  @ApiOperation(value = "Retrieves time domain of processed collection")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 500, message = "Something wrong in Server")
  })
  public DataCollectionDomainRepresentation getDataCollectionOrigin() {
    final Range<Long> domain = api.getDataCollectionOrigin();
    return new DataCollectionDomainRepresentation(domain.lowerEndpoint(), domain.upperEndpoint());
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
