package audioplayer.commands.playlist;

import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class CreateNew {
    /**
     * creates a new Playlist Owner
     * @param inputCommand gives us access to name and username
     * @param playlistOwners a list to where we add the new owner
     * @param newN ObjectNode for output
     */
    public static void owner(final CommandsInput inputCommand,
                             final ArrayList<PlaylistOwners> playlistOwners,
                             final ObjectNode newN) {
        PlaylistOwners newP = new PlaylistOwners();
        Playlist play = new Playlist(inputCommand.getPlaylistName(), inputCommand.getUsername(),
                inputCommand.getTimestamp());
        newP.getPlaylists().add(play);
        newP.setOwner(inputCommand.getUsername());
        playlistOwners.add(newP);
        newN.put("message", "Playlist created successfully.");
    }

    /**
     * creates a new playlist and adds it to owner's playlists
     * @param inputCommand gives us access to name and username
     * @param crt the owner's playlists
     * @param newN ObjectNode for output
     */
    public static void playlist(final CommandsInput inputCommand, final PlaylistOwners crt,
                                final ObjectNode newN) {
        Playlist play = new Playlist(inputCommand.getPlaylistName(), inputCommand.getUsername(),
                inputCommand.getTimestamp());
        crt.getPlaylists().add(play);
        newN.put("message", "Playlist created successfully.");
    }
    private CreateNew() {

    }
}
