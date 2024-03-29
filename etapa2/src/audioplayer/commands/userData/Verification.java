package audioplayer.commands.userData;
import java.util.HashMap;

import fileio.input.EpisodeInput;
import fileio.input.SongInput;

import java.util.ArrayList;

public final class Verification {
    private  Verification() {
    }

    /**
     * checks if there are more occurrences of the same song in input
     * @param songs list from the input
     * @return true if there are more than 2 equal songs, false instead
     */
    public static boolean song(final ArrayList<SongInput> songs) {
        HashMap<String, Integer> songNameCount = new HashMap<>();
        // Count occurrences of song names
        for (SongInput song : songs) {
            String songName = song.getName();
            songNameCount.put(songName, songNameCount.getOrDefault(songName, 0) + 1);
        }

        // Check if there are more than two songs with the same name
        boolean aux = false; // auxiliary field that retains if there are more than 2 equal songs
        for (HashMap.Entry<String, Integer> entry : songNameCount.entrySet()) {
            if (entry.getValue() >= 2) {
                aux = true;
                break;
            }
        }
        return aux;
    }

    /**
     * splits and formats the date in 3 int entities and checks the invalid options
     * @param inputDate from command
     * @return true if the date is valid, false instead
     */
    public static boolean event(final String inputDate) {
        String[] dateParts = inputDate.split("-");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        int badDay = 31;
        int badFebruary = 28;
        int february = 2;
        int badMonth = 12;
        int badYear1 = 1900;
        int badYear2 = 2023;
        if (day > badDay || (day > badFebruary && month == february) || month > badMonth
                || year < badYear1 || year > badYear2) {
            return false;
        }
        return true;
    }

    /**
     * checks if there are more occurrences of the same episode in input
     * @param episodes from input
     * @return true if there are more than 2 equal episodes, false instead
     */
    public static boolean podcast(final ArrayList<EpisodeInput> episodes) {
        HashMap<String, Integer> episodeNameCount = new HashMap<>();
        // Count occurrences of podcast names
        for (EpisodeInput episode : episodes) {
            String podcastName = episode.getName();
            episodeNameCount.put(podcastName, episodeNameCount.getOrDefault(podcastName, 0) + 1);
        }

        // Check if there are more than two episodes with the same name
        boolean aux = false; // auxiliary field that retains if there are more than 2 equal
        for (HashMap.Entry<String, Integer> entry : episodeNameCount.entrySet()) {
            if (entry.getValue() >= 2) {
                aux = true;
                break;
            }
        }
        return aux;
    }
}
