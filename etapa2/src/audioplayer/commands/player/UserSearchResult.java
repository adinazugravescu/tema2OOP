package audioplayer.commands.player;
import audioplayer.commands.playlist.Playlist;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for user-that-did-search info
 */
public class UserSearchResult {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private int numberOfResults;
    @Getter @Setter
    private ArrayList<SongInput> listOfsongs = new ArrayList<>();
    @Getter @Setter
    private ArrayList<PodcastInput> listOfpodcasts = new ArrayList<>();
    @Getter @Setter
    private ArrayList<Playlist> listOfplaylists = new ArrayList<>();
    public UserSearchResult(final String u, final int n) {
        this.username = u;
        this.numberOfResults = n;
    }
    public UserSearchResult() {

    }

    /**
     * result name by index given
     * @param index for search in list
     * @return a name of the result
     */
    public final String nameOfResult(final int index) {
        if (!listOfsongs.isEmpty()) {
            return listOfsongs.get(index).getName();
        } else {
            if (!listOfpodcasts.isEmpty()) {
                return listOfpodcasts.get(index).getName();
            } else {
                return listOfplaylists.get(index).getName();
            }
        }
    }
}

