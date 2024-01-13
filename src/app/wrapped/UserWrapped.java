package app.wrapped;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.Host;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class UserWrapped {
    private Map<String, Integer> artists = new LinkedHashMap<>();
    private Map<String, Integer> songs = new LinkedHashMap<>();
    private Map<String, Integer> episodes = new LinkedHashMap<>();
    private Map<String, Integer> albums = new LinkedHashMap<>();
    private Map<String, Integer> genres = new LinkedHashMap<>();
    private boolean wasUpdated;

    public UserWrapped() {
        wasUpdated = false;
    }

    /**
     * Updates the number of times a song was played
     *
     * @param name name of the song
     * @param value number of times the song was played
     */
    public void updateSong(final String name, final int value) {
        songs.put(name, value);
    }

    /**
     * Updates the number of times an album was played
     *
     * @param name name of the album
     * @param value number of times the album was played
     */
    public void updateAlbum(final String name, final int value) {
        albums.put(name, value);
    }

    /**
     * Updates the number of times an episode was played
     *
     * @param name name of the episode
     * @param value number of times the episode was played
     */
    public void updateEpisode(final String name, final int value) {
        episodes.put(name, value);
    }


    /**
     * Adds a listen to the audio file, to the artist/host, to the album, to the genre
     *
     * @param audioFile the audio file
     * @param username the username of the user
     */
    public void addListen(final AudioFile audioFile, final String username) {
        wasUpdated = true;
        int value;
        if (audioFile instanceof Song song) {
            value = 0;
            if (songs.containsKey(song.getName())) {
                value = songs.get(song.getName());
            }
            value++;
            songs.put(song.getName(), value);

            value = 0;
            if (artists.containsKey(song.getArtist())) {
                value = artists.get(song.getArtist());
            }
            value++;
            artists.put(song.getArtist(), value);

            value = 0;
            if (albums.containsKey(song.getAlbum())) {
                value = albums.get(song.getAlbum());
            }
            value++;
            albums.put(song.getAlbum(), value);

            value = 0;
            if (genres.containsKey(song.getGenre())) {
                value = genres.get(song.getGenre());
            }
            value++;
            genres.put(song.getGenre(), value);

            Artist artist = Admin.getArtist(song.getArtist());
            if (artist != null) {
                artist.getWrappedStats().addListen(song, username);
                artist.wasPlayed();
            }
        } else if (audioFile instanceof Episode episode) {
            value = 0;
            if (episodes.containsKey(episode.getName())) {
                value = episodes.get(episode.getName());
            }
            value++;
            episodes.put(episode.getName(), value);
            Host host = Admin.getHost(episode.getOwner());
            if (host != null) {
                host.getWrappedStats().addListen(episode, username);
            }
        }
    }
}
