package org.step.linked.step.model;

import javax.persistence.*;

@Entity
@Table(name = "user_courses")
public class CourseRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    public Course course;
    @Column(name = "rating")
    public Double rating;

    public CourseRating() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
