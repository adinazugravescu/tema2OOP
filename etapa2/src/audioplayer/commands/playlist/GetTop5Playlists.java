package audioplayer.commands.playlist;
import java.util.Comparator;
import audioplayer.Constants;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collections;
import java.util.ArrayList;

public class GetTop5Playlists {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * returns a list of all public playlists
     * @param playlistOwners the owners and their playlists
     * @return list of public playlists
     */
    public static ArrayList<Playlist> getPublicPlaylists(final ArrayList<PlaylistOwners>
                                                                 playlistOwners) {
        ArrayList<Playlist> allPublicPlaylists = new ArrayList<>();
        for (PlaylistOwners iter : playlistOwners) {
            for (Playlist playlist : iter.getPlaylists()) {
                if (!allPublicPlaylists.contains(playlist) && playlist.publicPlaylist) {
                    allPublicPlaylists.add(playlist);
                }
            }
        }
        return allPublicPlaylists;
    }

    /**
     * returns in the output the name of the top 5 most followed playlists
     * @param playlists the list that we look into
     * @param node ObjectNode where we put the names
     */
    public final void getTop5(final ArrayList<Playlist> playlists, final ObjectNode node) {
        ArrayNode resultsArray = objectMapper.createArrayNode();
        //playlists.sort(Comparator.comparingInt(Playlist::getFollowers).reversed());
        Collections.sort(playlists, Comparator
                .comparingInt(Playlist::getFollowers) // by number of followers in reversed order
                .reversed()
                .thenComparingInt(Playlist::getTimestamp)); // by timestamp in ascending order

        // Now your playlists ArrayList is sorted based on your criteria
        int size = Math.min(playlists.size(), Constants.getFive());

        for (int i = 0; i < size; i++) {
            resultsArray.add(playlists.get(i).getName());
        }
        node.put("result", resultsArray);

    }

}
