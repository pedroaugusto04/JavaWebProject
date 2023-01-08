package system.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import static system.auxmethods.Format.formatJson;
import static system.auxmethods.Format.jsonToDoubleArray;
import static system.auxmethods.Haversine.shorterDistanceKm;
import system.dao.PartnerDAO;
import system.model.Partner;

public class PartnerController {

    private PartnerDAO pDao;

    public PartnerController() throws ClassNotFoundException {
        this.pDao = new PartnerDAO();
    }

    public void createPartner(String tradingName, String ownerName, String document,
            String coverageArea, String address) throws ClassNotFoundException {
        Partner partner = new Partner();
        partner.setTradingName(tradingName);
        partner.setOwnerName(ownerName);
        partner.setDocument(document);
        partner.setCoverageArea(coverageArea);
        partner.setAddress(address);
        pDao.create(partner);
    }

    public Partner getPartner(int id) throws ClassNotFoundException {
        Partner partner = new Partner();
        partner.setId(id);
        partner = pDao.search(partner);
        return partner;
    }

    public List<Partner> getPartners() throws ClassNotFoundException {
        List<Partner> listPartners = pDao.read();
        return listPartners;
    }

    public Partner searchBestPartner(String clientAddress) throws ClassNotFoundException, IOException {
        List<Partner> listPartners = pDao.read();
        Partner bestPartner = null;
        GeometryJSON geoJSON = new GeometryJSON();
        Iterator<Partner> iter = listPartners.iterator();
        double shorterDistance = Double.MAX_VALUE;
        
        Point clientAddressPoint = geoJSON.readPoint(clientAddress);
       
        clientAddress = formatJson(clientAddress);
        double clientAddressArray[] = jsonToDoubleArray(clientAddress);

        while (iter.hasNext()) {
            Partner partner = iter.next();
            MultiPolygon coverageArea = geoJSON.readMultiPolygon(partner.getCoverageArea());
            
            //formatting
            String partnerAddressFormat = formatJson(partner.getAddress());
            double partnersAddressArrayDouble[] = jsonToDoubleArray(partnerAddressFormat);

            if (clientAddressPoint.within(coverageArea)) {
                // calculating the shortest distance between the points (haversine)
                double haversineResult = shorterDistanceKm(clientAddressArray[0], clientAddressArray[1], partnersAddressArrayDouble[0],
                        partnersAddressArrayDouble[1]);
                if (shorterDistance > haversineResult) {
                    shorterDistance = haversineResult;
                    bestPartner = partner;
                }
            }
        }

        return bestPartner;
    }

}
