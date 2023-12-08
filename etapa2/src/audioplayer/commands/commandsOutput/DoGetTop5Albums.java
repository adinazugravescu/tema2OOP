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
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     * @param database
     * @param prefferedSongs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final ArrayList<PreferredSongs> prefferedSongs) {
        newN.put("command", "getTop5Albums");
        newN.put("timestamp", inputCommand.getTimestamp());
        GetTop5Albums get = new GetTop5Albums();
        HashMap<Album, Integer> albumLikes = GetTop5Albums.getAlbumLikes(database.getLibrary().
                getUsers(), prefferedSongs);
        get.getTop5(albumLikes, newN);
        outputs.add(newN);
    }
}
