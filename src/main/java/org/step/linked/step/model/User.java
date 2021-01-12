package org.step.linked.step.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", indexes = {@Index(name = "IDX_USER_USERNAME", unique = true, columnList = "username")})
public class User {

    // GenerationType.TABLE - hibernate is responsible for generation ids by hibernate_sequence
    // GenerationType.IDENTITY - table must have auto increment property on id column
    // GenerationType.SEQUENCE - (@SequenceGenerator) - hibernate creates sequence as we declared
    // or it is uses our sequence which we have created (1 ... 1_000_000_000)
    // GenerationType.AUTO - allow hibernate decide what strategy to use (not recommended)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
//    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @Id
    private String id;
    // По дефолту Хибернейт генерирует название колонки из названия поля
    @Column(name = "username", unique = true, nullable = false, length = 312)
    private String username;
    @Column(name = "password", nullable = false, length = 512)
    private String password;
    /*
    FetchType.LAZY - при выборке юзеров не будет загружать профиля (ставится всегда, за исключением определенных случаев)
    FetchType.EAGER - при выборке юзеров будет загружать сразу профиля
     */
    @OneToOne(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
//            orphanRemoval = true
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;

    public User() {
    }

    private User(String id, String username, String password, Profile profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public void addProfile(Profile profile) {
        this.setProfile(profile);
        profile.setUser(this);
    }

    public static class UserBuilder {
        private String id;
        private String username;
        private String password;
        private Profile profile;

        UserBuilder() {}

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public User build() {
            return new User(id, username, password, profile);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
