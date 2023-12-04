package audioplayer.commands.searchbar;
import audioplayer.Constants;
import audioplayer.commands.commandsInput.Filters;
import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.playlist.PlaylistOwners;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SearchPlaylist extends SearchBar {
    public SearchPlaylist(final Filters f, final ArrayList<PlaylistOwners> c, final String o) {
        this.setPlaylistOwners(c);
        this.setFilters(f);
        this.ownerCurrentPlaylist = o;
    }
    @Getter @Setter
    private ArrayList<Playlist> results = new ArrayList<>();
    @Getter @Setter
    private String ownerCurrentPlaylist;
    /**
     * filters the playlists in playlistOwners based on the given filters;
     * after one filter applies, we add the current playlist to filteredPlaylists list
     * if it is empty, if not we test the playlists in this list with current filter;
     * after applying all the filters, we update the numberOfOc and the results
     * accordingly (if there are more than 5 results, we get the first 5 in filteredPlaylists)
     */
    @Override
    public final void search() {
        ArrayList<Playlist> filteredPlaylists = new ArrayList<>();
        if (this.getFilters().getName() != null) {
            for (PlaylistOwners iter1 : this.getPlaylistOwners()) {
                for (Playlist iter2 : iter1.getPlaylists()) {
                    if (iter2.privatePlaylist && !iter2.getOwner().
                            equals(ownerCurrentPlaylist)) { // if playlist is private and the user
                                                            // that searches it does not own it
                                                            // the user can't access it
                        continue;
                    }
                    String str1 = iter2.getName();
                    if (str1.startsWith(this.getFilters().getName())) {
                        filteredPlaylists.add(iter2);
                    }
                }
            }
        }
        if (this.getFilters().getOwner() != null) {
            if (filteredPlaylists.isEmpty()) {
                for (PlaylistOwners iter : this.getPlaylistOwners()) {
                    String str1 = iter.getOwner();
                    if (str1.equals(this.getFilters().getOwner())) {
                        for (Playlist iter2 : iter.getPlaylists()) {
                            //se adauga doar cele public
                            if (iter2.privatePlaylist && !iter2.getOwner().
                                    equals(ownerCurrentPlaylist)) {
                                // if playlist is private and the user that searches it
                                // does not own it then the user can't access it
                                continue;
                            }
                            filteredPlaylists.add(iter2);
                        }
                    }
                }
            } else {
                for (Playlist playlist : filteredPlaylists) {
                    String str1 = playlist.getOwner();
                    if (!str1.equals(this.getFilters().getOwner())) {
                        filteredPlaylists.remove(playlist);
                    }
                }
            }
        }
        int nr = filteredPlaylists.size();
        if (nr > Constants.getFive()) {
            nr = Constants.getFive(); // we need to return a list with first 5 results
        }
        for (int i = 0; i < nr; i++) {
            results.add(filteredPlaylists.get(i));
        }
        this.setNumberOfOc(nr);
    }

}
