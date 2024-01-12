package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.notifications.ContentCreator;
import app.notifications.Subscriber;
import app.page.HostPage;
import app.user.content.Announcement;
import app.utils.Enums;
import app.wrapped.HostWrapped;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Host extends User implements ContentCreator {
    private List<Podcast> podcasts;
    private List<Announcement> announcements;
    private HostPage hostPage;
    private HostWrapped wrappedStats;
    List<Subscriber> subscribers;

    public Host(final String username, final int age,
                final String city, final Enums.UserType type) {
        super(username, age, city, type);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        hostPage = new HostPage(getUsername());
        wrappedStats = new HostWrapped();
        subscribers = new ArrayList<>();
    }

    /**
     * Adds a podcast
     *
     * @param name podcast's name
     * @param episodes podcast's episodes
     * @return the string
     */
    public String addPodcast(final String name, final List<EpisodeInput> episodes) {
        if (podcasts.stream().anyMatch(podcast -> podcast.getName().equals(name))) {
            return getUsername() + " has another podcast with the same name.";
        }
        for (int i = 0; i < episodes.size() - 1; i++) {
            for (int j = i + 1; j <  episodes.size(); j++) {
                if (episodes.get(i).getName().equals(episodes.get(j).getName())) {
                    return getUsername() + " has the same episode in this podcast.";
                }
            }
        }
        List<Episode> episodesPodcast = new ArrayList<>();
        for (EpisodeInput episodeInput : episodes) {
            Episode episode = new Episode(episodeInput.getName(),
                    episodeInput.getDuration(), episodeInput.getDescription());
            episode.setOwner(getUsername());
            episodesPodcast.add(episode);
        }
        Podcast podcast = new Podcast(name, getUsername(), episodesPodcast);
        podcasts.add(podcast);
        Admin.addNewPodcast(podcast);
        return getUsername() + " has added new podcast successfully.";
    }

    /**
     * Adds an announcement
     *
     * @param name announcement's name
     * @param description announcement's description
     * @return the string
     */
    public String addAnnouncement(final String name, final String description) {
        if (announcements.stream().anyMatch(announcement -> announcement.getName().equals(name))) {
            return getUsername() + " has already added an announcement with this name.";
        }
        Announcement announcement = new Announcement(name, description);
        announcements.add(announcement);
        notifySubscribers("New Announcement", getUsername());
        return getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes the announcement from host's announcement list
     *
     * @param name the announcement name
     * @return the string
     */
    public String removeAnnouncement(final String name) {
        if (announcements.stream().noneMatch(announcement -> announcement.getName().equals(name))) {
            return getUsername() + " has no announcement with the given name.";
        }
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                announcements.remove(announcement);
                break;
            }
        }
        return getUsername() + " has successfully deleted the announcement.";
    }

    /**
     * Removes the podcast from host's podcast list
     *
     * @param name the podcast name
     * @return the string
     */
    public String removePodcast(final String name) {
        if (podcasts.stream().noneMatch(podcast -> podcast.getName().equals(name))) {
            return getUsername() + " doesn't have a podcast with the given name.";
        }
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                podcasts.remove(podcast);
                break;
            }
        }
        return getUsername() + " deleted the podcast successfully.";
    }

    /**
     * Deletes all the items created/added by host
     *
     * @return the string
     */
    public String deleteHost() {
        for (User user : Admin.getUsers()) {
            if (!user.getUsername().equals(getUsername())
                    && user.getCurrentAudioCollection() != null) {
                if (user.getCurrentAudioCollection()
                        .getOwner().equals(getUsername())) {
                    return getUsername() + " can't be deleted.";
                }
            }

            if (user.getCurrentPage() == hostPage) {
                return getUsername() + " can't be deleted.";
            }
        }

        for (Podcast podcast : podcasts) {
            Admin.removePodcast(podcast);
        }
        Admin.removeUser(this);
        return getUsername() + " was successfully deleted.";
    }

    @Override
    public ObjectNode wrapped() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        List<Map.Entry<String, Integer>> sortedEpisodes = wrappedStats.getEpisodes().entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();
        ObjectNode topEpisodes = objectMapper.createObjectNode();
        for (Map.Entry<String, Integer> entry : sortedEpisodes) {
            topEpisodes.put(entry.getKey(), entry.getValue());
        }

        result.set("topEpisodes", topEpisodes);
        result.put("listeners", wrappedStats.getListeners());

        return result;
    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(String message, String username) {
        for (Subscriber subscriber : subscribers) {
            subscriber.update(message, username);
        }
    }
}
