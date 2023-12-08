package audioplayer.commands.player;

import audioplayer.commands.playlist.Playlist;
import audioplayer.commands.userData.Album;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;

public final class LoadPrev {
    private LoadPrev() {

    }

    /**
     * implements the logic for prev
     * @param podcast current in load
     * @param name of the current episode in podcast
     * @param passedTime of the episode in load
     * @return the needed episode
     */
    public static EpisodeInput forPodcast(final PodcastInput podcast, final String name,
                                          final int passedTime) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            if (podcast.getEpisodes().get(i).getName().equals(name)) {
                if (i > 0) {
                    if (passedTime > 0) {
                        return podcast.getEpisodes().get(i);
                    } else {
                        return podcast.getEpisodes().get(i - 1);
                    }
                } else {
                    return podcast.getEpisodes().get(0);
                }
            }
        }
        return null;
    }

    /**
     * implements the logic for prev
     * @param playlist current in load
     * @param name of the current song in playlist
     * @param passedTime of the song in load
     * @return the needed song
     */
    public static SongInput forPlaylist(final Playlist playlist, final String name,
                                        final int passedTime) {
        for (int i = 0; i < playlist.getSongs().size(); i++) {
            if (playlist.getSongs().get(i).getName().equals(name)) {
                if (i > 0) {
                    if (passedTime > 0) {
                        return playlist.getSongs().get(i);
                    } else {
                        return playlist.getSongs().get(i - 1);
                    }
                } else {
                    return playlist.getSongs().get(0);
                }
            }
        }
        return null;
    }

    /**
     *
     * @param album
     * @param name
     * @param passedTime
     * @return
     */
    public static SongInput forAlbum(final Album album, final String name,
                                     final int passedTime) {
        for (int i = 0; i < album.getSongs().size(); i++) {
            if (album.getSongs().get(i).getName().equals(name)) {
                if (i > 0) {
                    if (passedTime > 0) {
                        return album.getSongs().get(i);
                    } else {
                        return album.getSongs().get(i - 1);
                    }
                } else {
                    return album.getSongs().get(0);
                }
            }
        }
        return null;
    }
}
