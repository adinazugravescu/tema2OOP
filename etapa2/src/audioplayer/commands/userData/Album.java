package audioplayer.commands.userData;

import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Album {
    @Getter @Setter
    private ArrayList<SongInput> songs = new ArrayList<SongInput>();
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int releaseYear;
    @Getter @Setter
    private String description;
    public Album(final String n, final int release, final String d,
                 final ArrayList<SongInput> s) {
        this.name = n;
        this.releaseYear = release;
        this.description = d;
        this.songs = s;
    }
}
