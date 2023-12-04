package audioplayer.commands.player;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

public final class Forward {
    private Forward() {

    }

    /**
     * returns the episode in player by forward rule
     * @param podcast in player
     * @param name of the current episode
     * @param remainingTime of the current episode
     * @return needed episode
     */
    public static EpisodeInput podcast(final PodcastInput podcast, final String name,
                                          final int remainingTime) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            if (podcast.getEpisodes().get(i).getName().equals(name)) {
                if (remainingTime > 90) {
                    return podcast.getEpisodes().get(i);
                } else {
                    if (i < podcast.getEpisodes().size() - 1) { // if it is not the last episode
                                                                // returns the next one
                        return  podcast.getEpisodes().get(i + 1);
                    } else {
                        return null; // in case of last episode
                    }
                }
            }
        }
        return null;
    }
}
