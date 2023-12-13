package audioplayer.commands.searchbar;

import audioplayer.Constants;
import audioplayer.commands.commandsInput.Filters;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SearchHost extends SearchBar {
    private final ArrayList<UserInput> users;
    @Getter @Setter
    private ArrayList<UserInput> results = new ArrayList<>();
    public SearchHost(final Filters f, final ArrayList<UserInput> i) {
        this.setFilters(f);
        this.users = i;
    }

    /**
     * filters the hosts in users records based on the name;
     * after one filter applies, we add the current host to filteredHosts list
     * we update the numberOfOc and the results accordingly (if there are more than 5 results,
     * we get the first 5 in filteredHosts)
     */
    @Override
    public final void search() {
        ArrayList<UserInput> filteredHosts = new ArrayList<UserInput>();
        if (this.getFilters().getName() != null) {
            String value = this.getFilters().getName();
            for (UserInput user : users) {
                if (user.getUsername().startsWith(value)
                && user.getType().equals("host")) {
                    filteredHosts.add(user);
                }
            }
        }
        int nr = filteredHosts.size();
        int five = Constants.getFive();
        if (nr > five) {
            nr = five; // we need to return a list with first 5 results
        }
        for (int i = 0; i < nr; i++) {
            results.add(filteredHosts.get(i));
        }
        this.setNumberOfOc(nr);
    }
}
