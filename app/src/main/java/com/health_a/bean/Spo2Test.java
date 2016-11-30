package com.health_a.bean;

/**
 * Created by Admin on 2016/1/11. 血氧测试数据
 */
public class Spo2Test {


    private String IDCard;
    private String time;
    private String spo2;
    private String pulse;
    private String doctor;

    public Spo2Test() {
        super();
    }

    public Spo2Test(String IDCard, String time, String spo2, String pulse, String doctor) {
        super();
        this.IDCard = IDCard;
        this.time = time;
        this.spo2 = spo2;
        this.pulse = pulse;
        this.doctor = doctor;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getTime() {
        return time;
    }

    public String getSpo2() {
        return spo2;
    }

    public String getPulse() {
        return pulse;
    }

    public String getDoctor() {
        return doctor;
    }


    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Spo2Test{" +
                "IDCard='" + IDCard + '\'' +
                ", time='" + time + '\'' +
                ", spo2='" + spo2 + '\'' +
                ", pulse='" + pulse + '\'' +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
