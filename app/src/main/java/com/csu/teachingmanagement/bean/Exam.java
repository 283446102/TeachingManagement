package com.csu.teachingmanagement.bean;

import cn.bmob.v3.BmobObject;

/**
 * @author
 * @Date 2020-04-10 9:24
 * 功能：
 */
public class Exam extends BmobObject {
    //试题名称
    private String examName;
    //考试截止时间
    private String endTime;
    //课程id
    private String courseId;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
