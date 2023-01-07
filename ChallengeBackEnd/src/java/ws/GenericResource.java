package ws;

import com.google.gson.Gson;
import java.io.IOException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import system.model.Partner;
import java.util.List;
import javax.ws.rs.QueryParam;
import system.controller.PartnerController;

@Path("generic")
public class GenericResource {

    private PartnerController partnerController;

    @Context
    private UriInfo context;

    public GenericResource() throws ClassNotFoundException {
        this.partnerController = PartnerController.getInstance();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("Partner/get/{id}")
    public String getPartner(@PathParam("id") int id) throws ClassNotFoundException {
        Partner partner = partnerController.getPartner(id);
        Gson g = new Gson();
        return g.toJson(partner);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getPartners/")
    public String getPartners() throws ClassNotFoundException {
        List<Partner> listPartners = partnerController.getPartners();
        Gson g = new Gson();
        return g.toJson(listPartners);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("CreatePartner/")
    public String createPartner(@QueryParam("tradingName") String tradingName,
            @QueryParam("ownerName") String ownerName,
            @QueryParam("document") String document,
            @QueryParam("coverageArea") String coverageArea,
            @QueryParam("address") String address) throws ClassNotFoundException {
        partnerController.createPartner(tradingName, ownerName, document, coverageArea, address);
        return "OK";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("searchBestPartner/getLocalization/{address}")
    public String searchBestPartner(@PathParam("address") String address) throws ClassNotFoundException, IOException {
        Partner bestPartner = partnerController.searchBestPartner(address);
        Gson g = new Gson();
        if (bestPartner == null) {
            return g.toJson("No partner in coverage area");
        }
        return g.toJson("Closest partner than you: " + g.toJson(bestPartner));
    }

}
