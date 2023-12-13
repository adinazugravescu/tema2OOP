package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.userData.Album;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoRemoveAlbum {
    private DoRemoveAlbum() {
    }

    /**
     * implements the logic for removeAlbum command
     * (verifies if the album exists and if it does, checks if it can
     * be deleted(if there isn't in someone's load) and deletes it and its
     * songs from the library)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "removeAlbum", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; // auxiliary field that retains if specified user exists
        int isArtist = 0; // auxiliary field that retains if specified user is an artist
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("artist")) {
                    isArtist = 1;
                    int hasIt = 0;
                    Album crt = new Album();
                    for (Album album : user.getAlbums()) {
                        if (album.getName().equals(inputCommand.getName())) {
                            hasIt = 1;
                            crt = album;
                            break;
                        }
                    }
                    if (hasIt == 0) {
                        message = inputCommand.getUsername() + " doesn't have an album "
                                + "with the given name.";
                        newN.put("message", message);
                        outputs.add(newN);
                        return;
                    }
                    // verify if it can be deleted
                    int interaction = 0;
                    for (Loaders loader : listOfLoaders) {
                        if (loader.getAlbum() != null && loader.getAlbum().getOwner().
                                equals(inputCommand.getUsername())) {
                            int time = 0;
                            if (!loader.getStats().paused) { // find the song
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
                    if (interaction == 1) {
                        message = inputCommand.getUsername() + " can't delete this album.";
                    } else {
                        // delete the album's songs from database
                        for (SongInput song : crt.getSongs()) {
                            database.getLibrary().getSongs().removeIf(s -> s.getName().
                                    equals(song.getName()));
                        }
                        // delete album from user
                        user.getAlbums().removeIf(album -> album.getName().
                                equals(inputCommand.getName()));
                        message = inputCommand.getUsername() + " deleted the album successfully.";
                    }
                    break;
                }
            }
        }
        if (exists == 0) {
            message = "The username " + inputCommand.getUsername() + " doesn't exist.";
        } else {
            if (isArtist == 0) {
                message = inputCommand.getUsername() + " is not an artist.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
