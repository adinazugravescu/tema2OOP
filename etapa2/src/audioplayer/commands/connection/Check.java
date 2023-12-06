package audioplayer.commands.connection;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import fileio.input.UserInput;

public final class Check {
    private Check() {
    }

    /**
     *
     * @param inputCommand
     * @param database
     * @return
     */
    public static int ifOnline(final CommandsInput inputCommand, final Database database) {
        if (database.getOnlineUsers() != null) {
            for (UserInput user : database.getOnlineUsers()) {
                if (user.getUsername().equals(inputCommand.getUsername())) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
