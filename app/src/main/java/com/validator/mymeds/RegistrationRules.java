package com.validator.mymeds;

/**
 * Class is instantiated in RegistrationActivity.
 * Checks if users password is valid as they entered it.
 */
public class RegistrationRules {
    //Attributes
    private String email;
    private String password;
    private String name;
    private int numRules;

    //Constructor
    public RegistrationRules(String e, String p, String n) {
        email = e;
        password = p;
        name = n;
        numRules = 4;
    }

    //Set Methods
    public void setPassword(String p) {
        password = p;
    }

    public void setEmail(String e) {
        email = e;
    }

    public void setName(String n) {
        name = n;
    }

    //Get Methods
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    /**
     * Runs all created rules (specified below).
     * @return int equivalent to the number of rules passed.
     */
    public int runRules() {
        int numPassed = 0;
        if (emailNotEmpty()) {
            numPassed++;
        }
        if (containsAt()) {
            numPassed++;
        }
        if (passwordNotShort()) {
            numPassed++;
        }
        if (nameNotEmpty()) {
            numPassed++;
        }
        return numPassed;
    }


    /* Method for testing email */
    //Returns true if the email is not empty
    public boolean emailNotEmpty() {
        if (email == null) {
            return false;
        }
        else return true;
    }

    //Returns true if '@' symbol is within entered email.
    public boolean containsAt() {
            return email.contains("@");
        }


    /* Methods for testing password */
    //Returns true if the password is not empty
    public boolean passwordNotShort() {
        if (password.length() <= 8) {
            return false;
        }
        else return true;
    }

    /* Methods for testing name */
    //Returns true if name is not empty.
    public boolean nameNotEmpty() {
        if (name == null) {
            return false;
        }
        else return true;
    }
}
