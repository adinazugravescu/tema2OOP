package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.userData.Album;
import audioplayer.commands.userData.Verification;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

public final class DoAddAlbum {
    private DoAddAlbum() {
    }
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final LibraryInput library) {
        Output.put(newN, "addAlbum", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; // auxiliary field that retains if specified user exists
        int isArtist = 0; // auxiliary field that retains if specified user is an artist
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("artist")) {
                    isArtist = 1;
                    for (Album album : user.getAlbums()) {
                        if (album.getName().equals(inputCommand.getName())) {
                            message = inputCommand.getUsername() + " has another album with " +
                                    "the same name.";
                            newN.put("message", message);
                            outputs.add(newN);
                            return;
                        }
                    }
                    // check for more than 2 songs in inputCommand
                    if (Verification.song(inputCommand.getSongs())) {
                        message = inputCommand.getUsername() + " has the same song at least twice " +
                                "in this album.";
                    } else {
                        // add the album
                        // first create it
                        Album newAlbum = new Album(inputCommand.getName(), inputCommand.
                                getReleaseYear(), inputCommand.getDescription(), inputCommand.
                                getSongs());
                        user.getAlbums().add(newAlbum);
                        message = inputCommand.getUsername() + " has added new album successfully.";
                        for (SongInput song : inputCommand.getSongs()) {
                            library.getSongs().add(song); // add the new songs in library
                        }
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
