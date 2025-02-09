# Island of Secrets

This project is a modern translation of the game *Island of Secrets*, originally published as a BASIC listing in a book of the same name. Players would type the game into their computers to play it. However, the authors intentionally obfuscated the code to make it difficult for users to deduce the game’s solution, while still leaving the game playable. 

This approach came with challenges: errors often crept in during transcription, and the lack of comments or documentation made it hard for users to learn the tricks of the BASIC language. The code’s readability was also notoriously poor, adding to the difficulty.

- [Read the book on the Internet Archive](https://archive.org/details/island-of-secrets_202303)  
- [View the original BASIC version on GitHub](https://github.com/s3664099/basic_scripts/tree/master/Island%20of%20Secrets)

- Redo the classes using the same style as Command Process 

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

### Version 3 (v03)
- **Status**: Completed testing and are able to complete Game without errors or changing any variables.
- **Challenges**:
  - Numerous Issues with regards to being able to solve the game without help or knowing the code
  - Styling of command input and button need to be fixed
  - Errors produced when save game using more than one word

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

This class processes the player's commands, breaking them into components, encoding them, and determining their validity. If the command is invalid, the class provides feedback to the player, explaining why it couldn't be processed.

The class does not have an explicit constructor. Starting variables are hardcoded, and the `Player` object is initialized once when the game begins. This single instance remains active throughout the game.

#### **Methods**

- **`getDisplayRoom() → int`**  
  Retrieves the room number to display to the player.  

- **`updateDisplayRoom(int roomNumber) → int`**  
  Updates the room number displayed to the player. For example, if the player’s room is 20, this may set a random room to display.  

- **`getStatus() → String`**  
  Returns the player's current stats as a string summarizing strength and wisdom.  

- **`update()`**  
  Processes the passage of time for the player. Reduces the remaining time and adjusts strength based on the weight of items carried.

- **`getStrengthWisdom() → float`**  
  Calculates the sum of the player’s strength and wisdom.  

- **`getRoom() → int`**  
  Retrieves the room the player is currently in.  

- **`setRoom(int roomNumber)`**  
  Updates the player’s current room to the specified room.  

- **`getTimeDetails() → String`**  
  Formats the remaining time as a string for display purposes.  

- **`getTime() → int`**  
  Retrieves the remaining time as an integer.  

- **`reduceTime()`**  
  Reduces the remaining time by one unit.

- **`setTime(int newTime)`**  
  Updates the remaining time to the specified value.  

- **`getWisdom() → int`**  
  Retrieves the player’s current wisdom.  

- **`setWisdom(int newWisdom)`**  
  Updates the player’s wisdom to the specified value.  

- **`adjustWisdom(int adjustment)`**  
  Adjusts the player’s wisdom by the specified amount.  

- **`getStrength() → float`**  
  Retrieves the player’s current strength.  

- **`setStrength(float newStrength)`**  
  Updates the player’s strength to the specified value.  

- **`adjustStrength(float adjustment)`**  
  Adjusts the player’s strength by the specified amount.  

- **`getWeight() → int`**  
  Retrieves the total weight of items the player is carrying.  

- **`setWeight(int newWeight)`**  
  Updates the player’s weight to the specified value.  

- **`adjustWeight(int adjustment)`**  
  Adjusts the player’s weight by the specified amount.  

- **`adjustFood(int adjustment)`**  
  Adjusts the amount of food the player has by the specified value.  

- **`getFood() → int`**  
  Retrieves the amount of food the player currently has.  

- **`adjustDrink(int adjustment)`**  
  Adjusts the amount of drink the player has by the specified value.  

- **`getDrink() → int`**  
  Retrieves the amount of drink the player currently has.  

- **`setPanelFlag(int panelId)`**  
  Switches the displayed panel to the one specified by `panelId`.  
  The panel to display (`0`, `2`, or `3`).

- **`getPanelFlag() → int`**  
  Retrieves the current value of the `panelFlag`.  

- **`setSwimming()`**  
  Activates swimming mode, using the player’s current room as the starting point.

- **`adjustPosition()`**  
  Advances the player’s swimming position by one.

- **`resetPosition()`**  
  Resets the player’s swimming position to the start.

- **`checkPosition() → boolean`**  
  Checks if the player has reached the swimming target.  
  `true` if the target has been reached, `false` otherwise.

---

### **CommandProcess**

This class processes the player's commands, breaking them into components, encoding them, and determining their validity. If the command is invalid, the class provides feedback to the player, explaining why it couldn't be processed.

#### **Attributes**

- splitCommand (Array of Strings): Stores the verb and noun from the player's command, initialized as blank strings.
- commands (Array of Strings): Typically contains two elements but can hold more if the input is malformed.
- originalCommand (String): Stores the exact command entered by the player before processing.
- verbNo (int): The index of the verb that matches one from the game's predefined list.
- nounNo (int): The index of the noun that matches one from the game's predefined list.
- codedCommand (String): Encodes command details to simplify checks. The format includes:
  - Noun number  
  - Noun location  
  - Noun flag  
  - Player's current room
- nounNumber (int): Holds a secondary noun for commands like "give" (e.g., giving an item to someone).
- game (Game): The game object managing non-player-specific aspects of the game.
- player (Player): The player object representing the current player's stats and location.
- loadedGame (boolean); A flag indicating whether a saved game has been loaded.
- rand (Random): A utility for generating random numbers.

#### **Constructors**

1. **CommandProcess(String command, Game game)**  
   Processes the player's command:  
   - Converts it to lowercase.  
   - Splits it into components (verb and noun).  
   - Stores the original command.  
   - Checks if the command has more than two components and trims it.  
   - If invalid, sets a response advising that two words are required.  
   - Calls the private `fixCommand()` method to normalize certain inputs (e.g., abbreviations).

2. **CommandProcess()**  
   Creates an empty instance for special cases like handling "give" commands.

#### **Methods**

- **getGame() → Game**  
  Returns the current `Game` object.

- **getPlayer() → Player**  
  Returns the current `Player` object.

- **checkLoadedGame() → boolean**  
  Checks if a saved game has been loaded.

- **fixCommand() → String** *(private)*  
  Normalizes commands for processing. For example:  
  - Converts directional inputs like "n" to "north."  
  - Maps common abbreviations to full commands.

- **getVerbNumber() → int**  
  Matches the verb from the player's command to the predefined list and returns its index.

- **getNounNumber() → int**  
  Matches the noun from the player's command to the predefined list and returns its index.  
  - If only a verb is present, sets `nounNumber` to -1.  
  - If no match is found, sets `nounNumber` to the maximum value.

- **getNounNum() → int** *(private)*  
  A helper method used by `getNounNumber()` to locate the correct noun.

- **codeCommand() → String**  
  Encodes the command details into a single string for efficient processing.  
  The code includes:  
  - Noun index  
  - Noun location  
  - Noun flag  
  - Player's current room

- **executeCommand()**  
  Processes and executes the player's command:  
  - Creates a `Command` object based on the verb number.  
  - Executes the appropriate method within the `Command` object.  
  - Verifies whether the command requires one or two words and validates accordingly.  
  - Handles special cases like displaying saved games.

- **postUpdates()**  
  Handles game updates after processing the player's command:  
  - Applies automatic changes to the game state.  
  - Processes endgame events if triggered.

- **executeGive(String recipient)**  
  Handles the "give" command:  
  - Confirms the player entered a valid recipient.  
  - Verifies the recipient is present in the current location.  
  - Uses the encoded command to determine how the recipient reacts.  
  - If conditions are met, completes the "give" action; otherwise, refuses the request.

- **executeShelter(String location)**  
  Handles the "shelter" command:  
  - Moves the player to the specified shelter location.  
  - Resets the storm flag.

#### **Key Notes**

- This class ensures the command is valid before executing it.  
- Commands with specific responses ("give" or "shelter") have dedicated methods for additional processing.  
- The `codedCommand` string simplifies complex multi-variable checks, improving efficiency.  
- Feedback is provided when commands are invalid or incomplete.

---

### **Commands**

This class handles most game commands, excluding *give* and *shelter*, which require special handling. It is instantiated whenever a command is entered and is responsible for processing and executing that command.

#### **Attributes**

- verb (int): Holds the verb's number from the predefined list.
- noun (int): Holds the noun's number from the predefined list.
- code (String): Encodes the command details into a concise format.
- rand (Random): Generates random numbers for use in the game.
- command (String): Stores the full command entered by the player.
- game (Game): The game object representing the current game state.
- player (Player): The player object containing details about the player's stats and inventory.

#### **Constructor**

1. **Commands(int verb, int noun, String code, String command, Game game, Player player)**  
   Initializes the class with the given verb, noun, code, and command, linking it to the game and player objects.

#### **Methods**

- **getPlayer() → Player**  
  Returns the `Player` object. Used primarily for *save* and *load* functions.

- **getGame() → Game**  
  Returns the `Game` object. Used primarily for *save* and *load* functions.

#### **Command-Specific Methods**

#### **Movement Commands**
- **move()**  
  Moves the player between locations:  
  - Converts special directions (*in, out, up, down*) to standard directions.  
  - Checks for blocked paths and prevents movement if a direction is unavailable.  
  - Updates the player's location based on the direction (e.g., north decreases room number by 10, east increases by 1).  
  - Handles special events triggered by movement.


#### **Item Interaction Commands**
- **take()**  
  Moves an item from the current room to the player's inventory:  
  - Checks if the item is present in the room.  
  - Updates the game state for special events triggered by the action.  
  - Handles specific cases like consuming food or drink.

- **drop()**  
  Places an item from the player's inventory into the current room:  
  - Checks if the item is fragile and breaks it if applicable.  
  - Otherwise, places the item in the room.

- **eat()**  
  Consumes food from the player's inventory:  
  - Increases strength by 10 if the food is safe.  
  - If poisonous, applies appropriate penalties (e.g., reduces strength and wisdom).

- **drink()**  
  Similar to *eat()*, but for liquids.

- **ride()**  
  Sets a flag allowing the player to access specific locations, provided the correct item is available.

- **open()**  
  Handles the *open* command:  
  - Checks for required conditions (e.g., specific flags or items).  
  - Updates the game state if conditions are met; otherwise, displays an error message.

- **fill()**  
  Fills a specific item (e.g., an Earthware jug) at a designated location.

#### **Combat Commands**
- **kill()**  
  Executes the *kill* command:  
  - Checks if the target is present in the room.  
  - Produces an appropriate response based on the game state.

- **attack()**  
  Executes the *attack* command:  
  - Checks the target noun and its presence in the room.  
  - Triggers specific events for certain targets, including endgame conditions.

#### **Other Commands**
- **chip()**  
  A multipurpose command (*tap, break, chip, chop*):  
  - Executes specific actions based on the settings.  
  - Displays an error message if conditions aren't met.

- **swim()**  
  Moves the player to a designated swimming area, provided conditions are met.

- **shelter()**  
  Checks the relevant flags and prompts the player to select a shelter location.

- **help()**  
  Provides help to another character rather than asking for game hints:  
  - Produces context-specific responses based on game flags.

- **polish()**  
  Advances the game in specific locations where polishing is required.

- **examine()**  
  Examines an object:  
  - Provides a hint if conditions are met.  
  - Displays an error message otherwise.

- **say()**  
  Executes the *say* command:  
  - Takes everything after *say* as the input.  
  - Advances the game if the correct phrase is spoken in the right location.  
  - Otherwise, repeats the phrase.

- **rest()**  
  Causes the player to rest for a random duration:  
  - Increases strength and wisdom proportionally to the time spent resting.

- **wave()**  
  Executes the *wave* command:  
  - Produces one of two outcomes: advancing the game or triggering a response from the boatman.

- **info()**  
  Displays the player's inventory.
  
#### **File Management Commands**
- **save(String filename)**  
  Saves the current game state to a file:  
  - Checks if the file already exists.  
  - Advises the player on how to overwrite an existing file.

- **load(String filename)**  
  Loads a saved game state from a file if it exists.

- **display()**  
  Displays a list of saved game files.

#### **Game Termination Command**
- **quit()**  
  Ends the game session.

### **Key Notes**

- The class supports a wide range of commands, each tied to specific in-game actions or responses.  
- Commands are validated before execution, ensuring proper feedback for invalid inputs.  
- Special handling for commands like *give*, *shelter*, and *say* ensures context-sensitive outcomes.  
- The design supports flexibility for future expansion or additional commands.

---

### **Test**

The **Test** class is designed specifically to assist with testing the game. It provides two primary methods: one for setting up variables at the start of the game and another for displaying values during runtime to validate game logic.

#### **Methods**

- **setTest(Game game, Player player)**  
  This method initializes the game and player objects with predefined values to simplify testing.  
  - Used to bypass certain setups or advance the game state directly for debugging purposes.

- **displayValue(Game game, Player player)**  
  Displays key variables from the game and player objects.  
  - Useful for monitoring the state of the game during runtime and verifying that changes are applied as expected.  
  - Ideal for debugging issues related to gameplay mechanics or player actions.

#### **Purpose**

The **Test** class is intended as a development tool, streamlining the debugging process by:  
- Reducing the need for manual setup during repetitive tests.  
- Providing a quick way to monitor and validate game states, player attributes, and other key variables.  

This class is not intended for use in the final release version of the game.

---

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

This class is to place a panel that will flash when the 'lightning flashes' function is called.

This contains the following attributes:

- game GamePanel - the main panel for holding the game engine and running the game
- label Jlabel - A class that will display a text label on a JFrame
- engine GameEngine - the class that runs the game
- long serialVersonUID - not sure, but this was added automatically
- int number - this defines the number of time the display runs.

The constructor takes the starting number, the game and the engine and sets up the panel text before starting the effect by calling the method.

- **startLightningEffect**
  This method sets a time and then will check the number to see whether the background will be yellow or back, and sets it. The number will be increased and if it is the it will end it.
  
 - **resetPanel(GamePanel)**
   This method resents the panel back to the initial panel that runs the game engine.

**MessagePanel**

This class is used to display special messages that have their own panel. This is based on the original game where some messages would be displayed immediately. This overwrites the standard panel to display the message for a period before returning to the standard GamePanel.

- long serialVersonUID - not sure, but this was added automatically
- label Jlabel - A class that will display a text label on a JFrame
- game GamePanel - the main panel for holding the game engine and running the game
- engine GameEngine - the class that runs the game

The constructor takes the game panel, enginger, and details of the messages. It will set up the messages (and split them so if there are multiple ones they will be displayed in order). The messages are passed through with a pipe '|' to tell where they should be split. One set up the sequence is started.

- **createLabel(String) → JPanel**
  This method sets up the label with the message that will be displayed
  
- **startSequence(int,String,String)**
  This method creates a thread and sets the delay to two seconds, which is the time it takes for the next message to be displayed. After that it will check to see if there are anymore messages. If there are none, then it will wait for 5 seconds instead if two, and then return to the origin panel. Otherwise the panel will be set with a new panel with the next part of the message.
  
- **setPanel(JPanel,JPanel)**
  This method basically sets the new panel.

### Controller ###

## **CommandListener**

### **Overview**

The `CommandListener` class is part of the **Controller** package and implements the `KeyListener` interface. It is responsible for handling user input via a `JTextField` and processing commands entered into the text field. This class connects the **View** (`GamePanel`) and the **Model** (`GameEngine`) by ensuring commands are properly forwarded to the game logic and updates are reflected in the interface.

### **Key Responsibilities**

1. **Captures Player Input**  
   - Listens for key presses, specifically the `Enter` key, and retrieves the text input entered by the player.

2. **Delegates Command Processing**  
   - Directs commands to the appropriate method in `GameEngine` based on the current `responseType`.

3. **Integrates with View and Model**  
   - Uses the `GamePanel` for displaying results and `GameEngine` for executing game logic.

### **Class Structure**

#### **Instance Variables**

- `JTextField text`: Holds the reference to the input field for player commands.
- `GameEngine game`: Represents the core game logic.
- `GamePanel gamePanel`: Represents the UI component for displaying game output.

### **Constructor**

- Initializes the `CommandListener` with references to the `JTextField`, `GameEngine`, and `GamePanel`.
- Ensures that the necessary components for user input, game logic, and UI updates are properly connected.

### **Methods**

- **`keyPressed(KeyEvent evt)`**
  Captures the event when a key is pressed.
  Checks if the `Enter` key is pressed (`KeyEvent.VK_ENTER`):
  1. Retrieves the command text from the input field.
  2. Clears the text field for new input.
  3. Routes the command to one of the following methods in `GameEngine`:
   - `processGive` (when `responseType == 1`).
   - `processShelter` (when `responseType == 2`).
   - `processCommand` (default behavior for other commands).

#### **`keyReleased(KeyEvent evt)`**
- Not implemented. Required for KeyListener.

#### **`keyTyped(KeyEvent evt)`**
- Not implemented. Required for KeyListener.

## **QuitButton**

### **Overview**
The `QuitButton` class is part of the `Controller` package and is responsible for handling the quit action in the game. It listens for the "quit" button press and performs the necessary operation to close the game window. This class ensures a seamless user experience when exiting the game by disposing of the main game frame (`GameFrame`).

### **Key Responsibilities**
1. Listens for user interaction with the "Quit" button.
2. Disposes of the `GameFrame` instance to close the game window.
3. Simple and lightweight design for ease of use.

### **Constructor**

- Stores the reference to the `GameFrame` to dispose of it upon triggering the quit action.

### ** Methods**
- **actionPerformed(ActionEvent arg0)**
- Executes when the quit button is pressed. It closes the game window by calling the `dispose()` method on the `GameFrame`.

## Image Acknowledgements ##
forest <a href="https://www.flaticon.com/free-icons/forest" title="forest icons">Forest icons created by Smashicons - Flaticon</a>
eviltree <a href="https://www.flaticon.com/free-icons/spooky" title="spooky icons">Spooky icons created by Graphiverse - Flaticon</a>
pods <a href="https://www.flaticon.com/free-icons/vaping" title="vaping icons">Vaping icons created by Freepik - Flaticon</a>
cliff <a href="https://www.flaticon.com/free-icons/cliff" title="cliff icons">Cliff icons created by Freepik - Flaticon</a>
factory <a href="https://www.flaticon.com/free-icons/factory" title="factory icons">Factory icons created by Eucalyp - Flaticon</a>
vat <a href="https://www.flaticon.com/free-icons/cauldron" title="cauldron icons">Cauldron icons created by Triangle Squad - Flaticon</a>
battlement <a href="https://www.flaticon.com/free-icons/battlement" title="battlement icons">Battlement icons created by ppangman - Flaticon</a>
sanctum <a href="https://www.flaticon.com/free-icons/control-room" title="control room icons">Control room icons created by IYIKON - Flaticon</a>
cave <a href="https://www.flaticon.com/free-icons/cave" title="cave icons">Cave icons created by Freepik - Flaticon</a>
bush <a href="https://www.flaticon.com/free-icons/bushes" title="bushes icons">Bushes icons created by Freepik - Flaticon</a>
stone <a href="https://www.flaticon.com/free-icons/mineral" title="mineral icons">Mineral icons created by Freepik - Flaticon</a>
cloud <a href="https://www.flaticon.com/free-icons/cloud" title="cloud icons">Cloud icons created by Freepik - Flaticon</a>
paddock - <a href="https://www.flaticon.com/free-icons/paddock" title="paddock icons">Paddock icons created by Nikita Golubev - Flaticon</a>
well - <a href="https://www.flaticon.com/free-icons/water-well" title="water well icons">Water well icons created by Hasymi - Flaticon</a>
hands - <a href="https://www.flaticon.com/free-icons/grab" title="grab icons">Grab icons created by Freepik - Flaticon</a>
room - <a href="https://www.flaticon.com/free-icons/room" title="room icons">Room icons created by Iconjam - Flaticon</a>
creek - <a href="https://www.flaticon.com/free-icons/creek" title="creek icons">Creek icons created by Freepik - Flaticon</a>
bridge - <a href="https://www.flaticon.com/free-icons/bridge" title="bridge icons">Bridge icons created by photo3idea_studio - Flaticon</a>
dune - <a href="https://www.flaticon.com/free-icons/landscape" title="landscape icons">Landscape icons created by Freepik - Flaticon</a>
here - <a href="https://www.flaticon.com/free-icons/exclamation-mark" title="exclamation mark icons">Exclamation mark icons created by Creatype - Flaticon</a>
archway - <a href="https://www.flaticon.com/free-icons/entrance" title="entrance icons">Entrance icons created by VectorPortal - Flaticon</a>
hut - <a href="https://www.flaticon.com/free-icons/hut" title="hut icons">Hut icons created by Freepik - Flaticon</a>
table - <a href="https://www.flaticon.com/free-icons/table" title="table icons">Table icons created by RaftelDesign - Flaticon</a>
nest - <a href="https://www.flaticon.com/free-icons/nest" title="nest icons">Nest icons created by Freepik - Flaticon</a>
castle - <a href="https://www.flaticon.com/free-icons/castle" title="castle icons">Castle icons created by Freepik - Flaticon</a>
bones - <a href="https://www.flaticon.com/free-icons/bone" title="bone icons">Bone icons created by Freepik - Flaticon</a>
bookshelf - <a href="https://www.flaticon.com/free-icons/bookshelf" title="bookshelf icons">Bookshelf icons created by Freepik - Flaticon</a>
hill - <a href="https://www.flaticon.com/free-icons/hills" title="hills icons">Hills icons created by mnauliady - Flaticon</a>
stonehenge - <a href="https://www.flaticon.com/free-icons/stonehenge" title="stonehenge icons">Stonehenge icons created by Freepik - Flaticon</a>
pyramid - <a href="https://www.flaticon.com/free-icons/landmark" title="landmark icons">Landmark icons created by Eucalyp - Flaticon</a>

