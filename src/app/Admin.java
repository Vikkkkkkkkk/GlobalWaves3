package app;

import app.audio.Collections.Podcast;
import app.audio.Collections.Playlist;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PodcastOutput;
import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.monetization.ArtistRevenue;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import fileio.input.UserInput;
import fileio.input.SongInput;
import fileio.input.PodcastInput;
import fileio.input.EpisodeInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The type Admin.
 */
public final class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<ArtistRevenue> artistRevenues = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets songs.
     *
     * @return the songs
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets artists
     *
     * @return the artists
     */
    public static List<Artist> getArtists() {
        List<Artist> artists = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.UserType.ARTIST) {
                artists.add((Artist) user);
            }
        }
        return artists;
    }

    /**
     * Gets hosts
     *
     * @return the hosts
     */
    public static List<Host> getHosts() {
        List<Host> hosts = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.UserType.HOST) {
                hosts.add((Host) user);
            }
        }
        return hosts;
    }

    /**
     * Gets albums
     *
     * @return the albums
     */
    public static List<Album> getAlbums() {
        List<Album> albums = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.UserType.ARTIST) {
                albums.addAll(((Artist) user).getAlbums());
            }
        }
        return albums;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Artist getArtist(final String username) {
        for (Artist artist : getArtists()) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    public static void updateOwners() {
        for (Podcast podcast : podcasts) {
            for (Episode episode : podcast.getEpisodes()) {
                episode.setOwner(podcast.getOwner());
            }
        }
    }

    public static Host getHost(final String username) {
        for (Host host : getHosts()) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }

    public static List<ArtistRevenue> getArtistRevenues() {
        return new ArrayList<>(artistRevenues);
    }

    /**
     * Removes user from admin library
     *
     * @param user the user
     */
    public static void removeUser(final User user) {
        users.remove(user);
    }

    /**
     * Removes song from admin library
     *
     * @param song the song
     */
    public static void removeSong(final Song song) {
        for (User user : users) {
            if (user.getLikedSongs().contains(song)) {
                user.unlikeSong(song);
            }
        }
        songs.remove(song);
    }

    /**
     * Adds song to admin library
     *
     * @param song the song
     */
    public static void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Adds podcast to admin library
     *
     * @param podcast the podcast
     */
    public static void addNewPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Removes podcast from admin library
     *
     * @param podcast the podcast
     */
    public static void removePodcast(final Podcast podcast) {
        podcasts.remove(podcast);
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Adds new user depending on type
     *
     * @param username the username
     * @param age the age
     * @param city the city
     * @param type the type
     * @return the string
     */
    public static String addUser(final String username, final Integer age,
                                 final String city, final Enums.UserType type) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "The username " + username + " is already taken.";
            }
        }
        if (type == Enums.UserType.USER) {
            User user = new User(username, age, city, type);
            users.add(user);
        } else if (type == Enums.UserType.ARTIST) {
            Artist artist = new Artist(username, age, city, type);
            users.add(artist);
        } else if (type == Enums.UserType.HOST) {
            Host host = new Host(username, age, city, type);
            users.add(host);
        }
        return "The username " + username + " has been added successfully.";
    }

    /**
     * Switches connection status of user ONLINE/OFFLINE
     *
     * @param username the username
     * @return the string
     */
    public static String switchConnectionStatus(final String username) {
        User foundUser = getUser(username);
        if (foundUser == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!(foundUser.getType() == Enums.UserType.USER)) {
            return username + " is not a normal user.";
        }
        foundUser.switchConnectionStatus();
        return username + " has changed status successfully.";
    }

    /**
     * Gets all online users
     *
     * @return list of usernames
     */
    public static List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.UserType.USER && user.getStatus() == Enums.Status.ONLINE) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * Adds an album for an artist
     *
     * @param username the username of the artist
     * @param name the name of the album
     * @param releaseYear release year of the album
     * @param description description of the album
     * @param albumSongs songs of the album
     * @return the string
     */
    public static String addAlbum(final String username, final String name,
                                  final Integer releaseYear,
                                  final String description, final List<SongInput> albumSongs) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.ARTIST) {
            return username + " is not an artist.";
        }
        return ((Artist) user).addAlbum(name, releaseYear, description, albumSongs);
    }

    /**
     * Shows all the albums of user
     *
     * @param username username of the user
     * @return list of albums in printable form
     */
    public static ArrayList<AlbumOutput> showAlbums(final String username) {
        Artist artist = (Artist) getUser(username);
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : artist.getAlbums()) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }

    /**
     * Adds an event for an artist
     *
     * @param username the username of the artist
     * @param name name of the event
     * @param description description of the event
     * @param date date of the event
     * @return the string
     */
    public static String addEvent(final String username, final String name,
                                  final String description, final String date) {
        User user = Admin.getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.ARTIST) {
            return username + " is not an artist.";
        }
        return ((Artist) user).addEvent(name, description, date);
    }

    /**
     * Adds merch for an artist
     *
     * @param username the username of the artist
     * @param name name of merch
     * @param description description of merch
     * @param price price of merch
     * @return the string
     */
    public static String addMerch(final String username, final String name,
                                  final String description, final Integer price) {
        User user = Admin.getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.ARTIST) {
            return username + " is not an artist.";
        }
        return ((Artist) user).addMerch(name, description, price);
    }

    /**
     * Gets all users
     *
     * @return list of usernames
     */
    public static List<String> getAllUsers() {
        List<String> normalUsers = new ArrayList<>();
        List<String> artistUsers = new ArrayList<>();
        List<String> hostUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == Enums.UserType.ARTIST) {
                artistUsers.add(user.getUsername());
            } else if (user.getType() == Enums.UserType.USER) {
                normalUsers.add(user.getUsername());
            } else if (user.getType() == Enums.UserType.HOST) {
                hostUsers.add(user.getUsername());
            }
        }
        normalUsers.addAll(artistUsers);
        normalUsers.addAll(hostUsers);
        return normalUsers;
    }

    /**
     * Deletes an user depending on its type
     *
     * @param username the username
     * @return the string
     */
    public static String deleteUser(final String username) {
        User user = Admin.getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() == Enums.UserType.USER) {
            return user.deleteUser();
        } else if (user.getType() == Enums.UserType.ARTIST) {
            return ((Artist) user).deleteArtist();
        } else {
            return ((Host) user).deleteHost();
        }
    }

    /**
     * Adds a podcast for a host
     *
     * @param username the username of the host
     * @param name name of the podcast
     * @param episodes episodes of the podcast
     * @return the string
     */
    public static String addPodcast(final String username, final String name,
                                    final List<EpisodeInput> episodes) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.HOST) {
            return username + " is not a host.";
        }
        return ((Host) user).addPodcast(name, episodes);
    }

    /**
     * Adds an announcement for a host
     *
     * @param username the username of the host
     * @param name name of the announcement
     * @param description description of the announcement
     * @return the string
     */
    public static String addAnnouncement(final String username, final String name,
                                         final String description) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.HOST) {
            return username + " is not a host.";
        }
        return ((Host) user).addAnnouncement(name, description);
    }

    /**
     * Removes an announcement for a host
     *
     * @param username the username of the host
     * @param name name of the announcement
     * @return the string
     */
    public static String removeAnnouncement(final String username, final String name) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.HOST) {
            return username + " is not a host.";
        }
        return ((Host) user).removeAnnouncement(name);
    }

    /**
     * Shows all the podcasts of a host
     *
     * @param username username of the host
     * @return list of podcasts in printable form
     */
    public static ArrayList<PodcastOutput> showPodcasts(final String username) {
        Host host = (Host) getUser(username);
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : host.getPodcasts()) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }
        return podcastOutputs;
    }

    /**
     * Removes an album for an artist
     *
     * @param username the username of the artist
     * @param name name of the album
     * @return the string
     */
    public static String removeAlbum(final String username, final String name) {
        User user = Admin.getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.ARTIST) {
            return username + " is not an artist.";
        }
        for (User userIter : users) {
            List<Playlist> playlists = userIter.getPlaylists();
            for (Playlist playlist : playlists) {
                for (Song song : playlist.getSongs()) {
                    if (song.getAlbum().equals(name)) {
                        return username + " can't delete this album.";
                    }
                }
            }
            AudioCollection audioCollection = userIter.getCurrentAudioCollection();
            AudioFile audioFile = userIter.getCurrentAudioFile();
            if (audioCollection != null) {
                if (audioCollection.getName().equals(name)
                        && audioCollection.getOwner().equals(username)) {
                    return username + " can't delete this album.";
                }
            }
            if (audioFile != null) {
                if (audioFile instanceof Song) {
                    if (((Song) audioFile).getAlbum().equals(name)) {
                        return username + " can't delete this album.";
                    }
                }
            }
        }
        return ((Artist) user).removeAlbum(name);
    }

    /**
     * Removes an event for an artist
     *
     * @param username the username of the artist
     * @param name name of the event
     * @return the string
     */
    public static String removeEvent(final String username, final String name) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.ARTIST) {
            return username + " is not an artist.";
        }
        return ((Artist) user).removeEvent(name);
    }

    /**
     * Removes a podcast for a host
     *
     * @param username the username of the host
     * @param name name of the podcast
     * @return the string
     */
    public static String removePodcast(final String username, final String name) {
        User user = getUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getType() != Enums.UserType.HOST) {
            return username + " is not a host.";
        }
        for (User userIter : users) {
            AudioCollection audioCollection = userIter.getCurrentAudioCollection();
            if (audioCollection != null) {
                if (audioCollection.getName().equals(name)
                        && audioCollection.getOwner().equals(username)) {
                    return username + " can't delete this podcast.";
                }
            }
        }
        return ((Host) user).removePodcast(name);
    }

    /**
     * Gets top 5 albums
     *
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparingInt(Album::getTotalLikes)
                .reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= LIMIT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * Gets top 5 artists
     *
     * @return the top 5 artists
     */
    public static List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(getArtists());
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes).reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }
            topArtists.add(artist.getUsername());
            count++;
        }
        return topArtists;
    }

    public static void endProgram() {
        for (User user : users) {
            if (user.isPremium()) {
                user.giveRevenue();
            }
            user.resetActivity();
        }
        for (Artist artist : getArtists()) {
            artist.roundRevenue();
            artist.sortSongs();
            if (artist.getRevenue().isWasPlayed()) {
                artistRevenues.add(artist.getRevenue());
            }
        }
        if (artistRevenues.isEmpty()) {
            return;
        }
        artistRevenues.sort(Comparator.comparingDouble(ArtistRevenue::getTotalRevenue)
                .reversed()
                .thenComparing(ArtistRevenue::getName, Comparator.naturalOrder()));
        for (ArtistRevenue artistRevenue : artistRevenues) {
            artistRevenue.setRanking(artistRevenues.indexOf(artistRevenue) + 1);
        }
    }

    public static AudioFile getAd() {
        return songs.get(0);
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        artistRevenues = new ArrayList<>();
        timestamp = 0;
    }
}
