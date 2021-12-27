package com.company.dto;

public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String fullInfo;

    public UserDTO(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullInfo = firstName + " " + lastName + "\n" + email;
    }

    public String getFullInfo() {
        return fullInfo;
    }

    public void setFullInfo(String fullInfo) {
        this.fullInfo = fullInfo;
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

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String toString() {
        return firstName + '\'' + lastName + '\n' + email;

    }
}
