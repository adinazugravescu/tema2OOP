package audioplayer.commands.searchbar;
import java.util.Iterator;

import audioplayer.Constants;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import audioplayer.commands.commandsInput.Filters;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;


public class SearchMelody extends SearchBar {
    private final ArrayList<SongInput> songs;
    @Getter @Setter
    private ArrayList<SongInput> results = new ArrayList<>();
    public SearchMelody(final Filters f, final LibraryInput i) {
        this.setFilters(f);
        this.setInput(i);
        this.songs = this.getInput().getSongs();
    }

    /**
     * filters the songs in library based on the given filters;
     * after one filter applies, we add the current song to filteredSongs list
     * if it is empty, if not we test the songs in this list with current filter;
     * after applying all the filters, we update the numberOfOc and the results
     * accordingly (if there are more than 5 results, we get the first 5 in filteredSongs)
     */
    @Override
    public final void search() {
        ArrayList<SongInput> filteredSongs = new ArrayList<SongInput>();
        if (this.getFilters().getName() != null) {
            String value = this.getFilters().getName();
            for (SongInput song : songs) {
                if (song.getName().startsWith(value)) {
                    filteredSongs.add(song);
                }
            }
        }
        if (this.getFilters().getAlbum() != null) {
            if (filteredSongs.isEmpty()) {
                for (SongInput song : songs) {
                    String str1 = song.getAlbum();
                    if (str1.equals(this.getFilters().getAlbum())) {
                        filteredSongs.add(song);
                    }
                }
            } else {
                for (SongInput song : filteredSongs) {
                    String str1 = song.getAlbum();
                    if (!str1.equals(this.getFilters().getAlbum())) {
                        filteredSongs.remove(song);
                    }
                }
            }
        }
        if (this.getFilters().getTags() != null && !this.getFilters().getTags().isEmpty()) {
            Collections.sort(this.getFilters().getTags());
            if (filteredSongs.isEmpty()) {
                for (SongInput song : songs) {
                    ArrayList<String> songTags = new ArrayList<>(song.getTags());
                    Collections.sort(songTags);
                    if (songTags.containsAll(this.getFilters().getTags())) {
                        filteredSongs.add(song);
                    }
                }
            } else {
                for (SongInput song : filteredSongs) {
                    ArrayList<String> songTags = new ArrayList<>(song.getTags());
                    Collections.sort(songTags);
                    if (!songTags.containsAll(this.getFilters().getTags())) {
                        filteredSongs.add(song);
                    }
                }
            }
        }
        if (this.getFilters().getLyrics() != null) {
            if (filteredSongs.isEmpty()) {
                for (SongInput song : songs) {
                    String str1 = song.getLyrics();
                    if (str1.toLowerCase().contains(this.getFilters().getLyrics().toLowerCase())) {
                        filteredSongs.add(song);
                    }
                }
            } else {
                for (SongInput song : filteredSongs) {
                    String str1 = song.getLyrics();
                    if (!str1.contains(this.getFilters().getLyrics())) {
                        filteredSongs.remove(song);
                    }
                }
            }
        }
        if (this.getFilters().getGenre() != null) {
            if (filteredSongs.isEmpty()) {
                for (SongInput song : songs) {
                    String str1 = song.getGenre();
                    if (str1.equalsIgnoreCase(this.getFilters().getGenre())) {
                        filteredSongs.add(song);
                    }
                }
            } else {
                /*for (SongInput song : filteredSongs) {
                    String str1 = song.getGenre();
                    str1 = str1.toLowerCase();
                    if (!str1.equals(this.getFilters().getGenre())) {
                        filteredSongs.remove(song);
                    }
                } */
                Iterator<SongInput> iterator = filteredSongs.iterator();
                while (iterator.hasNext()) {
                    SongInput song = iterator.next();
                    String str1 = song.getGenre().toLowerCase();
                    if (!str1.equals(this.getFilters().getGenre())) {
                        iterator.remove();
                    }
                }
            }
        }
        if (this.getFilters().getReleaseYear() != null) {
            String sign = this.getFilters().getReleaseYear().substring(0, 1);
            String value = this.getFilters().getReleaseYear();
            String numericPart = value.replaceAll("\\D", "");
            int year = Integer.parseInt(numericPart);
            if (filteredSongs.isEmpty()) {
                for (SongInput song : songs) {
                    if (sign.equals("<")) {
                        if (song.getReleaseYear() < year) {
                            filteredSongs.add(song);
                        }
                    } else {
                        if (sign.equals(">")) {
                            if (song.getReleaseYear() > year) {
                                filteredSongs.add(song);
                            }
                        }
                    }
                }
            } else {
                filteredSongs.removeIf(song -> (song.getReleaseYear() > year && sign.equals("<"))
                        || song.getReleaseYear() < year && sign.equals(">"));
            }
        }
        if (this.getFilters().getArtist() != null) {
            if (filteredSongs.isEmpty()) {
                for (SongInput song : songs) {
                    String str1 = song.getArtist();
                    if (str1.equals(this.getFilters().getArtist())) {
                        filteredSongs.add(song);
                    }
                }
            } else {
                Iterator<SongInput> iterator = filteredSongs.iterator();
                while (iterator.hasNext()) {
                    SongInput song = iterator.next();
                    String str1 = song.getArtist();
                    if (!str1.equals(this.getFilters().getArtist())) {
                        iterator.remove();
                    }
                }
            }
        }
        int nr = filteredSongs.size();
        int five = Constants.getFive();
        if (nr > five) {
            nr = five; // we need to return a list with first 5 results
        }
        for (int i = 0; i < nr; i++) {
            results.add(filteredSongs.get(i));
        }
        this.setNumberOfOc(nr);
    }
}
