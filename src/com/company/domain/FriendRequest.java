package com.company.domain;

import java.time.LocalDateTime;

/**
 * FriendRequest is a class which models a friend request
 */
public class FriendRequest extends Entity<Long>{
    private Long from;
    private Long to;
    private FriendRequestStatus status;

    /**
     * Constructs a new pending FriendRequest
     * @param from - the id of the user that sends the friend request
     * @param to - the id of the user the friend request is send to
     */
    public FriendRequest(Long from, Long to) {
        this.from = from;
        this.to = to;
        this.status = FriendRequestStatus.pending;
    }

    /**
     *Get the id of the user that is sendind the friend request
     * @return the id of the user that is sendind the friend request
     */
    public Long getFrom() {
        return from;
    }

    /**
     * Set the id of the user that is sendind the friend request
     * @param from the id of the user that is sendind the friend request
     */
    public void setFrom(Long from) {
        this.from = from;
    }

    /**
     *Get the id of the user the friend request is send to
     * @return the id of the user the friend request is send to
     */
    public Long getTo() {
        return to;
    }

    /**
     *Set the id of the user the friend request is send to
     * @param to - the id of the user the friend request is send to
     */
    public void setTo(Long to) {
        this.to = to;
    }

    /**
    *Get the status of the friend request
     * @return status - the status of the friend request
     */
    public FriendRequestStatus getStatus() {
        return status;
    }

    /**
    *Set the status of the friend request
     * @param status - the status of the friend request
     */
    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }
}
