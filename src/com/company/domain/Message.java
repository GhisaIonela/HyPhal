package com.company.domain;

import com.company.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Message class
 */
public class Message extends Entity<Long>{
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime dataTime;
    private Message replay;

    /**
     * Constructs a new message
     * @param from - user sender
     * @param to - user receiver
     * @param message - the text message
     */
    public Message(User from, List<User> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.dataTime = LocalDateTime.now();
        this.replay = null;
    }

    /**
     * Get sender
     * @return - the sender
     */
    public User getFrom() {
        return from;
    }

    /**
     * Set user sender
     * @param from - user sender
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * Get receivers
     * @return - the receivers
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * Set receivers
     * @param to - the receivers
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * Get the message
     * @return - the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message
     * @param message - the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the date and time of the message
     * @return - date and time
     */
    public LocalDateTime getDateTime() {
        return dataTime;
    }

    /**
     * Set the messege date time
     * @param dataTime - messsage date time
     */
    public void setDateTime(LocalDateTime dataTime) {
        this.dataTime = dataTime;
    }

    /**
     *  Get the replayed to  message
     * @return - the replayed to
     */
    public Message getReplay() {
        return replay;
    }

    /**
     * Set a message to replay to
     * @param replay - the message to replay
     */
    public void setReplay(Message replay) {
        this.replay = replay;
    }

    /**
     * Constructs a string representation of the receivers
     * @return - the receivers
     */
    private String toStringReceivers(){
        StringBuilder stringBuilder = new StringBuilder();
        to.forEach(receiver -> stringBuilder.append(receiver.getEmail() + " " + receiver.getFirstName() + " " + receiver.getLastName() + " | "));
        stringBuilder.delete(stringBuilder.length()-3, stringBuilder.length());
        return stringBuilder.toString();
    }

    /**
     * Constructs a string representation of the message
     * @return - the string message
     */
    @Override
    public String toString() {
        if (replay!=null){
            return
                    "From: " + from.getFirstName() + " " + from.getLastName() + " " + from.getEmail() + "\n"+
                    "To: " + toStringReceivers() + "\n"+
                    "Message: " + message + "\n" +
                    "Replayed to: " + replay.getMessage() + "\n" +
                    "Date: " + dataTime.format(Constants.DATE_TIME_FORMATTER) ;
        }
        return
                "From: " + from.getFirstName() + " " + from.getLastName() + " " + from.getEmail() + "\n"+
                        "To: " + toStringReceivers() + "\n"+
                        "Message: " + message + "\n" +
                        "Date: " + dataTime.format(Constants.DATE_TIME_FORMATTER) ;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(getId(), message1.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
