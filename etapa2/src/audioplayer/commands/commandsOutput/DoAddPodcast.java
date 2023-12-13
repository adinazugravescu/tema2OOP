package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.userData.Verification;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.PodcastInput;
import fileio.input.UserInput;

public final class DoAddPodcast {
    private DoAddPodcast() {
    }

    /**
     * implements the logic for addPodcast command
     * (goes through the list of users(hosts) and builds the logic for the 5 conditions)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "addPodcast", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; // auxiliary field that retains if specified user exists
        int isHost = 0; // auxiliary field that retains if specified user is a host
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                if (user.getType().equals("host")) {
                    isHost = 1;
                    for (PodcastInput podcast : user.getPodcasts()) {
                        if (podcast.getName().equals(inputCommand.getName())) {
                            message = inputCommand.getUsername() + " has another podcast with "
                                    + "the same name.";
                            newN.put("message", message);
                            outputs.add(newN);
                            return;
                        }
                    }
                    // check for more than 2 episodes in inputCommand
                    if (Verification.podcast(inputCommand.getEpisodes())) {
                        message = inputCommand.getUsername() + " has the same episode in"
                                + "this podcast.";
                    } else {
                        // add the podcast
                        // first create it
                        PodcastInput newPodcast = new PodcastInput(inputCommand.getName(),
                                inputCommand.getUsername(), inputCommand.getEpisodes());
                        user.getPodcasts().add(newPodcast);
                        message = inputCommand.getUsername()
                                + " has added new podcast successfully.";
                        database.getLibrary().getPodcasts().add(newPodcast);
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
