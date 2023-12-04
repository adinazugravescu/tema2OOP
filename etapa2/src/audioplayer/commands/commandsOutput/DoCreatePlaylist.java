package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.CreateNew;
import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.playlist.PlaylistOwners;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoCreatePlaylist {
    private DoCreatePlaylist() {

    }

    /**
     * implements the logic for createPlaylist command
     * (it updates the playlistOwners list based on user's
     * historic and the given ID)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param playlistOwners list of users and the owned playlists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
                    ArrayNode outputs, final ArrayList<PlaylistOwners> playlistOwners) {
        Output.put(newN, "createPlaylist", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        if (playlistOwners.isEmpty()) {
            // creates a new owner, adds it to playlistOwners and completes the ObjectNode
            CreateNew.owner(inputCommand, playlistOwners, newN);
        } else {
            // search for the owner among the list
            int f1 = 0; // auxiliary field that retains if the current user has info
            // in playlistOwners
            int f2 = 0; // auxiliary field that retains if the current user has
            // already a playlist named that way
            for (PlaylistOwners owner : playlistOwners) {
                PlaylistOwners crt;
                if (owner.getOwner().equals(inputCommand.getUsername())) {
                    f1 = 1;
                    crt = new PlaylistOwners(owner);
                    for (Playlist playlist : owner.getPlaylists()) {
                        if (playlist.getName().equals(inputCommand.
                                getPlaylistName())) {
                            newN.put("message",
                                    "A playlist with the same name already exists.");
                            f2 = 1;
                        }
                    }
                    if (f2 == 0) {  // if there isn't a playlist with that name, create one
                                    // and complete the ObjectNode
                        CreateNew.playlist(inputCommand, crt, newN);
                        break;
                    }
                    break;
                }

            }
            if (f1 == 0) {  // if the user has no info, create a new element in playlistOwners
                            // and complete the ObjectNode
                CreateNew.owner(inputCommand, playlistOwners, newN);
            }
        }
        outputs.add(newN);
    }
}
