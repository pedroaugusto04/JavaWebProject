package ws;

import com.google.gson.Gson;
import haversine.Haversine;
import java.io.IOException;
import java.util.Iterator;
import system.dao.PartnerDAO;
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

    @Context
    private UriInfo context;

    public GenericResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("Partner/get/{id}")
    public String getPartner(@PathParam("id") int id) throws ClassNotFoundException {
        PartnerDAO pDao = new PartnerDAO();
        Partner p = new Partner();
        p.setId(id);
        p = pDao.search(p);
        Gson g = new Gson();
        return g.toJson(p);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getPartners/")
    public String getPartners() throws ClassNotFoundException {
        PartnerDAO pDao = new PartnerDAO();
        List<Partner> listPartners = pDao.read();
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
        PartnerDAO pDao = new PartnerDAO();
        Partner partner = new Partner();
        partner.setTradingName(tradingName);
        partner.setOwnerName(ownerName);
        partner.setDocument(document);
        partner.setCoverageArea(coverageArea);
        partner.setAddress(address);
        pDao.create(partner);
        return "OK";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("searchBestPartner/getLocalization/{address}")
    public String searchBestPartner(@PathParam("address") String address) throws ClassNotFoundException, IOException {
        PartnerController partnerController = new PartnerController();
        Partner bestPartner = partnerController.searchBestPartner(address);
        Gson g = new Gson();
        return g.toJson("Closest partner than you: " + g.toJson(bestPartner));
    }

    public static String formatJson(String json) {
        json = json.replaceAll("\\{", "");
        json = json.replaceAll("}", "");
        json = json.replaceAll("\\[", "");
        json = json.replaceAll("]", "");
        json = json.replaceAll("\".*?\":", "");
        return json;
    }

    public static double[] jsonToDoubleArray(String json) {
        String jsonArrayString[] = json.split(",");
        double jsonArrayDouble[] = new double[jsonArrayString.length];

        for (int i = 0; i < jsonArrayString.length; i++) {
            jsonArrayDouble[i] = Double.parseDouble(jsonArrayString[i]);
        }
        return jsonArrayDouble;
    }
}
