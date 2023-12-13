package audioplayer.commands.userData;

import lombok.Getter;
import lombok.Setter;

/**
 * class for merch entity
 */
public class Merch {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private int price;
    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
