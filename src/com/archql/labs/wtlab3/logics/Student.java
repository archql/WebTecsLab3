package com.archql.labs.wtlab3.logics;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Student implements Serializable {
    private String id;
    private String name, surname, passportId, passportNo;
    private String groupNumber;

    private boolean isOnline;
    private int year;
    private double avgMark;

    Student(String id, String name, String surname, String passportId, String passportNo, String groupNumber)
    {
        this.name = name;
        this.surname = surname;
        this.passportId = passportId;
        this.passportNo = passportNo;
        this.groupNumber = groupNumber;
        this.id = id;

        year = 1;
        avgMark = 0.0;
        isOnline = false;
    }

    public String getId() {
        return id;
    }

    public static Student fromString(String str)
    {
        XMLDecoder d = new XMLDecoder(new ByteArrayInputStream(str.getBytes()));
        Student obj = (Student) d.readObject();
        d.close();
        return obj;
    }
    static String toString(Student obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder e = new XMLEncoder(baos);
        e.writeObject(obj);
        e.close();
        return baos.toString();
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(double avgMark) {
        this.avgMark = avgMark;
    }
}
