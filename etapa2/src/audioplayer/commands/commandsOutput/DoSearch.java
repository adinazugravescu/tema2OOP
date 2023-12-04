package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.player.PodcastStats;
import audioplayer.commands.player.UserSearchResult;
import audioplayer.commands.player.UserSelectResult;
import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.playlist.PlaylistOwners;
import audioplayer.commands.searchbar.SearchMelody;
import audioplayer.commands.searchbar.SearchPlaylist;
import audioplayer.commands.searchbar.SearchPodcast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class DoSearch {
    private DoSearch() {

    }

    /**
     * implements the logic for search command
     * (creates a searchbar extended object depending on what type
     * of file is searched, calls its search method that calculates
     * the number of and the results for output and updates the listOfResults)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param library used in case there are not enough liked songs,
     *                and a search is made among library songs
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfResults list for last search result for every user
     * @param playlistOwners list that stores the user and the owned playlists
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand,
                           final LibraryInput library, final ArrayNode outputs, final
                           ArrayList<UserSearchResult> listOfResults, final
                           ArrayList<PlaylistOwners> playlistOwners) {
        Output.put(newN, "search", inputCommand.getUsername(), inputCommand.getTimestamp());
        ObjectMapper objectMapper = new ObjectMapper();
        if (inputCommand.getType().equals("song")) {
            SearchMelody searchbar = new SearchMelody(inputCommand.getFilters(),
                    library);
            searchbar.search();
            ArrayNode resultsArray = objectMapper.createArrayNode(); // for songs name for output
            for (SongInput result : searchbar.getResults()) {
                resultsArray.add(result.getName());
            }
            Output.search(newN, searchbar.message(), resultsArray); // complete the ObjectNode with
                                                                    // the info in searchbar
            outputs.add(newN);
            UserSearchResult current = new UserSearchResult(
                    inputCommand.getUsername(),
                    searchbar.getNumberOfOc());
            current.setListOfsongs(searchbar.getResults());
            listOfResults.add(current);
        } else if (inputCommand.getType().equals("podcast")) {
            SearchPodcast searchbar = new SearchPodcast(inputCommand.getFilters(),
                    library);
            searchbar.search();
            ArrayNode resultsArray = objectMapper.createArrayNode(); // for podcasts name for output
            for (PodcastInput results : searchbar.getResults()) {
                resultsArray.add(results.getName());
            }
            Output.search(newN, searchbar.message(), resultsArray); // complete the ObjectNode with
                                                                    // the info in searchbar
            outputs.add(newN);
            UserSearchResult current = new UserSearchResult(
                    inputCommand.getUsername(), searchbar.getNumberOfOc());
            current.setListOfpodcasts(searchbar.getResults());
            listOfResults.add(current);
        } else if (inputCommand.getType().equals("playlist")) {
            SearchPlaylist searchbar = new SearchPlaylist(inputCommand.getFilters(),
                    playlistOwners, inputCommand.getUsername()); //
            searchbar.search();
            ArrayNode resultsArray = objectMapper.createArrayNode(); //playlists names for output
            for (Playlist results : searchbar.getResults()) {
                resultsArray.add(results.getName());
            }
            Output.search(newN, searchbar.message(), resultsArray); // complete the ObjectNode with
                                                                    // the info in searchbar
            outputs.add(newN);
            UserSearchResult current = new UserSearchResult(
                    inputCommand.getUsername(), searchbar.getNumberOfOc());
            current.setListOfplaylists(searchbar.getResults());
            listOfResults.add(current);
        }
    }

    /**
     * method called when a user commands search to erase the historic from
     * listOfResults, listOfLoaders, listOfSelectors
     * @param inputCommand used for current user info
     * @param listOfResults search history
     * @param listOfLoaders load history
     * @param listOfSelectors select history
     * @param podcastStats list updated in case a user has a podcast in load and
     *                     it is needed to retain the current file statistics in case
     *                     of next load
     */
    public static void remove(final CommandsInput inputCommand, final ArrayList<UserSearchResult>
            listOfResults, final ArrayList<Loaders> listOfLoaders, final
            ArrayList<UserSelectResult> listOfSelectors, final ArrayList<PodcastStats>
            podcastStats) {
        listOfResults.removeIf(result -> (result.getUsername().equals(inputCommand.
                getUsername())));
        for (Loaders loader : listOfLoaders) {
            if (loader.getUsername().equals(inputCommand.getUsername())
                    && loader.getPodcast() != null) {
                int time = inputCommand.getTimestamp() - loader.getTimestamp();
                PodcastStats loadedPodcast = new PodcastStats(inputCommand.
                        getUsername(), loader.getPodcast(), loader.getStats(), time);
                podcastStats.add(loadedPodcast);
            }
        }
        listOfLoaders.removeIf(loader -> (loader.getUsername().equals(inputCommand.
                getUsername())));
        listOfSelectors.removeIf(selector -> (selector.getUsername().
                equals(inputCommand.getUsername())));
    }
}
