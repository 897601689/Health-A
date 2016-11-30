package com.health_a.bean;


/**
 * Created by Admin on 2016/1/8. 用户信息
 */
public class UserInfo {

    private String IDCard;
    private String name;
    private String age;
    private String sex;
    private String phone;

    public UserInfo() {
        super();
    }

    public UserInfo(String IDCard, String name,String sex, String age,  String phone) {
        super();
        this.IDCard = IDCard;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "IDCard='" + IDCard + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
