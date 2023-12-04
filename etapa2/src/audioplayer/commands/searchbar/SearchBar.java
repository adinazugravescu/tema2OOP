package audioplayer.commands.searchbar;
import audioplayer.commands.commandsInput.Filters;
import audioplayer.commands.playlist.PlaylistOwners;
import fileio.input.LibraryInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * superclass to implement search commands depending on the file's type
 */
public class SearchBar {
    @Getter @Setter
    private Filters filters; // based on which we do the search
    @Getter @Setter
    private LibraryInput input; // the database for songs and podcasts
    @Getter @Setter
    private ArrayList<PlaylistOwners> playlistOwners; // database for playlists
    @Getter @Setter
    private int numberOfOc; // number of results after search
    public SearchBar() {

    }

    /**
     * function implemented in the subclasses
     */
    public void search() {

    }

    /**
     * @return message after the search
     */
    public String message() {
        return "Search returned " + numberOfOc + " results";
    }
}

