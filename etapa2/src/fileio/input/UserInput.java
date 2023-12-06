package fileio.input;


import audioplayer.commands.userData.Album;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class UserInput {
    private String username;
    private int age;
    private String city;
    @Getter @Setter
    private String type = "user";
    @Getter @Setter
    private ArrayList<Album> albums = new ArrayList<Album>();
    @Getter @Setter
    private String currentPage = "HomePage";

    public UserInput() {
    }
    public UserInput(final UserInput user) {
        this.username = user.getUsername();
        this.age = user.getAge();
        this.city = user.getCity();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }
}

