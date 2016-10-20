package com.danylo.spokdbupdate;

public class User {
    private String name;
    private String email;
    private String password;
    private String RegistrationDate;
    private String type;

    public User(String name, String email, String password, String registrationDate, String type) {
        this.name = name;
        this.email = email;
        this.password = password;
        RegistrationDate = registrationDate;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistrationDate() {
        return RegistrationDate;
    }

    public String getType() {
        return type;
    }
}
