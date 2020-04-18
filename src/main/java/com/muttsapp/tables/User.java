package com.muttsapp.tables;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int userId;
    @Column(columnDefinition = "boolean default 1")
    Boolean enabled;
//    @NotNull
    String email;
//    @NotNull
    String firstName;
//    @NotNull
    String lastName;
//    @NotNull
    String password;
//    @NotNull
    String photoUrl;
//    @NotNull
    String userName;
    @Column(columnDefinition = "int default 1")
    int roleId;

    public User() {
    }

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = userId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
