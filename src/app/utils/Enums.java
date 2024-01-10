package app.utils;

public class Enums { // different enums

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    public enum RepeatMode {
        REPEAT_ALL, REPEAT_ONCE, REPEAT_INFINITE, REPEAT_CURRENT_SONG, NO_REPEAT,
    }

    public enum PlayerSourceType {
        LIBRARY, PLAYLIST, PODCAST, ALBUM
    }

    public enum Status {
        ONLINE, OFFLINE
    }

    public enum UserType {
        USER, HOST, ARTIST
    }
}
