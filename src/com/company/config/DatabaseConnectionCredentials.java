package com.company.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DatabaseConnectionCredentials {
    private String url;
    private String username;
    private String password;
    private static final DatabaseConnectionCredentials databaseConnectionCredentialsInstance = new DatabaseConnectionCredentials();

    /** Reads from the file "DatabaseConnectionCredentials.txt" the url, username and password
     * of a postgresql database connection
     * The file must contain the url, username and password in this order and separated lines
     */
    private DatabaseConnectionCredentials(){
        try {
            File configFile = new File("src/com/company/config/DatabaseConnectionCredentials.txt");
            Scanner myReader = new Scanner(configFile);

            url = myReader.nextLine();
            username = myReader.nextLine();
            password = myReader.nextLine();

            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the instance of DatabaseConnectionCredentials
     * @return the instance
     */
    public static DatabaseConnectionCredentials getInstance() { return databaseConnectionCredentialsInstance; }

    /**
     * Gets the url credential for the database connection
     * @return the url for the database connection
     */
    public String getUrl() { return url; }

    /**
     * Gets the username for the database connection
     * @return the username for the database connection
     */
    public String getUsername() { return username; }

    /**
     * Gets the password for the database connection
     * @return the password for the database connection
     */
    public String getPassword() { return password; }
}
