package audioplayer.commands.playlist;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for follow info
 */
public class FollowedPlaylists {
    @Getter @Setter
    private String follower;
    @Getter @Setter
    private ArrayList<Playlist> playlists = new ArrayList<>();
}
