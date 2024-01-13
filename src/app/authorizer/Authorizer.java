package app.authorizer;

import app.Admin;
import app.user.User;

public final class Authorizer {
    private static Authorizer instance = null;

    private Authorizer() {
    }

    /**
     * Gets the instance of the Authorizer class.
     *
     * @return the instance
     */
    public static Authorizer getInstance() {
        if (instance == null) {
            instance = new Authorizer();
        }
        return instance;
    }

    /**
     * Checks if the user exists in the database.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean existsUser(final String username) {
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user is a normal user.
     *
     * @param username the username
     * @return the boolean
     */
    public boolean isNormalUser(final String username) {
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                if (user.getType().equals("USER")) {
                    return true;
                }
            }
        }
        return false;
    }
}
