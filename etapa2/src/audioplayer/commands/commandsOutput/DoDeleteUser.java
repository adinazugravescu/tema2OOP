package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoDeleteUser {
    private DoDeleteUser() {
    }

    /**
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     * @param database
     * @param listOfLoaders
     * @param preferredSongs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final ArrayList<Loaders> listOfLoaders, final
                           ArrayList<PreferredSongs> preferredSongs) {
        Output.put(newN, "deleteUser", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        int exists = 0; // auxiliary field that retains if the current user exists
        String message = "";
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                int interaction = 0; // auxiliary field that retains if the current user
                                    // has an interaction in the app
                for (Loaders loader : listOfLoaders) {
                    if (loader.getSong() != null && loader.getSong().getArtist().
                                equals(inputCommand.getUsername())) {
                        if (loader.getStats().getRemainedTime() - inputCommand.getTimestamp()
                                    + loader.getTimestamp() > 0) {
                                interaction = 1;
                                break;
                            }
                        } else {
                        if (loader.getAlbum() != null && loader.getAlbum().getOwner().
                                equals(inputCommand.getUsername())) {
                            int time = 0;
                            if (!loader.getStats().paused) { // find the current track
                                time = loader.getStats().getRemainedTime() - inputCommand.
                                        getTimestamp() + loader.getTimestamp(); //update timestamp
                                if (time <= 0) {
                                    // song finished, look for the next one
                                    while (time <= 0) {
                                        String name = loader.getStats().getName();
                                        SongInput nextSong = LoadNext.forAlbum(loader.
                                                getAlbum(), name);
                                        if (nextSong != null) {
                                            name = nextSong.getName();
                                            time = nextSong.getDuration() + time;
                                        } else {
                                            time = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (time > 0) {
                                interaction = 1;
                                break;
                            }
                        }
                    }
                }
                if (interaction == 0) {
                    database.getLibrary().getSongs().removeIf(song -> song.getArtist().equals(user.
                            getUsername()));
                    for (PreferredSongs crt : preferredSongs) {
                        crt.getSongs().removeIf(song -> song.getArtist().equals(user.
                                getUsername()));
                    }
                    database.getLibrary().getUsers().remove(user);
                    database.getOnlineUsers().removeIf(u -> u.getUsername().equals(user.
                            getUsername()));
                    message = inputCommand.getUsername() + " was successfully deleted.";
                } else {
                    message = inputCommand.getUsername() + " can't be deleted.";
                }
                break;
            }
        }
        if (exists == 0) {
            message = "The username " + inputCommand.getUsername() + "  doesn't exist.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
