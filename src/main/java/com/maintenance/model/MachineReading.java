package com.maintenance.model;

public class MachineReading {
    private int udi;
    private String productId;
    private String productType;       // L, M, or H
    private double airTempK;
    private double processTempK;
    private int rotationalSpeedRpm;
    private double torqueNm;
    private int toolWearMin;
    private int machineFailure;       // 0 = pass, 1 = fail
    private int twf;                  // Tool Wear Failure
    private int hdf;                  // Heat Dissipation Failure
    private int pwf;                  // Power Failure
    private int osf;                  // Overstrain Failure
    private int rnf;                  // Random Failure

    public MachineReading() {
    }

    public MachineReading(int udi, String productId, String productType,
                          double airTempK, double processTempK, int rotationalSpeedRpm,
                          double torqueNm, int toolWearMin, int machineFailure,
                          int twf, int hdf, int pwf, int osf, int rnf) {
        this.udi = udi;
        this.productId = productId;
        this.productType = productType;
        this.airTempK = airTempK;
        this.processTempK = processTempK;
        this.rotationalSpeedRpm = rotationalSpeedRpm;
        this.torqueNm = torqueNm;
        this.toolWearMin = toolWearMin;
        this.machineFailure = machineFailure;
        this.twf = twf;
        this.hdf = hdf;
        this.pwf = pwf;
        this.osf = osf;
        this.rnf = rnf;
    }

    public int getUdi() { return udi; }
    public void setUdi(int udi) { this.udi = udi; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public double getAirTempK() { return airTempK; }
    public void setAirTempK(double airTempK) { this.airTempK = airTempK; }

    public double getProcessTempK() { return processTempK; }
    public void setProcessTempK(double processTempK) { this.processTempK = processTempK; }

    public int getRotationalSpeedRpm() { return rotationalSpeedRpm; }
    public void setRotationalSpeedRpm(int rotationalSpeedRpm) { this.rotationalSpeedRpm = rotationalSpeedRpm; }

    public double getTorqueNm() { return torqueNm; }
    public void setTorqueNm(double torqueNm) { this.torqueNm = torqueNm; }

    public int getToolWearMin() { return toolWearMin; }
    public void setToolWearMin(int toolWearMin) { this.toolWearMin = toolWearMin; }

    public int getMachineFailure() { return machineFailure; }
    public void setMachineFailure(int machineFailure) { this.machineFailure = machineFailure; }

    public int getTwf() { return twf; }
    public void setTwf(int twf) { this.twf = twf; }

    public int getHdf() { return hdf; }
    public void setHdf(int hdf) { this.hdf = hdf; }

    public int getPwf() { return pwf; }
    public void setPwf(int pwf) { this.pwf = pwf; }

    public int getOsf() { return osf; }
    public void setOsf(int osf) { this.osf = osf; }

    public int getRnf() { return rnf; }
    public void setRnf(int rnf) { this.rnf = rnf; }
}