package com.health_a.bean;

/**
 * Created by Admin on 2016/1/11. 医生信息
 */
public class DoctorInfo {

    private String ID;
    private String name;
    private String sex;
    private String password;
    private String level;

    public DoctorInfo() {
        super();
    }

    public DoctorInfo(String name, String sex, String password, String level) {
        super();
        this.name = name;
        this.sex = sex;
        this.password = password;
        this.level = level;
    }
    public DoctorInfo(String ID , String name, String sex, String password, String level) {
        super();
        this.ID = ID;
        this.name = name;
        this.sex = sex;
        this.password = password;
        this.level = level;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getPassword() {
        return password;
    }

    public String getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
