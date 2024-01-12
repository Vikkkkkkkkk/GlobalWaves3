package app.notifications;

public interface ContentCreator {
    void addSubscriber(Subscriber subscriber);
    void removeSubscriber(Subscriber subscriber);
    void notifySubscribers(String message, String username);
}
