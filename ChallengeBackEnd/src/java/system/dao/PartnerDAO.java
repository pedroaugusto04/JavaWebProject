package system.dao;

import system.model.Partner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartnerDAO {

    private Connection connection;

    public PartnerDAO() throws ClassNotFoundException {
        this.connection = new ConnectionFactory().getConnection();
    }

    public void create(Partner partner) {
        String sql = "INSERT INTO partners"
                + "(tradingName,ownerName,document,coverageArea,address)" + "values(?,?,?,ST_AsGeoJSON(ST_GeomFromGeoJSON(?)),"
                + "ST_AsGeoJSON(ST_GeomFromGeoJSON(?)))";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, partner.getTradingName());
            stmt.setString(2, partner.getOwnerName());
            stmt.setString(3, partner.getDocument());
            stmt.setString(4, partner.getCoverageArea());
            stmt.setString(5, partner.getAddress());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Partner> read() {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM partners");
            ResultSet rs = stmt.executeQuery();
            List<Partner> partners = new ArrayList<Partner>();
            while (rs.next()) {
                Partner partner = new Partner();
                partner.setId(rs.getInt("id"));
                partner.setTradingName(rs.getString("tradingName"));
                partner.setOwnerName(rs.getString("ownerName"));
                partner.setDocument(rs.getString("document"));
                partner.setCoverageArea(rs.getString("coverageArea"));
                partner.setAddress(rs.getString("address"));
                partners.add(partner);
            }
            rs.close();
            stmt.close();
            return partners;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*public void update(Partner partner) {
        String sql = "update partners set tradingName=?, ownerName=?, document=?,"
                + "coverageArea= ST_AsGeoJSON(ST_GeomFromGeoJSON(?)), address=ST_AsGeoJSON(ST_GeomFromGeoJSON(?)) where id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, partner.getNome());
            stmt.setString(2, partner.getEmail());
            stmt.setString(3, partner.getEndereco());
            stmt.setLong(5, partner.getId());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
    public void delete(Partner partner) {
        try {
            PreparedStatement stmt = connection.prepareStatement("delete"
                    + "from partners where id=?");
            stmt.setInt(1, partner.getId());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Partner search(Partner partner) {
        Partner p = new Partner();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * from partners");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("id") == partner.getId()) {
                    p.setId(rs.getInt("id"));
                    p.setTradingName(rs.getString("tradingName"));
                    p.setOwnerName(rs.getString("ownerName"));
                    p.setDocument(rs.getString("document"));
                    p.setCoverageArea(rs.getString("coverageArea"));
                    p.setAddress(rs.getString("address"));
                }
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return p;
    }
}
