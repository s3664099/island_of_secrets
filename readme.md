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

### Data

This package contains classes designed primarily to carry data. Two key classes, **Location** and **Item**, serve as the backbone of the game, holding the objects and locations that drive the gameplay.

---

#### **Constants**

The `Constants` class stores immutable variables used throughout the game. These variables prevent the need for hardcoding numbers into the game logic (although there may still be some lingering instances). 

- **Purpose**: Provides a centralized location for unchanging values.  
- **Key Features**:
  - Variables are declared as `final` (immutable) and `static` (accessible without instantiating the class).
  - The class is accessible across the entire program, ensuring consistent reference to these constants.

---

#### **Raw Data**

The `RawData` class holds the game's foundational data, such as item details, locations, verbs, and nouns. While this data could be stored in a database, the simplicity and portability of file-based storage make it the preferred choice here.  

- **Purpose**: Stores the game's raw, unchanging data, read once during the setup phase.  
- **Key Features**:
  - Data is read at the start of the game and used to initialize locations, items, and other elements.
  - The class is `static`, so its contents can be accessed without creating an instance.
  - Eliminates the need for a database, making the game easier to distribute and run.

---

#### **Location**

The `Location` class manages details about the game's various locations.  

- **Variables**:
  - `name` (`String`): The name of the location.  
  - `exits` (`boolean[]`): An array of four booleans indicating the presence of exits in the cardinal directions.  
  - `visited` (`boolean`): Tracks whether the player has visited this location (planned for map functionality).  
  - `roomType` (`int`): Represents the type of room, used to display an icon on the map.

- **Key Features**:
  - Only the `visited` variable changes during gameplay, transitioning from `false` to `true` when a player enters a location.
  - Includes methods to:
    - Retrieve the name of the location.
    - Access the array of exits.

- **Constructor**: Initializes the location's name and exit configuration.

---

#### **Item**

The `Item` class encapsulates the details of in-game items.  

- **Variables**:
  - `itemFlag` (`int`): A flag determining item visibility and status. Items with a flag less than `1` are visible to the player.  
  - `itemLocation` (`int`): Indicates where the item is located:
    - `0`: The player is carrying the item.
    - `81`: The item has been destroyed.
  - `item` (`String`): The item's name.

- **Key Features**:
  - Constructor initializes the item's flag, location, and name.  
  - Methods include:
    - Getters and setters for the item's flag and location.
    - A method to retrieve the item's name.
    - A method to compare the item's location with a given integer, returning `true` or `false` based on the match.

---

This structure ensures that both `Location` and `Item` classes serve as efficient containers for data, with minimal but effective methods for retrieving and manipulating that data where necessary.

### Model ###

The **Model** package serves as the core of the game, containing the primary logic and data structures. It connects the game's data, operations, and presentation layers, aligning with the Model-View-Controller (MVC) design pattern. While I primarily use this pattern in my personal projects, it has proven reliable, even when working with legacy systems that predate widespread adoption of design patterns.

#### **Main**

The `Main` class is responsible for initializing and launching the game. While the `Start` class fires up the application, `Main` handles the setup and execution of the game.

- **Workflow**:
  1. Creates a `Game` object, which holds all the game's data.
  2. Creates a `Player` object, which contains player-specific data (though some overlap with the `Game` object exists).
  3. Instantiates a `GameEngine` object, which drives the game's logic.
  4. Uses `SwingUtilities` to launch a GUI-based version of the game.

#### **Game Engine**

The `GameEngine` class bridges the `Game` and `Player` objects with the View and Controller components. It serves as the primary logic layer, managing gameplay flow and interaction.

##### Key Methods:

- **`getTime()`**  
  Retrieves the number of moves remaining, representing the time left in the game.

- **`getStatus()`**  
  Returns the player's current strength and wisdom as a formatted string.

- **`getRoom()`**  
  Retrieves the name of the current room or provides special descriptions, such as for "poisoned waters."  
  - Checks for special conditions like poisoned waters.  
  - Retrieves and formats the room description from the `Game` object.

- **`getItems()`**  
  Lists visible items in the current room as a formatted string.

- **`getExits()`**  
  Displays available exits in the current room.  
  - Note: The original game didn’t show exits, but they have been added for clarity.

- **`getSpecialExits()`**  
  Displays any special exits for the current room.

- **`getMessage()`**  
  Retrieves the message for the player's previous move.  
  - Includes special warnings if the player is in poisoned waters, such as low strength warnings.

- **`getCommands()`**  
  Retrieves the last three commands entered by the player for display in the UI.

- **`processCommand(String command)`**  
  Processes player input and executes game logic.  
  - Adds the command to the recent command list.  
  - Determines the current panel (e.g., normal gameplay or special scenarios like poisoned waters).  
  - Parses the command to determine the verb and noun.  
  - Executes the command and updates game state.  
  - Handles special scenarios, such as loading a game or navigating poisoned waters.  
  - Determines the appropriate panel to display after command execution.

- **`getResponseType()`**  
  Determines the type of response expected:  
  - Normal command processing.  
  - `Give` item response.  
  - `Shelter` response.

- **`processGive(String input)`**  
  Handles the "give" command.  
  - Validates the input to ensure it refers to an NPC present in the current room.  
  - If the input is invalid, it generates an error response.  
  - Resets the response type to normal after processing.

- **`processShelter(String input)`**  
  Manages the shelter command, requiring the player to choose one of three options.  
  - If the input is invalid, an error is returned.  
  - Resets the response type to normal after processing.

- **`setPanel(int panelId)`**  
  Switches the displayed panel to the one specified by `panelId`.

- **`resetPanel()`**  
  Refreshes the current game panel to update the display.  
  - Note: After updating, the cursor does not return to the input field.

- **`checkEndGame()`**  
  Checks if the `endGame` flag has been triggered in the `Game` object.

- **`getFinalScore()`**  
  Calculates the player's final score at the end of the game.

- **`determinePanel()`**  
  Determines which panel should display after processing the player's command:  
  - `2`: Lightning panel.  
  - `3`: Message panel.  
  - Otherwise: Normal gameplay panel.

---

The **GameEngine** class is the game's powerhouse, managing both logic and interaction seamlessly. It allows the game to dynamically adapt to player input and ensures a smooth gameplay experience.

### **Game**

The `Game` class is responsible for storing and managing all game-related data that is not strictly player-specific (though it does include some player-related elements, such as item locations). It plays a central role in maintaining the state of the game and provides essential methods for interacting with locations, items, and game messages. Below is a detailed breakdown of its attributes and methods:

- **Variables**
  - `noRooms` ('int'): The total number of rooms in the game, derived from a constant.
  - `noItems` ('int'): The total number of items in the game, also derived from a constant.
  - `locationList` (Array of `Location` objects): Contains all the locations in the game.
  - `itemList` (Array of `Item` objects): Contains all the items in the game.
  - `message` ('String'): Stores the response to the player’s most recent action.
  - `commands` (Array of Strings): Keeps track of the player’s last three commands.
  - `rand` (Random): A Java `Random` object used for generating random numbers.
  - `panelMessageOne` (String): The first message displayed on the special message panel.
  - `panelMessageTwo` (String): The second message displayed on the special message panel.
  - `panelLoop` (int): Specifies how many times the special message panel will loop.
  - `endGame` (boolean): A flag indicating whether the game has ended.
  - `saveGameCount` (int): Tracks the number of save games currently available.
  - `responseRequired` (int): Indicates the type of response required from the player. 
  
#### **Constructor**

The constructor initializes the game state when it starts. It:
- Creates and populates the list of `Location` and `Item` objects by iterating through the `RawData` class.
- Sets initial values for attributes, ensuring the game is ready to run.

---

#### **Key Methods**

- **`getRoomName(int)`**  
  Returns the name of the room corresponding to the given room number.

- **`getItems(int)`**  
  Retrieves a string listing the names of all items in the specified room.  

- **`getExits(int)`**  
  Returns the exits available for the given room, excluding special exits.  

- **`getSpecialExits(int)`**  
  Returns the special exits for the specified room, if any.  

- **`checkExit(int)`**  
  Checks whether movement in a specified direction is possible.  

- **`addExit(String)`** *(private)*  
  Appends an exit to the list of exits, adding a comma if necessary.  

- **`getMessage()`**  
  Retrieves the stored message for the player's previous action.  

- **`clearMessage()`**  
  Clears the current stored message.  

- **`setMessageGame(String)`**  
  Sets the message to describe the outcome of a player's action.  

- **`addMessage(String)`**  
  Appends a string to the current message if it isn’t blank; otherwise, sets a new message.  

- **`getCommand(int)`**  
  Retrieves a command from the history by index.  

- **`getItem(int)`**  
  Returns the `Item` object corresponding to the specified item number.  

- **`getItemFlagSum(int)`**  
  Calculates and returns the sum of the flag and location for a given item.  

- **`setPanelMessages(String)`**  
  Updates the special message panel with new messages and loop count.  

- **`getMsgOne(String)`**  
  Retrieves the first special panel message.  

- **`getMsgTwo(String)`**  
  Retrieves the second special panel message.  

- **`getLoop()`**  
  Retrieves the loop count for the special panel messages.  

- **`endGame()`**  
  Sets the `endGame` flag to true, signaling that the game has ended.  

- **`checkEndGame()`**  
  - Returns the value of the `endGame` flag.  

- **`getCount()`**  
  Returns the number of save games currently on file.  

- **`setCount()`**  
  Increments the save game count by one.  

- **`resetCount()`**  
  Resets the save game count to zero.  

- **`setResponse(int)`**  
  Sets the type of response required from the player.  
  - Input Response type *(int)*:
    - `0`: Standard response.
    - `1`: "Give" response.
    - `2`: "Shelter" response.  

- **`getResponse(int)`**  
  Retrieves the current response type.  
  
---


### **Player**

The `Player` class represents all player-specific details, including the player's location, stats, and gameplay-related variables. This class is instantiated once and persists throughout the entire game. Below is a breakdown of its attributes and methods.

#### **Attributes**

- `room` (int): The room number where the player is currently located.  
- `roomToDisplay` (int): The room number displayed to the player, which may differ from the actual room.  
- `strength` (float): The player’s current strength.  
- `wisdom` (int): The player’s current wisdom.  
- `timeRemaining` (int): The remaining time in the game.  
- `weight` (int): The total weight of items the player is carrying.  
- `food` (int): The amount of food the player has.  
- `drink` (int): The amount of drink the player has.  
- `rand` (Random): A `Random` object for generating random numbers.  
- `panelFlag` (int): Indicates the panel to display after a command is processed:
  - `0`: Normal panel.  
  - `2`: Lightning panel.  
  - `3`: Message panel.  
- `swimming` (int): Tracks if the player is swimming in poisoned waters.  
- `swimPosition` (int): The player’s current position while swimming.  
- `swimTarget` (int): The target position the player must reach while swimming.

#### **Constructor**

The class does not have an explicit constructor. Starting variables are hardcoded, and the `Player` object is initialized once when the game begins. This single instance remains active throughout the game.

#### **Methods**

- **`getDisplayRoom()`**  
  Retrieves the room number to display to the player.  

- **`updateDisplayRoom(int roomNumber)`**  
  Updates the room number displayed to the player. For example, if the player’s room is 20, this may set a random room to display.  

- **`getStatus()`**  
  Returns the player's current stats as a string summarizing strength and wisdom.  

- **`update()`**  
  Processes the passage of time for the player. Reduces the remaining time and adjusts strength based on the weight of items carried.

- **`getStrengthWisdom()`**  
  Calculates the sum of the player’s strength and wisdom.  

- **`getRoom()`**  
  Retrieves the room the player is currently in.  

- **`setRoom(int roomNumber)`**  
  Updates the player’s current room to the specified room.  

- **`getTimeDetails()`**  
  Formats the remaining time as a string for display purposes.  

- **`getTime()`**  
  Retrieves the remaining time as an integer.  

- **`reduceTime()`**  
  Reduces the remaining time by one unit.

- **`setTime(int newTime)`**  
  Updates the remaining time to the specified value.  

- **`getWisdom()`**  
  Retrieves the player’s current wisdom.  

- **`setWisdom(int newWisdom)`**  
  Updates the player’s wisdom to the specified value.  

- **`adjustWisdom(int adjustment)`**  
  Adjusts the player’s wisdom by the specified amount.  

- **`getStrength()`**  
  Retrieves the player’s current strength.  

- **`setStrength(float newStrength)`**  
  Updates the player’s strength to the specified value.  

- **`adjustStrength(float adjustment)`**  
  Adjusts the player’s strength by the specified amount.  

- **`getWeight()`**  
  Retrieves the total weight of items the player is carrying.  

- **`setWeight(int newWeight)`**  
  Updates the player’s weight to the specified value.  

- **`adjustWeight(int adjustment)`**  
  Adjusts the player’s weight by the specified amount.  

- **`adjustFood(int adjustment)`**  
  Adjusts the amount of food the player has by the specified value.  

- **`getFood()`**  
  Retrieves the amount of food the player currently has.  

- **`adjustDrink(int adjustment)`**  
  Adjusts the amount of drink the player has by the specified value.  

- **`getDrink()`**  
  Retrieves the amount of drink the player currently has.  

- **`setPanelFlag(int panelId)`**  
  Switches the displayed panel to the one specified by `panelId`.  
  The panel to display (`0`, `2`, or `3`).

- **`getPanelFlag()`**  
  Retrieves the current value of the `panelFlag`.  

- **`setSwimming()`**  
  Activates swimming mode, using the player’s current room as the starting point.

- **`adjustPosition()`**  
  Advances the player’s swimming position by one.

- **`resetPosition()`**  
  Resets the player’s swimming position to the start.

- **`checkPosition()`**  
  Checks if the player has reached the swimming target.  
  `true` if the target has been reached, `false` otherwise.

---


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

This class is used primarily for testing and has two methods, one for setting variables when the game starts and one for displaying values when the game is running.

- setTest - this takes the game and player object and will set varibales in it to make testing easier.

- displayValue - this also takes the game and player objects and are used to display variables in them.

### View ###

**GameFrame**
This sets up the frame for the GUI. It sets the title at the top, sets the exit, builds the GamePabel, sets the bounds, and sets it visible.

**GamePanel**
This class creates the main panel for running the game. The variables are as follows:

- Color - sets the backgroun color.
- GameEngine - holds the GameEngine object.
- GameFrame - holds the GameFrame object.

The constructor takes the game engine and the frame and adds the engine and sets the Frame to the game Frame.

- add - Takes the GameEngine object and sets up the panel. It sets the layout as a BorderLayout, and then creates the topPanel, middlePanel, and bottemPanel, which are all JPanels. All these panels use a GridLayout. The top panel add a status Panel. The middlePanel details the room, and will display the room name in a LabelPanel. The items are then loaded, spilt so that all will be displayed (with a max of all items) and displayed in LabelPanels. The same is done for the exits, and then the message is displayed. Finally, the End Game flag is checked, and if it is true will display the final score. The bottonPanel displays the last three commands, and then will either display a text field or an exit button if the end game flag is true.

- addPanel - this takes a JPanel and adds it to the frame.

- CreatePanel - this method takes an int which outlines the flow type. A FlowLayout is created with centre being standard, but if the type is 1 or 2 it is then left.

- CreateLabelPanel - This takes the labelString and the flowType. The panel is created based on the flowType, the label is created and added to the panel. The final panel is returned.

- getLineLength - takes the string which is the line to be divided. If the length is greater than a certain number, it will then go back and find the previous white space, and then return an int where the line will be cut.

**LightningPanel**

**MessagePanel**

### Controller ###

**CommandListener**

**QuitButton**


