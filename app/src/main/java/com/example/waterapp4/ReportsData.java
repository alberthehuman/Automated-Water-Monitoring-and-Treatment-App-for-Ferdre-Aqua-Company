package com.example.waterapp4;

public class ReportsData {
    private String DO_Report;
    private String pH_Report;
    private String forCal_Report;

    public ReportsData(String DO_Report, String pH_Report, String forCal_Report) {
        this.DO_Report = DO_Report;
        this.pH_Report = pH_Report;
        this.forCal_Report = forCal_Report;
    }

    public ReportsData(){
        // empty constructor needed
    }

    public String getDO_Report() {
        return DO_Report;
    }

    public void setDO_Report(String DO_Report) {
        this.DO_Report = DO_Report;
    }

    public String getpH_Report() {
        return pH_Report;
    }

    public void setpH_Report(String pH_Report) {
        this.pH_Report = pH_Report;
    }

    public String getForCal_Report() {
        return forCal_Report;
    }

    public void setForCal_Report(String forCal_Report) {
        this.forCal_Report = forCal_Report;
    }
}
