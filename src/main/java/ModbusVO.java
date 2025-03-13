package main.java;

import com.google.gson.Gson;

public class ModbusVO {

    private int slaveId1;
    private int photo1 = 0;
    private int gas1 = 0;
    private Double temp1 = 0.0;
    private Double humi1 = 0.0;
    private Double heatIndex1 = 0.0;

    private int slaveId2;
    private int photo2 = 0;
    private int gas2 = 0;
    private Double temp2 = 0.0;
    private Double humi2 = 0.0;
    private Double heatIndex2 = 0.0;

    private String raspiData;
    private String raspiTime;

    /**
     * mqtt로 받은 데이터
     */
    private String receiveMqttData;

    public void setRaspiData(String raspiData) {
        this.raspiData = raspiData;
    }

    public String getRaspiData() {
        return this.raspiData;
    }

    public void setRaspiTime(String raspiTime) {
        this.raspiTime = raspiTime;
    }

    public String getRaspiTime() {
        return this.raspiTime;
    }

    public void setReceiveMqttData(String receiveMqttData) {
        this.receiveMqttData = receiveMqttData;
    }

    public String getReceiveMqttData() {
        return this.receiveMqttData;
    }

    public void setSlaveId1(int slaveId1) {
        this.slaveId1 = slaveId1;
    }

    public int getSlaveId1() {
        return this.slaveId1;
    }

    public void setPhoto1(int photo1) {
        this.photo1 = photo1;
    }

    public int getPhoto1() {
        return this.photo1;
    }

    public void setGas1(int gas1) {
        this.gas1 = gas1;
    }

    public int getGas1() {
        return this.gas1;
    }

    public void setTemp1(Double temp1) {
        this.temp1 = temp1 / 1000;
    }

    public Double getTemp1() {
        return this.temp1;
    }

    public void setHumi1(Double humi1) {
        this.humi1 = humi1 / 1000;
    }

    public Double getHumi1() {
        return this.humi1;
    }

    public void setHeatIndex1(Double heatIndex1) {
        this.heatIndex1 = Math.round(heatIndex1 / 1000 * 10) / 10.0;
    }

    public Double getHeatIndex1() {
        return this.heatIndex1;
    }

    public void setSlaveId2(int slaveId2) {
        this.slaveId2 = slaveId2;
    }

    public int getSlaveId2() {
        return this.slaveId2;
    }

    public void setPhoto2(int photo2) {
        this.photo2 = photo2;
    }

    public int getPhoto2() {
        return this.photo2;
    }

    public void setGas2(int gas2) {
        this.gas2 = gas2;
    }

    public int getGas2() {
        return this.gas2;
    }

    public void setTemp2(Double temp2) {
        this.temp2 = temp2 / 1000;
    }

    public Double getTemp2() {
        return this.temp2;
    }

    public void setHumi2(Double humi2) {
        this.humi2 = humi2 / 1000;
    }

    public Double getHumi2() {
        return this.humi2;
    }

    public void setHeatIndex2(Double heatIndex2) {
        this.heatIndex2 = Math.round(heatIndex2 / 1000 * 10) / 10.0;
    }

    public Double getHeatIndex2() {
        return this.heatIndex2;
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
