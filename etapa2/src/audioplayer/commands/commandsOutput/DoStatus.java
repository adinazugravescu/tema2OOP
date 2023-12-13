package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.connection.Check;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoStatus {
    private DoStatus() {

    }

    /**
     * implements the logic for status command
     * (updates the load history of current user based on what type
     * of file is in load, what remaining time and what repeat mode are set)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfLoaders list of users and what they have in load
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<Loaders> listOfLoaders, final Database database) {
        Output.put(newN, "status", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        int load = 0; // auxiliary field that retains if the current user has something in load
        ObjectNode newS = objectMapper.createObjectNode(); // for stats info
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())) {
                load = 1;
                if (loader.getSong() != null) {
                    if (!loader.getStats().paused) { // loader was on
                        loader.getStats().setRemainedTime(loader.getStats().
                                getRemainedTime() - inputCommand.getTimestamp()
                                + loader.getTimestamp());
                        if (loader.getStats().getRemainedTime() <= 0) {
                            if (loader.getStats().getRepeat().equals("No Repeat")) {
                                loader.getStats().setRemainedTime(0);
                                loader.getStats().setName("");
                                loader.getStats().paused = true;
                            } else {
                                if (loader.getStats().getRepeat().equals("Repeat Once")) {
                                    loader.getStats().setRemainedTime(loader.getSong().
                                            getDuration() + loader.getStats().getRemainedTime());
                                    loader.getStats().setRepeat("No Repeat");
                                } else {
                                    if (loader.getStats().getRepeat().equals("Repeat Infinite")) {
                                        // current song stays in load and calculate remained time
                                        loader.getStats().setRemainedTime((loader.getStats().
                                                getRemainedTime() % loader.getSong().
                                                getDuration()));
                                        loader.getStats().setRemainedTime(loader.getSong().
                                                getDuration() + loader.getStats().
                                                getRemainedTime());
                                    }
                                }
                            }
                        }
                        loader.setTimestamp(inputCommand.getTimestamp());
                    }
                    if (Check.ifOnline(inputCommand, database) == 0) {
                        loader.getStats().paused = !(loader.getStats().paused);
                    }
                    Output.status(newS, loader.getStats().getName(), loader.getStats().
                                    getRemainedTime(), loader.getStats().getRepeat(),
                            loader.getStats().shuffle, loader.getStats().paused);
                    newN.set("stats", newS);
                } else {
                    if (loader.getPodcast() != null) {
                        if (!loader.getStats().paused) {
                            loader.getStats().setRemainedTime(loader.getStats().
                                    getRemainedTime() - inputCommand.getTimestamp()
                                    + loader.getTimestamp());
                            if (loader.getStats().getRemainedTime() <= 0) {
                                while (loader.getStats().getRemainedTime() <= 0) {
                                    // find the next episode until remained time > 0
                                    EpisodeInput nextEpisode = LoadNext.forPodcast(loader.
                                            getPodcast(), loader.getStats().getName());
                                    if (nextEpisode != null) {
                                        loader.getStats().setName(nextEpisode.getName());
                                        loader.getStats().setRemainedTime(nextEpisode.
                                                getDuration() + loader.getStats().
                                                getRemainedTime());
                                    } else {
                                        loader.getStats().setRemainedTime(0);
                                        loader.getStats().setName("");
                                        loader.getStats().paused = true;
                                        break;
                                    }
                                }
                            }
                            loader.setTimestamp(inputCommand.getTimestamp());
                        }
                        if (Check.ifOnline(inputCommand, database) == 0) {
                            loader.getStats().paused = !(loader.getStats().paused);
                        }
                        Output.status(newS, loader.getStats().getName(), loader.
                                        getStats().getRemainedTime(), loader.getStats().
                                        getRepeat(), loader.getStats().shuffle,
                                loader.getStats().paused);
                        newN.set("stats", newS);
                    } else {
                        if (loader.getPlaylist() != null) {
                            if (!loader.getStats().paused) {
                                loader.getStats().setRemainedTime(loader.getStats().
                                        getRemainedTime() - inputCommand.getTimestamp()
                                        + loader.getTimestamp());
                                if (loader.getStats().getRemainedTime() <= 0) {
                                    if (loader.getStats().getRepeat().equals("No Repeat")) {
                                        while (loader.getStats().getRemainedTime() <= 0) {
                                            // find the next song until remained time > 0
                                            SongInput nextSong = LoadNext.forPlaylist(loader.
                                                    getPlaylist(), loader.getStats().getName());
                                            if (nextSong != null) {
                                                loader.getStats().setName(nextSong.getName());
                                                loader.getStats().setRemainedTime(nextSong.
                                                        getDuration() + loader.getStats().
                                                        getRemainedTime());
                                            } else {
                                                loader.getStats().setRemainedTime(0);
                                                loader.getStats().setName("");
                                                loader.getStats().paused = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        if (loader.getStats().getRepeat().equals("Repeat All")) {
                                            while (loader.getStats().getRemainedTime() <= 0) {
                                                // find the next song until remained time > 0
                                                SongInput nextSong = LoadNext.forPlaylist(loader.
                                                        getPlaylist(), loader.getStats().getName());
                                                if (nextSong == null && !loader.getPlaylist().
                                                        getSongs().isEmpty()) {
                                                    // playlists starts from the beginning
                                                    nextSong = loader.getPlaylist().getSongs().
                                                            get(0);
                                                }
                                                    loader.getStats().setName(nextSong.getName());
                                                    loader.getStats().setRemainedTime(nextSong.
                                                            getDuration() + loader.getStats().
                                                            getRemainedTime());
                                            }
                                        } else {
                                            if (loader.getStats().getRepeat().
                                                    equals("Repeat Current Song")) {
                                                String current = loader.getStats().getName();
                                                SongInput currentSong = new SongInput();
                                                for (int i = 0; i < loader.getPlaylist().
                                                        getSongs().size(); i++) {
                                                    if (loader.getPlaylist().getSongs().get(i).
                                                            getName().equals(current)) {
                                                        currentSong = loader.getPlaylist().
                                                                getSongs().get(i);
                                                    }
                                                }
                                                // repeat current song
                                                if (currentSong.getDuration() != null) {
                                                    loader.getStats().setRemainedTime(loader.
                                                            getStats().getRemainedTime()
                                                            + currentSong.getDuration());
                                                    // find the next one if remained time < 0
                                                    while (loader.getStats().
                                                            getRemainedTime() <= 0) {
                                                        loader.getStats().setRemainedTime(
                                                                currentSong.getDuration()
                                                                        + loader.getStats().
                                                                        getRemainedTime());

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                loader.setTimestamp(inputCommand.getTimestamp());
                            }
                            if (Check.ifOnline(inputCommand, database) == 0) {
                                loader.getStats().paused = !(loader.getStats().paused);
                            }
                            Output.status(newS, loader.getStats().getName(), loader.
                                            getStats().getRemainedTime(), loader.
                                            getStats().getRepeat(), loader.getStats().shuffle,
                                    loader.getStats().paused);
                            newN.set("stats", newS);
                        } else {
                            if (loader.getAlbum() != null) {
                                if (!loader.getStats().paused) {
                                    loader.getStats().setRemainedTime(loader.getStats().
                                            getRemainedTime() - inputCommand.getTimestamp()
                                            + loader.getTimestamp());
                                    if (loader.getStats().getRemainedTime() <= 0) {
                                        if (loader.getStats().getRepeat().equals("No Repeat")) {
                                            while (loader.getStats().getRemainedTime() <= 0) {
                                                // find the next song until remained time > 0
                                                SongInput nextSong = LoadNext.forAlbum(loader.
                                                        getAlbum(), loader.getStats().getName());
                                                if (nextSong != null) {
                                                    loader.getStats().setName(nextSong.getName());
                                                    loader.getStats().setRemainedTime(nextSong.
                                                            getDuration() + loader.getStats().
                                                            getRemainedTime());
                                                } else {
                                                    loader.getStats().setRemainedTime(0);
                                                    loader.getStats().setName("");
                                                    loader.getStats().paused = true;
                                                    break;
                                                }
                                            }
                                        } else {
                                            if (loader.getStats().getRepeat().equals(
                                                    "Repeat All")) {
                                                while (loader.getStats().getRemainedTime() <= 0) {
                                                    // find the next song until remained time > 0
                                                    SongInput nextSong = LoadNext.forAlbum(loader.
                                                            getAlbum(), loader.getStats().
                                                            getName());
                                                    if (nextSong == null && !loader.getAlbum().
                                                            getSongs().isEmpty()) {
                                                        // album starts from the beginning
                                                        nextSong = loader.getAlbum().getSongs().
                                                                get(0);
                                                    }
                                                    loader.getStats().setName(nextSong.getName());
                                                    loader.getStats().setRemainedTime(nextSong.
                                                            getDuration() + loader.getStats().
                                                            getRemainedTime());
                                                }
                                            } else {
                                                if (loader.getStats().getRepeat().
                                                        equals("Repeat Current Song")) {
                                                    String current = loader.getStats().getName();
                                                    SongInput currentSong = new SongInput();
                                                    for (int i = 0; i < loader.getAlbum().
                                                            getSongs().size(); i++) {
                                                        if (loader.getAlbum().getSongs().get(i).
                                                                getName().equals(current)) {
                                                            currentSong = loader.getAlbum().
                                                                    getSongs().get(i);
                                                        }
                                                    }
                                                    // repeat current song
                                                    if (currentSong.getDuration() != null) {
                                                        loader.getStats().setRemainedTime(loader.
                                                                getStats().getRemainedTime()
                                                                + currentSong.getDuration());
                                                        // find the next one if remained time < 0
                                                        while (loader.getStats().getRemainedTime()
                                                                <= 0) {
                                                            loader.getStats().setRemainedTime(
                                                                    currentSong.getDuration()
                                                                            + loader.getStats().
                                                                            getRemainedTime());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    loader.setTimestamp(inputCommand.getTimestamp());
                                }
                                if (Check.ifOnline(inputCommand, database) == 0) {
                                    loader.getStats().paused = !(loader.getStats().paused);
                                }
                                Output.status(newS, loader.getStats().getName(), loader.
                                                getStats().getRemainedTime(), loader.
                                                getStats().getRepeat(), loader.getStats().shuffle,
                                        loader.getStats().paused);
                                newN.set("stats", newS);
                            }
                        }
                    }
                }
                break;
            }
        }
        if (load == 0) {
            Output.status(newS, "", 0, "No Repeat", false, true);
            newN.set("stats", newS);
        }
        outputs.add(newN);
    }
}
