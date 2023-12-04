package audioplayer;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.commandsOutput.*;
import audioplayer.commands.player.*;
import audioplayer.commands.playlist.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;

public class Implement {
    private final LibraryInput library;
    private final List<CommandsInput> input;
    private static ArrayNode outputs;

    /**
     * public constructor that initializes with the :
     * @param library the library that contains all the information about songs, podcasts and users
     * @param input the input list of commands read from the json files
     * @param output the output ArrayNode that is completed during the execution of exe() method
     */

    public Implement(final LibraryInput library, final List<CommandsInput> input,
                     ArrayNode output) {
        this.library = library;
        this.outputs = output;
        this.input = input;
    }

    /**
     * this method uses a couple of ArrayList objects that act as a minimal database;
     * based on the list of commands, it uses a switch with cases that refer to the
     * type of command given at the current node, and call the static methods implemented
     * in the 'Do' classes for each command to process the data, build the logic for
     * the 'ouputs' and for the ArrayList objects that are mandatory for further
     * commands implementation
     */
    public final void exe() {
        ObjectMapper objectMapper = new ObjectMapper();
        // list for last search result for every user, that stores the results type and the number
        ArrayList<UserSearchResult> listOfResults = new ArrayList<UserSearchResult>();
        // list for last select result for every user, that stores the result type
        ArrayList<UserSelectResult> listOfSelectors = new ArrayList<UserSelectResult>();
        // list for what each user has in load
        ArrayList<Loaders> listOfLoaders = new ArrayList<Loaders>();
        // list that stores the user and the owned playlists
        ArrayList<PlaylistOwners> playlistOwners = new ArrayList<PlaylistOwners>();
        // list that stores the user and the owned playlists in their initial order
        ArrayList<UnshuffledSongs> initialPlaylists = new ArrayList<UnshuffledSongs>();
        // list that stores the user and the liked songs
        ArrayList<PreferredSongs> prefferedSongs = new ArrayList<PreferredSongs>();
        // list that stores the user and the followed playlists
        ArrayList<FollowedPlaylists> followedPlaylists = new ArrayList<FollowedPlaylists>();
        // list that stores the loaded podcast stats in case the user reloads them
        ArrayList<PodcastStats> podcastStats = new ArrayList<PodcastStats>();
        for (CommandsInput inputCommand : input) {
            ObjectNode newN = objectMapper.createObjectNode();
            switch (inputCommand.getCommand()) {
                case "search" -> {
                    // remove
                    // when a user command is 'search', we do not need to keep track of
                    // its search results, what was selected/loaded, but(if a podcast was loaded)
                    // we need to remember the statistics for it in case of a future load
                    // (in podcastStats)
                    DoSearch.remove(inputCommand, listOfResults, listOfLoaders, listOfSelectors,
                            podcastStats);
                    DoSearch.exe(newN, inputCommand, library, outputs, listOfResults,
                            playlistOwners);
                }
                case "select" -> {
                    DoSelect.exe(newN, inputCommand, outputs, listOfResults, listOfSelectors);
                }
                case "load" -> {
                    DoLoad.exe(newN, inputCommand, outputs, listOfSelectors, listOfLoaders,
                            podcastStats);
                }
                case "playPause" -> {
                    DoPlayPause.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case "status" -> {
                    DoStatus.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case "createPlaylist" -> {
                    DoCreatePlaylist.exe(newN, inputCommand, outputs, playlistOwners);
                }
                case "addRemoveInPlaylist" -> {
                    DoAddRemove.exe(newN, inputCommand, outputs, listOfLoaders, playlistOwners);
                }
                case "like" -> {
                    DoLike.exe(newN, inputCommand, outputs, listOfLoaders, prefferedSongs);
                }
                case "showPreferredSongs" -> {
                    DoShowPreferredSongs.exe(newN, inputCommand, outputs, prefferedSongs);
                }
                case "showPlaylists" -> {
                    DoShowPlaylists.exe(newN, inputCommand, outputs, playlistOwners);
                }
                case ("switchVisibility") -> {
                    DoSwitchVisibility.exe(newN, inputCommand, outputs, playlistOwners);
                }
                case ("follow") -> {
                    DoFollow.exe(newN, inputCommand, outputs, listOfSelectors, followedPlaylists);
                }
                case ("getTop5Playlists") -> {
                    DoGetTop5Playlists.exe(newN, inputCommand, outputs, playlistOwners);
                }
                case ("getTop5Songs") -> {
                    DoGetTop5Songs.exe(newN, inputCommand, library, outputs, prefferedSongs);
                }
                case ("next") -> {
                    DoNext.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case ("prev") -> {
                    DoPrev.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case ("forward") -> {
                    DoForward.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case ("backward") -> {
                    DoBackward.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case ("repeat") -> {
                    DoRepeat.exe(newN, inputCommand, outputs, listOfLoaders);
                }
                case ("shuffle") -> {
                    DoShuffle.exe(newN, inputCommand, outputs, listOfLoaders, initialPlaylists);
                }
                default -> {
                    continue;
                }
            }
        }
    }
}