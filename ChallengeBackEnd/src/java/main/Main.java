package main;

import haversine.Haversine;
import system.dao.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Connection connection = new ConnectionFactory().getConnection();
        System.out.println("Connection opened!");
        /*PartnerDAO partnerDao = new PartnerDAO();
        Partner partner = new Partner();
        partner.setTradingName("PontoPedro");
        partner.setOwnerName("Pedro");
        partner.setDocument("1232kd1234");
        partner.setCoverageArea("coverageArea");
        partner.setAddress("endereco");*/
        connection.close();
        System.out.println("Connection closed!");
    }
}
