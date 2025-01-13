# Island of Secrets

This project is a modern translation of the game *Island of Secrets*, originally published as a BASIC listing in a book of the same name. Players would type the game into their computers to play it. However, the authors intentionally obfuscated the code to make it difficult for users to deduce the game’s solution, while still leaving the game playable. 

This approach came with challenges: errors often crept in during transcription, and the lack of comments or documentation made it hard for users to learn the tricks of the BASIC language. The code’s readability was also notoriously poor, adding to the difficulty.

- [Read the book on the Internet Archive](https://archive.org/details/island-of-secrets_202303)  
- [View the original BASIC version on GitHub](https://github.com/s3664099/basic_scripts/tree/master/Island%20of%20Secrets)

## Notes on the Basic Code

### Reading the Data
Unlike many other games, this program doesn't load all data into memory at once, likely due to memory constraints of early computers. Instead, it reads one item at a time, which slows down the game as data must be read repeatedly.  

- **Line 2780**: Repositions the pointer to the beginning of the data and iterates through all locations, discarding each one.  
- **Line 4110**: Iterates through and discards all objects.

Object locations are stored in a string `H$`, where ASCII symbols represent specific locations. The next line uses the string `Q$` as a flag for each object, influencing gameplay throughout.

The game operates on a 10x10 grid, with movement between nodes determined by:
- Adding or subtracting **1** (east or west movement)
- Adding or subtracting **10** (north or south movement)

### Documentation
A supplementary file contains detailed notes on:
- The BASIC code sections and their functions
- Variables, items, locations, verbs, and nouns

## Executing the Game
To compile and run the game, follow these steps:

1. Compile the Java files:
   ```bash
   javac *.java
   ```
2. Run the game:
   ```bash
   java Start
   ```

## Development Notes ##

### Version 1 (v01)
- **Status**: The game works but has some issues. 
- **Goals**:
  - Upgrade the game for better readability.
  - Transition from a command-line interface to a windowed environment for smoother gameplay.
  - Refine and organize the notes file.

**2 November 2024**:  
- Completed initialization classes.
- Segregated locations and objects into dedicated methods within the game class.  
- Adopted full words for commands instead of truncating to the first three letters, a practice originally used for simplicity on older systems. Commands are stored in immutable arrays.

### Version 2 (v02)
- **Status**: Transitioned to a Swing-based UI.  
- **Challenges**:
  - Styling issues need resolution.
  - Extensive testing required to ensure functionality.
  - Aim to reuse the structure for future similar games.

**29 December 2024**:  
- Added a dedicated testing class:
  - Allows setting variables before the game starts.
  - Includes methods to output details to the console for debugging.

**30 December 2024**:  
- Enhanced store room descriptions to include a door to the east and a trapdoor.  
- Added detailed descriptions for special directions, improving clarity compared to earlier iterations where these were missing.

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

- determinePanel - this determine what panel will display after the command processing is complete.
    		   2 - the lightning panel
    		   3 - the message panle
    		   Otherwise just a normal panel
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

- getGame - Game - returns the current version of the game object.

- getPlayer - Player - returns the current version of the player object

- checkLoadedGame - boolean - returns a boolean to advise whether a game has been loaded or not

- fixCommand - String - (private) - The method is designed to convert commands into commands that can be read by the programe.
				    The main ones are for directions, such as n,s,e,w, as well as some other specific ones.

- getVerbNumber - int - This method will compare the verb entered with the list of verbs and will return the number the 
			corresponding verb that is on the list.

- getNounNumber - int - This method compares the noun entered with a list of nouns, and returns the number of the
			corresponding noun on the list. It sets the number as the total number of nouns, however
			if there is only a verb, then the nounNumber is set to -1. If the corresponding noun cannot be
			found, then it is set to the max number.

- getNounNum - int - (private) - this private method is called from getNounNumber and is where the correct noun number
			is located, and is only called if there is a noun.

- codeCommand - String - This method is where the code is commanded, and allows for multiple checks, though I suspect
			 this was mainly for the computers with limited memory at the time (though it is a nice short-cut
			 for where there are multiple areas where all the same variables are being checked). The variables
			 in order at the nounNumber, the location, the flag, and the room the player is in.

- executeCommand - This is the method where the command is executed. First a command object is created and then the
		   method checks the verbNumber with the corresponding command and executes the method in the command object.
		   The first 11 commands are only one word, and the remainder require two words (and this is checked to
		   make sure that only commands with two words go there. There is also a section that resets a count
		   and this is for the display command which displays a list of saved games.

- postUpdates - This method is executed after the commands have been processed and handles any updates to the game
		that occur after the command has been executed, whether the player has intervened or not. The last
		section of this method deals with the end game processes.

- executeGive - This is a special method since the player is asked who they wish to give the item to, at which point this
		method is called instead of the standard command process method. Initially the method checks that something
		was entered, and also confirms whether the subject is present. The initial state is that the object is
		refused, but it then, using the codes, goes through each of the potential people that will respond and
		processes it that way. 

- executeShelter - Similarly to the above, but this is processing the shelter command. Basically the player is asked
		where they wish to shelter, and that are taken there. The storm flag is then reset.

**Commands**

This class is where all of the commands (except for give and shelter which require and extra entry) are processed. This
is created everytime a command is entered and processes the command. The variables are as follows:

- verb - int - holds the verb number.
- noun - int - holds the noun number.
- code - String - holds the command's code
- rand - an object that produces a random number.
- command - String - holds the command the player entered.
- game - Game - the game object.
- player - Player - the player object.

The constructor takes the verb and noun number, the code, and the command.

- getPlayer - Player - returns the player object. This is used for the load and save game functions.

- getGame - Game - returns the game object. This is used for the load and save game functions.

- move - This moves the player from one spot to another. The first section handles special directions (namely in, out,
	 up and down) and converts them to normal directions. The next section looks to see if there are any directions
	 that are blocked and prevents movement. If it isn't a listed blocked direction, the movement is handled by looking
	 if the direction is marked as available. As the rooms are set in a 10x10 square, north reduces the room by 10, south
	 increases by 10, east adds 1 and west subtracts 1. Finally, the game determines if the move results in a special
	 event.

- take - this method moves an item from the room into the player's inventory. It checks if the item is in the room the 
	 player is in, and if so takes it. Then the game handles any special events, and specific commands (such
	 as picking an apple. It handles the food and drink by removing the item and then increasing the player's
	 food and drink value.

- give - This involves the player giving an item to a specific person. The main function is elsewhere as it will
	 ask the player who they are giving the item to, however it will check if the player is currently
	 carrying the item.

- drop - This command puts an item in the player's inventory into the room the player is currently in. Initially checks
	 if the item is fragile (and the player has it). If it is, it breaks. Otherwise it checks if the player is
	 carrying the item, and if so puts it in the room the player is in.

- eat - This will have the player eat food, as long as the player has more than 0 food. It checks if it is poisonous
	and if it, it will reduce the food by 1 and increase strength by 10. If the food is poisonous then the 
	appropriate damage is done (less 2 strength and 5 wisdom).

- drink - This does the same as the eat, except that it does it for liquid.

- ride - This basically will set a flag that will allow the player to enter a location. The player needs to 
	have the correct item, and if so the flag will be set.

- open - Executes the open command, which is used twice in the game. The settings are checked and if they match
	 then the message is displayed and the changes are made. Otherwise the error message is displayed.

- chip - This is one of four commands - tap, break, chip, or chop. I originally had break but that is a reserved word so
	 I used chip instead. Like the other commands this check if the settings and the command is correct and if
	 so the command is executed. If the settings aren't correct then an error message is produced.

- kill - This executes the kill command, which checks if the subject is present, and then responds appropriately.

- attack - The attack command is executed. Differently the command checks which noun is being used, if the resultant
	   object (creature) is present, and responds appropriately. There is a special command at the end which is 
	   specifically for striking something that appears to produce an end game flag.

- swim - This command checks the player's location (and flag of specific item) and moves the player to the swimming
	 section.

- shelter - This checks if a flag is correct and then sets the response up to get a choice of the player to where
	 they wich to shelter.

- help - The help command is executed. Unlike a lot of other games, this is to help an individual as opposed to
	 asking the computer for help. Once again it checks the flags and produces a response based on it.

- polish - A command that is used in a couple of specific locations to advance the game.

- examine - Either sets an error message, or if the flags are correct, then it responds with a hint. Could be used better.

- fill - Used to fill the Earthwenware jug in a specific location.

- say - This is different to the normal verb-noun command. Instead everything after 'say' is what is said.
	If a specific phrase is said at a specific location then the game is advance. Otherwise the phrase is repeated.

- rest - Based on some factors, a random amount of time is spent resting, and for every period strength and wisdom are increased.

- wave - There are two events, one which advances the game, the other which is a response from the boatman.

- info - This displays the player's inventory.

- save - The status of the current game is written to file. The file name needs to be stated after save. It checks
	 if the filename has already been used and advises the player how to overwrite.

- load - The named file (if it exists) is loaded into the game.

- display - Displays a list of saved files.

- quit - Brings the game to an end.

**Test**

### View ###

### Controller ###



