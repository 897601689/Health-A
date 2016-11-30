package com.health_a.bean;

/**
 * Created by Admin on 2016/1/11. 血压检测数据
 */
public class BpTest {

    private String IDCard;
    private String time;
    private String h_bg;
    private String l_bp;
    private String m_bp;
    private String doctor;

    public BpTest(String IDCard, String time, String h_bg, String l_bp, String m_bp, String doctor) {
        this.IDCard = IDCard;
        this.time = time;
        this.h_bg = h_bg;
        this.l_bp = l_bp;
        this.m_bp = m_bp;
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

    public String getH_bg() {
        return h_bg;
    }

    public void setH_bg(String h_bg) {
        this.h_bg = h_bg;
    }

    public String getL_bp() {
        return l_bp;
    }

    public void setL_bp(String l_bp) {
        this.l_bp = l_bp;
    }

    public String getM_bp() {
        return m_bp;
    }

    public void setM_bp(String m_bp) {
        this.m_bp = m_bp;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }


    @Override
    public String toString() {
        return "BpTest{" +
                "IDCard='" + IDCard + '\'' +
                ", time='" + time + '\'' +
                ", h_bg='" + h_bg + '\'' +
                ", l_bp='" + l_bp + '\'' +
                ", m_bp='" + m_bp + '\'' +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
