package com.health_a.bean;

/**
 * Created by Admin on 2016/1/11. 心电测试数据
 */
public class EcgTest {
    private String IDCard;
    private String time;
    private String ecg;
    private String doctor;

    public EcgTest(String IDCard, String time, String ecg, String doctor) {
        this.IDCard = IDCard;
        this.time = time;
        this.ecg = ecg;
        this.doctor = doctor;
    }


    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "EcgTest{" +
                "IDCard='" + IDCard + '\'' +
                ", time='" + time + '\'' +
                ", ecg='" + ecg + '\'' +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
