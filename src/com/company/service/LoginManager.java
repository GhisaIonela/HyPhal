package com.company.service;

import com.company.credentials.VerifyPassword;
import com.company.domain.User;
import com.company.exceptions.LoginException;
import com.company.repository.db.UserDbRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * LoginManager class manages the process of user's login
 */
public class LoginManager {
    private UserDbRepository userDbRepository;
    private User logged;

    /**
     * Constructs a new LoginManager
     * @param userDbRepository
     */
    public LoginManager(UserDbRepository userDbRepository) {
        this.userDbRepository = userDbRepository;
        this.logged = null;
    }

    /**
     * Gets the logged user
     * @return the logged user
     */
    public User getLogged(){
        return logged;
    }

    /**
     * Verify if the user who wants to log has an account
     * @param email - user's email
     * @return the user if the user's email is found in database, null otherwise
     */
    private User findUserToLog(String email){
        return userDbRepository.findUserByEmail(email);
    }

    /**
     * Verify if the typed password for login matches the user's password stored in databse
     * @param password - the typed password
     * @param encryptedPassword - the password stored in database
     * @return true if the passwords match, false otherwise
     */
    private boolean verifyPasswordMatch(String password, String encryptedPassword){
        try {
            return VerifyPassword.match(password, encryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Proceed the login for a user
     * @param email - user's email
     * @param password - user's password
     */
    public void login(String email, String password){
        User toLog = findUserToLog(email);
        if(toLog!=null){
            if(verifyPasswordMatch(password, toLog.getUserCredentials().getPassword())){
                logged = toLog;
            }
            else
                throw new LoginException("Incorrect password");
        }else
            throw new LoginException("There's no account associated with this email");
    }

    /**
     * Tell if a user is logged
     * @return true if is logged, false otherwise
     */
    public boolean isLogged(){
        return getLogged()!= null;
    }

    /**
     * Logout a user
     */
    public void logOut(){
        logged = null;
    }

}