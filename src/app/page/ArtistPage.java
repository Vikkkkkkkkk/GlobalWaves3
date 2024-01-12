package app.page;

import app.audio.Collections.Album;
import app.user.content.Event;
import app.user.content.Merch;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArtistPage implements Page {
    private List<Album> albums = new ArrayList<>();
    private List<Merch> merch = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private String owner;

    public ArtistPage(final String owner) {
        this.owner = owner;
    }

    /**
     * Refresh the album list
     *
     * @param albumsList album list
     */
    public void refreshAlbums(final List<Album> albumsList) {
        this.albums.clear();
        this.albums.addAll(albumsList);
    }

    /**
     * Refresh the merch list with latest data
     *
     * @param merchList the merch list
     */
    public void refreshMerch(final List<Merch> merchList) {
        this.merch.clear();
        this.merch.addAll(merchList);
    }

    /**
     * Refresh the event list with latest data
     *
     * @param eventsList the event list
     */
    public void refreshEvents(final List<Event> eventsList) {
        this.events.clear();
        this.events.addAll(eventsList);
    }

    /**
     * Prints artists page
     *
     * @return the string with page info
     */
    @Override
    public String printPage() {
        ArrayList<String> albumList = new ArrayList<>();
        ArrayList<String> merchList = new ArrayList<>();
        ArrayList<String> eventList = new ArrayList<>();

        String printFormat;
        for (Album album : albums) {
            albumList.add(album.getName());
        }
        for (Merch merch1 : merch) {
            printFormat = "";
            printFormat += merch1.getName() + " - " + merch1.getPrice() + ":\n";
            printFormat += "\t" + merch1.getDescription();
            merchList.add(printFormat);
        }
        for (Event event : events) {
            printFormat = "";
            printFormat += event.getName() + " - " + event.getDate() + ":\n";
            printFormat += "\t" + event.getDescription();
            eventList.add(printFormat);
        }

        return "Albums:\n\t" + albumList + "\n\nMerch:\n\t"
                + merchList + "\n\nEvents:\n\t" + eventList;
    }
}
