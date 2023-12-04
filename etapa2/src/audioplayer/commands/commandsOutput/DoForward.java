package audioplayer.commands.commandsOutput;

import audioplayer.Constants;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Forward;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;

import java.util.ArrayList;

public final class DoForward {
    private  DoForward() {

    }

    /**
     * implements the logic for forward command
     * (if a user has a podcast in load, it changes its
     * remaining time based on the current one in statistics)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "forward", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int load = 0; // auxiliary field that retains if the current user has something in load
        int isPodcast = 0; // auxiliary field that retains if the current user has podcast in load
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                load = 1;
                if (loader.getPodcast() != null) {
                    isPodcast = 1;
                    // calculates the remaining time at the current moment
                    int remainingTime = loader.getStats().getRemainedTime() - (inputCommand.
                            getTimestamp() - loader.getTimestamp());
                    EpisodeInput episode = Forward.podcast(loader.getPodcast(), loader.
                            getStats().getName(), remainingTime);
                    if (episode != null) {
                        loader.getStats().setName(episode.getName());
                        if (remainingTime > Constants.getNinety()) { // stays at current episode
                            loader.getStats().setRemainedTime(remainingTime - Constants.
                                    getNinety());
                        } else {
                            if (remainingTime >= 0) {
                                loader.getStats().setRemainedTime(episode.
                                        getDuration());
                            } else {
                                loader.getStats().setRemainedTime(episode.getDuration()
                                        + remainingTime - Constants.getNinety());
                            }
                        }
                        message = "Skipped forward successfully.";
                        loader.setTimestamp(inputCommand.getTimestamp());
                    } else {
                        message = "Please load a source before attempting to forward.";
                        listOfLoaders.remove(loader);
                        break;
                    }
                }
            }
        }
        if (isPodcast == 0 && load == 1) {
            message = "The loaded source is not a podcast.";
        } else {
            if (load == 0) {
                message = "Please load a source before attempting to forward.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
