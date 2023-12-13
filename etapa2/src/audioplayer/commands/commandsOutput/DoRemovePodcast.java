package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoRemovePodcast {
    private DoRemovePodcast() {
    }

    /**
     * implements the logic for removePodcast command
     * (verifies if the current podcast exists and if it does,
     * it checks for interaction in other user's load routine then removes it if it doesn't exist)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "removePodcast", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; // auxiliary field that retains if specified user exists
        int isHost = 0; // auxiliary field that retains if specified user is a host
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("host")) {
                    isHost = 1;
                    int hasIt = 0;
                    for (PodcastInput podcast : user.getPodcasts()) {
                        if (podcast.getName().equals(inputCommand.getName())) {
                            hasIt = 1;
                            break;
                        }
                    }
                    if (hasIt == 0) {
                        message = inputCommand.getUsername() + " doesn't have a podcast "
                                + "with the given name.";
                        newN.put("message", message);
                        outputs.add(newN);
                        return;
                    }
                    // verify if it can be deleted
                    int interaction = 0;
                    for (Loaders loader : listOfLoaders) {
                        if (loader.getPodcast() != null && loader.getPodcast().getOwner().
                                equals(inputCommand.getUsername())) {
                            int time = 0;
                            if (!loader.getStats().paused) { // find the ep
                                time = loader.getStats().getRemainedTime() - inputCommand.
                                        getTimestamp() + loader.getTimestamp(); //update timestamp
                                if (time <= 0) {
                                    // episode finished, look for the next one
                                    while (time <= 0) {
                                        String name = loader.getStats().getName();
                                        EpisodeInput nextEpisode = LoadNext.forPodcast(loader.
                                                getPodcast(), name);
                                        if (nextEpisode != null) {
                                            name = nextEpisode.getName();
                                            time = nextEpisode.getDuration() + time;
                                        } else {
                                            time = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (time > 0) {
                                interaction = 1;
                                break;
                            }
                        }
                    }
                    if (interaction == 1) {
                        message = inputCommand.getUsername() + " can't delete this podcast.";
                    } else {
                        // delete podcast
                        user.getPodcasts().removeIf(podcast -> podcast.getName().
                                equals(inputCommand.getName()));
                        database.getLibrary().getPodcasts().removeIf(podcast -> podcast.getName().
                                equals(inputCommand.getName()));
                        message = inputCommand.getUsername() + " deleted the podcast successfully.";
                    }
                    break;
                }
            }
        }
        if (exists == 0) {
            message = "The username " + inputCommand.getUsername() + " doesn't exist.";
        } else {
            if (isHost == 0) {
                message = inputCommand.getUsername() + " is not a host.";
            }
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
