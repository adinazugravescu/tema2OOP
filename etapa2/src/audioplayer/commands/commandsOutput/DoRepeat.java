package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoRepeat {
    private DoRepeat() {

    }

    /**
     * implements the logic for repeat command
     * (updates the statistics for what the user has in load)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "repeat", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        int load = 0; // auxiliary field that retains if the current user has something in load
        String message = "";
        String mode = ""; // "message" field in output will be message + mode
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                if (loader.getStats().getRemainedTime() == 0) {
                    message = "Please load a source before setting the repeat status.";
                    newN.put("message", message);
                    outputs.add(newN);
                    return;
                }
                load = 1;
                message = "Repeat mode changed to "; // if user has something in load
                                                     // then we change the repeat mode
                if (loader.getSong() != null || loader.getPodcast() != null) {
                    if (loader.getStats().getRepeat().equals("No Repeat")) {
                        loader.getStats().setRepeat("Repeat Once");
                        mode = "repeat once.";
                    } else {
                        if (loader.getStats().getRepeat().equals("Repeat Once")) {
                            loader.getStats().setRepeat("Repeat Infinite");
                            mode = "repeat infinite.";
                        } else {
                            // the case of repeat infinite - no repeat
                            if (loader.getSong() != null) {
                                if (!loader.getStats().paused) {
                                    loader.getStats().setRemainedTime(loader.getStats().
                                            getRemainedTime() - inputCommand.getTimestamp()
                                            + loader.getTimestamp());
                                    // depending on how much time has passed, we search for
                                    // current song and its statistics and update the timestamp
                                    if (loader.getStats().getRemainedTime() <= 0) {
                                        loader.getStats().setRemainedTime((loader.getStats().
                                                getRemainedTime() % loader.getSong().
                                                getDuration()));
                                        loader.getStats().setRemainedTime(loader.
                                                getSong().getDuration() + loader.getStats().
                                                getRemainedTime());
                                    }
                                }
                            }
                            loader.setTimestamp(inputCommand.getTimestamp());
                            loader.getStats().setRepeat("No Repeat");
                            mode = "no repeat.";

                        }
                    }

                } else {
                    if (loader.getPlaylist() != null) {
                        if (loader.getStats().getRepeat().equals("No Repeat")) {
                            loader.getStats().setRepeat("Repeat All");
                            mode = "repeat all.";
                        } else {
                            if (loader.getStats().getRepeat().equals("Repeat All")) {
                                loader.getStats().setRepeat("Repeat Current Song");
                                mode = "repeat current song.";
                            } else {
                                loader.getStats().setRepeat("No Repeat");
                                mode = "no repeat.";
                            }
                        }
                    }
                }
            }

        }
        if (load == 0) {
            newN.put("message", "Please load a source before setting the repeat status.");
        } else {
            newN.put("message", message + mode);
        }
        outputs.add(newN);
    }
}
