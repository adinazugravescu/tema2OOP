package audioplayer.commands.playlist;

import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for liked songs info
 */
public class PreferredSongs {
    @Getter @Setter
    private String owner;
    @Getter @Setter
    private ArrayList<SongInput> songs = new ArrayList<>();
}
