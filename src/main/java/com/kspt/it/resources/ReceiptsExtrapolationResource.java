package com.kspt.it.resources;

import com.kspt.it.services.receipts.ReceiptsExtrapolationApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Path("/receipts/extrapolation")
@Produces(MediaType.APPLICATION_XML)
@Api(value = "receipts_extrapolation", description = "Extrapolation for receipts")
public class ReceiptsExtrapolationResource {

    @Inject
    private ReceiptsExtrapolationApi service;

    @GET
    @Path("/all")
    @ApiOperation(value = "Extrapolate receipts ", notes = "Anything Else?")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Something wrong in Server")
    })
    public List<ReceiptsExtrapolationAllShopsResultRepresentation> extrapolateAllShops() {
        final List<ReceiptsExtrapolationAllShopsResultRepresentation> list = service
                .extrapolateInAllShops().stream()
                .map(ar -> new ReceiptsExtrapolationAllShopsResultRepresentation(
                        ar.getTimestamp(),
                        ar.getTotalSum())
                ).collect(toList());
        return list;
    }
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ReceiptsExtrapolationAllShopsResultRepresentation {

    private long timestamp;

    private double totalSum;

    public ReceiptsExtrapolationAllShopsResultRepresentation(
            final long timestamp,
            final double totalSum) {
        this.timestamp = timestamp;
        this.totalSum = totalSum;
    }

    public ReceiptsExtrapolationAllShopsResultRepresentation() {}

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }
}