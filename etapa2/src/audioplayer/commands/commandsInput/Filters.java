package audioplayer.commands.commandsInput;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class for filters input
 */
public class Filters {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String album;
    @Getter @Setter
    private ArrayList<String> tags;
    @Getter @Setter
    private String lyrics;
    @Getter @Setter
    private String genre;
    @Getter @Setter
    private String releaseYear;
    @Getter @Setter
    private String artist;
    @Getter @Setter
    private String owner;
}
