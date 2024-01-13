package app.wrapped;

import app.audio.Files.Episode;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class HostWrapped extends UserWrapped {
    private Integer listeners;
    private HashMap<String, Integer> fans = new HashMap<>();


    public HostWrapped() {
        super();
        listeners = 0;
    }

    /**
     * Increment the number of listeners.
     */
    public void incrementListeners() {
        listeners++;
    }

    /**
     * Adds a listen to the episode and increments the number of listeners.
     *
     * @param episode the episode
     * @param username the username of the user
     */
    public void addListen(final Episode episode, final String username) {
        setWasUpdated(true);
        int value = 0;

        if (getEpisodes().containsKey(episode.getName())) {
            value = getEpisodes().get(episode.getName());
        }
        value++;
        updateEpisode(episode.getName(), value);

        value = 0;
        if (fans.containsKey(username)) {
            value = fans.get(username);
        } else {
            incrementListeners();
        }
        value++;
        fans.put(username, value);
    }
}
