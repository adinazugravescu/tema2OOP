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
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     * @param database
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
                    user.setCurrentPage(inputCommand.getNextPage());
                    message = inputCommand.getUsername() + " accessed " + inputCommand.getNextPage()
                            + " successfully.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
