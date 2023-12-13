package audioplayer.commands.searchbar;

import audioplayer.Constants;
import audioplayer.commands.commandsInput.Filters;
import audioplayer.commands.userData.Album;
import fileio.input.LibraryInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SearchAlbum extends SearchBar {
    private final ArrayList<UserInput> users;
    @Getter @Setter
    private ArrayList<Album> results = new ArrayList<>();
    public SearchAlbum(final Filters f, final LibraryInput i) {
        this.setFilters(f);
        this.setInput(i);
        this.users = this.getInput().getUsers();
    }

    /**
     * filters the albums in users records based on the given filters;
     * after one filter applies, we add the current album to filteredAlbums list
     * if it is empty, if not we test the albums in this list with current filter;
     * after applying all the filters, we update the numberOfOc and the results
     * accordingly (if there are more than 5 results, we get the first 5 in filteredAlbums)
     */
    @Override
    public final void search() {
        ArrayList<Album> filteredAlbums = new ArrayList<Album>();
        ArrayList<Album> usersAlbums = new ArrayList<Album>();
        for (UserInput user : users) {
            if (!user.getAlbums().isEmpty()) {
                for (Album album : user.getAlbums()) {
                    usersAlbums.add(album);
                }
            }
        }
        if (this.getFilters().getName() != null) {
            String value = this.getFilters().getName();
            for (Album album : usersAlbums) {
                if (album.getName().startsWith(value)) {
                    filteredAlbums.add(album);
                }
            }
        }
        if (this.getFilters().getOwner() != null) {
            if (filteredAlbums.isEmpty()) {
                String value = this.getFilters().getOwner();
                for (UserInput user : users) {
                    if (!user.getAlbums().isEmpty()) {
                        if (user.getUsername().startsWith(value)) {
                            for (Album album : user.getAlbums()) {
                                filteredAlbums.add(album);
                            }
                        }
                    }
                }
            } else {
                filteredAlbums.removeIf(album -> !album.getOwner().startsWith(this.getFilters().
                        getOwner()));
            }
        }
        if (this.getFilters().getDescription() != null) {
            if (filteredAlbums.isEmpty()) {
                String value = this.getFilters().getDescription();
                for (Album album : usersAlbums) {
                    if (album.getDescription().startsWith(value)) {
                        filteredAlbums.add(album);
                    }
                }
            } else {
                filteredAlbums.removeIf(album -> !album.getDescription().startsWith(this.
                        getFilters().getDescription()));
            }
        }
        int nr = filteredAlbums.size();
        int five = Constants.getFive();
        if (nr > five) {
            nr = five; // we need to return a list with first 5 results
        }
        for (int i = 0; i < nr; i++) {
            results.add(filteredAlbums.get(i));
        }
        this.setNumberOfOc(nr);

    }
}
