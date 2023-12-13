package audioplayer.commands.commandsOutput;

import audioplayer.Database;
import audioplayer.commands.commandsInput.CommandsInput;
import audioplayer.commands.player.UserSearchResult;
import audioplayer.commands.player.UserSelectResult;
import audioplayer.commands.searchbar.Select;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.UserInput;

import java.util.ArrayList;

public final class DoSelect {
    private DoSelect() {

    }

    /**
     * implements the logic for select command
     * (if the current user has select history in listOfResults and
     * the item number is valid, we update the listOfSelectors)
     * using :
     * @param newN ObjectNode to store output fields
     * @param inputCommand the current command
     * @param outputs ArrayNode - main output node completed with current
     *                       info (newN) at every command
     * @param listOfResults list for last search result for every user
     * @param listOfSelectors list for last select for every user
     */
    public static void exe(final ObjectNode newN, final CommandsInput inputCommand, final ArrayNode
                            outputs, final ArrayList<UserSearchResult> listOfResults,
                           final ArrayList<UserSelectResult> listOfSelectors, final
                           Database database) {
        Output.put(newN, "select", inputCommand.getUsername(), inputCommand.getTimestamp());
        int aux = 0;
        UserSearchResult crt = new UserSearchResult();
        for (UserSearchResult user : listOfResults) {
            if (user.getUsername().equals(inputCommand.getUsername())) {
                aux = 1;
                crt = user;
                break;
            }
        }
        if (aux == 0) { // user did no search before
            Output.select(newN, Select.message2());
        } else
        if (inputCommand.getItemNumber() > crt.getNumberOfResults()
                || crt.getNumberOfResults() == 0) {  // the item number or number of results are
                                                    // not valid
            Output.select(newN, Select.message3());
        } else {
            // we retain the select info in a new UserSelectResult object
            UserSelectResult newSelect = new UserSelectResult();
            if (crt.getListOfusers().isEmpty()) {
                Output.select(newN, Select.message1(crt.nameOfResult(
                        inputCommand.getItemNumber() - 1)));
                newSelect.setUsername(inputCommand.getUsername());
            }
            if (!crt.getListOfsongs().isEmpty()) {
                newSelect.setSong(crt.getListOfsongs().get(inputCommand.getItemNumber()
                        - 1));
            } else {
                if (!crt.getListOfpodcasts().isEmpty()) {
                    newSelect.setPodcast(crt.getListOfpodcasts().get(
                            inputCommand.getItemNumber() - 1));
                } else {
                    if (!crt.getListOfplaylists().isEmpty()) {
                        newSelect.setPlaylist(crt.getListOfplaylists().get(
                                inputCommand.getItemNumber() - 1));
                    } else {
                        if (!crt.getListOfusers().isEmpty()) {
                            // if user selects a user entity do not update newSelect
                            // only change current user's page to what it is in search results
                            for (UserInput user : database.getLibrary().getUsers()) {
                                if (user.getUsername().equals(inputCommand.getUsername())) {
                                    if (crt.getListOfusers().get(inputCommand.getItemNumber() - 1).
                                            getType().equals("artist")) {
                                        user.setCurrentPage("Artist page");
                                    } else {
                                        if (crt.getListOfusers().get(inputCommand.getItemNumber()
                                                - 1).getType().equals("host")) {
                                            user.setCurrentPage("Host page");
                                        }
                                    }
                                    user.setUsersPage(crt.getListOfusers().
                                            get(inputCommand.getItemNumber() - 1));
                                    Output.select(newN, Select.message4(crt.nameOfResult(
                                            inputCommand.getItemNumber() - 1)));
                                }
                            }
                        } else {
                            if (!crt.getListOfalbums().isEmpty()) {
                                newSelect.setAlbum(crt.getListOfalbums().get(inputCommand.
                                        getItemNumber() - 1));
                            }
                        }
                    }
                }
            }
            if (crt.getListOfusers().isEmpty()) {
                listOfSelectors.add(newSelect);
            }
            listOfResults.remove(crt);
        }
        outputs.add(newN);
    }
}
