package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.FollowedPlaylists;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoPrintCurrentPage {
    private DoPrintCurrentPage() {
    }
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<PreferredSongs> prefferedSongs, final
    ArrayList<FollowedPlaylists> followedPlaylists, final LibraryInput library) {
        Output.put(newN, "printCurrentPage", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                if (user.getCurrentPage().equals("HomePage")) {
                    message = "Liked songs:\n\t[";
                    for (PreferredSongs iter : prefferedSongs) {
                        if (iter.getOwner().equals(inputCommand.getUsername())) {
                            for (int i = 0; i < iter.getSongs().size(); i++) {
                                message = message + iter.getSongs().get(i).getName();
                                if (i < iter.getSongs().size() - 1) {
                                    message = message + " ,";
                                }
                            }
                        }
                    }
                    message = message + "]\n\nFollowed playlists:\n\t[";
                    for (FollowedPlaylists iter : followedPlaylists) {
                        if (iter.getFollower().equals(inputCommand.getUsername())) {
                            for (int i = 0 ; i < iter.getPlaylists().size() ; i++) {
                                message = message + iter.getPlaylists().get(i).getName();
                                if (i < iter.getPlaylists().size() - 1) {
                                    message = message + " ,";
                                }
                            }
                        }
                    }
                    message = message + "]";
                }
                break;
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
