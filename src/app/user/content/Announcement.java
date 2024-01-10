package app.user.content;

import lombok.Getter;

@Getter
public class Announcement {
    private String name;
    private String description;

    public Announcement() { }

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
