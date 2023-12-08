package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.GetTop5Songs;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import audioplayer.Database;
import java.util.ArrayList;
import java.util.HashMap;

public final class DoGetTop5Songs {
    private DoGetTop5Songs() {

    }

    /**
     * implements the logic for getTop5Songs command
     * (finding the liked songs and sorting them
     * in the top)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param database used in case there are not enough liked songs,
     *                and a search is made among library songs
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param prefferedSongs list of owners and their liked songs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    Database database, final ArrayNode outputs, final ArrayList<PreferredSongs> prefferedSongs) {
        newN.put("command", "getTop5Songs");
        newN.put("timestamp", inputCommand.getTimestamp());
        GetTop5Songs get = new GetTop5Songs();
        // returns a hashMap that has all the likes for each song
        HashMap<SongInput, Integer> likedSongs = GetTop5Songs.getLikedSongs(
                prefferedSongs);
        // finds most liked songs and formats ObjectNode
        get.getTop5(likedSongs, newN, database.getLibrary().getSongs());
        outputs.add(newN);
    }
}
