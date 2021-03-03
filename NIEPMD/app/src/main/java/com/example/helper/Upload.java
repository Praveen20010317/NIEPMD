package com.example.helper;

import java.io.Serializable;

public class Upload implements Serializable {

    public String userName;
    public String phone1;
    public String key1;
    public String roomno;
    public String phone;
    public String issue;
    public String dateTime;
    public String other;
    public String key;
    public String AssignEmployee;
    public String url;
    public String status;
    public String time;
    public String EmpUserName;
    public String EmpId;
    public String EmpPhone;

    public Upload() {

    }
    public Upload(String empUserName, String empId, String empPhone) {
        EmpUserName = empUserName;
        EmpId = empId;
        EmpPhone = empPhone;
    }

    public Upload(String userName, String phone1) {
        this.userName = userName;
        this.phone1 = phone1;
    }

    public Upload(String roomno, String phone, String issue, String dateTime, String other, String url, String status, String assignEmployee, String time) {
        this.roomno = roomno;
        this.phone = phone;
        this.issue = issue;
        this.dateTime = dateTime;
        this.other = other;
        this.url = url;
        this.status = status;
        this.AssignEmployee = assignEmployee;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssign() {
        return AssignEmployee;
    }

    public void setAssign(String assignEmployee) {
        this.AssignEmployee = assignEmployee;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

}
