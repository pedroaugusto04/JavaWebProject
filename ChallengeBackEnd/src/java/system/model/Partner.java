package system.model;


public class Partner {

    private int id;
    private String tradingName;
    private String ownerName;
    private String document;
    private String coverageArea; 
    private String address; 

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTradingName() {
        return tradingName;
    }

    public String getDocument() {
        return document;
    }


    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getCoverageArea() {
        return coverageArea;
    }

    public void setCoverageArea(String coverageArea) {
        this.coverageArea = coverageArea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    } 
}
