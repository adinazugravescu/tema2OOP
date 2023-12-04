package audioplayer.commands.playlist;

import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for retaining the initial order of a user's playlist songs in songs
 */

public class UnshuffledSongs {
    @Getter @Setter
    private String owner;
    @Getter @Setter
    private ArrayList<SongInput> songs;
}
