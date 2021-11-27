package com.company.domain;

import java.util.Objects;

/**
 * Friendships is the class which models the friendship relation between two users
 */
public class Friendship extends Entity<Long> {
    private Long idUser1;
    private Long idUser2;

    /**
     * Constructs a new Friendship with users' ids
     * @param idUser1 - the id of the first user
     * @param idUser2 - the id of the second user
     */
    public Friendship(Long idUser1, Long idUser2) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
    }

    /**
     * Get the id of the second user in the friendship
     * @return id of the first user in the friendship
     */
    public Long getIdUser1() {
        return idUser1;
    }

    /**
     * Set the idUser1 to the value provided
     * @param idUser1 an id to be set which represents the id of the first user
     */
    public void setIdUser1(Long idUser1) {
        this.idUser1 = idUser1;
    }

    /**
     * Get the id of the second user in the friendship
     * @return id of the second user in the friendship
     */
    public Long getIdUser2() {
        return idUser2;
    }

    /**
     * Set the idUser2 to the value provided
     * @param idUser2 an id to be set which represents the id of the second user
     */
    public void setIdUser2(Long idUser2) {
        this.idUser2 = idUser2;
    }

    /**
     * Returns a string representation of the friendship.
     * @return a string representation for a friendship
     */
    @Override
    public String toString() {
        return "Friendship{" +
                "id='"+getId()+'\''+
                ", idUser1='" + idUser1 + '\'' +
                ", idUser2='" + idUser2 + '\'' +
                '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one
     * @param o - the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (getIdUser1().equals(that.getIdUser1()) && getIdUser2().equals(that.getIdUser2())) ||
                (getIdUser1().equals(that.getIdUser2())&& getIdUser2().equals(that.getIdUser1()));
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idUser1, idUser2);
    }

}
