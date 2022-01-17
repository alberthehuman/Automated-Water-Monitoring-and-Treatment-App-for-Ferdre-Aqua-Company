package com.example.waterapp4;

public class HomeData {

    private String DO_Home;
    private String pH_Home;
    private String temp_Home;
    private String documentId;
    private String lime_Home;
    private String mix_Home;

    private String DO_Range_Home, pH_Range_Home;

    public HomeData(String DO_Home, String pH_Home, String temp_Home, String documentId, String lime_Home, String mix_Home, String DO_Range_Home, String pH_Range_Home) {
        this.DO_Home = DO_Home;
        this.pH_Home = pH_Home;
        this.temp_Home = temp_Home;
        this.documentId = documentId;
        this.lime_Home = lime_Home;
        this.mix_Home = mix_Home;
        this.DO_Range_Home = DO_Range_Home;
        this.pH_Range_Home = pH_Range_Home;
    }

    public HomeData(){
        // empty constructor needed
    }

    public String getDO_Home() {
        return DO_Home;
    }

    public String getpH_Home() {
        return pH_Home;
    }

    public void setDO_Home(String DO_Home) {
        this.DO_Home = DO_Home;
    }

    public void setpH_Home(String pH_Home) {
        this.pH_Home = pH_Home;
    }

    public String getTemp_Home() {
        return temp_Home;
    }

    public void setTemp_Home(String temp_Home) {
        this.temp_Home = temp_Home;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getLime_Home() {
        return lime_Home;
    }

    public void setLime_Home(String lime_Home) {
        this.lime_Home = lime_Home;
    }

    public String getMix_Home() {
        return mix_Home;
    }

    public void setMix_Home(String mix_Home) {
        this.mix_Home = mix_Home;
    }

    public String getDO_Range_Home() {
        return DO_Range_Home;
    }

    public void setDO_Range_Home(String DO_Range_Home) {
        this.DO_Range_Home = DO_Range_Home;
    }

    public String getpH_Range_Home() {
        return pH_Range_Home;
    }

    public void setpH_Range_Home(String pH_Range_Home) {
        this.pH_Range_Home = pH_Range_Home;
    }
}
