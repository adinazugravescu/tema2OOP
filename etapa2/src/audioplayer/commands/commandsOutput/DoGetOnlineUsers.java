package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.UserInput;

public final class DoGetOnlineUsers {
    private DoGetOnlineUsers() {
    }

    /**
     * implements the logic for getOnlineUsers command
     * (goes through online users list and lists their names)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        newN.put("command", "getOnlineUsers");
        newN.put("timestamp", inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode resultsArray = objectMapper.createArrayNode();
        for (UserInput user : database.getOnlineUsers()) {
            resultsArray.add(user.getUsername());
        }
        newN.put("result", resultsArray);
        outputs.add(newN);
    }
}
