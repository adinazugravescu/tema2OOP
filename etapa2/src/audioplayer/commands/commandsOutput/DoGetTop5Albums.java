package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.PreferredSongs;
import audioplayer.commands.userData.Album;
import audioplayer.commands.userData.GetTop5Albums;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;

public final class DoGetTop5Albums {
    private DoGetTop5Albums() {
    }

    /**
     * implements the logic for getTop5Albums command
     * (calculates the number of total likes per user's album and sorts them)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     * @param preferredSongs list of owners and their liked songs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final ArrayList<PreferredSongs> preferredSongs) {
        newN.put("command", "getTop5Albums");
        newN.put("timestamp", inputCommand.getTimestamp());
        GetTop5Albums get = new GetTop5Albums();
        HashMap<Album, Integer> albumLikes = GetTop5Albums.getAlbumLikes(database.getLibrary().
                getUsers(), preferredSongs); // get every album(key) and its likes(value)
        get.getTop5(albumLikes, newN); // sort albumLikes and get the first 5/size albums
                                    // + formats the ObjectNode
        outputs.add(newN);
    }
}
