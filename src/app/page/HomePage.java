package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class HomePage implements Page {
    private List<Song> likedSongs = new ArrayList<>();
    private List<Playlist> followedPlaylists = new ArrayList<>();
    private static final int LIMIT = 5;

    public HomePage() { }

    /**
     * Refresh the liked songs with latest data
     *
     * @param songs the liked songs
     */
    public void refreshLikedSongs(final ArrayList<Song> songs) {
        likedSongs.clear();
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            likedSongs.add(song);
            count++;
        }
    }

    /**
     * Refresh the followed playlists with latest data
     *
     * @param playlists the followed playlists
     */
    public void refreshFollowedPlaylists(final ArrayList<Playlist> playlists) {
        followedPlaylists.clear();
        List<Playlist> sortedPlaylists = new ArrayList<>(playlists);
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            followedPlaylists.add(playlist);
            count++;
        }
    }

    /**
     * Prints home page
     *
     * @return the string with page info
     */
    @Override
    public String printPage() {

        ArrayList<String> songs = new ArrayList<>();
        ArrayList<String> playlists = new ArrayList<>();
        for (Song song : likedSongs) {
            songs.add(song.getName());
        }
        for (Playlist playlist : followedPlaylists) {
            playlists.add(playlist.getName());
        }
        return "Liked songs:\n\t" + songs + "\n\nFollowed playlists:\n\t" + playlists;
    }
}
