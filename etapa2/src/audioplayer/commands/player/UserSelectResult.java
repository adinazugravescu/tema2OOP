package audioplayer.commands.player;

import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.userData.Album;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

/**
 * class for user-that-did-select info
 */

public class UserSelectResult {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private SongInput song = new SongInput();
    @Getter @Setter
    private PodcastInput podcast = new PodcastInput();
    @Getter @Setter
    private Playlist playlist = new Playlist();
    @Getter @Setter
    private Album album = new Album();

}
