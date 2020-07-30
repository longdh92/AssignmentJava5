package com.vn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admin")
@Getter
@Setter
public class Admin implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAdmin")
    private Long idAdmin;

    @Column(name = "usernameAdmin", unique = true)
    private String usernameAdmin;

    @Column(name = "passwordAdmin")
    private String passwordAdmin;

    @Column(name = "emailAdmin")
    private String emailAdmin;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private boolean role;

    @Override
    public String toString() {
        return "Admin{" +
                "username='" + usernameAdmin + '\'' +
                '}';
    }
}
