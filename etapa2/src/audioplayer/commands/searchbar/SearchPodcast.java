package audioplayer.commands.searchbar;

import audioplayer.Constants;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import audioplayer.commands.commandsInput.Filters;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class SearchPodcast extends SearchBar {
    private final ArrayList<PodcastInput> podcasts;
    @Getter @Setter
    private ArrayList<PodcastInput> results = new ArrayList<>();
    public SearchPodcast(final Filters f, final LibraryInput i) {
        this.setFilters(f);
        this.setInput(i);
        this.podcasts = this.getInput().getPodcasts();
    }
    /**
     * filters the podcasts in library based on the given filters;
     * after one filter applies, we add the current podcast to filteredPodcasts list
     * if it is empty, if not we test the podcasts in this list with current filter;
     * after applying all the filters, we update the numberOfOc and the results
     * accordingly (if there are more than 5 results, we get the first 5 in filteredPodcasts)
     */
    @Override
    public final void search() {
        ArrayList<PodcastInput> filteredPodcasts = new ArrayList<PodcastInput>();
        if (this.getFilters().getName() != null) {
            String value = this.getFilters().getName();
            int n = value.length();
            for (PodcastInput podcast : podcasts) {
                if (podcast.getName().contains(value)) {
                    filteredPodcasts.add(podcast);
                }
            }
        }
        if (this.getFilters().getOwner() != null) {
            if (filteredPodcasts.isEmpty()) {
                for (PodcastInput podcast : podcasts) {
                    String str1 = podcast.getOwner();
                    if (str1.equals(this.getFilters().getOwner())) {
                        filteredPodcasts.add(podcast);
                    }
                }
            } else {
                for (PodcastInput podcast : filteredPodcasts) {
                    String str1 = podcast.getOwner();
                    if (!str1.equals(this.getFilters().getOwner())) {
                        filteredPodcasts.remove(podcast);
                    }
                }
            }
        }
        int nr = filteredPodcasts.size();
        if (nr > Constants.getFive()) {
            nr = Constants.getFive(); // we need to return a list with first 5 results
        }
        for (int i = 0; i < nr; i++) {
            results.add(filteredPodcasts.get(i));
        }
        this.setNumberOfOc(nr);
    }
}
