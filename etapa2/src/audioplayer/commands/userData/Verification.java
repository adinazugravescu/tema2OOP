package audioplayer.commands.userData;
import java.util.HashMap;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class Verification {
    private  Verification() {
    }

    /**
     *
     * @param songs
     * @return
     */
    public static boolean song(final ArrayList<SongInput> songs) {
        HashMap<String, Integer> songNameCount = new HashMap<>();
        // Count occurrences of song names
        for (SongInput song : songs) {
            String songName = song.getName();
            songNameCount.put(songName, songNameCount.getOrDefault(songName, 0) + 1);
        }

        // Check if there are more than two songs with the same name
        boolean aux = false; // auxiliary field that retains if there are more than 2 equal songs
        for (HashMap.Entry<String, Integer> entry : songNameCount.entrySet()) {
            if (entry.getValue() >= 2) {
                aux = true;
                break;
            }
        }
        return aux;
    }
}
