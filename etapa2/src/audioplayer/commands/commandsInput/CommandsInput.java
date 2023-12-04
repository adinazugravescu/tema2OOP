package audioplayer.commands.commandsInput;

import lombok.Getter;
import lombok.Setter;


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
}
