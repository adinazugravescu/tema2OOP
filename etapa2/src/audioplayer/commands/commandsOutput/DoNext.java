package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoNext {
    private DoNext() {

    }

    /**
     * implements the logic for next command
     * (updates the listOfLoaders for current user
     * based on what is in load - for a song there is no next,
     * but for podcast/playlist it searches the next possible choice)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "next", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        int load = 0; // auxiliary field that retains if the current user has something in load
        String message = "";
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals((inputCommand.getUsername()))) {
                load = 1;
                if (loader.getSong() != null) { // if loader has a song, there is no next
                    if (loader.getStats().getRepeat().equals("No Repeat")) {
                        message = "Please load a source before skipping to the "
                                + "next track.";
                        listOfLoaders.remove(loader);
                        break;
                    }
                } else {
                    if (loader.getPlaylist() != null) {
                        SongInput nextSong = LoadNext.forPlaylist(loader.
                                getPlaylist(), loader.getStats().getName());
                        // next song will be the next in playlist or null in case current was last
                        if (nextSong != null) {
                            // update statistics if there is a next
                            loader.getStats().setName(nextSong.getName());
                            loader.getStats().setRemainedTime(nextSong.getDuration());
                            message = "Skipped to next track successfully. The "
                                    + "current track is " + nextSong.getName() + ".";
                            loader.setTimestamp(inputCommand.getTimestamp());
                        } else {
                            message = "Please load a source before skipping to the "
                                    + "next track.";
                            listOfLoaders.remove(loader);
                            break;
                        }
                    } else {
                        if (loader.getPodcast() != null) {
                            EpisodeInput nextEpisode = LoadNext.forPodcast(loader.
                                    getPodcast(), loader.getStats().getName());
                            // nextEpisode will be the next in podcast or null
                            if (nextEpisode != null) {
                                // update statistics if there is a next
                                loader.getStats().setName(nextEpisode.getName());
                                loader.getStats().setRemainedTime(nextEpisode.
                                        getDuration());
                                loader.setTimestamp(inputCommand.getTimestamp());
                                message = "Skipped to next track successfully. The "
                                        + "current track is " + nextEpisode.getName()
                                        + ".";
                            } else {
                                message = "Please load a source before skipping to the "
                                        + "next track.";
                                listOfLoaders.remove(loader);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (load == 0) {
            message = "Please load a source before skipping to the "
                    + "next track.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
