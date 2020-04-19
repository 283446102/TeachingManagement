package com.csu.teachingmanagement.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author
 * @Date 2020-04-11 10:47
 * 功能：
 */
public class StudentSignInBean extends BmobObject {
    private String day;
    private String name;
    private String courseId;
    private String signTime;
    private String student;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
