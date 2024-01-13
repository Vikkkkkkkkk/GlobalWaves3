package app.notifications;

public interface ContentCreator {
    /**
     * Adds a subscriber.
     *
     * @param subscriber the subscriber
     */
    void addSubscriber(Subscriber subscriber);

    /**
     * Removes a subscriber.
     *
     * @param subscriber the subscriber
     */
    void removeSubscriber(Subscriber subscriber);

    /**
     * Notifies all subscribers.
     *
     * @param message the message
     * @param username the username of the content creator
     */
    void notifySubscribers(String message, String username);
}
