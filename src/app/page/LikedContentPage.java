package app.page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LikedContentPage implements Page {
    private List<Song> likedSongs = new ArrayList<>();
    private List<Playlist> followedPlaylists = new ArrayList<>();

    public LikedContentPage() { }

    /**
     * Refresh the liked songs list
     *
     * @param songs the liked songs list
     */
    public void refreshLikedSongs(final List<Song> songs) {
        this.likedSongs.clear();
        this.likedSongs.addAll(songs);
    }

    /**
     * Refresh the followed playlists
     *
     * @param playlists the followed playlists
     */
    public void refreshFollowedPlaylists(final List<Playlist> playlists) {
        this.followedPlaylists.clear();
        this.followedPlaylists.addAll(playlists);
    }

    /**
     * Prints liked content page
     *
     * @return the string with page info
     */
    @Override
    public String printPage() {
        List<String> songs = new ArrayList<>();
        List<String> playlists = new ArrayList<>();

        String printFormat;
        for (Song song : likedSongs) {
            printFormat = "";
            printFormat += song.getName() + " - " + song.getArtist();
            songs.add(printFormat);
        }
        for (Playlist playlist : followedPlaylists) {
            printFormat = "";
            printFormat += playlist.getName() + " - " + playlist.getOwner();
            playlists.add(printFormat);
        }

        return "Liked songs:\n\t" + songs + "\n\nFollowed playlists:\n\t" + playlists;
    }
}
