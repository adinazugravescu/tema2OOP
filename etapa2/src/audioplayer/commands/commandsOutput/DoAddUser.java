package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.LibraryInput;
import fileio.input.UserInput;

public final class DoAddUser {
    private DoAddUser() {

    }
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final
    ArrayNode outputs, final LibraryInput library) {
        Output.put(newN, "addUser", inputCommand.getUsername(),
                inputCommand.getTimestamp());
        String message = "";
        int exists = 0;
        for (UserInput user : library.getUsers()) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                exists = 1;
                message = "The username " + inputCommand.getUsername() + " is already taken.";
                break;
            }
        }
        if (exists == 0) {
            UserInput newUser = new UserInput();
            newUser.setType(inputCommand.getType());
            newUser.setUsername(inputCommand.getUsername());
            newUser.setAge(inputCommand.getAge());
            newUser.setCity(inputCommand.getCity());
            library.getUsers().add(newUser);
            message = "The username " + inputCommand.getUsername() + " has been added " +
                    "successfully.";
        }
        newN.put("message", message);
        outputs.add(newN);
    }
}
