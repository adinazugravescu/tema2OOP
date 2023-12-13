package audioplayer;

import fileio.input.LibraryInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * class created for database data management
 */
public final class Database {
    private static Database instance = null;
    @Getter
    private ArrayList<UserInput> onlineUsers = new ArrayList<UserInput>();
    @Getter @Setter
    private LibraryInput library = new LibraryInput();

    /**
     * setter for onlineUsers
     * @param onlineUsers for initialization data
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
     * @return the instance if it exists or creates a new one and returns it then
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

}
