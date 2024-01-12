package app.monetization;

import app.Admin;
import app.audio.Files.Song;
import app.user.Artist;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserActivity {
    private List<Song> songs;
    private boolean premium;
    private Map<String, Integer> artists;
    private Integer totalListens;
    private static final Double PREMIUM_CREDIT = 1000000.0;
    private Double adPrice;
    private UserActivity backup;

    public UserActivity() {
        totalListens = 0;
        premium = false;
        songs = new ArrayList<>();
        artists = new LinkedHashMap<>();
        backup = new UserActivity(true);
    }

    public UserActivity(final boolean backup) {
        totalListens = 0;
        premium = false;
        songs = new ArrayList<>();
        artists = new LinkedHashMap<>();
    }

    public void addListen(Song song) {
        totalListens++;
        songs.add(song);
        int value = 0;
        if (artists.containsKey(song.getArtist())) {
            value = artists.get(song.getArtist());
        }
        value++;
        artists.put(song.getArtist(), value);
    }

    public void giveRevenue() {
        if (premium) {
            for(Map.Entry<String, Integer> entry : artists.entrySet()) {
                Artist artist = Admin.getArtist(entry.getKey());
                Integer listens = entry.getValue();
                Double revenue = (PREMIUM_CREDIT / totalListens) * listens;
                artist.addSongRevenue(revenue);
                for (Song song : songs) {
                    if (song.getArtist().equals(artist.getUsername())) {
                        revenue = PREMIUM_CREDIT / totalListens;
                        artist.updateSongRevenue(song.getName(), revenue);
                    }
                }
            }
        } else {
            for(Map.Entry<String, Integer> entry : artists.entrySet()) {
                Artist artist = Admin.getArtist(entry.getKey());
                Integer listens = entry.getValue();
                Double revenue = (adPrice / totalListens) * listens;
                artist.addSongRevenue(revenue);
                for (Song song : songs) {
                    if (song.getArtist().equals(artist.getUsername())) {
                        revenue = adPrice / totalListens;
                        artist.updateSongRevenue(song.getName(), revenue);
                    }
                }
            }
        }
    }

    public void reset() {
        totalListens = 0;
        artists = new LinkedHashMap<>();
        songs = new ArrayList<>();
    }

    public void backupActivity() {
        backup.setPremium(premium);
        backup.setAdPrice(adPrice);
        backup.setTotalListens(totalListens);
        backup.setArtists(artists);
        backup.setSongs(songs);
    }

    public void restoreActivity() {
        premium = backup.isPremium();
        adPrice = backup.getAdPrice();
        totalListens = backup.getTotalListens();
        artists = backup.getArtists();
        songs = backup.getSongs();
    }

}
