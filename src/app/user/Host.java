package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.page.HostPage;
import app.user.content.Announcement;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Host extends User {
    private List<Podcast> podcasts;
    private List<Announcement> announcements;
    private HostPage hostPage;

    public Host(final String username, final int age,
                final String city, final Enums.UserType type) {
        super(username, age, city, type);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        hostPage = new HostPage();
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
            episodesPodcast.add(new Episode(episodeInput.getName(),
                    episodeInput.getDuration(), episodeInput.getDescription()));
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
}
