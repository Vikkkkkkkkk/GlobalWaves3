package app.monetization;

import lombok.Getter;
import lombok.Setter;

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

    public ArtistRevenue(final String name) {
        mostProfitableSong = "N/A";
        merchRevenue = 0.0;
        songRevenue = 0.0;
        ranking = 0;
        wasPlayed = false;
        this.name = name;
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
}
