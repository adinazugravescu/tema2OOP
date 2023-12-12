package audioplayer.commands.playlist;

import audioplayer.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class GetTop5Songs {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * returns a HashMap with all liked songs
     * @param followers list of what each user has followed
     * @return a map : key = song name, value = number of likes
     */
    public static HashMap<SongInput, Integer> getLikedSongs(final ArrayList<PreferredSongs>
                                                                    followers) {
        HashMap<SongInput, Integer> likedSongs = new HashMap<>();
        for (PreferredSongs iter : followers) {
            for (SongInput song : iter.getSongs()) {
                if (!likedSongs.containsKey(song)) {
                    likedSongs.put(song, 1);
                } else {
                    int currentCount = likedSongs.get(song);
                    likedSongs.put(song, currentCount + 1);
                }
            }
        }
        return likedSongs;
    }

    /**
     * writes in the output node the name of the songs
     * @param likedSongs list of all liked songs
     * @param node output node
     * @param songs if liked songs size < 5 , it gets songs from the library
     */
    public final void getTop5(final HashMap<SongInput, Integer> likedSongs, final ObjectNode node,
                              final ArrayList<SongInput> songs) {
       ArrayNode resultsArray = objectMapper.createArrayNode();
        List<Map.Entry<SongInput, Integer>> list = new ArrayList<>(likedSongs.entrySet());
        list.sort((entry1, entry2) -> {
            int comparison = entry2.getValue().compareTo(entry1.getValue()); // in reverse order
            if (comparison != 0) {
                return comparison; // if values are different, return the comparison result
            } else {
                // if values are the same, maintain the original order in the list
                return Integer.compare(list.indexOf(entry2), list.indexOf(entry1));
            }
        });

        int count = 0;
        for (Map.Entry<SongInput, Integer> entry : list) {
            if (count < Constants.getFive()) {
                resultsArray.add(entry.getKey().getName());
                count++;
            } else {
                break;
            }
        }
        if (count < Constants.getFive()) {
            for (SongInput song : songs) {
                if (count >= Constants.getFive()) {
                    break;
                }
                resultsArray.add(song.getName());
                count++;
            }
        }
        node.put("result", resultsArray);

    }
}
