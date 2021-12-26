package com.company.domain;

import java.time.LocalDateTime;

/**
 * FriendRequest is a class which models a friend request
 */
public class FriendRequest extends Entity<Long>{
    private Long idFrom;
    private Long idTo;
    private FriendRequestStatus status;

    /**
     * Constructs a new pending FriendRequest
     * @param idFrom - the id of the user that sends the friend request
     * @param idTo - the id of the user the friend request is send to
     */
    public FriendRequest(Long idFrom, Long idTo) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.status = FriendRequestStatus.pending;
    }

    /**
     * Constructs a new FriendRequest
     * @param idFrom - the id of the user that sends the friend request
     * @param idTo - the id of the user the friend request is send to
     * @param status - the status of the friend request
     */
    public FriendRequest(Long idFrom, Long idTo, FriendRequestStatus status) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.status = status;
    }

    /**
     *Get the id of the user that is sendind the friend request
     * @return the id of the user that is sendind the friend request
     */
    public Long getIdFrom() {
        return idFrom;
    }

    /**
     * Set the id of the user that is sendind the friend request
     * @param idFrom the id of the user that is sendind the friend request
     */
    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    /**
     *Get the id of the user the friend request is send to
     * @return the id of the user the friend request is send to
     */
    public Long getIdTo() {
        return idTo;
    }

    /**
     *Set the id of the user the friend request is send to
     * @param idTo - the id of the user the friend request is send to
     */
    public void setIdTo(Long idTo) {
        this.idTo = idTo;
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

    /**
     * Returns a string representation of the friend request
     * @return a string representation for a friend request
     */
    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + getId() +
                ", idFrom=" + idFrom +
                ", idTo=" + idTo +
                ", status=" + status +
                '}';
    }
}
