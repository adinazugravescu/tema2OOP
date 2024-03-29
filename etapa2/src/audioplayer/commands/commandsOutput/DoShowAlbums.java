package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.userData.Album;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import fileio.input.UserInput;



public final class DoShowAlbums {
    private DoShowAlbums() {
    }

    /**
     * implements the logic for showAlbums command
     * (lists an artist's albums and their songs)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "showAlbums", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode results = objectMapper.createArrayNode();
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                for (Album album : user.getAlbums()) {
                    ArrayNode songNames = objectMapper.createArrayNode();
                    ObjectNode current = objectMapper.createObjectNode();
                    for (SongInput song : album.getSongs()) {
                        songNames.add(song.getName());
                    }
                    current.put("name", album.getName());
                    current.put("songs", songNames);
                    results.add(current);
                }
            }
        }
        newN.set("result", results);
        outputs.add(newN);
    }
}
