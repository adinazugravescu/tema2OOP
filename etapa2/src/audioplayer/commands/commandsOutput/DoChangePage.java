package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

public final class DoChangePage {
    private DoChangePage() {
    }

    /**
     * implements the logic for changePage command
     * (if input command's nextPage is a valid option, it changes the user's current page)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "changePage", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        if (!inputCommand.getNextPage().equals("LikedContent") && !inputCommand.getNextPage().
                equals("Home")) {
            message = inputCommand.getUsername() + " is trying to access a non-existent page.";
            newN.put("message", message);
            outputs.add(newN);
            return;
        }
        for (UserInput user : database.getLibrary().getUsers()) {
            if (inputCommand.getUsername().equals(user.getUsername())) {
                    user.setCurrentPage(inputCommand.getNextPage()); // set current page
                    message = inputCommand.getUsername() + " accessed " + inputCommand.getNextPage()
                            + " successfully.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
