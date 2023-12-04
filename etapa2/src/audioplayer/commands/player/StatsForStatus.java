package audioplayer.commands.player;

import lombok.Getter;
import lombok.Setter;

/**
 * class for status info
 */
public class StatsForStatus {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int remainedTime;
    @Getter @Setter
    private String repeat;
    @Getter @Setter
    public boolean shuffle;
    @Getter @Setter
    public boolean paused;

    @Override
    public final String toString() {
        return  "name='" + name + '\''
                + ", remainedTime=" + remainedTime
                + ", repeat='" + repeat
                + '\'' + ", shuffle=" + shuffle
                + ", paused=" + paused
                + '}';
    }
}
