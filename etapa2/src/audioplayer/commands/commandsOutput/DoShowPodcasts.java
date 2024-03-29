package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.UserInput;

public final class DoShowPodcasts {
    private DoShowPodcasts() {
    }

    /**
     * implements the logic for showPodcasts command
     * (lists a host's podcasts and their episodes)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "showPodcasts", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode results = objectMapper.createArrayNode();
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                for (PodcastInput podcast : user.getPodcasts()) {
                    ArrayNode podcastNames = objectMapper.createArrayNode();
                    ObjectNode current = objectMapper.createObjectNode();
                    for (EpisodeInput episode : podcast.getEpisodes()) {
                        podcastNames.add(episode.getName());
                    }
                    current.put("name", podcast.getName());
                    current.put("episodes", podcastNames);
                    results.add(current);
                }
            }
        }
        newN.set("result", results);
        outputs.add(newN);
    }
}
