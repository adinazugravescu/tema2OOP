package audioplayer.commands.userData;

import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for album entity
 */
public class Album {
    @Getter @Setter
    private ArrayList<SongInput> songs = new ArrayList<SongInput>();
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int releaseYear;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String owner;
    public Album() {
    }
    public Album(final String n, final int release, final String d,
                 final ArrayList<SongInput> s, final String o) {
        this.name = n;
        this.releaseYear = release;
        this.description = d;
        this.songs = s;
        this.owner = o;
    }
}
