package com.company.dto;

import com.company.utils.Constants;

import java.time.LocalDateTime;

public class UserFriendshipDTO
{
    private String firstName;
    private String lastName;
    private LocalDateTime dateTime;
    public UserFriendshipDTO(String firstName, String lastName, LocalDateTime dateTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return firstName + '\t' + '|' + lastName + '\t' + '|' + dateTime.format(Constants.DATE_TIME_FORMATTER);
    }
}