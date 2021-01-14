package org.step.linked.step.model;

import javax.persistence.*;

@Entity
@Table(name = "course_rating_composite")
public class CourseRatingComposite {

    @EmbeddedId
    private CourseRatingKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseRatingComposite() {
    }

    public CourseRatingKey getId() {
        return id;
    }

    public void setId(CourseRatingKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
