package com.archql.labs.wtlab3.logics;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class Student implements Serializable {
    private String id;
    private String name, surname;
    private String groupNumber;

    private boolean isOnline;
    private int year;
    private double avgMark;

    public Student()
    {
        year = 1;
        avgMark = 0.0;
        isOnline = false;
    }

    public Student(String id, String name, String surname, String groupNumber)
    {
        this();
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.groupNumber = groupNumber;
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
    public static String toString(Student obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder e = new XMLEncoder(baos);
        e.writeObject(obj);
        e.close();
        return baos.toString();
    }

    public static String toSingleLineString(String str)
    {
        return str.replaceAll("\n", "<new line symbol>");
    }
    public static String toMultiLineString(String str)
    {
        return str.replaceAll("<new line symbol>", "\n");
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
