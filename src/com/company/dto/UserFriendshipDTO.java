package com.company.dto;

import com.company.utils.Constants;

import java.time.LocalDateTime;

public class UserFriendshipDTO
{
    private Long friendId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime dateTime;
    private String info;
    public UserFriendshipDTO(String firstName, String lastName, String email, LocalDateTime dateTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateTime = dateTime;
        this.email = email;
        this.info = firstName + " " + lastName + "\n" + email;
    }

    public String getEmail(){
        return email;
    }

    public String getInfo(){
        return info;
    }

    public Long getFriendId() {
        return friendId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return firstName + '\t' + '|' + lastName + '\t' + '|' + email + '\t' + '|' + dateTime.format(Constants.DATE_TIME_FORMATTER);
    }
}