package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.playlist.PlaylistOwners;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoShowPlaylists {
    private DoShowPlaylists() {

    }

    /**
     * implements the logic for showPlaylists command
     * (if the current user has info in playlistOwners
     * it formats in outputs the statistics of every playlist in the list)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param playlistOwners list of users and the owned playlists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
                        ArrayNode outputs, final ArrayList<PlaylistOwners> playlistOwners) {
        Output.put(newN, "showPlaylists", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode result = objectMapper.createArrayNode(); // this goes to newN results
        for (PlaylistOwners owner : playlistOwners) {
            if (owner.getOwner().equals(inputCommand.getUsername())) {
                for (Playlist playlist : owner.getPlaylists()) {
                    ObjectNode newResult = objectMapper.createObjectNode();
                    newResult.put("name", playlist.getName());
                    ArrayNode songs = objectMapper.createArrayNode(); // for playlist's songs names
                    for (SongInput s : playlist.getSongs()) {
                        songs.add(s.getName());
                    }
                    newResult.put("songs", songs);
                    if (playlist.publicPlaylist) {
                        newResult.put("visibility", "public");
                    } else {
                        newResult.put("visibility", "private");
                    }
                    newResult.put("followers", playlist.getFollowers());
                    result.add(newResult);
                }
            }
        }
        newN.put("result", result);
        outputs.add(newN);
    }
}
