package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.userData.Announcement;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

public final class DoAddAnnouncement {
    private DoAddAnnouncement() {
    }

    /**
     * implements the logic for addAnnouncement command
     * (goes through the list of users(hosts) and builds the logic for the 4 conditions)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "addAnnouncement", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; // auxiliary field that retains if specified user exists
        int isHost = 0; // auxiliary field that retains if specified user is a host
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("host")) {
                    isHost = 1;
                    for (Announcement announcement : user.getAnnouncements()) {
                        if (announcement.getName().equals(inputCommand.getName())) {
                            message = inputCommand.getUsername() + " has already added an "
                                    + "announcement with this name.";
                            newN.put("message", message);
                            outputs.add(newN);
                            return;
                        }
                    }
                    // create and add announcement
                    Announcement newAnnouncement = new Announcement(inputCommand.getName(),
                            inputCommand.getDescription());
                    user.getAnnouncements().add(newAnnouncement);
                    message = inputCommand.getUsername()
                                + " has successfully added new announcement.";
                    break;
                }
            }
        }
        if (exists == 0) {
            message = "The username " + inputCommand.getUsername() + " doesn't exist.";
        } else {
            if (isHost == 0) {
                message = inputCommand.getUsername() + " is not a host.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
