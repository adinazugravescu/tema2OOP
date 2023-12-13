package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

public final class DoAddUser {
    private DoAddUser() {

    }

    /**
     * implements the logic for addUser command
     * (verifies if that username exists in database and if it doesn't, creates one)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param database database that provides updated library data and online users data
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final Database database) {
        Output.put(newN, "addUser", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0;
        for (UserInput user : database.getLibrary().getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                message = "The username " + inputCommand.getUsername() + " is already taken.";
                break;
            }
        }
        if (exists == 0) { // create new user, initialize the instance with input data
            UserInput newUser = new UserInput();
            newUser.setType(inputCommand.getType());
            newUser.setUsername(inputCommand.getUsername());
            newUser.setAge(inputCommand.getAge());
            newUser.setCity(inputCommand.getCity());
            database.getLibrary().getUsers().add(newUser); // add user to database's library
            if (newUser.getType().equals("user")) { // all users are online when added
                database.getOnlineUsers().add(newUser); // then, update the database
            }
            message = "The username " + inputCommand.getUsername() + " has been added "
                    + "successfully.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
