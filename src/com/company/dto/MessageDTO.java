package com.company.dto;

public class MessageDTO {
    private String from;
    private String message;
    private String date;
    private String id;

    public MessageDTO(String from, String message, String date, String id) {
        this.from = from;
        this.message = message;
        this.date = date;
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
