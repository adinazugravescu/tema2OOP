package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoLike {
    private DoLike() {

    }

    /**
     * implements the logic for like command
     * (depending on a user load history to be a song,
     * it updates the prefferedSongs)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param prefferedSongs list of owners and their liked songs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
                        ArrayNode outputs, final ArrayList<Loaders> listOfLoaders,
                           final ArrayList<PreferredSongs> prefferedSongs) {
        Output.put(newN, "like", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int load = 0; // auxiliary field that retains if the current user has something in load
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                if (loader.getSong() != null && loader.getSong().getName()
                        != null) {
                    load++;
                    int userHaspreffered = 0; // retains if current user has info in prefferedSongs
                    for (PreferredSongs pref : prefferedSongs) {
                        if (pref.getOwner().equals(inputCommand.getUsername())) {
                            userHaspreffered++;
                            if (pref.getSongs().contains(loader.getSong())) {
                                message = "Unlike registered successfully.";
                                pref.getSongs().remove(loader.getSong());
                            } else {
                                message = "Like registered successfully.";
                                pref.getSongs().add(loader.getSong());
                            }
                        }
                    }
                    if (userHaspreffered == 0) { // creates a new element in prefferedSongs
                                                // with current info
                        PreferredSongs newP = new PreferredSongs();
                        newP.setOwner(inputCommand.getUsername());
                        newP.getSongs().add(loader.getSong());
                        message = "Like registered successfully.";
                        prefferedSongs.add(newP);
                    }
                } else {
                    if (loader.getPlaylist() != null && loader.getPlaylist().getSongs() != null
                            && loader.getPlaylist().getName() != null) {
                        load++;
                        int userHasPreffered = 0;
                        for (PreferredSongs pref : prefferedSongs) {
                            if (pref.getOwner().equals(inputCommand.getUsername())) {
                                userHasPreffered++;
                                for (SongInput s : loader.getPlaylist().getSongs()) {
                                    // search through playlist
                                    if (pref.getSongs().contains(s)) {
                                        message = "Unlike registered successfully.";
                                        pref.getSongs().remove(s);
                                    } else {
                                        message = "Like registered successfully.";
                                        pref.getSongs().add(s);
                                    }
                                }
                            }
                        }
                        if (userHasPreffered == 0) { // creates a new element in prefferedSongs
                                                    // with current info
                            PreferredSongs newP = new PreferredSongs();
                            newP.setOwner(inputCommand.getUsername());
                            newP.getSongs().add(loader.getPlaylist().getSongs().get(0));
                            message = "Like registered successfully.";
                            prefferedSongs.add(newP);
                        }
                    } else {
                        load++;
                        message = "Loaded source is not a song.";
                    }
                }
            }

        }
        if (load == 0) {
            message = "Please load a source before liking or unliking.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
