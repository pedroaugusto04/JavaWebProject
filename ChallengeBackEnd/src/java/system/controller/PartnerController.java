package system.controller;

import haversine.Haversine;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import system.dao.PartnerDAO;
import system.model.Partner;

public class PartnerController {

    private static PartnerController instance;

    private PartnerController() {

    }

    public static PartnerController getInstance() {
        if (instance == null) {
            instance = new PartnerController();
        }
        return instance;
    }

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

    public Partner searchBestPartner(String clientAddress) throws ClassNotFoundException, IOException {
        PartnerDAO pDao = new PartnerDAO();
        List<Partner> listPartners = pDao.read();
        Partner bestPartner = null;
        GeometryJSON geoJSON = new GeometryJSON();
        Iterator<Partner> iter = listPartners.iterator();
        Point clientAddressPoint = geoJSON.readPoint(clientAddress);
        double shorterDistance = Double.MAX_VALUE;

        clientAddress = formatJson(clientAddress);
        double clientAddressArray[] = jsonToDoubleArray(clientAddress);

        while (iter.hasNext()) {
            Partner partner = iter.next();
            //formatting
            String partnerAddressFormat = formatJson(partner.getAddress());
            double partnersAddressArrayDouble[] = jsonToDoubleArray(partnerAddressFormat);

            MultiPolygon coverageArea = geoJSON.readMultiPolygon(partner.getCoverageArea());
            if (clientAddressPoint.within(coverageArea)) {
                // calculating the shortest distance between the points (haversine)
                double haversineResult = Haversine.distanceInKm(clientAddressArray[0], clientAddressArray[1], partnersAddressArrayDouble[0],
                        partnersAddressArrayDouble[1]);
                if (shorterDistance > haversineResult) {
                    shorterDistance = haversineResult;
                    bestPartner = partner;
                }
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
