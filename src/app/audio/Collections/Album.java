package app.audio.Collections;

import app.Admin;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.user.Artist;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends AudioCollection {
    private final ArrayList<Song> songs;
    private Integer releaseYear;
    private String description;
    private int totalLikes;

    /**
     * Instantiates a new Album.
     *
     * @param name  the name
     * @param owner the owner
     */
    public Album(final String name, final String owner,
                 final Integer releaseYear, final String description) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.releaseYear = releaseYear;
        this.description = description;
        this.totalLikes = 0;
    }

    /**
     * Adds a song to the album
     *
     * @param song the song
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Adds the likes of a song to the total likes
     *
     * @param songLikes likes of the song
     */
    public void addLikes(final Integer songLikes) {
        totalLikes += songLikes;
    }

    /**
     * Increments likes of the album and of the artist
     */
    public void increaseLikes() {
        totalLikes++;
        Artist artist = (Artist) Admin.getUser(this.getOwner());
        artist.updateLikes(1);
    }

    /**
     * Decrements likes of the album and of the artist
     */
    public void decreaseLikes() {
        totalLikes--;
        Artist artist = (Artist) Admin.getUser(this.getOwner());
        artist.updateLikes(-1);
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }
}
