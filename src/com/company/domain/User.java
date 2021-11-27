package com.company.domain;

import java.util.List;
import java.util.Objects;

/**
 * User is the class which models a user
 */
public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<User> friends;

    /**
     * Constructs a new user with first name and last name
     * @param firstName user's first name
     * @param lastName user's last name
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Get the user's first name
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the user's last name
     * @param firstName - a first name to be set for user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the user's last name
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the user's last name
     * @param lastName - a last name to be set for user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get a list with all the user's friends
     * @return a list with all the user's friends
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * Set user's friends
     * @param friends a list with users to be set as friends
     */
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }


    /**
     * Returns a string representation of the user.
     * @return a string representation for a user
     */
    @Override
    public String toString() {
        if (friends!=null){
            return "User{" +
                    "id='"+ getId().toString() + '\''+
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", friends=" + friends +
                    '}';
        }
        else{
            return "User{" +
                    "id='"+ getId().toString() + '\''+
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName +
                    '}';
        }
    }

    /**
     * Indicates whether some other object is "equal to" this one
     * @param o - the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return getFirstName().equals(u.getFirstName()) &&
                getLastName().equals(u.getLastName());
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        //return Objects.hash(getFirstName(), getLastName(), getFriends());
        return Objects.hash(getFirstName(), getLastName());
    }
}
