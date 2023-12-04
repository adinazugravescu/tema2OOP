package audioplayer.commands.commandsOutput;

import audioplayer.Constants;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Backward;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;

import java.util.ArrayList;

public final class DoBackward {
    private DoBackward() {

    }

    /**
     * implements the logic for backward command
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
        Output.put(newN, "backward", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int load = 0; // auxiliary field that retains if the current user has a podcast in load
        for (Loaders loader : listOfLoaders) {
            if (loader.getPodcast() != null && loader.getUsername().equals(inputCommand.
                    getUsername())) { // checks if the user has a podcast in load, otherwise
                                      // the command does not apply
                load = 1;
                int time = inputCommand.getTimestamp() - loader.getTimestamp();
                // calculates the remaining time at the current moment
                int remainingTime  = loader.getStats().getRemainedTime() - time;
                EpisodeInput episode = Backward.podcast(loader.getPodcast(),
                        loader.getStats().getName()); // returns the current episode
                // passed time of current episode
                int passedTime = episode.getDuration() - remainingTime;
                if (passedTime < Constants.getNinety()) {
                    loader.getStats().setRemainedTime(episode.getDuration()); // restarts from 0
                } else {
                    // it gets 90sec backwards, so the remaining time increases by 90
                    loader.getStats().setRemainedTime(remainingTime + Constants.getNinety());
                }
                message = "Rewound successfully.";
                loader.setTimestamp(inputCommand.getTimestamp());
            }
        }
        if (load == 0) {
            message = "The loaded source is not a podcast.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
