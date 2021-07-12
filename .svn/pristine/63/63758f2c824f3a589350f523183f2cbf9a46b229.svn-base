package com.wolfking.jeesite.modules.api.entity.md;

import com.kkl.kklplus.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class RestEngineer {
    private String id;
    private String name = "";
    private String photo = "";
    private double balance;
    private int orderCount;
    private int planCount;
    private int breakCount;
    private double grade;
    private String phone;
    private String address;
    private List<RestArea> areaList;
    private String areaNames;
    private String servicePointName;


    public void setAreaNames(String areaNames) {
        this.areaNames = areaNames;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getPlanCount() {
        return planCount;
    }

    public void setPlanCount(int planCount) {
        this.planCount = planCount;
    }

    public int getBreakCount() {
        return breakCount;
    }

    public void setBreakCount(int breakCount) {
        this.breakCount = breakCount;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RestArea> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<RestArea> areaList) {
        this.areaList = areaList;
        List<String> nameList = areaList.stream().map(t -> t.getName()).collect(Collectors.toList());
        this.areaNames = StringUtils.join(nameList, "、");
    }

    public String getAreaNames() {
        return areaNames;
    }

    public String getServicePointName() {
        return servicePointName;
    }

    public void setServicePointName(String servicePointName) {
        this.servicePointName = servicePointName;
    }
}
