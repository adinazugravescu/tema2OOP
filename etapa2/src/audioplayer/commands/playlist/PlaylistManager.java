package audioplayer.commands.playlist;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import fileio.input.SongInput;
public final class PlaylistManager {
    private Random random;

    public PlaylistManager(final long seed) {
        this.random = new Random(seed);
    }

    /**
     * shuffles the songs of given playlist based random
     * @param playlist given playlist
     */

    public void shufflePlaylist(final Playlist playlist) {
        if (playlist != null && playlist.getSongs() != null) {
            ArrayList<Integer> indices = new ArrayList<>();
            for (int i = 0; i < playlist.getSongs().size(); i++) {
                indices.add(i);
            }
            Collections.shuffle(indices, random);
            ArrayList<SongInput> shuffledSongs = new ArrayList<>();
            for (int index : indices) {
                shuffledSongs.add(playlist.getSongs().get(index));
            }
            playlist.setSongs(shuffledSongs);
        }
    }
}
