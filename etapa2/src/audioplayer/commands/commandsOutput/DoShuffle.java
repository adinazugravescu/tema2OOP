package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.playlist.PlaylistManager;
import audioplayer.commands.playlist.UnshuffledSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoShuffle {
    private DoShuffle() {

    }

    /**
     * implements the logic for shuffle command
     * (updates the load history of current user if
     * there is a playlist in load, more exactly the order of
     * the songs in the loaded playlist if shuffle is activated
     * or get them in initial order found in initialPlaylists for that user)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     * @param initialPlaylists stores the initial order of songs of a user's playlist
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<Loaders> listOfLoaders, final ArrayList<UnshuffledSongs>
                                                                            initialPlaylists) {
        Output.put(newN, "shuffle", inputCommand.getUsername(), inputCommand.getTimestamp());
        String message = "";
        int load = 0; // auxiliary field that retains if the current user has something in load
        int isPlaylist = 0; // auxiliary field that retains if the user has playlist in load
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                load = 1;
                if (loader.getPlaylist() != null) {
                    isPlaylist = 1;
                    if (!loader.getStats().shuffle) { // shuffle will be activated
                        // creating a new UnshuffledSongs object that retains the user and
                        // the original order of songs in playlist in case of a future deactivation
                        UnshuffledSongs current = new UnshuffledSongs();
                        current.setOwner(inputCommand.getUsername());
                        current.setSongs(loader.getPlaylist().getSongs());
                        initialPlaylists.add(current);
                        PlaylistManager manager = new PlaylistManager(inputCommand.getSeed());
                        manager.shufflePlaylist(loader.getPlaylist()); // shuffle songs
                        loader.getStats().shuffle = true;
                        message = "Shuffle function activated successfully.";
                        int remainingTime = loader.getStats().getRemainedTime() - inputCommand.
                                getTimestamp() + loader.getTimestamp();
                        // if load has finished, doesn't do shuffle
                        if (remainingTime <= 0) {
                            loader.getStats().setName("");
                            loader.getStats().setRemainedTime(0);
                            message = "Please load a source before using the shuffle function.";
                        }
                    } else {
                        loader.getStats().shuffle = false;
                        message = "Shuffle function deactivated successfully.";
                        //if the player runs all the songs, you can't command shuffle
                        int remainingTime = loader.getStats().getRemainedTime() - inputCommand.
                                getTimestamp() + loader.getTimestamp();
                        if (remainingTime <= 0) {
                            loader.getStats().setName("");
                            loader.getStats().setRemainedTime(0);
                            message = "Please load a source before using the shuffle function.";
                        }
                        for (UnshuffledSongs iter : initialPlaylists) {
                            if (iter.getOwner().equals(inputCommand.getUsername()) && iter.
                                    getSongs().size() == loader.getPlaylist().getSongs().size()) {
                                loader.getPlaylist().setSongs(iter.getSongs()); // setting songs in
                                initialPlaylists.remove(iter);                  // initial order
                                break;
                            }
                        }
                    }
                }
                break;
            }
        }
        if (load == 0) {
            message = "Please load a source before using the shuffle function.";
        } else {
            if (isPlaylist == 0) {
                message = "The loaded source is not a playlist.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
