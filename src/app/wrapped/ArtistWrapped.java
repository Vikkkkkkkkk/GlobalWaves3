package app.wrapped;

import app.audio.Files.Song;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class ArtistWrapped extends UserWrapped {
    private HashMap<String, Integer> fans = new HashMap<>();
    private Integer listeners;

    public ArtistWrapped() {
        super();
        listeners = 0;
    }
    public void incrementListeners() {
        listeners++;
    }

    public void addListen(Song song, String username) {
        setWasUpdated(true);
        int value = 0;

        if (getSongs().containsKey(song.getName())) {
            value = getSongs().get(song.getName());
        }
        value++;
        updateSong(song.getName(), value);

        value = 0;
        if (fans.containsKey(username)) {
            value = fans.get(username);
        } else {
            incrementListeners();
        }
        value++;
        fans.put(username, value);

        value = 0;
        if (getAlbums().containsKey(song.getAlbum())) {
            value = getAlbums().get(song.getAlbum());
        }
        value++;
        updateAlbum(song.getAlbum(), value);
    }
}
