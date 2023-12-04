package audioplayer.commands.player;

public final class PlayPause {
    /**
     * @param status greater than 0, then resumed, else paused
     * @return message
     */
    public static String message(final int status) {
        if (status == 0) {
            return "Playback paused successfully.";
        }
        return "Playback resumed successfully.";
    }

    /**
     * @return message if no source is loaded
     */
    public static String noLoad() {
        return "Please load a source before attempting to pause or resume playback.";
    }
    private PlayPause() {

    }
}
