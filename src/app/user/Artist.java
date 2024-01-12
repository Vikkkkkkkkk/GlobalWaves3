package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.monetization.ArtistRevenue;
import app.page.ArtistPage;
import app.user.content.Event;
import app.user.content.Merch;
import app.utils.Enums;
import app.wrapped.ArtistWrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Getter
public class Artist extends User {
    private List<Album> albums;
    private List<Event> events;
    private List<Merch> merchList;
    private ArtistPage artistPage;
    private Integer likes;
    private ArtistWrapped wrappedStats;
    private ArtistRevenue revenue;
    public Artist(final String username, final int age,
                  final String city, final Enums.UserType type) {
        super(username, age, city, type);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchList = new ArrayList<>();
        artistPage = new ArtistPage();
        wrappedStats = new ArtistWrapped();
        revenue = new ArtistRevenue(username);
        likes = 0;
    }

    /**
     * Adds an album
     *
     * @param name name of the album
     * @param releaseYear release year of the album
     * @param description description of the album
     * @param songs songs of the album
     * @return the string
     */
    public String addAlbum(final String name, final Integer releaseYear,
                           final String description, final List<SongInput> songs) {
        if (albums.stream().anyMatch(album -> album.getName().equals(name))) {
            return getUsername() + " has another album with the same name.";
        }
        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = i + 1; j <  songs.size(); j++) {
                if (songs.get(i).getName().equals(songs.get(j).getName())) {
                    return getUsername() + " has the same song at least twice in this album.";
                }
            }
        }
        Album album = new Album(name, getUsername(), releaseYear, description);
        for (SongInput songInput : songs) {
            Song song = new Song(songInput.getName(), songInput.getDuration(),
                    songInput.getAlbum(), songInput.getTags(), songInput.getLyrics(),
                    songInput.getGenre(), songInput.getReleaseYear(), songInput.getArtist());
            album.addSong(song);
            Admin.addSong(song);
            album.addLikes(song.getLikes());
        }
        albums.add(album);
        return getUsername() + " has added new album successfully.";
    }

    /**
     * Removes an album
     *
     * @param name name of the album
     * @return the string
     */
    public String removeAlbum(final String name) {
        if (albums.stream().noneMatch(album -> album.getName().equals(name))) {
            return getUsername() + " doesn't have an album with the given name.";
        }
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                for (Song song : album.getSongs()) {
                    Admin.removeSong(song);
                }
                albums.remove(album);
                break;
            }
        }
        return getUsername() + " deleted the album successfully.";
    }

    /**
     * Adds an event
     *
     * @param name name of the event
     * @param description description of the event
     * @param date date of the event
     * @return the string
     */
    public String addEvent(final String name, final String description, final String date) {
        if (events.stream().anyMatch(event -> event.getName().equals(name))) {
            return getUsername() + " has another event with the same name.";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate dateFormatted = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }
        Event event = new Event(name, description, date);
        events.add(event);
        return getUsername() + " has added new event successfully.";
    }

    /**
     * Adds merch
     *
     * @param name name of the merch
     * @param description description of the merch
     * @param price price of the merch
     * @return the string
     */
    public String addMerch(final String name, final String description, final Integer price) {
        if (merchList.stream().anyMatch(merch -> merch.getName().equals(name))) {
            return getUsername() + " has merchandise with the same name.";
        }
        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }
        Merch merch = new Merch(name, description, price);
        merchList.add(merch);
        return getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Removes the event
     *
     * @param name name of the event
     * @return the string
     */
    public String removeEvent(final String name) {
        if (events.stream().noneMatch(event -> event.getName().equals(name))) {
            return getUsername() + " doesn't have an event with the given name.";
        }
        for (Event event : events) {
            if (event.getName().equals(name)) {
                events.remove(event);
                break;
            }
        }
        return getUsername() + " deleted the event successfully.";
    }

    /**
     * Deletes an artist, including all the albums
     *
     * @return the string
     */
    public String deleteArtist() {
        for (User user : Admin.getUsers()) {
            if (!user.getUsername().equals(getUsername())
                    && user.getCurrentAudioCollection() != null) {
                if (user.getCurrentAudioCollection().getOwner().equals(getUsername())) {
                    return getUsername() + " can't be deleted.";
                }
            }

            if (user.getCurrentPage() == artistPage) {
                return getUsername() + " can't be deleted.";
            }

            AudioFile audioFile = user.getCurrentAudioFile();
            if (!user.getUsername().equals(getUsername()) && audioFile != null) {
                if (audioFile instanceof Song) {
                    for (Album album : albums) {
                        if (((Song) audioFile).getAlbum().equals(album.getName())) {
                            return getUsername() + " can't be deleted.";
                        }
                    }
                }
            }

            for (Playlist playlist : user.getPlaylists()) {
                for (Song song : playlist.getSongs()) {
                    if (song.getArtist().equals(getUsername())) {
                        return getUsername() + " can't be deleted.";
                    }
                }
            }
        }

        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                Admin.removeSong(song);
            }
        }
        Admin.removeUser(this);
        return getUsername() + " was successfully deleted.";
    }

    /**
     * Updates the likes of an artist
     *
     * @param like number of likes to be added
     */
    public void updateLikes(final Integer like) {
        likes += like;
    }

    @Override
    public ObjectNode wrapped() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

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

        List<Map.Entry<String, Integer>> sortedFans = wrappedStats.getFans().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .toList();
        List<String> topFans = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedFans) {
            topFans.add(entry.getKey());
        }

        result.set("topAlbums", topAlbums);
        result.set("topSongs", topSongs);
        result.set("topFans", objectMapper.valueToTree(topFans));
        result.put("listeners", wrappedStats.getListeners());

        return result;
    }

    public void wasPlayed() {
        revenue.setWasPlayed(true);
    }

    public void updateSongRevenue(String name, Double price) {
        revenue.addSong(name, price);
    }

    public void addSongRevenue(Double price) {
        revenue.addSongRevenue(price);
    }

    public void roundRevenue() {
        revenue.roundRevenue();
    }

    public void sortSongs() {
        revenue.sortSongs();
    }
}
