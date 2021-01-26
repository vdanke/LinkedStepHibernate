package org.step.linked.step.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.step.linked.step.model.User.FIND_ALL_USERS_NATIVE;
import static org.step.linked.step.model.User.USER_POSTS_ENTITY_GRAPH;


@Entity
@Table(name = "users", indexes = {@Index(name = "IDX_USER_USERNAME", unique = true, columnList = "username")})
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = USER_POSTS_ENTITY_GRAPH,
                attributeNodes = {
                        @NamedAttributeNode(value = "posts")
                }
//                subgraphs = {@NamedSubgraph(name = "", attributeNodes = {@NamedAttributeNode("")})}
        )
})
@NamedNativeQueries(value = {
        @NamedNativeQuery(
                name = FIND_ALL_USERS_NATIVE,
                query = "SELECT ID, USERNAME FROM USERS",
                resultClass = User.class,
                hints = @QueryHint(name = QueryHints.FETCH_SIZE, value = "50")
        )
})
//@NamedQueries(value = {
//        @NamedQuery(
//                name = "",
//                query = ""
//        )
//})
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
@Cacheable(value = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User {

    public static final String USER_POSTS_ENTITY_GRAPH = "user[posts]";
    public static final String FIND_ALL_USERS_NATIVE = "native[find_all]";

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
    @NotNull(message = "Username should not be null")
    @Size(min = 5, max = 1024)
    @Email(message = "Not valid email")
    private String username;

    @Column(name = "password", nullable = false, length = 512)
    @NotBlank(message = "Password should not be blank")
    private String password;

    @Column(name = "age")
    @Min(value = 16)
    @Max(value = 100)
    private int age;

    @Column(name = "filename")
    private String filename;

    @Lob
    @Column(name = "file")
    private String file;

    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "user_authorities_fk"))
    )
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.LAZY)
    @Enumerated(value = EnumType.STRING)
//    @BannVerifier(authorities = {Authority.ROLE_ADMIN, Authority.ROLE_USER})
    private Set<Authority> authorities = new HashSet<>();
    /*
    FetchType.LAZY - при выборке юзеров не будет загружать профиля (ставится всегда, за исключением определенных случаев)
    FetchType.EAGER - при выборке юзеров будет загружать сразу профиля
     */
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
//            orphanRemoval = true
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Profile profile;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    // EntityGraph
    // @BatchSize(size = 15)
    // @Fetch(FetchMode.SUBSELECT)
    // join fetch u.posts
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

//    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Set<CourseRating> courses = new HashSet<>();

    @PrePersist
    public void prePersist() {
        System.out.println("Persist: " + this.id);
    }

    @PreRemove
    public void preRemove() {
        System.out.println("Remove");
    }

    @PreUpdate
    public void preUpdate() {
        System.out.println("Update");
    }


    public User() {
    }

    private User(String id,
                 String username,
                 String password,
                 Profile profile,
                 Set<Post> posts,
                 Set<CourseRating> courses,
                 int age,
                 Set<Authority> authorities,
                 String filename,
                 String file) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profile = profile;
        this.posts = posts;
        this.courses = courses;
        this.age = age;
        this.authorities = authorities;
        this.filename = filename;
        this.file = file;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public void addProfile(Profile profile) {
        this.setProfile(profile);
        profile.setUser(this);
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setUser(this);
    }

    public static class UserBuilder {
        private String id;
        private String username;
        private String password;
        private Profile profile;
        private int age;
        private String filename;
        private Set<Post> posts = new HashSet<>();
        private Set<CourseRating> courses = new HashSet<>();
        private Set<Authority> authorities = new HashSet<>();
        private String file;

        UserBuilder() {
        }

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

        public UserBuilder posts(Set<Post> posts) {
            this.posts = posts;
            return this;
        }

        public UserBuilder age(int age) {
            this.age = age;
            return this;
        }

        public UserBuilder authorities(Set<Authority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserBuilder courses(Set<CourseRating> courses) {
            this.courses = courses;
            return this;
        }

        public UserBuilder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public UserBuilder file(String file) {
            this.file = file;
            return this;
        }

        public User build() {
            return new User(id, username, password, profile, posts, courses, age, authorities, filename, file);
        }
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
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

    public Set<CourseRating> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseRating> courses) {
        this.courses = courses;
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
