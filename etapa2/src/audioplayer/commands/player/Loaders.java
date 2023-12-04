package audioplayer.commands.player;

import audioplayer.commands.playlist.Playlist;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

/**
 * class for load info
 */

public class Loaders {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private int timestamp;
    @Getter @Setter
    private SongInput song = null;
    @Getter @Setter
    private PodcastInput podcast = null;
    @Getter @Setter
    private Playlist playlist = null;
    @Getter @Setter
    private StatsForStatus stats;
    public Loaders(final String u, final int t) {
        this.username = u;
        this.timestamp = t;
    }
}
