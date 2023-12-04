package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.GetTop5Playlists;
import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.playlist.PlaylistOwners;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoGetTop5Playlists {
    private DoGetTop5Playlists() {

    }

    /**
     * implements the logic for getTop5Playlists command
     * (it finds the possible choices - the public playlists,
     * and sorts them based on the followers)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param playlistOwners list of users and the owned playlists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<PlaylistOwners> playlistOwners) {
        newN.put("command", "getTop5Playlists");
        newN.put("timestamp", inputCommand.getTimestamp());
        GetTop5Playlists get = new GetTop5Playlists();
        // find all the public playlists
        ArrayList<Playlist> publicPlaylists = GetTop5Playlists.
                getPublicPlaylists(playlistOwners);
        // find the most followed min(publicPlaylists.size(), Constants.getFive()) playlists
        get.getTop5(publicPlaylists, newN); // and formats the ObjectNode
        outputs.add(newN);
    }
}
