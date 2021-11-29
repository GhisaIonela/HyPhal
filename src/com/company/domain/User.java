package com.company.domain;

import com.company.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * User is the class which models a user
 */
public class User extends Entity<Long>{
    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private LocalDateTime dateOfBirth;

    /**
     * Constructs a new user with first name and last name
     * @param firstName user's first name
     * @param lastName user's last name
     * @param email  user's email address
     * @param city user's city
     * @param dateOfBirth user's date of birth
     */
    public User(String email, String firstName, String lastName, String city, LocalDateTime dateOfBirth) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get the user's email address
     * @return the user's first name
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     * @param email - an email to be set for user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's city
     * @return the user's first name
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the user's city
     * @param city - a city to be set for user
     */
    public void setCity(String city) {
        this.city = city;
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
     * Get the user's date of birth
     * @return the user's date of birth
     */
    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set the user's date of birth
     * @param dateOfBirth - a date of birth to be set for user
     */
    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns a string representation of the user.
     * @return a string representation for a user
     */
    @Override
    public String toString() {
        return "User{" +
                    "id='"+ getId().toString() + '\''+
                    ", email='"+ email + '\''+
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", city='" + city + '\'' +
                    ", dateOfBirth='" + dateOfBirth.format(Constants.DATE_TIME_FORMATTER) +
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
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return  getEmail().equals(u.getEmail())&&
                getFirstName().equals(u.getFirstName()) &&
                getLastName().equals(u.getLastName())&&
                getCity().equals(u.getCity())&&
                getDateOfBirth().equals(u.getDateOfBirth());
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
