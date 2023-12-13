# Proiect GlobalWaves  - Etapa 2

<div align="center"><img src="https://tenor.com/view/listening-to-music-spongebob-gif-8009182.gif" width="300px"></div>

## Student : Zugravescu Adina Andreea - 322 CD

## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.

## Implementation
* For the second stage of the project's implementation I used my own structure from the first stage.
***
My implementation for this Audio Player has as starting point reading the commands, instantiating an object of type 'Implement'
in Main.java, and executing its method that utilizes data from the library and the commands to process the data and format the output.
The step-by-step development of the logic and code can be found in the 'src/audioplayer' package.
***

## Structure - Roles and Connections
* src/
  * audioplayer/ - contains player implementation
    * Implement.java - the audio player's menu, filtering the commands and processing them in 'Do.*'-class methods depending on type
    * Constants.java - for numeric constants
    * Database.java - database used in managing library and user data, created with Singleton design pattern
    * commands/ - package for managing commands
      * commandsInput/ - package for classes used to read data from the json files
        * CommandsInput.java - the input's format
        * Filters.java - the format used for commands' filter input
      * commandsOutput/ - package for classes used to execute each type of command and format the output
        * Do"name-of-command".java (e.g. DoSearch.java, DoSelect.java, DoLoad.java, ...) - classes for logic execution for all commands
        * Output.java - provides the standard output format regardless of the command type (all results must have "command", "user" and "timestamp" fields completed)
      * connection/ - package for Check.java class used to verify the connection status
        * Check.java - checks if current user belongs to onlineUsers structure from database
      * player/ - package for classes used in player operations
        * Backward.java - finds the needed episode when player rules backward (in this case it coincides with the current one)
        * Forward.java - finds the needed episode when player rules forward (depending on the remaining time of the current in player)
        * Load.java - utility class for possible load messages
        * Loaders.java - stores info about a user's load
        * LoadNext.java - provides next song/episode in player when a user commands next
        * LoadPrev.java - provides previous song/episode in player when a user commands prev
        * PlayPause.java - utility class for possible playPause messages
        * PodcastStats.java - stores info about a previously loaded user's podcast in case of future load
        * StatsForStatus.java - format used to store the statistics of a loaded file
        * UserSearchResult.java - class for a user's search history
        * UserSelectResult.java - class for a user's select history
      * playlist/ - package for classes used in playlist operations
        * CreateNew.java - creates a new owner/playlist for an owner
        * FollowedPlaylists.java - class for a user's follow history
        * GetTop5Playlists.java - searches for public playlists and sorts them by the number of followers
        * GetTop5Songs.java - gets the total number of likes for each song and sorts them by the number of likes
        * Playlist.java - class for playlist format and info
        * PlaylistManager.java - shuffles a playlist songs based on the input seed
        * PlaylistOwners.java - class for a user's playlists ownership history
        * PreferredSongs.java - class for a user's liked songs history
        * UnshuffledSongs.java - class for retaining initial order of an owner's playlist songs
      * searchbar/ - package for classes used in the implementation of the searchbar
        * SearchAlbum.java - provides the filtered albums and the number of results after searching through users data
        * SearchArtist.java - provides the filtered artists and the number of results after searching through all the artist users
        * SearchBar.java - superclass for search operations depending on the type of audio file
        * SearchHost.java - provides the filtered hosts and the number of results after searching through all the host users
        * SearchMelody.java - provides the filtered songs and the number of results after searching through library
        * SearchPlaylist.java - provides the filtered playlists and the number of results after searching through the list of playlist owners
        * SearchPodcast.java - provides the filtered podcasts and the number of results after searching through library
        * Select.java - utility class for possible select messages
      * userData/ -package for classes used in user operations
        * Album.java - class for album format and info
        * Announcement.java - class for announcement format and info
        * Event.java - class for event format and info
        * GetTop5Albums.java - gets total number of likes for each album and sorts them by the likes
        * Merch.java - class for merch format and info
        * Verification.java - implements the verifications needed when 'add' operations are called
        
## The Design Pattern used is Singleton pattern
  * in src/audioplayer/Database.java