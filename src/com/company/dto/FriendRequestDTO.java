package com.company.dto;

public class FriendRequestDTO {
    private Long fromId;
    private String fromFirstName;
    private String fromLastName;
    private String fromEmail;
    private Long toId;
    private String toFirstName;
    private String toLastName;
    private String toEmail;

    public FriendRequestDTO(Long fromId, String fromFirstName, String fromLastName, String fromEmail, Long toId, String toFirstName, String toLastName, String toEmail) {
        this.fromId = fromId;
        this.fromFirstName = fromFirstName;
        this.fromLastName = fromLastName;
        this.fromEmail = fromEmail;
        this.toId = toId;
        this.toFirstName = toFirstName;
        this.toLastName = toLastName;
        this.toEmail = toEmail;
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public String getFromLastName() {
        return fromLastName;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getToFirstName() {
        return toFirstName;
    }

    public Long getToId() {
        return toId;
    }

    public String getToLastName() {
        return toLastName;
    }

    public String getToEmail() {
        return toEmail;
    }

    @Override
    public String toString() {
        return fromId.toString() + ' ' + fromFirstName + ' ' + fromLastName + ' ' + fromEmail + " -> " + toId.toString() + ' ' + toFirstName + ' ' + toLastName + ' ' + toEmail;
    }

    public String senderToString() {
        return fromFirstName + ' ' + fromLastName + ' ' + fromEmail;
    }

    public String receiverToString() {
        return toFirstName + ' ' + toLastName + ' ' + toEmail;
    }
}