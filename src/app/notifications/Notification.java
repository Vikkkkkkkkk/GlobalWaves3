package app.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
    private String message;
    private String username;

    public Notification(String message, String username) {
        this.message = message;
        this.username = username;
    }

    public String printNotification() {
        return message + " from " + username + ".";
    }
}
