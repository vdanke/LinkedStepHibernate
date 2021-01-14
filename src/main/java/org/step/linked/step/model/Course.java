package org.step.linked.step.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    private String id;
    @Column(name = "topic", unique = true, nullable = false)
    private String topic;
    @Column(name = "description")
    private String description;
    @Column(name = "author")
    private String author;
//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST
////            CascadeType.DETACH, CascadeType.MERGE, , CascadeType.REFRESH
//    )
//    @JoinTable(
//            name = "users_courses",
//            joinColumns = @JoinColumn(name = "course_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"),
//            foreignKey = @ForeignKey(name = "course_user_fk"),
//            inverseForeignKey = @ForeignKey(name = "user_course_fk")
//    )
    @OneToMany(mappedBy = "user")
    private Set<CourseRating> users = new HashSet<>();

    public Course() {
    }

    private Course(String id, String topic, String description, String author, Set<CourseRating> users) {
        this.id = id;
        this.topic = topic;
        this.description = description;
        this.author = author;
        this.users = users;
    }

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public static class CourseBuilder {
        private String id;
        private String topic;
        private String description;
        private String author;
        private Set<CourseRating> users = new HashSet<>();

        CourseBuilder() {}

        public CourseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public CourseBuilder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public CourseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CourseBuilder author(String author) {
            this.author = author;
            return this;
        }

        public CourseBuilder users(Set<CourseRating> users) {
            this.users = users;
            return this;
        }

        public Course build() {
            return new Course(id, topic, description, author, users);
        }
    }

    public Set<CourseRating> getUsers() {
        return users;
    }

    public void setUsers(Set<CourseRating> users) {
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
