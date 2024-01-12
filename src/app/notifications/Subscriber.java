package app.notifications;

public interface Subscriber {
    void update(String message, String username);
}
