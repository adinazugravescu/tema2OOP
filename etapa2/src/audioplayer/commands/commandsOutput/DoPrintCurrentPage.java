package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.FollowedPlaylists;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoPrintCurrentPage {
    private DoPrintCurrentPage() {
    }

    /**
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     * @param prefferedSongs
     * @param followedPlaylists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<PreferredSongs> prefferedSongs, final
    ArrayList<FollowedPlaylists> followedPlaylists, final Database database) {
        Output.put(newN, "printCurrentPage", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        for (UserInput user : database.getLibrary().getUsers()) {
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
                            for (int i = 0; i < iter.getPlaylists().size(); i++) {
                                message = message + iter.getPlaylists().get(i).getName();
                                if (i < iter.getPlaylists().size() - 1) {
                                    message = message + " ,";
                                }
                            }
                        }
                    }
                    message = message + "]";
                } else {
                    if (user.getCurrentPage().equals("Artist page")) {
                        message = "Albums:\n\t[";
                        for (int i = 0; i < user.getUsersPage().getAlbums().size(); i++) {
                            message = message + user.getUsersPage().getAlbums().get(i).getName();
                            if (i < user.getUsersPage().getAlbums().size() - 1) {
                                message = message + " ,";
                            }
                        }
                        message = message + "]\n\nMerch:\n\t[";
                        for (int i = 0; i < user.getUsersPage().getMerches().size(); i++) {
                            message = message + user.getUsersPage().getMerches().get(i).getName()
                                    + " - " + user.getUsersPage().getMerches().get(i).getPrice()
                                    + ":\n\t" + user.getUsersPage().getMerches().
                                    get(i).getDescription();
                            if (i < user.getUsersPage().getMerches().size() - 1) {
                                message = message + ", ";
                            }
                        }
                        message = message + "]\n\nEvents:\n\t[";
                        for (int i = 0; i < user.getUsersPage().getEvents().size(); i++) {
                            message = message + user.getUsersPage().getEvents().get(i).getName()
                                    + " - " + user.getUsersPage().getEvents().get(i).getDate()
                                    + ":\n\t" + user.getUsersPage().getEvents().get(i).
                                    getDescription();
                            if (i < user.getUsersPage().getEvents().size() - 1) {
                                message = message + ", ";
                            }
                        }
                        message = message + "]";
                    }
                }
                break;
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
