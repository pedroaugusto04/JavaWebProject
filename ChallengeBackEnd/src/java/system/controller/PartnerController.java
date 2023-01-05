package system.controller;

import haversine.Haversine;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
//import org.geotools.geojson.geom.GeometryJSON;
//import org.locationtech.jts.geom.MultiPolygon;
import system.dao.PartnerDAO;
import system.model.Partner;
import static ws.GenericResource.formatJson;
import static ws.GenericResource.jsonToDoubleArray;

public class PartnerController {

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
}
