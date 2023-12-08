package audioplayer.commands.player;

import audioplayer.commands.playlist.Playlist;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import audioplayer.commands.userData.Album;

public final class LoadNext {
    private LoadNext() {

    }

    /**
     * method searches for next episode in podcast
     * @param podcast current podcast
     * @param name of the current episode in player
     * @return next episode or null in case of finalization
     */
    public static EpisodeInput forPodcast(final PodcastInput podcast, final String name) {
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            if (podcast.getEpisodes().get(i).getName().equals(name)) {
                if (i < podcast.getEpisodes().size() - 1) {
                    return podcast.getEpisodes().get(i + 1);
                }
            }
        }
        return null;
    }

    /**
     * method searches for the next song in playlist
     * @param playlist current playlist
     * @param name of the current song in player
     * @return next song or null in case of finalization
     */
    public static SongInput forPlaylist(final Playlist playlist, final String name) {
        for (int i = 0; i < playlist.getSongs().size(); i++) {
            if (playlist.getSongs().get(i).getName().equals(name)) {
                if (i < playlist.getSongs().size() - 1) {
                    return playlist.getSongs().get(i + 1);
                }
            }
        }
        return null;
    }

    /**
     *
     * @param album
     * @param name
     * @return
     */
    public static SongInput forAlbum(final Album album, final String name) {
        for (int i = 0; i < album.getSongs().size(); i++) {
            if (album.getSongs().get(i).getName().equals(name)) {
                if (i < album.getSongs().size() - 1) {
                    return album.getSongs().get(i + 1);
                }
            }
        }
        return null;
    }
}
