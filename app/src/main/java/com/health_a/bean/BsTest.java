package com.health_a.bean;

/**
 * Created by Admin on 2016/1/11. 血糖检测数据
 */
public class BsTest {

    private String IDCard;
    private String time;
    private String fbg;
    private String bs;
    private String doctor;

    public BsTest() {
        super();
    }

    public BsTest(String IDCard, String time, String fbg, String bs, String doctor) {
        super();
        this.IDCard = IDCard;
        this.time = time;
        this.fbg = fbg;
        this.bs = bs;
        this.doctor = doctor;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFbg(String fbg) {
        this.fbg = fbg;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getIDCard() {

        return IDCard;
    }

    public String getTime() {
        return time;
    }

    public String getFbg() {
        return fbg;
    }

    public String getBs() {
        return bs;
    }

    public String getDoctor() {
        return doctor;
    }

    @Override
    public String toString() {
        return "BsTest{" +
                "IDCard='" + IDCard + '\'' +
                ", time='" + time + '\'' +
                ", fbg='" + fbg + '\'' +
                ", bs='" + bs + '\'' +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
