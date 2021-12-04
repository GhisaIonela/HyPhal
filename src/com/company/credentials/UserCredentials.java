package com.company.credentials;

import com.company.domain.Entity;

/**
 * A class for user's credentials(email and paswword)
 */
public class UserCredentials extends Entity<Long> {
    private String email;
    private String password;

    /**
     * Creates a new UserCredentials object
     * @param email - user's email
     * @param password - user's password
     */
    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Get the user's email
     * @return - user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email
     * @param email - the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get user's password
     * @return - the user's paswword
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set user's password
     * @param password - user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
