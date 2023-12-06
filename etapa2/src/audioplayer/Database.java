package audioplayer;

import fileio.input.UserInput;
import lombok.Getter;

import java.util.ArrayList;

public final class Database {
    private static Database instance = null;
    @Getter
    private ArrayList<UserInput> onlineUsers = new ArrayList<UserInput>();

    /**
     *
     * @param onlineUsers
     */
    public void setOnlineUsers(final ArrayList<UserInput> onlineUsers) {
        ArrayList<UserInput> copy = new ArrayList<UserInput>();
        for (UserInput user : onlineUsers) {
            UserInput iter = new UserInput(user);
            copy.add(iter);
        }
        this.onlineUsers = copy;

    }
    private Database() {
    }

    /**
     *
     * @return
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

}
