package audioplayer.commands.playlist;

import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for playlist entity
 */
public class Playlist {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String owner;
    @Getter @Setter
    public boolean privatePlaylist = false;
    @Getter @Setter
    public boolean publicPlaylist = true;
    @Getter @Setter
    private int followers = 0;
    @Getter @Setter
    private ArrayList<SongInput> songs;
    @Getter @Setter
    private ArrayList<EpisodeInput> episodes;
    @Getter @Setter
    private int timestamp;
    public Playlist(final String n, final String o, final int t) {
        this.name = n;
        this.owner = o;
        this.songs = new ArrayList<>();
        this.episodes = new ArrayList<>();
        this.timestamp = t;
    }
    public Playlist() {

    }
    final void addSongs(final SongInput s) {
        this.songs.add(s);
    }
    final void addEpisodes(final EpisodeInput e) {
        this.episodes.add(e);
    }
}
