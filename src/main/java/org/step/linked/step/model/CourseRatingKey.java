package org.step.linked.step.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CourseRatingKey implements Serializable {

    @Column(name = "user_id")
    private String userId;
    @Column(name = "course_id")
    private String courseId;

    public CourseRatingKey() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
