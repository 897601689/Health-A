package com.health_a.bean;


/**
 * Created by Admin on 2016/1/8. 体温测试数据
 */
public class TempTest {

    private String IDCard;
    private String time;
    private String temp;
    private String doctor;

    public TempTest() {
        super();
    }

    public TempTest(String IDCard, String time, String temp, String doctor) {
        super();
        this.IDCard = IDCard;
        this.time = time;
        this.temp = temp;
        this.doctor = doctor;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
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

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
