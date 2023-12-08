package audioplayer.commands.userData;

import audioplayer.Constants;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import fileio.input.UserInput;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

public class GetTop5Albums {
    /**
     *
     * @param users
     * @param prefferedSongs
     * @return
     */
    public static HashMap<Album, Integer> getAlbumLikes(final ArrayList<UserInput> users, final
    ArrayList<PreferredSongs> prefferedSongs) {
        HashMap<Album, Integer> likesOfAlbums = new HashMap<>();
        for (UserInput user : users) {
            for (Album album : user.getAlbums()) {
                int likesPeralbum = 0;
                for (PreferredSongs likedSong : prefferedSongs) {
                    for (SongInput song : album.getSongs()) {
                        if (likedSong.getSongs().contains(song)) {
                            likesPeralbum++;
                        }
                    }
                }
                likesOfAlbums.put(album, likesPeralbum);
            }
        }
        return likesOfAlbums;
    }

    /**
     *
     * @param likesOfAlbums
     * @param node
     */
    public final void getTop5(final HashMap<Album, Integer> likesOfAlbums, final ObjectNode
            node) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode resultsArray = objectMapper.createArrayNode();
        List<Map.Entry<Album, Integer>> list = new ArrayList<>(likesOfAlbums.entrySet());
        Collections.sort(list, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        int count = list.size();
        if (count > 5) {
            count = Constants.getFive();
        }
        for (Map.Entry<Album, Integer> entry : list) {
            if (count < Constants.getFive()) {
                resultsArray.add(entry.getKey().getName());
                count++;
            } else {
                break;
            }
        }
        node.put("result", resultsArray);
    }

}
