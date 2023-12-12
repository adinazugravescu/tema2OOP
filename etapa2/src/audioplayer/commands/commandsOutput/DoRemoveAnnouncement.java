package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.userData.Announcement;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

public final class DoRemoveAnnouncement {
    private DoRemoveAnnouncement() {
    }

    /**
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     * @param database
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "removeAnnouncement", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; // auxiliary field that retains if specified user exists
        int isHost = 0; // auxiliary field that retains if specified user is a host
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("host")) {
                    isHost = 1;
                    int hasIt = 0;
                    for (Announcement announcement : user.getAnnouncements()) {
                        if (announcement.getName().equals(inputCommand.getName())) {
                            hasIt = 1;
                            break;
                        }
                    }
                    if (hasIt == 0) {
                        message = inputCommand.getUsername() + " has no announcement "
                                + "with the given name.";
                    } else {
                        user.getAnnouncements().removeIf(announcement -> announcement.getName().
                                equals(inputCommand.getName()));
                        message = inputCommand.getUsername() + " has successfully deleted the "
                                + "announcement.";
                    }
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
