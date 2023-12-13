package audioplayer.commands.connection;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import fileio.input.UserInput;

public final class Check {
    private Check() {
    }

    /**
     * checks the user that gave the command online status
     * @param inputCommand the current command
     * @param database database that provides updated library data and online users data
     * @return 1 in case of online entity and 0 instead
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
