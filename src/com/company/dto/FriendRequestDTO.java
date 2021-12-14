package com.company.dto;

public class FriendRequestDTO {
    private String fromFirstName;
    private String fromLastName;
    private String fromEmail;
    private String toFirstName;
    private String toLastName;
    private String toEmail;

    public FriendRequestDTO(String fromFirstName, String fromLastName, String fromEmail, String toFirstName, String toLastName, String toEmail) {
        this.fromFirstName = fromFirstName;
        this.fromLastName = fromLastName;
        this.fromEmail = fromEmail;
        this.toFirstName = toFirstName;
        this.toLastName = toLastName;
        this.toEmail = toEmail;
    }

    @Override
    public String toString() {
        return fromFirstName + ' ' + fromLastName + ' ' + fromEmail + " -> " + toFirstName + ' ' + toLastName + ' ' + toEmail;
    }

    public String senderToString() {
        return fromFirstName + ' ' + fromLastName + ' ' + fromEmail;
    }

    public String receiverToString() {
        return toFirstName + ' ' + toLastName + ' ' + toEmail;
    }
}
