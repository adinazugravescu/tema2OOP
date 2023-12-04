package audioplayer.commands.player;

import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;

public final class Backward {
    private Backward() {

    }

    /**
     * returns the current episode in player by name
     * @param podcast current loaded podcast
     * @param name of the current episode
     * @return needed episode
     */
    public static EpisodeInput podcast(final PodcastInput podcast, final String name) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            if (podcast.getEpisodes().get(i).getName().equals(name)) {
                return podcast.getEpisodes().get(i);
            }
        }
        return null;
    }
}
