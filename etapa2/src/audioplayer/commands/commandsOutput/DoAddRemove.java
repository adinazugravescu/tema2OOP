package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.playlist.PlaylistOwners;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoAddRemove {
    private DoAddRemove() {

    }

    /**
     * implements the logic for addRemoveInPlaylist command
     * (if the current user has in load (listOfLoaders) a song
     * it is added / removed based on each user playlist history)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     * @param playlistOwners list of users and their playlists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
                        ArrayNode outputs, final ArrayList<Loaders> listOfLoaders, final
                        ArrayList<PlaylistOwners> playlistOwners) {
        Output.put(newN, "addRemoveInPlaylist", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int loaded = 0; // auxiliary field that retains if the current user has something in load
        int exists = 0; // auxiliary field that retains if the current user has a valid playlist
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                loaded = 1;
                if (loader.getSong() == null && loader.getAlbum() == null) {
                    message = "The loaded source is not a song.";
                } else {
                    for (PlaylistOwners owner : playlistOwners) {
                        if (owner.getOwner().equals(loader.getUsername())) { //if user has playlist
                            if (owner.getPlaylists().size()
                                    < inputCommand.getPlaylistId()) { // playlistId is not valid
                                message = "The specified playlist does not exist.";
                            } else {
                                if (owner.getPlaylists().get(inputCommand.
                                        getPlaylistId() - 1).getSongs() != null) {
                                    if (loader.getSong() != null) {
                                        if (owner.getPlaylists().get(inputCommand.getPlaylistId()
                                                - 1).getSongs().contains(loader.getSong())) {
                                            owner.getPlaylists().get(inputCommand.getPlaylistId()
                                                    - 1).getSongs().remove(loader.getSong());
                                            message = "Successfully removed "
                                                    + "from playlist.";
                                        } else {
                                            if (loader.getSong().getName() != null) {
                                                owner.getPlaylists().get(inputCommand.
                                                                getPlaylistId() - 1).
                                                        getSongs().add(loader.getSong());
                                                message = "Successfully added to playlist.";
                                            }
                                        }
                                    } else {
                                        if (loader.getAlbum() != null) {
                                            SongInput song = new SongInput();
                                            for (SongInput crt : loader.getAlbum().getSongs())  {
                                                if (crt.getName().equals(loader.getStats().
                                                        getName())) {
                                                    song = crt;
                                                    break;
                                                }
                                            }
                                            if (owner.getPlaylists().get(inputCommand.
                                                    getPlaylistId() - 1).getSongs().
                                                    contains(song)) {
                                                owner.getPlaylists().get(inputCommand.
                                                                getPlaylistId() - 1).
                                                        getSongs().remove(song);
                                                message = "Successfully removed "
                                                        + "from playlist.";
                                            } else {
                                                if (song.getName() != null) {
                                                    owner.getPlaylists().get(inputCommand.
                                                                    getPlaylistId() - 1).
                                                            getSongs().add(song);
                                                    message = "Successfully added to playlist.";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            exists = 1;
                        }
                    }
                }
                break;
            }
        }
        if (loaded == 0) {
            message = "Please load a source before adding to or removing "
                    + "from the playlist.";
        }
        if (exists == 0 && loaded != 0 && message.isEmpty()) {
            message = "The specified playlist does not exist.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
