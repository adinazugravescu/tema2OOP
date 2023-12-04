package audioplayer.commands.playlist;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * info about each owner and their playlists
 */
public class PlaylistOwners {
    @Getter @Setter
    private String owner;
    @Getter @Setter
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public PlaylistOwners() {
    }

    public PlaylistOwners(final PlaylistOwners owner) {
        this.owner = owner.owner;
        this.playlists = owner.playlists;
    }
}
