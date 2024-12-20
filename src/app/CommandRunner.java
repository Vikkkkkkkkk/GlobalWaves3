package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.authorizer.Authorizer;
import app.monetization.ArtistRevenue;
import app.page.ArtistPage;
import app.page.HostPage;
import app.page.Page;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * The Instance.
     */
    private static CommandRunner instance = null;

    private CommandRunner() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CommandRunner getInstance() {
        if (instance == null) {
            instance = new CommandRunner();
        }
        return instance;
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        ArrayList<String> results;
        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
            results = new ArrayList<>();
        } else {
            Filters filters = new Filters(commandInput.getFilters());
            String type = commandInput.getType();

            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.select(commandInput.getItemNumber());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.load();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.playPause();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.repeat();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            Integer seed = commandInput.getSeed();
            message = user.shuffle(seed);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.forward();
        }


        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.backward();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.like();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.next();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.prev();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.createPlaylist(commandInput.getPlaylistName(),
                    commandInput.getTimestamp());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.switchPlaylistVisibility(commandInput.getPlaylistId());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.follow();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Add new user
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addUser(final CommandInput commandInput) {
        Enums.UserType type;
        if (commandInput.getType().equals("user")) {
            type = Enums.UserType.USER;
        } else if (commandInput.getType().equals("artist")) {
            type = Enums.UserType.ARTIST;
        } else {
            type = Enums.UserType.HOST;
        }
        String message = Admin.addUser(commandInput.getUsername(), commandInput.getAge(),
                commandInput.getCity(), type);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switches connection status for user
     * ONLINE/OFFLINE
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        String message = Admin.switchConnectionStatus(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets online users
     *
     * @param commandInput the command input
     * @return all online users
     */
    public ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> users = Admin.getOnlineUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * Adds a new album
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addAlbum(final CommandInput commandInput) {
        String message = Admin.addAlbum(commandInput.getUsername(),
                commandInput.getName(), commandInput.getReleaseYear(),
                commandInput.getDescription(), commandInput.getSongs());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shows all the albums of a given user
     *
     * @param commandInput the command input
     * @return all albums
     */
    public ObjectNode showAlbums(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = Admin.showAlbums(user.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Prints the current page
     *
     * @param commandInput the command input
     * @return the current page in object node
     */
    public ObjectNode printCurrentPage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user.isUserOffline()) {
            message = commandInput.getUsername() + " is offline.";
        } else {
            message = user.printCurrentPage();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds event for an artist
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = Admin.addEvent(commandInput.getUsername(), commandInput.getName(),
                commandInput.getDescription(), commandInput.getDate());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds merch for an artist
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addMerch(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = Admin.addMerch(commandInput.getUsername(), commandInput.getName(),
                commandInput.getDescription(), commandInput.getPrice());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets all users
     *
     * @param commandInput the command input
     * @return all users
     */
    public ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> users = Admin.getAllUsers();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * Deletes an user
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode deleteUser(final CommandInput commandInput) {
        String message = Admin.deleteUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds a podcast for a host
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addPodcast(final CommandInput commandInput) {
        String message = Admin.addPodcast(commandInput.getUsername(),
                commandInput.getName(), commandInput.getEpisodes());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds an announcement for a host
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode addAnnouncement(final CommandInput commandInput) {
        String message = Admin.addAnnouncement(commandInput.getUsername(),
                commandInput.getName(), commandInput.getDescription());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes an announcement for a host
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removeAnnouncement(final CommandInput commandInput) {
        String message = Admin.removeAnnouncement(commandInput.getUsername(),
                commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shows all podcasts of a host
     *
     * @param commandInput the command input
     * @return the podcasts of the host
     */
    public ObjectNode showPodcasts(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PodcastOutput> albums = Admin.showPodcasts(user.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Removes an album for an artist
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removeAlbum(final CommandInput commandInput) {
        String message = Admin.removeAlbum(commandInput.getUsername(),
                commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Changes the current page of an user
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode changePage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.changePage(commandInput.getNextPage());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes an event for an artist
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removeEvent(final CommandInput commandInput) {
        String message = Admin.removeEvent(commandInput.getUsername(),
                commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes a podcast for a host
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode removePodcast(final CommandInput commandInput) {
        String message = Admin.removePodcast(commandInput.getUsername(),
                commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets top 5 albums
     *
     * @param commandInput the command input
     * @return the top 5 albums
     */
    public ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets top 5 artists.
     *
     * @param commandInput the command input
     * @return the top 5 artists
     */
    public ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> artists = Admin.getTop5Artists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artists));

        return objectNode;
    }

    /**
     * Gets the wrapped stats.
     *
     * @param commandInput the command input
     * @return the wrapped stats
     */
    public ObjectNode wrapped(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user.getWrappedStats().isWasUpdated()) {
            ObjectNode result = user.wrapped();
            objectNode.put("result", result);
        } else {
            String type;
            switch (user.getType()) {
                case USER:
                    type = "user";
                    break;
                case ARTIST:
                    type = "artist";
                    break;
                case HOST:
                    type = "host";
                    break;
                default:
                    type = "";
            }
            String out = "No data to show for " + type + " " + commandInput.getUsername() + ".";
            objectNode.put("message", out);
        }
        return objectNode;
    }

    /**
     * Gives user premium.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode buyPremium(final CommandInput commandInput) {
        String message;
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            message = "The username "
                    + commandInput.getUsername() + " doesn't exist.";
        }
        User user = Admin.getUser(commandInput.getUsername());
        message = user.buyPremium();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Cancels user premium.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode cancelPremium(final CommandInput commandInput) {
        String message;
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            message = "The username "
                    + commandInput.getUsername() + " doesn't exist.";
        }
        User user = Admin.getUser(commandInput.getUsername());
        message = user.cancelPremium();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Inserts ad break.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode adBreak(final CommandInput commandInput) {
        String message;
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            message = "The username "
                    + commandInput.getUsername() + " doesn't exist.";
        }
        User user = Admin.getUser(commandInput.getUsername());
        message = user.adBreak(commandInput.getPrice());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Subscribes user to an artist or host.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode subscribe(final CommandInput commandInput) {
        String message;
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            message = "The username "
                    + commandInput.getUsername() + " doesn't exist.";
        }
        User user = Admin.getUser(commandInput.getUsername());
        Page page = user.getCurrentPage();
        if (page instanceof ArtistPage) {
            String owner = ((ArtistPage) page).getOwner();
            message = user.subscribe(Admin.getArtist(owner));
        }  else if (page instanceof HostPage) {
            String owner = ((HostPage) page).getOwner();
            message = user.subscribe(Admin.getHost(owner));
        } else {
            message = "To subscribe you need to be on the page of an artist or host.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets notifications.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode getNotifications(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        List<ObjectNode> notifications = user.getNotifications();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("notifications", objectMapper.valueToTree(notifications));

        return objectNode;
    }

    /**
     * Buys merch.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode buyMerch(final CommandInput commandInput) {
        String message;
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            message = "The username "
                    + commandInput.getUsername() + " doesn't exist.";
        }
        User user = Admin.getUser(commandInput.getUsername());
        Page page = user.getCurrentPage();
        if (!(page instanceof ArtistPage)) {
            message = "Cannot buy merch from this page.";
        } else {
            Artist artist = Admin.getArtist(((ArtistPage) page).getOwner());
            message = user.buyMerch(artist, commandInput.getName());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets all the merch bought by a user.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode seeMerch(final CommandInput commandInput) {
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
            return objectNode;
        }
        User user = Admin.getUser(commandInput.getUsername());
        List<String> result = user.seeMerch();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(result));

        return objectNode;
    }

    /**
     * Goes to the next page.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode nextPage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.nextPage();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Goes to the previous page.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode previousPage(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.previousPage();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Updates recommendations.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode updateRecommendations(final CommandInput commandInput) {
        String message;
        if (!Authorizer.getInstance().existsUser(commandInput.getUsername())) {
            message = "The username "
                    + commandInput.getUsername() + " doesn't exist.";
        }
        if (!Authorizer.getInstance().isNormalUser(commandInput.getUsername())) {
            message = commandInput.getUsername() + " is not a normal user.";
        }
        User user = Admin.getUser(commandInput.getUsername());
        message = user.updateRecommendations(commandInput.getRecommendationType());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Loads recommendations.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public ObjectNode loadRecommendations(final CommandInput commandInput) {
        String message;
        User user = Admin.getUser(commandInput.getUsername());
        message = user.loadRecommendations();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Ends the program.
     *
     * @return the object node
     */
    public ObjectNode endProgram() {
        Admin.endProgram();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");

        ObjectNode result = objectMapper.createObjectNode();
        for (ArtistRevenue artistRevenue : Admin.getArtistRevenues()) {
            ObjectNode total = objectMapper.createObjectNode();
            total.put("merchRevenue", artistRevenue.getMerchRevenue());
            total.put("songRevenue", artistRevenue.getSongRevenue());
            total.put("ranking", artistRevenue.getRanking());
            total.put("mostProfitableSong", artistRevenue.getMostProfitableSong());
            result.set(artistRevenue.getName(), total);
        }
        objectNode.set("result", result);

        return objectNode;
    }
}
