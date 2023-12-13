package audioplayer.commands.commandsOutput;

import audioplayer.Constants;
import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.LoadNext;
import audioplayer.commands.player.Loaders;
import audioplayer.commands.playlist.FollowedPlaylists;
import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.playlist.PlaylistOwners;
import audioplayer.commands.playlist.PreferredSongs;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoDeleteUser {
    private DoDeleteUser() {
    }

    /**
     * implements the logic for deleteUser command
     * (verifies if the subject of removal has interaction with other users
     * and if it does, delete all records of this user)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     * @param listOfLoaders list of users and what they have in load
     * @param preferredSongs list of owners and their liked songs
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database, final ArrayList<Loaders> listOfLoaders, final
    ArrayList<PreferredSongs> preferredSongs, final ArrayList<PlaylistOwners> playlistOwners,
                           final ArrayList<FollowedPlaylists> followedPlaylists) {
        Output.put(newN, "deleteUser", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        int exists = 0; // auxiliary field that retains if the current user exists
        String message = "";
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                int interaction = 0; // auxiliary field that retains if the current user
                                    // has an interaction in the app
                for (UserInput auxUser : database.getLibrary().getUsers()) {
                    // a user being on the subject's page means interaction
                    if (auxUser.getUsersPage().getUsername().equals(user.getUsername())
                        && !auxUser.getCurrentPage().equals("HomePage")
                        && !auxUser.getCurrentPage().equals("Home")) {
                        interaction = 1;
                    }
                }
                for (Loaders loader : listOfLoaders) { // check if someone has sth
                                                    // in load related to the user
                    if (loader.getSong() != null && loader.getSong().getArtist().
                                equals(inputCommand.getUsername())) {
                        // a song whose artist is the subject
                        int time = loader.getStats().getRemainedTime() - inputCommand.
                                getTimestamp() + loader.getTimestamp();
                        // check if it still loads
                        if (time > 0 || (time <= 0 && loader.getStats().getRepeat().equals(
                                "Repeat Once") && time > Constants.getLimit())) {
                                interaction = 1;
                                break;
                            }
                        } else {
                        if (loader.getAlbum() != null && loader.getAlbum().getOwner().
                                equals(inputCommand.getUsername())) {
                            // an album whose owner is the subject
                            int time = 0;
                            // check if it still loads
                            if (!loader.getStats().paused) { // find the current track
                                time = loader.getStats().getRemainedTime() - inputCommand.
                                        getTimestamp() + loader.getTimestamp(); //update timestamp
                                if (time <= 0) {
                                    // song finished, look for the next one
                                    String name = loader.getStats().getName();
                                    while (time <= 0) {
                                        SongInput nextSong = LoadNext.forAlbum(loader.
                                                getAlbum(), name);
                                        if (nextSong != null) {
                                            name = nextSong.getName();
                                            time = nextSong.getDuration() + time;
                                        } else {
                                            time = 0;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (time > 0) {
                                interaction = 1;
                                break;
                            }
                        } else {
                            if (loader.getPodcast() != null && loader.getPodcast().getOwner().
                                    equals(inputCommand.getUsername())) {
                                // a podcast whose owner is the subject
                                int time = 0;
                                // check if it still loads
                                if (!loader.getStats().paused) { // find the current episode
                                    time = loader.getStats().getRemainedTime() - inputCommand.
                                            getTimestamp() + loader.getTimestamp();
                                    if (time <= 0) {
                                        // episode finished, look for the next one
                                        String name = loader.getStats().getName();
                                        while (time <= 0) {
                                            EpisodeInput nextEpisode = LoadNext.forPodcast(loader.
                                                    getPodcast(), name);
                                            if (nextEpisode != null) {
                                                name = nextEpisode.getName();
                                                time = nextEpisode.getDuration() + time;
                                            } else {
                                                time = 0;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (time > 0) {
                                    interaction = 1;
                                    break;
                                }
                            } else {
                                if (loader.getPlaylist() != null && loader.getPlaylist().
                                        getOwner().equals(inputCommand.getUsername())) {
                                    // a playlist whose owner is the subject
                                    int time = 0;
                                    // check if it still loads
                                    if (!loader.getStats().paused) { // find the current track
                                        time = loader.getStats().getRemainedTime() - inputCommand.
                                                getTimestamp() + loader.getTimestamp();
                                        if (time <= 0) {
                                            // song finished, look for the next one
                                            String name = loader.getStats().getName();
                                            while (time <= 0) {
                                                SongInput nextSong = LoadNext.forPlaylist(loader.
                                                        getPlaylist(), name);
                                                if (nextSong != null) {
                                                    name = nextSong.getName();
                                                    time = nextSong.getDuration() + time;
                                                } else {
                                                    time = 0;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (time > 0) {
                                        interaction = 1;
                                        break;
                                    }
                                    if (!loader.getStats().getRepeat().equals("No Repeat")) {
                                        interaction = 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (interaction == 0) { // delete the user
                    database.getLibrary().getSongs().removeIf(song -> song.getArtist().equals(user.
                            getUsername())); // delete if there is song record related
                    for (PreferredSongs crt : preferredSongs) {
                        crt.getSongs().removeIf(song -> song.getArtist().equals(user.
                                getUsername())); // delete songs related from others' liked songs
                    }
                    // delete if user has playlists
                    playlistOwners.removeIf(item -> item.getOwner().equals(user.getUsername()));
                    // delete user followed playlists record
                    for (FollowedPlaylists follow : followedPlaylists) {
                        if (follow.getFollower().equals(user.getUsername())) {
                            for (Playlist playlist : follow.getPlaylists()) {
                                for (PlaylistOwners p2 : playlistOwners) {
                                    for (Playlist p3 : p2.getPlaylists()) {
                                        if (playlist.getName().equals(p3.getName())) {
                                            p3.setFollowers(p3.getFollowers() - 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    followedPlaylists.removeIf(item -> item.getFollower().
                            equals(user.getUsername()));
                    database.getLibrary().getUsers().remove(user); // remove user from library
                    database.getOnlineUsers().removeIf(u -> u.getUsername().equals(user.
                            getUsername())); // remove user from online data
                    message = inputCommand.getUsername() + " was successfully deleted.";
                } else {
                    message = inputCommand.getUsername() + " can't be deleted.";
                }
                break;
            }
        }
        if (exists == 0) {
            message = "The username " + inputCommand.getUsername() + "  doesn't exist.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
