package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoShowPreferredSongs {
    private DoShowPreferredSongs() {

    }

    /**
     * implements the logic for showPreferredSongs command
     * (depending on a user info in prefferedSongs it gets all
     * the songs names by iterating in that list)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param prefferedSongs list of owners and their liked songs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
                            ArrayNode outputs, final ArrayList<PreferredSongs> prefferedSongs) {
        ObjectMapper objectMapper = new ObjectMapper();
        Output.put(newN, "showPreferredSongs", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        ArrayNode resultsArray = objectMapper.createArrayNode();
        for (PreferredSongs list : prefferedSongs) {
            if (list.getOwner().equals(inputCommand.getUsername())) {
                for (SongInput result : list.getSongs()) {
                    resultsArray.add(result.getName());
                }
            }
        }
        newN.put("result", resultsArray);
        outputs.add(newN);
    }
}
