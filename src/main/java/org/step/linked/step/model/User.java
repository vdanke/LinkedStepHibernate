package org.step.linked.step.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    // GenerationType.TABLE - hibernate is responsible for generation ids by hibernate_sequence
    // GenerationType.IDENTITY - table must have auto increment property on id column
    // GenerationType.SEQUENCE - (@SequenceGenerator) - hibernate creates sequence as we declared
    // or it is uses our sequence which we have created (1 ... 1_000_000_000)
    // GenerationType.AUTO - allow hibernate decide what strategy to use (not recommended)
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
//    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    private String id;
    private String username;

    public User() {
    }

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
