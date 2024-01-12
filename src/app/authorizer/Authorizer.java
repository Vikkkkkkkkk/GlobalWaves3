package app.authorizer;

import app.Admin;
import app.user.User;

public class Authorizer {
    private static Authorizer instance = null;

    private Authorizer() {
    }

    public static Authorizer getInstance() {
        if (instance == null) {
            instance = new Authorizer();
        }
        return instance;
    }

    public boolean existsUser(final String username) {
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
