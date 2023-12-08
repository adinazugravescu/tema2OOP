package audioplayer.commands.searchbar;

import audioplayer.Constants;
import audioplayer.commands.commandsInput.Filters;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SearchArtist extends SearchBar {
    private final ArrayList<UserInput> users;
    @Getter @Setter
    private ArrayList<UserInput> results = new ArrayList<>();
    public SearchArtist(final Filters f, final ArrayList<UserInput> i) {
        this.setFilters(f);
        this.users = i;
    }
    @Override
    public final void search() {
        ArrayList<UserInput> filteredArtists = new ArrayList<UserInput>();
        if (this.getFilters().getName() != null) {
            String value = this.getFilters().getName();
            for (UserInput user : users) {
                if (user.getUsername().startsWith(value)) {
                    filteredArtists.add(user);
                }
            }
        }
        int nr = filteredArtists.size();
        int five = Constants.getFive();
        if (nr > five) {
            nr = five; // we need to return a list with first 5 results
        }
        for (int i = 0; i < nr; i++) {
            results.add(filteredArtists.get(i));
        }
        this.setNumberOfOc(nr);
    }
}
