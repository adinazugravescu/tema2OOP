package audioplayer.commands.searchbar;

public final class Select {
    /**
     * returns the message in case of a successful selection
     * @param name of the selected item
     * @return a String
     */
    public static String message1(final String name) {
        return "Successfully selected " + name + ".";
    }

    /**
     * returns the message in case of no pre-search
     * @return a String
     */
    public static String message2() {
        return "Please conduct a search before making a selection.";
    }

    /**
     * returns the message in case of no valid ID
     * @return a String
     */
    public static String message3() {
        return "The selected ID is too high.";
    }

    /**
     *
     * @param name
     * @return
     */
    public static String message4(final String name) {
        return "Successfully selected " + name + "'s page.";
    }
    private Select() {
    }
}
