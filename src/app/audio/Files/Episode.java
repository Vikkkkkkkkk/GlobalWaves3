package app.audio.Files;

import lombok.Getter;
import lombok.Setter;

@Getter
public final class Episode extends AudioFile {
    private final String description;
    @Setter
    private String owner;

    public Episode(final String name, final Integer duration, final String description) {
        super(name, duration);
        this.description = description;
    }
}
