package audioplayer.commands.commandsOutput;

import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.UserSelectResult;
import audioplayer.commands.playlist.FollowedPlaylists;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class DoFollow {
    private  DoFollow() {

    }

    /**
     * implements the logic for follow command
     * (based on the select history of the current user,
     * it updates the followedPlaylist list)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfSelectors list of users and their selected files
     * @param followedPlaylists list of users and what playlists have followed
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final ArrayList<UserSelectResult> listOfSelectors,
                           final ArrayList<FollowedPlaylists> followedPlaylists) {
        Output.put(newN, "follow", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int selectStatus = 0; // auxiliary field that retains if the current user has selected sth
        for (UserSelectResult select : listOfSelectors) {
            if (select.getUsername().equals(inputCommand.getUsername())) {
                selectStatus++;
                if (select.getPlaylist() != null && select.getPlaylist().getName()
                        != null) {
                    if (select.getPlaylist().getOwner().equals(inputCommand.
                            getUsername())) {
                        message = "You cannot follow or unfollow your own playlist.";
                    } else {
                        int UserHasFollowed = 0; // field that verifies that the user has info
                                                // in followedPlaylists
                        for (FollowedPlaylists iter : followedPlaylists) {
                            if (iter.getFollower().equals(inputCommand.
                                    getUsername())) {
                                UserHasFollowed++;
                                if (iter.getPlaylists().contains(select.
                                        getPlaylist())) {
                                    iter.getPlaylists().remove(select.
                                            getPlaylist());
                                    message = "Playlist unfollowed successfully.";
                                    select.getPlaylist().setFollowers(select.
                                            getPlaylist().getFollowers() - 1);
                                } else {
                                    iter.getPlaylists().add(select.getPlaylist());
                                    message = "Playlist followed successfully.";
                                    select.getPlaylist().setFollowers(select.
                                            getPlaylist().getFollowers() + 1);
                                }
                            }
                        }
                        if (UserHasFollowed == 0) { // if there is no info about user in followed
                                                    // Playlists create newFollow, add the current
                                                    //playlist to folllow
                            FollowedPlaylists newFollow = new FollowedPlaylists();
                            newFollow.setFollower(inputCommand.getUsername());
                            newFollow.getPlaylists().add(select.getPlaylist());
                            message = "Playlist followed successfully.";
                            followedPlaylists.add(newFollow);
                            select.getPlaylist().setFollowers(select.getPlaylist().
                                    getFollowers() + 1);
                        }
                    }
                } else {
                    message = "The selected source is not a playlist.";
                }
            }
        }
        if (selectStatus == 0) {
            message = "Please select a source before following or unfollowing.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
