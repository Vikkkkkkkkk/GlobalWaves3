package app.page;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.content.Announcement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HostPage implements Page {
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Announcement> announcements = new ArrayList<>();

    public HostPage() { }

    /**
     * Refresh the podcast list with latest data
     *
     * @param podcastList the podcast list
     */
    public void refreshPodcasts(final List<Podcast> podcastList) {
        this.podcasts.clear();
        this.podcasts.addAll(podcastList);
    }

    /**
     * Refresh the announcement list with latest data
     *
     * @param announcementList the announcement list
     */
    public void refreshAnnouncements(final List<Announcement> announcementList) {
        this.announcements.clear();
        this.announcements.addAll(announcementList);
    }

    /**
     * Prints host page
     *
     * @return the string with page info
     */
    @Override
    public String printPage() {
        ArrayList<String> podcastList = new ArrayList<>();
        ArrayList<String> episodeList = new ArrayList<>();
        ArrayList<String> announcementList = new ArrayList<>();

        String printFormat;
        for (Podcast podcast : podcasts) {
            episodeList.clear();
            for (Episode episode : podcast.getEpisodes()) {
                printFormat = "";
                printFormat += episode.getName() + " - " + episode.getDescription();
                episodeList.add(printFormat);
            }
            printFormat = "";
            printFormat += podcast.getName() + ":\n\t" + episodeList + "\n";
            podcastList.add(printFormat);
        }
        for (Announcement announcement : announcements) {
            printFormat = "";
            printFormat += announcement.getName() + ":\n\t" + announcement.getDescription() + "\n";
            announcementList.add(printFormat);
        }

        return "Podcasts:\n\t" + podcastList + "\n\nAnnouncements:\n\t" + announcementList;
    }
}
