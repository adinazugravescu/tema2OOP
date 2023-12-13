package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import audioplayer.Database;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;


public final class DoSwitchConnectionStatus {
    private DoSwitchConnectionStatus() {
    }

    /**
     * implements the logic for switchConnectionStatus command
     * (checks online status of a user, and in case of being online pauses the player
     * and changes statistics)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final
                           ArrayList<Loaders> listOfLoaders) {
        Output.put(newN, "switchConnectionStatus", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0; //auxiliary field that retains if the user exists
        int isOnline = 0; //auxiliary field that retains if the user is Online
        if (database.getOnlineUsers() != null && !database.getOnlineUsers().isEmpty()) {
            for (UserInput user : database.getOnlineUsers()) {
                if (inputCommand.getUsername().equals(user.getUsername())) {
                    message = user.getUsername() + " has changed status successfully.";
                    database.getOnlineUsers().remove(user);
                    isOnline = 1;
                    exists = 1;
                    // pause the loader if user has something
                    for (Loaders loader : listOfLoaders) {
                        if (loader.getUsername().equals(user.getUsername())) {
                            if (loader.getSong() != null) {
                                if (!loader.getStats().paused) { // song is on, so we pause
                                    loader.getStats().setRemainedTime(loader.getStats().
                                            getRemainedTime() - inputCommand.getTimestamp()
                                            + loader.getTimestamp()); // update remained time
                                    if (loader.getStats().getRemainedTime() < 0) { //song finished
                                        loader.getStats().setRemainedTime(0);
                                        loader.getStats().setName("");
                                        loader.getStats().setPaused(true);
                                    }
                                }
                                loader.getStats().paused = !(loader.getStats().paused);
                                loader.setTimestamp(inputCommand.getTimestamp());
                            } else {
                                if (loader.getPodcast() != null) {
                                    if (!loader.getStats().paused) { // episode is on, so we pause
                                        loader.getStats().setRemainedTime(loader.getStats().
                                                getRemainedTime() - inputCommand.getTimestamp()
                                                + loader.getTimestamp()); //update remained time
                                        if (loader.getStats().getRemainedTime() <= 0) {
                                            // episode finished, look for next one
                                            while (loader.getStats().getRemainedTime() <= 0) {
                                                EpisodeInput nextEpisode = LoadNext.forPodcast(
                                                        loader.getPodcast(), loader.getStats().
                                                                getName());
                                                if (nextEpisode != null) {
                                                    loader.getStats().setName(nextEpisode.
                                                            getName());
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
                                                    SongInput nextSong = LoadNext.forPlaylist(
                                                            loader.getPlaylist(), loader.getStats().
                                                                    getName());
                                                    if (nextSong != null) {
                                                        loader.getStats().setName(nextSong.
                                                                getName());
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
                                        }
                                        loader.getStats().paused = !(loader.getStats().paused);
                                        loader.setTimestamp(inputCommand.getTimestamp());
                                    }
                                }
                            }
                        }
                        break;
                    }

                    break;
                }
            }
        }
        if (isOnline == 0) {
            for (UserInput user : database.getLibrary().getUsers()) {
                if (inputCommand.getUsername().equals(user.getUsername())) {
                    exists = 1;
                    message = user.getUsername() + " has changed status successfully.";
                    database.getOnlineUsers().add(user);
                    break;
                }
            }
        }
        if (exists == 0) {
            message = "The username " + inputCommand.getUsername() + " doesn't exist.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
