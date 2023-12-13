package audioplayer.commands.userData;

import lombok.Getter;
import lombok.Setter;

/**
 * class for event entity
 */
public class Event {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String date;
    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }
}
