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
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     * @param database
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
