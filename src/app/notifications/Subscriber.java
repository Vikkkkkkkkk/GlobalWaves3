package app.notifications;

public interface Subscriber {
    /**
     * Update the subscriber with a notification.
     *
     * @param message the message
     * @param username the username of the content creator
     */
    void update(String message, String username);
}
