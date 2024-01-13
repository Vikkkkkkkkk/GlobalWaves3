package app.notifications;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {
    private String message;
    private String username;

    public Notification(final String message, final String username) {
        this.message = message;
        this.username = username;
    }

    /**
     * Prints the notification.
     *
     * @return the notification
     */
    public String printNotification() {
        return message + " from " + username + ".";
    }
}
