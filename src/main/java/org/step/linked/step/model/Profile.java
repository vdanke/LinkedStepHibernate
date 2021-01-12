package org.step.linked.step.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    private String id;
    @Column(name = "description")
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "profile_user_fk"))
    private User user;

    public Profile() {
    }

    private Profile(String id, String description, User user) {
        this.id = id;
        this.description = description;
        this.user = user;
    }

    public static ProfileBuilder builder() {
        return new ProfileBuilder();
    }

    public static class ProfileBuilder {
        private String id;
        private String description;
        private User user;

        ProfileBuilder() {}

        public ProfileBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ProfileBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProfileBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Profile build() {
            return new Profile(id, description, user);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
