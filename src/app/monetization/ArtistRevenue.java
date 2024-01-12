package app.monetization;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ArtistRevenue {
    @Setter
    private String name;
    private Double merchRevenue;
    private Double songRevenue;
    @Setter
    private Integer ranking;
    @Setter
    private String mostProfitableSong;
    @Setter
    private boolean wasPlayed;
    private Map<String, Double> songs;

    public ArtistRevenue(final String name) {
        mostProfitableSong = "N/A";
        merchRevenue = 0.0;
        songRevenue = 0.0;
        ranking = 0;
        wasPlayed = false;
        this.name = name;
        songs = new LinkedHashMap<>();
    }

    public void addMerchRevenue(final Double merchRevenue) {
        this.merchRevenue += merchRevenue;
    }

    public void addSongRevenue(final Double songRevenue) {
        this.songRevenue += songRevenue;
    }

    public Double getTotalRevenue() {
        return merchRevenue + songRevenue;
    }
    public void addSong(final String name, final Double price) {
        if (songs.containsKey(name)) {
            songs.put(name, songs.get(name) + price);
        } else {
            songs.put(name, price);
        }
    }

    public void roundRevenue() {
        merchRevenue = Math.round(merchRevenue * 100.0) / 100.0;
        songRevenue = Math.round(songRevenue * 100.0) / 100.0;
//        songs.replaceAll((k, v) -> Math.round(v * 100.0) / 100.0);
    }

    public void sortSongs() {
        List<Map.Entry<String, Double>> sortedSongs = songs.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .toList();
        songs = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : sortedSongs) {
            songs.put(entry.getKey(), entry.getValue());
        }
        if (sortedSongs.size() > 0) {
            mostProfitableSong = sortedSongs.get(0).getKey();
        }
    }
}
