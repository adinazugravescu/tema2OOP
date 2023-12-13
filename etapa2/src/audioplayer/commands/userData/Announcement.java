package audioplayer.commands.userData;

import lombok.Getter;
import lombok.Setter;

/**
 * class for announcement entity
 */
public class Announcement {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    public Announcement(final String n, final String d) {
        this.name = n;
        this.description = d;
    }
}
