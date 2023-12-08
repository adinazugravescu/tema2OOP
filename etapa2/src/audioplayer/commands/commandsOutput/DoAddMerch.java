package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.userData.Merch;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

public final class DoAddMerch {
    private DoAddMerch() {
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
        Output.put(newN, "addMerch", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        if (inputCommand.getPrice() < 0) {
            message = "Price for merchandise can not be negative.";
            newN.put("message", message);
            outputs.add(newN);
            return;
        }
        int exists = 0; // auxiliary field that retains if specified user exists
        int isArtist = 0; // auxiliary field that retains if specified user is an artist
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("artist")) {
                    isArtist = 1;
                    for (Merch merch : user.getMerches()) {
                        if (inputCommand.getName().equals(merch.getName())) {
                            message = inputCommand.getUsername() + " has merchandise with "
                                    + "the same name.";
                            newN.put("message", message);
                            outputs.add(newN);
                            return;
                        }
                    }
                    // all conditions verified, create and add event
                    Merch newMerch = new Merch(inputCommand.getName(), inputCommand.
                            getDescription(), inputCommand.getPrice());
                    user.getMerches().add(newMerch);
                    message = inputCommand.getUsername() + " has added new merchandise "
                            + "successfully.";
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
