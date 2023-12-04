package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoLoad {
    private DoLoad() {

    }

    /**
     * implements the logic for load command
     * (updates the listOfLoaders with the current user
     * and what was selected in listOfSelectors and initializes
     * the statistics for it)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfSelectors list of users and their select choices
     * @param listOfLoaders list of users and what they have in load
     * @param podcastStats list of last load statistics for podcasts
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<UserSelectResult> listOfSelectors, final ArrayList<Loaders>
            listOfLoaders, final ArrayList<PodcastStats> podcastStats) {
        Output.put(newN, "load", inputCommand.getUsername(), inputCommand.getTimestamp());
        int loaded = 0; // auxiliary field that retains if the current user has something in load
        for (UserSelectResult select : listOfSelectors) {
            if (select.getUsername().equals(inputCommand.getUsername())) {
                loaded = 1;
                if (select.getSong() != null && select.getSong().getName() != null) {
                    StatsForStatus stats = new StatsForStatus(); // initial statistics
                    stats.setName(select.getSong().getName());
                    if (select.getSong().getDuration() != null) {
                        stats.setRemainedTime(select.getSong().getDuration());
                    }
                    stats.setRepeat("No Repeat");
                    stats.setShuffle(false);
                    stats.setPaused(false);
                    Loaders loader = new Loaders(inputCommand.getUsername(),
                            inputCommand.getTimestamp());
                    loader.setSong(select.getSong());
                    loader.setStats(stats);
                    for (Loaders l : listOfLoaders) { // removes if user exists in listOfLoaders
                        if (l.getUsername().equals(inputCommand.getUsername())) {
                            listOfLoaders.remove(l);
                        }
                    }
                    listOfLoaders.add(loader);
                    newN.put("message", Load.message1());
                    listOfSelectors.remove(select);
                } else {
                    if (select.getPodcast() != null && select.getPodcast().getName()
                            != null) {
                        StatsForStatus stats = new StatsForStatus(); // initial statistics
                        if (select.getPodcast().getEpisodes() != null) {
                            stats.setRemainedTime(select.getPodcast().getEpisodes().
                                    get(0).getDuration());
                            stats.setName(select.getPodcast().getEpisodes().get(0).
                                    getName());
                        }
                        stats.setRepeat("No Repeat");
                        stats.setShuffle(false);
                        stats.setPaused(false);
                        Loaders loader = new Loaders(inputCommand.getUsername(),
                                inputCommand.getTimestamp());
                        loader.setPodcast(select.getPodcast());
                        loader.setStats(stats);
                        for (PodcastStats iter : podcastStats) { // if user loaded previously
                                                                // this podcast, get stats
                            if (iter.getUsername().equals(inputCommand.getUsername())
                                    && iter.getPodcast().getName().equals(select.
                                    getPodcast().getName())) {
                                loader.setStats(iter.getStats());
                            }
                        }
                        for (Loaders l : listOfLoaders) { // removes if user exists in listOfLoaders
                            if (l.getUsername().equals(inputCommand.getUsername())) {
                                listOfLoaders.remove(l);
                            }
                        }
                        listOfLoaders.add(loader);
                        newN.put("message", Load.message1());
                        listOfSelectors.remove(select);

                    } else {
                        if (select.getPlaylist() != null && select.getPlaylist().
                                getName() != null) {
                            StatsForStatus stats = new StatsForStatus(); // initial statistics
                            if (select.getPlaylist().getSongs().get(0).
                                    getDuration() != null) {
                                stats.setRemainedTime(select.getPlaylist().getSongs().
                                        get(0).getDuration());
                                stats.setName(select.getPlaylist().getSongs().
                                        get(0).getName());
                            }
                            stats.setRepeat("No Repeat");
                            stats.setShuffle(false);
                            stats.setPaused(false);
                            Loaders loader = new Loaders(inputCommand.getUsername(),
                                    inputCommand.getTimestamp());
                            loader.setPlaylist(select.getPlaylist());
                            loader.setStats(stats);
                            for (Loaders l : listOfLoaders) { // removes if it exists already
                                if (l.getUsername().equals(inputCommand.
                                        getUsername())) {
                                    listOfLoaders.remove(l);
                                }
                            }
                            listOfLoaders.add(loader);
                            newN.put("message", Load.message1());
                            listOfSelectors.remove(select);
                        }
                    }
                }
                break;
            }
        }
        //}
        if (loaded == 0) {
            newN.put("message", Load.message2());
        }
        outputs.add(newN);
    }
}
