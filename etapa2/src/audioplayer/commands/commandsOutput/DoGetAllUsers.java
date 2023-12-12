package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

public final class DoGetAllUsers {
    private DoGetAllUsers() {
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
        newN.put("command", "getAllUsers");
        newN.put("timestamp", inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode resultsArray = objectMapper.createArrayNode();
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getType().equals("user")) {
                resultsArray.add(user.getUsername());
            }
        }
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getType().equals("artist")) {
                resultsArray.add(user.getUsername());
            }
        }
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getType().equals("host")) {
                resultsArray.add(user.getUsername());
            }
        }
        newN.put("result", resultsArray);
        outputs.add(newN);
    }
}
