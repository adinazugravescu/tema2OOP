package audioplayer.commands.commandsInput;

import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


/**
 * class for input fields
 */

public class CommandsInput {
    @Getter @Setter
    private String command;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private int timestamp;

    @Getter @Setter
    private String type;

    @Getter @Setter
    private Filters filters;

    @Getter @Setter
    private int itemNumber;

    @Getter @Setter
    private String playlistName;

    @Getter @Setter
    private int playlistId;

    @Getter @Setter
    private int seed;
    @Getter @Setter
    private String nextPage;
    @Getter @Setter
    private int age;
    @Getter @Setter
    private String city;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private ArrayList<SongInput> songs;
    @Getter @Setter
    private ArrayList<EpisodeInput> episodes;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String date;
    @Getter @Setter
    private int price;
    @Getter @Setter
    private int releaseYear;
}
