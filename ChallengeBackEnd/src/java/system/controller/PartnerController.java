package system.controller;

import haversine.Haversine;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
//import org.geotools.geojson.geom.GeometryJSON;
//import org.locationtech.jts.geom.MultiPolygon;
import system.dao.PartnerDAO;
import system.model.Partner;

public class PartnerController {

    public void createPartner(String tradingName, String ownerName, String document,
            String coverageArea, String address) throws ClassNotFoundException {
        PartnerDAO pDao = new PartnerDAO();
        Partner partner = new Partner();
        partner.setTradingName(tradingName);
        partner.setOwnerName(ownerName);
        partner.setDocument(document);
        partner.setCoverageArea(coverageArea);
        partner.setAddress(address);
        pDao.create(partner);
    }

    public Partner getPartner(int id) throws ClassNotFoundException {
        PartnerDAO pDao = new PartnerDAO();
        Partner partner = new Partner();
        partner.setId(id);
        partner = pDao.search(partner);
        return partner;
    }

    public List<Partner> getPartners() throws ClassNotFoundException {
        PartnerDAO pDao = new PartnerDAO();
        List<Partner> listPartners = pDao.read();
        return listPartners;
    }

    public Partner searchBestPartner(String address) throws IOException, ClassNotFoundException {
        //formating json to use haversine
        address = formatJson(address);

        // Calculating the shortest distance between the points with Haversine
        //GeometryJSON geoJSON = new GeometryJSON();
        //MultiPolygon multiPolygon = geoJSON.readMultiPolygon(address);
        double addressArrayDouble[] = jsonToDoubleArray(address);

        Partner bestPartner = null;
        PartnerDAO pDao = new PartnerDAO();
        List<Partner> listPartners = pDao.read();
        Iterator<Partner> iter = listPartners.iterator();

        double shorterDistance = Double.MAX_VALUE;
        while (iter.hasNext()) {
            Partner partner = iter.next();
            String partnerAddress = formatJson(partner.getAddress());
            double partnersAddressArrayDouble[] = jsonToDoubleArray(partnerAddress);
            double haversineResult = Haversine.distanceInKm(addressArrayDouble[0], addressArrayDouble[1], partnersAddressArrayDouble[0],
                    partnersAddressArrayDouble[1]);
            if (shorterDistance > haversineResult) {
                shorterDistance = haversineResult;
                bestPartner = partner;
            }
        }
        return bestPartner;
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
