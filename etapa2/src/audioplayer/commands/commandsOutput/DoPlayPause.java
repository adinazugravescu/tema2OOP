package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.player.PlayPause;
import audioplayer.commands.player.LoadNext;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoPlayPause {
    private DoPlayPause() {

    }

    /**
     * implements the logic for playPause command
     * (depending on what type of audio a user has in load,
     * it updates its statistics based on the current timestamp)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
                            ArrayNode outputs, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "playPause", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        int loaded = 0; // auxiliary field that retains if the current user has something in load
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                loaded = 1;
                if (loader.getSong() != null) {
                    if (!loader.getStats().paused) { // song is on, so we pause
                        newN.put("message", PlayPause.message(0));
                        loader.getStats().setRemainedTime(loader.getStats().
                                getRemainedTime() - inputCommand.getTimestamp()
                                + loader.getTimestamp()); // update remained time
                        if (loader.getStats().getRemainedTime() < 0) { // then song finished
                            loader.getStats().setRemainedTime(0);
                            loader.getStats().setName("");
                            loader.getStats().setPaused(true);
                        }
                    } else {
                        newN.put("message", PlayPause.message(1));
                    }
                    loader.getStats().paused = !(loader.getStats().paused);
                    loader.setTimestamp(inputCommand.getTimestamp());
                } else {
                    if (loader.getPodcast() != null) {
                        if (!loader.getStats().paused) { // episode is on, so we pause
                            newN.put("message", PlayPause.message(0));
                            loader.getStats().setRemainedTime(loader.getStats().
                                    getRemainedTime() - inputCommand.getTimestamp()
                                    + loader.getTimestamp()); //update remained time
                            if (loader.getStats().getRemainedTime() <= 0) {
                                // episode finished, look for next one
                                while (loader.getStats().getRemainedTime() <= 0) {
                                EpisodeInput nextEpisode = LoadNext.forPodcast(loader.
                                        getPodcast(), loader.getStats().getName());
                                if (nextEpisode != null) {
                                    loader.getStats().setName(nextEpisode.getName());
                                    loader.getStats().setRemainedTime(nextEpisode.
                                            getDuration() + loader.getStats().
                                            getRemainedTime());
                                } else {
                                    // podcast finished
                                    loader.getStats().setRemainedTime(0);
                                    loader.getStats().setName("");
                                    loader.getStats().paused = true;
                                    break;
                                }
                                }
                            }
                        } else {
                            newN.put("message", PlayPause.message(1));
                        }
                        loader.getStats().paused = !(loader.getStats().paused);
                        loader.setTimestamp(inputCommand.getTimestamp());
                    } else {
                        if (loader.getPlaylist() != null) {
                            if (!loader.getStats().paused) { // song is on, so we pause
                                loader.getStats().setRemainedTime(loader.getStats().
                                        getRemainedTime() - inputCommand.getTimestamp()
                                        + loader.getTimestamp()); //update timestamp
                                if (loader.getStats().getRemainedTime() <= 0) {
                                    // song finished, look for the next one
                                    while (loader.getStats().getRemainedTime() <= 0) {
                                    SongInput nextSong = LoadNext.forPlaylist(loader.
                                            getPlaylist(), loader.getStats().getName());
                                    if (nextSong != null) {
                                        loader.getStats().setName(nextSong.getName());
                                        loader.getStats().setRemainedTime(nextSong.
                                                getDuration() + loader.getStats().
                                                getRemainedTime());
                                    } else {
                                        // playlist finished
                                        loader.getStats().setRemainedTime(0);
                                        loader.getStats().setName("");
                                        loader.getStats().paused = true;
                                        break;
                                    }
                                }
                                }
                            } else {
                                newN.put("message", PlayPause.message(1));
                            }
                            loader.getStats().paused = !(loader.getStats().paused);
                            loader.setTimestamp(inputCommand.getTimestamp());
                        }
                    }
                }
            }
            break;
        }
        if (loaded == 0) {
            newN.put("message", PlayPause.noLoad());
        }
        outputs.add(newN);
    }
}
