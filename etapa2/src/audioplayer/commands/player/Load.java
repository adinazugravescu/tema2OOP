package audioplayer.commands.player;

/**
 * Load possible messages
 */
public final class Load {
    /**
     * @return message for loaded successfully
     */
    public static String message1() {
        return "Playback loaded successfully.";
    }
    /**
     * @return message for not selected source
     */
    public static String message2() {
        return "Please select a source before attempting to load.";
    }
    private Load() {

    }
}
