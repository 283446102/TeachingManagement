package com.csu.teachingmanagement.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author
 * @Date 2020-04-10 13:06
 * 功能：
 */
public class BrowseBean extends BmobObject {
    private String day;
    private String course;
    private String username;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
