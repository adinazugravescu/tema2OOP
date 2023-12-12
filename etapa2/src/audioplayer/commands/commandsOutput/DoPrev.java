package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.LoadPrev;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoPrev {
    private DoPrev() {

    }

    /**
     * implements the logic for prev command
     * (updates the listOfLoaders for current user
     * based on what is in listOfLoaders)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "prev", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        int load = 0; // auxiliary field that retains if the current user has something in load
        String message = "";
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals((inputCommand.getUsername()))) {
                load = 1;
                if (loader.getSong() != null) { // if a user has a song in load, it starts again
                    loader.setTimestamp(inputCommand.getTimestamp());
                    loader.getStats().setRemainedTime(loader.getSong().getDuration());
                    message = "Returned to previous track successfully. The current "
                            + "track is " + loader.getSong().getName() + ".";
                } else {
                    if (loader.getPlaylist() != null) {
                        int passedTime = inputCommand.getTimestamp() - loader.
                                getTimestamp();
                        // depending on how much time passed since the load, we find prevSong
                        SongInput prevSong = LoadPrev.forPlaylist(loader.getPlaylist(),
                                loader.getStats().getName(), passedTime);
                        if (prevSong != null) {
                            loader.getStats().setName(prevSong.getName());
                            loader.getStats().setRemainedTime(prevSong.getDuration());
                            message = "Returned to previous track successfully. The "
                                    + "current track is " + prevSong.getName() + ".";
                            loader.setTimestamp(inputCommand.getTimestamp());
                        }
                    } else {
                        if (loader.getPodcast() != null) {
                            int passedTime = inputCommand.getTimestamp() - loader.
                                    getTimestamp();
                            // depending on how much time passed since the load, we find prevEpisode
                            EpisodeInput prevEpisode = LoadPrev.forPodcast(loader.
                                            getPodcast(), loader.getStats().getName(),
                                    passedTime);
                            if (prevEpisode != null) {
                                loader.getStats().setName(prevEpisode.getName());
                                loader.getStats().setRemainedTime(prevEpisode.
                                        getDuration());
                                message = "Returned to previous track successfully. The "
                                        + "current track is " + prevEpisode.getName() + ".";
                                loader.setTimestamp(inputCommand.getTimestamp());
                            }
                        } else {
                            if (loader.getAlbum() != null) {
                                if (!loader.getStats().paused) { // find the current track
                                    loader.getStats().setRemainedTime(loader.getStats().
                                            getRemainedTime() - inputCommand.getTimestamp()
                                            + loader.getTimestamp()); //update timestamp
                                    if (loader.getStats().getRemainedTime() <= 0) {
                                        // song finished, look for the next one
                                        while (loader.getStats().getRemainedTime() <= 0) {
                                            SongInput nextSong = LoadNext.forAlbum(loader.
                                                    getAlbum(), loader.getStats().getName());
                                            if (nextSong != null) {
                                                loader.getStats().setName(nextSong.getName());
                                                loader.getStats().setRemainedTime(nextSong.
                                                        getDuration() + loader.getStats().
                                                        getRemainedTime());
                                            } else {
                                                // album finished
                                                loader.getStats().setRemainedTime(0);
                                                loader.getStats().setName("");
                                                loader.getStats().paused = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                // depending on time passed since the load, we find prevSong
                                int passedTime = inputCommand.getTimestamp() - loader.
                                        getTimestamp();
                                SongInput prevSong = LoadPrev.forAlbum(loader.getAlbum(),
                                        loader.getStats().getName(), passedTime);
                                if (prevSong != null) {
                                    loader.getStats().setName(prevSong.getName());
                                    loader.getStats().setRemainedTime(prevSong.getDuration());
                                    message = "Returned to previous track successfully. The "
                                            + "current track is " + prevSong.getName() + ".";
                                    loader.setTimestamp(inputCommand.getTimestamp());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (load == 0) {
            message = "Please load a source before returning to the previous track.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
