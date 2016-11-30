package com.health_a.bean;

/**
 * Created by Admin on 2016/1/11.尿机测试数据
 */
public class UmTest {

    private String IDCard;
    private String time;
    private String LEU;
    private String NIT;
    private String UBG;
    private String PRO;
    private String PH;
    private String SG;
    private String BLD;
    private String KET;
    private String BIL;
    private String GLU;
    private String VC;
    private String doctor;

    public UmTest() {
        super();
    }

    public UmTest(String IDCard, String time, String LEU, String NIT,
                  String UBG, String PRO, String PH, String SG, String BLD,
                  String KET, String BIL, String GLU, String VC, String doctor) {
        super();
        this.IDCard = IDCard;
        this.time = time;
        this.LEU = LEU;
        this.NIT = NIT;
        this.UBG = UBG;
        this.PRO = PRO;
        this.PH = PH;
        this.SG = SG;
        this.BLD = BLD;
        this.KET = KET;
        this.BIL = BIL;
        this.GLU = GLU;
        this.VC = VC;
        this.doctor = doctor;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getTime() {
        return time;
    }

    public String getLEU() {
        return LEU;
    }

    public String getNIT() {
        return NIT;
    }

    public String getUBG() {
        return UBG;
    }

    public String getPRO() {
        return PRO;
    }

    public String getPH() {
        return PH;
    }

    public String getSG() {
        return SG;
    }

    public String getBLD() {
        return BLD;
    }

    public String getKET() {
        return KET;
    }

    public String getBIL() {
        return BIL;
    }

    public String getGLU() {
        return GLU;
    }

    public String getVC() {
        return VC;
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

    public void setLEU(String LEU) {
        this.LEU = LEU;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public void setUBG(String UBG) {
        this.UBG = UBG;
    }

    public void setPRO(String PRO) {
        this.PRO = PRO;
    }

    public void setPH(String PH) {
        this.PH = PH;
    }

    public void setSG(String SG) {
        this.SG = SG;
    }

    public void setBLD(String BLD) {
        this.BLD = BLD;
    }

    public void setKET(String KET) {
        this.KET = KET;
    }

    public void setBIL(String BIL) {
        this.BIL = BIL;
    }

    public void setGLU(String GLU) {
        this.GLU = GLU;
    }

    public void setVC(String VC) {
        this.VC = VC;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "UmTest{" +
                "IDCard='" + IDCard + '\'' +
                ", time='" + time + '\'' +
                ", LEU='" + LEU + '\'' +
                ", NIT='" + NIT + '\'' +
                ", UBG='" + UBG + '\'' +
                ", PRO='" + PRO + '\'' +
                ", PH='" + PH + '\'' +
                ", SG='" + SG + '\'' +
                ", BLD='" + BLD + '\'' +
                ", KET='" + KET + '\'' +
                ", BIL='" + BIL + '\'' +
                ", GLU='" + GLU + '\'' +
                ", VC='" + VC + '\'' +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
