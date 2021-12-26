package com.company.dto;

import com.company.utils.Constants;

import java.time.LocalDateTime;

public class UserFriendshipDTO
{
    private Long friendId;
    private String firstName;
    private String lastName;
    private LocalDateTime dateTime;
    public UserFriendshipDTO(Long friendId, String firstName, String lastName, LocalDateTime dateTime) {
        this.friendId = friendId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateTime = dateTime;
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
        return friendId + ' '+ firstName + '|' + lastName + '|' + dateTime.format(Constants.DATE_TIME_FORMATTER);
    }
}