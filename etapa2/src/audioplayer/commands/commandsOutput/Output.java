package audioplayer.commands.commandsOutput;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * class for formatting output in a ObjectNode
 */
public final class Output {
    /**
     * formats the generic output fields for all commands
     * @param node ObjectNode used for output
     * @param command current command
     * @param username current user
     * @param timestamp timestamp for action
     */
    public static void put(final ObjectNode node, final String command, final String username,
                           final int timestamp) {
        node.put("command", command);
        node.put("user", username);
        node.put("timestamp", timestamp);
    }

    /**
     * completes the ObjectNode with search command's specific output fields
     * @param node ObjectNode used for output
     * @param message message for search logic
     * @param resultsArray of names
     */
    public static void search(final ObjectNode node, final String message,
                              final ArrayNode resultsArray) {
        node.put("message", message);
        node.put("results", resultsArray);
    }

    /**
     * completes the ObjectNode with select command's field "message"
     * @param node ObjectNode used for output
     * @param message message for select logic
     */
    public static void select(final ObjectNode node, final String message) {
        node.put("message", message);
    }

    /***
     * implements the output structure stats for status
     * @param node for output
     * @param name of the current audio in load
     * @param time remained time for the current audio in load
     * @param repeat its repeat mode
     * @param shuffle its shuffle status
     * @param pause its pause status
     */
    public static void status(final ObjectNode node, final String name, final int time,
                              final String repeat, final boolean shuffle, final boolean pause) {
        node.put("name", name);
        node.put("remainedTime", time);
        node.put("repeat", repeat);
        node.put("shuffle", shuffle);
        node.put("paused", pause);
    }

    /**
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     */
    public static void doSearch(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs) {
        Output.put(newN, "search", inputCommand.getUsername(), inputCommand.getTimestamp());
        String message = inputCommand.getUsername() + " is offline.";
        newN.put("message", message);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode resultsArray = objectMapper.createArrayNode();
        newN.put("results", resultsArray);
        outputs.add(newN);
    }

    /**
     *
     * @param newN
     * @param inputCommand
     * @param outputs
     */
    public static void doLike(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs) {
        Output.put(newN, "like", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = inputCommand.getUsername() + " is offline.";
        newN.put("message", message);
        outputs.add(newN);
    }
    private Output() {

    }
}
