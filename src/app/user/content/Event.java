package app.user.content;

import lombok.Getter;


@Getter
public class Event {
    private String name;
    private String description;
    private String date;

    public Event() { }
    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
