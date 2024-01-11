package app.wrapped;

import app.audio.Files.Episode;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class HostWrapped extends UserWrapped{
    private Integer listeners;
    private HashMap<String, Integer> fans = new HashMap<>();


    public HostWrapped() {
        super();
        listeners = 0;
    }
    public void incrementListeners() {
        listeners++;
    }

    public void addListen(Episode episode, String username) {
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
