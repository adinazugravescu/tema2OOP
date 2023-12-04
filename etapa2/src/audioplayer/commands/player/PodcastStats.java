package audioplayer.commands.player;

import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;

/**
 * class for previously loaded podcast info
 */

public class PodcastStats {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private PodcastInput podcast;
    @Getter @Setter
    private StatsForStatus stats;
    public PodcastStats(final String u, final PodcastInput p, final StatsForStatus s,
                        final int time) {
        this.username = u;
        this.podcast = p;
        this.stats = s;
        this.stats.setRemainedTime(this.stats.getRemainedTime() - time);
    }
}
