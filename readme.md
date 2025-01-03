# Island of Secrets

This is an attempt to translate the game Island of Secrets to a modern computer. The game appeared as a basic
listing in a book of the same name and the idea was that you would type the game in and play it. The
problem with adventure games is that if you type it in then you can fairly easily work out the solution
to the game, so the authors made it as difficult as possible for somebody to be able to read the code.
While this meant that you would have a game that was playable, there was also issues with errors creeping
in, and it also made it difficult for users to learn some of the tricks of the basic language, namely
because there aren't any notes telling you what the functions did. Also, the readability is notoriously
bad.

[Anyway, you can find a copy of the book here.](https://archive.org/details/island-of-secrets_202303)
(It looks like they have all been moved to the Internet Archive).

[You can find the basic version of the game here.](https://github.com/s3664099/basic_scripts/tree/master/Island%20of%20Secrets)
(The various different basic versions are here).

## Notes on the Basic Code

*Reading the Data*

This is different to what I have seen in other games, and no doubt this is due to memory restrictions in some
of the computers. Instead of reading all of the data into the memory, it will only read one at a time (which will
be slower as it will need to do it for every item as well). 

Line 2780 repositions the pointer at the begining of the data and will read through all of the locations, discarding each one.
Line 4110 reads through all of the objects and discards them

With regards to coding the location of the objects, this is store in the string H$, which are symbols, but the ascii characters
represent a location. The next line, Q$ is a flag that is set on each of the objects, which are used throughout the game.

A file containing noted code outlining what each section of the basic code does. The notes file have also been completed outlining
what each of the variables do, and also listing each of the items, locations, verbs and nouns.

Another note is that the game is a 10x10 grid, and the map is determined by whether there is a wall between nodes or not. As such,
moving between nodes is either adding or subtracting 1 (going east or west) or adding or substracting 10 (going north or south).

## Executing the Game

To compile the game you need to do the following:

*javac* \*.*java*

Once it has compiled then you can run the game as follows:

*java Start*

## Development Notes ##
*v01*

Okay, it works, but there are some issues with it. However I won't be fulling testing it as yet since
I am looking to upgrade it future and make the code readable. Also, I want to move it away from the
command line so that it can be played in a window environment, and that the game runs smoother.

Also, I need to go over the code again and make further notes, and also tidy up the notes that I have
added to the notes file.

2 November 2024
Finished the initialisation classes. Moved the locations and objects into separate methods which are contained
in the game method (and are stored in set arrays). The commands are once again split into nouns and verbs, however
I have decided to use the whole words as opposed to the first three letters. I note that this method was originally
for making the code less complicated on older computers, but it can cause confusion (as well as being lazy). These
are also stored in arrays, and since they do not change they have been marked as final.

*v02*

I have completed a version that doesn't run on the command line through the use of swing. There are a few styling
issues that need to be fixed, as well as thorough testing is required to make sure that the game works as it is
supposed to work. The other aspect is that I should be able to use the same structure to quickly build a similar game.
However, before I do this I made some further additions.

29 December 2024 - I have added a class specifically for testing purposes. This will contain a method where I can set
specific variables before the game starts and after the classes have been instansiated, and a second one that will print
details to the console. The idea is that I should be able to keep them there without affecting the game.

30 December 2024 - Added further description to the store room to flag a door to the east & the trapdoor. Added the further
descriptions for the 'special' directions, or the ones that need further detail. Better than not displaying them at all.

## Program Structure ##

### Data ###

This package hold classes that simply carry data. Well that and the two classes - Location & Item - that are used to
hold the main objects that run the game.

**Constants**

This class holds a number of variables that do not change. These variables are used so that numbers are hardcoded into
the game (though I suspect that there are still some there). As such, the class is able to be accessed from across the
program, and each of the variable are listed as final (meaning that it cannot be changed) and static (meaning that it 
can be accessed without instanstatiating a class.

**Raw Data**

This class basically holds the raw data for the game. While it could be stored in a database, the fact that it does not 
change, and is only used while the game is running, storing it in a file is much easier (and it can be ported as well 
since a database doesn't need to be built to run the game).

Anyway, this file is only read once during the set up phase to build the locations, the items, the verbs, nouns, and 
codes for setting the item flags and location. Like the Constants class, this one is also static, meaning that it can be
read without needing to instansiate the object. Further, it is only read at the beginning of the game.

**Location**

This class holds the details of the locations. The variables that are in the class are as follows:

- name - A string. The name of the locations
- exits - an array of 4 bools. Where the exits are located.
- visited - boolean. Has the player visited this location (not currently used, but to be used for the map).
- roomType - int. The type of room. Used to display an icon on the map when created.

The only value that is changed is the visit boolean, which goes from False to True. The rest will remain as they are.
The class consists of the constructor which builds the name of the location and the exits.

It also has a method that retrieves the name of the location, and retrieves the exit array.

**Item**

This class contains three variables and is the details of the game's items. The variables are as follows:

- itemFlag - an integer. this is a flag that is used for the running of the game. Among other things, the 
	     flag determines whether an item is visible to the player. If the flag is less that 1, then it is visible.
- itemLocation - an integer. This is where the items is located. 0 indicates that the player is carrying 
             the item, while 81 indicate that the item has been destroyed.
- item - a string. This is the name of the item.

The class consists of a constructor that sets the flag and location and saves the name of the items.

Methods include a method to retrieve the name and getters and setters for the location and the flag. 
There is also a method that compares an int passed to it (which is a location) with the item's location, and returns a 
true or a false.

### Model ###

So, as it turns out, the only real pattern that I seem to use after uni is the MVC pattern, and that is basically for my
own projects. Then again, I basically work on legacy systems, so I guess there is that (they existed long before patterns
were a thing).

Anyway, this package holds the guts of the game, and is where pretty much everything occurs. Then again I suspect that that
is the purpose of the model package. 

**Main**

While the start class actually fires up the game, this class builds the game and the executes it. Initially is generates
a Game object, which holds all of the data pertaining to the game, a Player object, which holds the data for the player
(though, unfortunately, they do end up criss-crossing a bit). Finally the GameEngine object is generated which is the
guts of the game. Onse that has happened, the SwingUtilities is invokes to launch a GUI version of the game.

**Game Engine**

The engine provides the link between the Game/Player classes and the View/Controller packages. The constructor takes 
a Game object and a Player object and sets them to the corresponding objects in this class. There are a number of other
methods in the object as well.

- getTime - this method retrieves the amount of time that has passed since the beginning of the game, or rather how many
	    moves are left.

- getStatus - this retrieves the player's strength and wisdom as a string.

- getRoom - the name of the room is retrieved and passed through the the graphical display. However, first of all it
	    determines whether the player is in the 'poisoned waters' and displays that instead. Else, it will check
	    what description is displayed. We then retrieve the room number and pass a string through to the front end
	    after retrieving the description from the game object.

- getItems - a list if the visible items are displayed as a string based upon the room the player happens to be in (or viewing).

- getExits - The original game doesn't display the exits, though maybe there was a reason for this. Anyway, I have added the
	     exits, which are displayed as a string and are the exits based on the room the player is viewing.
	     
- getSpecialExits - Displays any special exists

- getMessage - the message for the previous move is retrieved. However, special message is retrieved if the player is swimming
	       in poisoned waters, so that are added here, which includes a warning if the player has a strength less than 15.
	       The message is retrieved from the game object and then cleared before being passed to the panel.

- getCommands - The retrieves the previous three commands that the player has executed and passes them through to the GamePanel.

- processCommand - This is where the command is passed when the player enters it. First it adds the command to the previous command
		   list (which lists the previous three commands). Then it determines which panel is displayed (namely not swimming
		   in poisoned waters). The processCommand object is created to determine the verb/noun numbers and then sets the
		   error response based on the number selected. If there is no noun, it is set to the end afterwards. Finally the
		   related item is retrieved (which includes some blank items which relate to noun with no matching item), and the
		   command is coded (which is a means of checking a number of varibles all at once).		   
		   The command is then executed. After this the response is determined, namely if a game has been loaded (meaning
		   that the current player/game classes are reset to the new ones from the loaded game). Finally, the panel response is
		   determined, and the game panel is reloaded.
		   As for the poisoned water section, this is basically a minigame where the player needs to escape from the poisoned
		   waters without dying (running out of strength).

- getResponseType - This is method determines the responmse type. Namely there are three types - the normal command process, the Give
		   Item response, and the Shelter response.

- processGive - This determines whether a response to a give command is correct. Namely it should be a single noun representing
		somebody present. If a new command is typed then an error response is sent. If the first word is 'to' then that is
		dropped. At the end of the process, the response type is reset to a normal response.

- processShelter - This is for the shelter command, which one option of three is selected. If the correct response is not selected
		   then an error is reported. Like the above, once the process is completed the response type is set to normal.

- setPanel - This sets the panel to a specific panel that is passed through. This is not the standard game panel.

- resetPanel - This method resets the current gamePanel, which basically updates the display (but for some reason when it is updated
	       the cursor does not go back to the input).

- checkEndGame - this checks whether the endGame flag has been set in the Game object.

- getFinalScore - this calculates the player's final score.	       
		    
**Game**

This object stores everything related to the game that isn't player specific (though there are some elements that are actually player
specific, such as where the items are located). The variables include the following:

- noRooms - int - the number of rooms, and is based on the constant.
- noItems - int -  the number of items, and is also based on the constant.
- locationList - Array of Location objects - holds all of the locations.
- itemList - Array of Item objects - holds all of the items.
- message - String - holds the response to the player's previous action
- commands - Array of Strings - holds the player's last three commands.
- rand - Random - Java standard object for generating random numbers.
- panelMessageOne & panelMessageTwo - String - These are for displaying special messages in response to specific actions.
- panelLoop - int - This is how many times the panel message display will loop.
- endGame - boolean - a flag to advise whether the game has come to an end or not.
- saveGameCount - int - counts the number of save games that are on file.
- responseRequired - int - a flag for the type of response that is required from the player.

The constructor is executed when the game starts, and this object will exist for as long as the game is being played. The constructor
will build the list of locations and objects, and sets the initial values for each of the. The location section will go through each
of the locations in the RawData class and instansiate it as a new location object and add it to a list. The same is done for the items.

- getRoomName - This returns the name of the room from the specific location that corresponds to the number that is passed
	through to it.

- getItems - This returns a string of the names of all of the items in the location corresponding to the room number passed.

- getExits - This returns the exits corresponding to the room number passed. However it checks if there are special exits,
	and if they are, they are excluded.

- getSpecialExits - These are the special exits. Each of them are listed individually. If a room corresponds, the string is returned.

- checkExit - returns a boolean with flags where it is possible to move in that direction.

- addExit - This private method simply checks whether there is an exit, and adds a dividing comma if there is.

- getMessage - Retrieves the stored message.

- clearMessage - Removes any stored message.

- setMessageGame - Sets the message that is displayed to advise the player the result of the action.

- addMessage - Checks if the message variable is blank, and if it isn't, adds the string to the end of it. Otherwise sets a new message.

- getCommand - Retrieves the command that corresponds to the number passed through.

- getItem - Returns the item from the item list that corresponds to the number passed through.

- getItemFlagSum - Returns the sum of the item flag and location from the item list corresponding to the number passed through.

- setPanelMessages - This sets the special messages that are displayed to the special message panel, which includes the number of loops.

- getMsgOne - This gets the first message from the special message panel.

- getMsgTwo - this retrieves the second message from the special message panel.

- getLoop - this returns the loop, or the number of times that the special messages will be displayed. (It will reset and display further info).

- endGame - this sets the endGame flag. Set to true, the game has ended (it will only be set to true, never to false).

- checkEndGame - The endGame flag is returned.

- getCount - the number of save games are returned.

- setCount - the number of save games are increased by one.

- resetCount - the number of saved games are reset to 0.

- setResponse - the response type is set. These are the response types the player should give. The response types are as follows:
	- 0 Standard Response.
	- 1 Give Response.
	- 2 Shelter Response.

- getResponse - the response type is returned.

**Player**

This object stores the details that relates to the player, which includes the room, the stats and others
that are detailed below.

- room - int - the room number the player is currently in
- roomToDisplay - int - the room that is displayed to the player (not necessarily the same).
- strength - float
- wisdom - int
- timeRemaining - int
- weight - int - the amount of stuff the player is carrying
- food - int - the amount of food the player is carrying
- drink - int - the amount of drink the player is carrying
- rand - Random - an object that generates a random number
- panelFlag - int - a number that tells the computer what panel to use after the command has been processed.
- swimming - int - The is one of the three variables for handling the swimming in poisoned waters
- swimPosition - int - the position the player is in for swimming
- swimTarget - int - where the player needs to reach for swimming.

There is not set constructor for this class since the starting variables are hardcoded, there will only be one of these classes created and
it will exists for the entire game. As such, a constructor does not need to be specified.

- getDisplayRoom - this returns the number of the room that is to be displayed when the details of the location is displayed.

- updateDisplayRoom - sets the room that is to be displayed. Specifically, if the player's room is 20, then a random room is displayed.

- getStatus - String - a string that outlines the player's strength and wisdom is returned.

- update - the method is called once a turn, and reduces the time remaining, and the player's strength based on the number of items
	   the player is carrying.

- getStrengthWisdom - float - returns the sum of the player's strength and wisdom.

- getRoom - int - returns the room that the player is currently in

- setRoom - this setter changes the room that the player is in by the new room that is passed to it.

- getTimeDetails - String - returns the time remaining as a string for display

- getTime - int - returns the time remainging

- reduceTime - deducts on from timeRemaining

- setTime - changes timeRemaining to a new time passed through it.

- getWisdom - int - returns the player's current wisdom

- setWisdom - changes the player's wisdom to the new wisdom that is passed through to it.

- adjustWisdom - changes the wisdom by the amount passed through it the method.

- getStrength - float - returns the strength of the player.

- setStrength -  changes the player's strength to the new strength that is passed through to it.

- adjustStrength - changes the strength by the amount passed through it the method.

- getWeight - int - returns the weight of the amount of stuff the player is carrying.

- setWeight -  changes the player's weight to the new weight that is passed through to it.

- adjustWeight - changes the weight by the amount passed through it the method.

- adjustFood -  changes the the amount of food the player has by the value passed through.

- getFood - int - returns the amount of food the player currently has.

- adjustDrink - changes the amount of drink the player has by the value passed through.

- getDrink - int - returns the amount of drink the player currently has.

- setPanelFlag - Sets panelFlag by the value passed through: 0 - normal, 2 - Lightning, 3 - message. 

- getPanelFlag - int - returns the current value of the panelFlag.

- setSwimming - sets the value of swimming to the current room the player is in.

- adjustPosition - changes the swimming position of the player by one.

- resetPosition - returns the position of the player to the beginning of the swim.

- checkPosition - Checks the position of the player and returns a boolean. Used to determine whether the
		  player has successfully escaped the poisoned waters.

**CommandProcess**

This class is designed to process the player's commands. So, it takes it, splits it, encodes it, and then sends it to 
where it needs to go, if the command is valid. If it isn't then the class will advise the player that the command is invalid
(and sort of give a reason why).

- splitCommand - Array of Two Strings - Stores the verb and the noun. Is created with blank strings.
- commands - Array of strings - Ideally this will have only two strings (though it might be more).
- originalCommand - string - the is the command that the player entered.
- verbNo - int - This is the number of the matching verb in the data (if there is one).
- nounNo - int - This is the number of the matching noun (if there is one).
- codedCommand - String - this is a code designed to make checking multiple variables easier. It consists of
		(in this order); noun number, noun location, noun flag, player location.
- nounNumber - int - this is for holding a second noun, such as giving something to somebody
- game - Game - this is the game object (as identified above).
- player - Player - this is the player object (as identified above).
- loadedGame - boolean - a flag that is set to advise the engine as to whether a game has been loaded.
- rand - Random - a class used to select a random number.

The constructor takes the the command (a string) and the game object. This is where the command is processed to determine where to send it.
Firstly it is made all lower case, then split, and the original command is store in the appropriate variable. The constructor then checks
if there are more than two commands, and trims them, otherwise sets the response advising the player that two words are required.
The fix command method is also called to further process the command (see below).

There is a second constructors which, well, just creates a shell of the class for the give command.

- getGame - Game - 

- getPlayer - Player - 

- checkLoadedGame - boolean - 

- fixCommand - String - (private) - 

- getVerbNumber - int -

- getNounNumber - int - 

- getNounNum - int -

- codeCommand - String - 

- executeCommand -  

- postUpdates - 

- executeGive - 

- executeShelter - 

**Commands**

**Test**

### View ###

### Controller ###


-- Add the determinePanel method in GameEngine


