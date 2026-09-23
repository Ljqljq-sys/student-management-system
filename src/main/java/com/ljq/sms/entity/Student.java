package com.ljq.sms.entity;

public class Student {
    private String id;
    private String name;
    private int age;
    private String major;
    private double score;

    public Student() {
    }

    public Student( String id,String name, int age, String major, double score) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.score = score;
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


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    //告诉java我这个对象应该怎么被描述
    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ",name='" + name + '\'' +
                ", age=" + age +
                ", major='" + major + '\'' +
                ", score=" + score +
                '}';
    }
}

