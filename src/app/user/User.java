package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.monetization.UserActivity;
import app.notifications.Notification;
import app.notifications.Subscriber;
import app.notifications.ContentCreator;
import app.page.HomePage;
import app.page.LikedContentPage;
import app.page.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.content.Merch;
import app.utils.Enums;
import app.wrapped.UserWrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.*;

/**
 * The type User.
 */
public class User implements Subscriber {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    private Enums.UserType type;
    @Getter
    private Enums.Status status;
    @Getter
    private Page currentPage;
    @Getter
    private HomePage homePage;
    @Getter
    private LikedContentPage likedContentPage;
    @Getter
    private UserWrapped wrappedStats;
    @Getter
    private boolean premium;
    @Getter
    private UserActivity userActivity;
    private List<ContentCreator> subscribedTo;
    private List<Notification> notifications;
    private List<Merch> boughtMerch;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        type = Enums.UserType.USER;
        status = Enums.Status.ONLINE;
        homePage = new HomePage();
        likedContentPage = new LikedContentPage();
        wrappedStats = new UserWrapped();
        currentPage = homePage;
        premium = false;
        userActivity = new UserActivity();
        subscribedTo = new ArrayList<>();
        notifications = new ArrayList<>();
        boughtMerch = new ArrayList<>();
    }

    public User(final String username, final int age,
                final String city, final Enums.UserType type) {
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        this.type = type;
        status = Enums.Status.ONLINE;
        homePage = new HomePage();
        likedContentPage = new LikedContentPage();
        wrappedStats = new UserWrapped();
        currentPage = homePage;
        premium = false;
        userActivity = new UserActivity();
        subscribedTo = new ArrayList<>();
        notifications = new ArrayList<>();
        boughtMerch = new ArrayList<>();
    }

    /**
     * Gets the current audio file
     *
     * @return the audio file
     */
    public AudioFile getCurrentAudioFile() {
        return player.getCurrentAudioFile();
    }

    /**
     * Gets the current audio collection
     *
     * @return the audio collection
     */
    public AudioCollection getCurrentAudioCollection() {
        return player.getCurrentAudioCollection();
    }

    /**
     * Removes a song from liked songs and decrements the number of likes
     *
     * @param song the song
     */
    public void unlikeSong(final Song song) {
        likedSongs.remove(song);
        song.dislike();
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param searchType    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String searchType) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        if (searchType.equals("artist") || searchType.equals("host")) {
            List<User> users = searchBar.searchUsers(filters, searchType);
            for (User user : users) {
                results.add(user.getUsername());
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, searchType);
            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (searchBar.getLastSearchType().equals("artist")
                || searchBar.getLastSearchType().equals("host")) {
            User selected = searchBar.selectUser(itemNumber);
            if (selected == null) {
                return "The selected ID is too high.";
            }
            if (searchBar.getLastSearchType().equals("artist")) {
                currentPage = ((Artist) selected).getArtistPage();
            } else {
                currentPage = ((Host) selected).getHostPage();
            }

            return "Successfully selected %s's page.".formatted(selected.username);
        } else {
            LibraryEntry selected = searchBar.select(itemNumber);
            if (selected == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
            && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }
        player.setAdBreak(false);
        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();
        updateWrapped(player.getCurrentAudioFile());
        if (player.getCurrentAudioFile() instanceof Song) {
            updateActivity((Song) player.getCurrentAudioFile());
        }

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();
            for (Album album : Admin.getAlbums()) {
                if (album.getSongs().contains(song)) {
                    album.decreaseLikes();
                }
            }

            return "Unlike registered successfully.";
        }

        for (Album album : Admin.getAlbums()) {
            if (album.getSongs().contains(song)) {
                album.increaseLikes();
            }
        }
        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String searchType = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!searchType.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Switches the connection status of user ONLINE/OFFLINE
     */
    public void switchConnectionStatus() {
        if (status == Enums.Status.ONLINE) {
            status = Enums.Status.OFFLINE;
        } else {
            status = Enums.Status.ONLINE;
        }
    }

    /**
     * Verifies if the user is offline
     *
     * @return 1 if user is offline
     */
    public boolean isUserOffline() {
        return status == Enums.Status.OFFLINE;
    }

    /**
     * Updates home and liked content page and prints current page
     *
     * @return the current page in string form
     */
    public String printCurrentPage() {
        for (Artist artist : Admin.getArtists()) {
            artist.getArtistPage().refreshAlbums(artist.getAlbums());
            artist.getArtistPage().refreshEvents(artist.getEvents());
            artist.getArtistPage().refreshMerch(artist.getMerchList());
        }
        for (Host host : Admin.getHosts()) {
            host.getHostPage().refreshPodcasts(host.getPodcasts());
            host.getHostPage().refreshAnnouncements(host.getAnnouncements());
        }
        likedContentPage.refreshLikedSongs(likedSongs);
        likedContentPage.refreshFollowedPlaylists(followedPlaylists);
        homePage.refreshFollowedPlaylists(followedPlaylists);
        homePage.refreshLikedSongs(likedSongs);
        return currentPage.printPage();
    }

    /**
     * Changes page
     *
     * @param nextPage next page to change to
     * @return the string
     */
    public String changePage(final String nextPage) {
        if (nextPage.equals("Home")) {
            currentPage = homePage;
            return username + " accessed " + nextPage + " successfully.";
        }
        if (nextPage.equals("LikedContent")) {
            currentPage = likedContentPage;
            return username + " accessed " + nextPage + " successfully.";
        }
        return username + " is trying to access a non-existent page.";
    }

    /**
     * Deletes user and all its playlists (if possible)
     *
     * @return the string
     */
    public String deleteUser() {
        for (User user : Admin.getUsers()) {
            if (!user.getUsername().equals(username) && user.getCurrentAudioCollection() != null) {
                if (user.getCurrentAudioCollection().getOwner().equals(username)) {
                    return username + " can't be deleted.";
                }
            }
        }
        for (Playlist playlist : playlists) {
            for (User user : Admin.getUsers()) {
                if (user.followedPlaylists.contains(playlist)) {
                    user.followedPlaylists.remove(playlist);
                    playlist.decreaseFollowers();
                }
            }
        }
        for (Song song : likedSongs) {
            song.dislike();
        }
        for (Playlist playlist : followedPlaylists) {
            playlist.decreaseFollowers();
        }
        Admin.removeUser(this);
        return username + " was successfully deleted.";
    }

    public void updateWrapped(AudioFile audioFile) {
        wrappedStats.addListen(audioFile, username);
    }

    public void updateActivity(Song song) {
        userActivity.addListen(song);
    }

    public ObjectNode wrapped() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        List<Map.Entry<String, Integer>> sortedArtists = wrappedStats.getArtists().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();
        ObjectNode topArtists = objectMapper.createObjectNode();
        for (Map.Entry<String, Integer> entry : sortedArtists) {
            topArtists.put(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<String, Integer>> sortedSongs = wrappedStats.getSongs().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();
        ObjectNode topSongs = objectMapper.createObjectNode();
        for (Map.Entry<String, Integer> entry : sortedSongs) {
            topSongs.put(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<String, Integer>> sortedEpisodes = wrappedStats.getEpisodes().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();
        ObjectNode topEpisodes = objectMapper.createObjectNode();
        for (Map.Entry<String, Integer> entry : sortedEpisodes) {
            topEpisodes.put(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<String, Integer>> sortedAlbums = wrappedStats.getAlbums().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();
        ObjectNode topAlbums = objectMapper.createObjectNode();
        for (Map.Entry<String, Integer> entry : sortedAlbums) {
            topAlbums.put(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<String, Integer>> sortedGenres = wrappedStats.getGenres().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();
        ObjectNode topGenres = objectMapper.createObjectNode();
        for (Map.Entry<String, Integer> entry : sortedGenres) {
            topGenres.put(entry.getKey(), entry.getValue());
        }

        result.set("topArtists", topArtists);
        result.set("topGenres", topGenres);
        result.set("topSongs", topSongs);
        result.set("topAlbums", topAlbums);
        result.set("topEpisodes", topEpisodes);

        return result;
    }

    public String buyPremium() {
        if (premium) {
            return username + " is already a premium user.";
        }
        premium = true;
        backupActivity();
        resetActivity();
        userActivity.setPremium(true);
        return username + " bought the subscription successfully.";
    }

    public String cancelPremium() {
        if (!premium) {
            return username + " is not a premium user.";
        }
        giveRevenue();
        resetActivity();
        restoreActivity();
        premium = false;
        userActivity.setPremium(false);
        return username + " cancelled the subscription successfully.";
    }

    public void giveRevenue() {
        userActivity.giveRevenue();
    }

    public void resetActivity() {
        userActivity.reset();
    }

    public void backupActivity() {
        userActivity.backupActivity();
    }

    public void restoreActivity() {
        userActivity.restoreActivity();
    }

    public String adBreak(final Integer price) {
        if (player.getCurrentAudioFile() == null) {
            return username + " is not playing any music.";
        }
        player.setAdBreak(true);
        userActivity.setAdPrice(price.doubleValue());
        return "Ad inserted successfully.";
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (status == Enums.Status.ONLINE) {
            player.simulatePlayer(time, this);
        }
    }

    @Override
    public void update(final String message, final String username) {
        notifications.add(new Notification(message, username));
    }

    public String subscribe(final ContentCreator contentCreator) {
        String user;
        if (contentCreator instanceof Artist) {
            user = ((Artist) contentCreator).getUsername();
        } else {
            user = ((Host) contentCreator).getUsername();
        }
        if (!subscribedTo.contains(contentCreator)) {
            subscribedTo.add(contentCreator);
            contentCreator.addSubscriber(this);
            return username + " subscribed to " + user + " successfully.";
        } else {
            subscribedTo.remove(contentCreator);
            contentCreator.removeSubscriber(this);
            return username + " unsubscribed from " + user + " successfully.";
        }
    }

    public List<ObjectNode> getNotifications() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ObjectNode> result = new ArrayList<>();
        for (Notification notification : notifications) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("name", notification.getMessage());
            objectNode.put("description", notification.printNotification());
            result.add(objectNode);
        }
        clearNotifications();
        return result;
    }

    public void clearNotifications() {
        notifications.clear();
    }

    public String buyMerch(Artist artist, String merchName) {
        if (artist.getMerchList().stream().noneMatch(merch -> merch.getName().equals(merchName))) {
            return "The merch " + merchName + " doesn't exist.";
        }
        Merch merch = artist.getMerch(merchName);
        boughtMerch.add(merch);
        artist.updateMerchRevenue(merch.getPrice());
        artist.removeMerch(merchName);
        artist.wasPlayed();
        return username + " has added new merch successfully.";
    }

    public List<String> seeMerch() {
        List<String> result = new ArrayList<>();
        for (Merch merch : boughtMerch) {
            result.add(merch.getName());
        }
        return result;
    }
}
