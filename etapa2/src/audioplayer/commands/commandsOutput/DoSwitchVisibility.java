package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.PlaylistOwners;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoSwitchVisibility {
    private DoSwitchVisibility() {

    }

    /**
     * implements the logic for switchVisibility command
     * (it updates the playlistOwners list based on current user's
     * historic and the playlist ID)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param playlistOwners list of users and the owned playlists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<PlaylistOwners> playlistOwners) {
        Output.put(newN, "switchVisibility", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int owned = 0;
        for (PlaylistOwners owner : playlistOwners) {
            if (owner.getOwner().equals(inputCommand.getUsername())) {
                owned = 1;
                if (owner.getPlaylists().size()  < inputCommand.getPlaylistId()) {
                    message = "The specified playlist ID is too high.";
                } else {
                    if (owner.getPlaylists().get(inputCommand.getPlaylistId() - 1).
                            privatePlaylist) {
                        owner.getPlaylists().get(inputCommand.getPlaylistId() - 1).
                                privatePlaylist = false;
                        owner.getPlaylists().get(inputCommand.getPlaylistId() - 1).
                                publicPlaylist = true;
                        message = "Visibility status updated successfully to public.";
                    } else {
                        owner.getPlaylists().get(inputCommand.getPlaylistId() - 1).
                                privatePlaylist = true;
                        owner.getPlaylists().get(inputCommand.getPlaylistId() - 1).
                                publicPlaylist = false;
                        message = "Visibility status updated successfully to private.";
                    }
                }
                break;
            }
        }
        if (owned == 0) {
            message = "The specified playlist ID is too high.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
