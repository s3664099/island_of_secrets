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

**5 March 2025**
- Completed v4 (as listed below). Redoing the program structure by having ChatGPT write and and provide recommendations for changing the code.

### Version 3 (v03)
- **Status**: Completed testing and are able to complete Game without errors or changing any variables.
- **Challenges**:
  - Numerous Issues with regards to being able to solve the game without help or knowing the code
  - Styling of command input and button need to be fixed
  - Errors produced when save game using more than one word

### Version 4 (v04)
- **Status**: Completed Game
- **Additions**:
  - Map screen added that user can toggle between main game and map
  - Image folder added to hold images for the map
  - Linked images to locations
  - Added buttons to repeat previous commands (Last three)
  - Made it possible to execute give and shelter in single command
  - Added buttons for loading saved games and removed display command
  - Added a restart game button
  - Added extra sections to examine so book isn't relied upon
  - Added link to open up book in Internet Archive

## Program Structure ##

### Default Package

This package is only used to start the game, and only holds the class that launches the game.

## **Start**  

### **Overview**  
The `Start` class serves as the entry point for the application. It initializes the game and starts execution by invoking the main game logic.  

### **Key Responsibilities**  
1. Initializes the `Main` game object.  
2. Calls the `startGame()` method to begin the game.  

### **Constructor**  
- **`Start()`**  
  - The class does not define an explicit constructor since it only contains the `main` method.  

### **Methods**  
1. **`main(String[] args)`**  
   - Entry point of the application.  
   - Creates an instance of `Main` and starts the game by calling `startGame()`.

### Controller Package

The controller package is a part of the MVP pattern and holds all of the classes that execute the commands. This does not process the commands but are rather classes that are tied to buttons or text input boxes, and basically retrieve the action and interpretates it, and then sends it to the specific model class.

## **BookButton**  

### **Overview**  
The `BookButton` class is responsible for handling button actions that open an external webpage and update the game's display.  

### **Key Responsibilities**  
1. Opens a specified webpage in the user's default web browser.  
2. Updates the game display state.  
3. Changes the current game panel to a new one.  

### **Constructor**  
- **`BookButton(GameEngine game, GamePanel panel)`**  
  - Initializes the `game` and `panel` objects.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Checks if the system supports the `Desktop` class.  
   - Attempts to open a specific webpage (`https://archive.org/details/island-of-secrets_202303`) in the default web browser.  
   - Updates the game display state.  
   - Changes the game panel to a new one.  

### **Potential Flaws and Possible Changes**  
- **Lack of user feedback:** If an error occurs while opening the URL, the exception is only printed to the console. A user-friendly error message could be displayed instead.  
- **Platform dependency:** `Desktop.getDesktop()` is not supported on all platforms, especially in some Linux environments. Consider handling this case more gracefully.  
- **Hardcoded URL:** The URL is hardcoded, which reduces flexibility. It could be moved to a configuration file or passed as a parameter for easier modification.

## **CommandButton**  

### **Overview**  
The `CommandButton` class handles button actions that trigger a specific in-game command.  

### **Key Responsibilities**  
1. Stores a predefined command string.  
2. Processes the command when the button is clicked.  
3. Passes the game panel to the command processor.  

### **Constructor**  
- **`CommandButton(GameEngine game, GamePanel panel, String command)`**  
  - Initializes the game engine, game panel, and the command string.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Calls `game.processCommand(command, panel)`, triggering the command when the button is clicked.  

### **Potential Flaws and Possible Changes**  
- **Lack of command validation:** The command is passed directly to `processCommand()`, which could lead to unexpected behavior if it is invalid. Consider adding validation.  
- **Hardcoded command:** Since the command is set at instantiation, this button cannot change its behavior dynamically. A more flexible approach might involve user input or a mapping system.  
- **Potential for null values:** If the `command` string is `null`, it might cause errors in `processCommand()`. Consider adding a null check before execution.

## **CommandListener**  

### **Overview**  
The `CommandListener` class listens for keyboard input in a text field and processes the entered command when the Enter key is pressed.  

### **Key Responsibilities**  
1. Captures user input from a `JTextField`.  
2. Detects when the Enter key is pressed.  
3. Sends the command to the game engine for processing based on the game’s response type.  

### **Constructor**  
- **`CommandListener(JTextField text, GameEngine game, GamePanel gameFrame)`**  
  - Initializes the text field, game engine, and game panel.  

### **Methods**  
1. **`keyPressed(KeyEvent evt)`**  
   - Checks if the Enter key is pressed.  
   - Retrieves and clears the text field input.  
   - Calls the appropriate game processing method based on `game.getResponseType()`.  
2. **`keyReleased(KeyEvent arg0)`**  
   - Required by `KeyListener`, but not implemented.  
3. **`keyTyped(KeyEvent arg0)`**  
   - Required by `KeyListener`, but not implemented.  

### **Potential Flaws and Possible Changes**  
- **No validation on user input:** If the text field is empty, the command is still sent. Consider handling empty input.  
- **Tightly coupled to response types:** The logic for selecting the processing method is hardcoded. A more flexible approach might use a strategy pattern or command mapping.  
- **Hardcoded Enter key detection:** If different keybindings are needed in the future, consider allowing configurable key mappings.

## **GameButton**  

### **Overview**  
The `GameButton` class handles button click events to switch the game display and update the game panel.  

### **Key Responsibilities**  
1. Handle button press events.  
2. Hide the current game display.  
3. Update the game panel with a new panel instance.  

### **Constructor**  
- `GameButton(GameEngine game, GamePanel panel)`: Initializes the button with the game engine and the new game panel.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Called when the button is clicked.  
   - Hides the current game display and sets the new game panel.  

### **Potential Flaws and Possible Changes**  
- **Lack of UI feedback:** The class updates the panel but does not provide any visual confirmation to the user. Consider adding animations or messages.  
- **Limited functionality:** If additional behavior is needed (e.g., verifying conditions before switching the panel), this should be handled within `actionPerformed()`.  
- **Hardcoded behavior:** Always sets the game display to `false`. If future functionality requires toggling or conditional behavior, modifications may be needed.

## **LoadGameButton**  

### **Overview**  
The `LoadGameButton` class handles button click events to load a saved game by executing a load command.  

### **Key Responsibilities**  
1. Handle button press events.  
2. Extract the game name from the provided filename.  
3. Trigger the game's load functionality using the processed command.  

### **Constructor**  
- `LoadGameButton(GameEngine game, GamePanel panel, String gameName)`:  
  - Initializes the button with the game engine, panel, and game name.  
  - Strips the last four characters from `gameName` to remove a file extension.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Executes the `"load <gameName>"` command through the game engine when the button is clicked.  

### **Potential Flaws and Possible Changes**  
- **String Manipulation Assumption:** The constructor removes the last four characters from `gameName`, assuming all game files have a four-character extension (e.g., `.sav`). If this assumption is incorrect, it may produce unexpected behavior.  
- **Hardcoded Command Format:** Always executes `"load <gameName>"`, which may not be flexible enough if different loading mechanisms are required in the future.  
- **No Error Handling:** If the game name is too short (fewer than four characters), an `IndexOutOfBoundsException` could occur. Adding validation would improve robustness.  
- **Lack of UI Feedback:** No confirmation message is displayed to indicate whether the game was successfully loaded. Consider adding a message or visual indicator.

## **MapButton**  

### **Overview**  
The `MapButton` class handles user interaction with a button designed to display the map panel in the game interface.  

### **Key Responsibilities**  
1. Detect button clicks.  
2. Update the game engine to display the map panel.  

### **Constructor**  
- `MapButton(GameEngine game, GamePanel panel)`:  
  - Initializes the button with the game engine and the panel that represents the map.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Calls `setMapPanel(this.panel)` on the game engine to switch the display to the map panel.  

### **Potential Flaws and Possible Changes**  
- **Assumes Panel is a Map Panel:** The constructor accepts any `GamePanel` without verifying if it actually represents a map. If the wrong panel is passed, unexpected behavior may occur.  
- **Lack of Confirmation or Feedback:** The method changes the panel but does not provide any confirmation to the user. Consider adding a visual indicator or a log message.  
- **No Null Checks:** If `panel` or `game` is `null`, the program may crash. Adding a null check would improve robustness.  
- **Limited Functionality:** This button only switches to the map panel. If additional actions (such as refreshing the map) are needed, the method might need to be extended.

## **QuitButton**  

### **Overview**  
The `QuitButton` class handles user interaction with a button designed to either close the game or restart it, depending on the provided configuration.  

### **Key Responsibilities**  
1. Close the game window if restarting is not required.  
2. Restart the game with a new game instance if the restart flag is `true`.  
3. Update the game panel after restarting the game.  

### **Constructor**  
- `QuitButton(GameFrame frame, boolean restart, GameEngine game, GamePanel panel)`  
  - Initializes the button with the game frame, a flag to determine whether to restart the game, the game engine, and a panel to display after restarting.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - If `restart` is `false`, disposes of the game window.  
   - If `restart` is `true`, resets the game by creating a new `Game` and `Player` instance, then updates the panel.  

### **Potential Flaws and Possible Changes**  
- **Game Restart Logic Might Not Preserve Progress**: The restart process initializes a new `Game` and `Player` without saving any previous state. If saving game progress is needed, additional logic should be implemented.  
- **No Confirmation Before Closing**: The game window closes immediately without asking the user for confirmation. Consider adding a confirmation dialog to prevent accidental closures.  
- **Hardcoded `new Game()` and `new Player()`**: If a different method of resetting the game is needed in the future (e.g., loading a saved state), this approach may need modification.  
- **Potential `null` Issues**: If `game` or `panel` is `null`, calling methods on them could cause a `NullPointerException`. Adding null checks would improve robustness.

## **SearchGameButton**  

### **Overview**  
The `SearchGameButton` class allows users to navigate through a list of available game saves, either moving to the next or previous entry.  

### **Key Responsibilities**  
1. Handle user interaction with a button for navigating game saves.  
2. Increase the game load index when moving to the next save.  
3. Decrease the game load index when moving to the previous save.  

### **Constructor**  
- `SearchGameButton(GameEngine game, GamePanel panel, boolean next)`  
  - Initializes the button with the game engine, game panel, and a boolean flag indicating whether to move forward (`true`) or backward (`false`).  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Calls `increaseLoad(panel)` if `next` is `true`, moving forward in the save list.  
   - Calls `decreaseLoad(panel)` if `next` is `false`, moving backward in the save list.  

### **Potential Flaws and Possible Changes**  
- **No Bounds Checking on Load Index**: If `increaseLoad` or `decreaseLoad` is called without validation, it may attempt to navigate past the first or last available save. Consider adding checks to prevent out-of-bounds errors.  
- **Button Behavior Not Explicitly Defined**: It is unclear what happens when no saves exist. Implementing proper UI feedback (e.g., disabling the button) would improve usability.  
- **Panel May Not Need to Be Passed**: If `panel` is only used for updating the display, consider handling UI updates within `increaseLoad` and `decreaseLoad` instead of passing `panel` as a parameter.

## **ShelterButton**  

### **Overview**  
The `ShelterButton` class handles user interaction for moving the player to a specific location,  representing a shelter or safe area in the game.  

### **Key Responsibilities**  
1. Update the player's current location in the game.  
2. Reset the game's response type to `0`, ensuring normal gameplay interaction.  
3. Update the game panel to reflect the new state.  

### **Constructor**  
- `ShelterButton(GameEngine game, GamePanel panel, int location)`  
  - Initializes the button with the game engine, game panel, and the target location to move the player.  

### **Methods**  
1. **`actionPerformed(ActionEvent arg0)`**  
   - Resets the game's response type to `0`.  
   - Updates the player's current room to the specified location.  
   - Sets the game panel to update the UI.  

### **Potential Flaws and Possible Changes**  
- **Lack of Validation on Location**: If `location` is invalid (e.g., out of bounds or null), it could cause unexpected behavior. Consider adding validation.  
- **No Confirmation for Movement**: If the move is irreversible or important, a confirmation dialog could prevent accidental clicks.  
- **Panel Update Assumes Immediate Transition**: If additional game logic is needed (e.g., animations or events triggering), consider handling this within `setGamePanel()`.

### Data Package

## **Constants**

### **Overview**
The `Constants` class is a utility class that defines a set of **public static final** constants. These constants are used to store fixed values that are shared across the application, such as configuration values, limits, or identifiers. The class does not have any behavior (methods) or state (instance variables) and is intended to be used as a centralized place for constant values.

### **Key Responsibilities**
1. **Storing Game Configuration**: Holds constants related to the game's structure, such as the number of rooms, items, verbs, and nouns.
2. **Defining Item Limits**: Stores thresholds or identifiers for specific item types (e.g., `foodLine` and `drinkLine`).
3. **Providing UI Utilities**: Includes a string (`line`) and its length (`lineLength`) for use as a visual separator or divider in the UI.

### **Constructor**
- The class does not have an explicit constructor. Since all fields are `static`, there is no need to instantiate the class. It is intended to be used as a static utility class.

### **Methods**
The class does not define any methods. It only contains static fields.

### **Potential Flaws and Possible Changes**

#### **1. Non-Final `line` Field**
- **Flaw**: The `line` field is not declared as `final`, which means it can be modified at runtime. This contradicts the purpose of a constants class, where all fields should be immutable.
- **Change**: Declare `line` as `final` to ensure immutability:
  ```java
  public static final String line = "----------------------------------------------------------------";
  ```

#### **2. Hardcoded `line` String**
- **Flaw**: The `line` string is hardcoded, which makes it less flexible. If `lineLength` changes, the `line` string will not automatically reflect the new length.
- **Change**: Generate the `line` string dynamically based on `lineLength`:
  ```java
  public static final String line = "-".repeat(lineLength);
  ```

#### **3. Lack of Grouping for Related Constants**
- **Flaw**: Constants like `foodLine` and `drinkLine` are related but are not grouped together. This can make the class harder to navigate and understand.
- **Change**: Group related constants in a nested class or use an `enum` for better organization:
  ```java
  public static final class ItemLimits {
      public static final int FOOD = 16;
      public static final int DRINK = 21;
  }
  ```

#### **4. Non-Descriptive Field Names**
- **Flaw**: Some field names (e.g., `noRooms`, `noItems`) are not very descriptive and could be improved for better readability.
- **Change**: Use more descriptive names:
  ```java
  public static final int NUMBER_OF_ROOMS = 80;
  public static final int NUMBER_OF_ITEMS = 43;
  ```

#### **5. No Encapsulation**
- **Flaw**: All fields are `public`, which exposes them to direct modification (if not `final`) and reduces encapsulation.
- **Change**: While constants are typically `public`, consider using a private constructor to prevent instantiation of the class:
  ```java
  private Constants() {
      throw new UnsupportedOperationException("Utility class");
  }
  ```

#### **6. Potential for Overloading**
- **Flaw**: The class could become bloated if too many unrelated constants are added in the future.
- **Change**: Split the class into multiple smaller classes or enums based on the context of the constants (e.g., `GameConstants`, `UIConstants`).

---

### **Improved Version of the Class**
Here’s how the class could look after applying the suggested changes:

```java
public final class Constants {

    // Prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Game-related constants
    public static final int NUMBER_OF_ROOMS = 80;
    public static final int NUMBER_OF_ITEMS = 43;
    public static final int NUMBER_OF_VERBS = 42;
    public static final int NUMBER_OF_NOUNS = 52;
    public static final int CARRIABLE_ITEMS = 24;

    // Item type limits
    public static final class ItemLimits {
        public static final int FOOD = 16;
        public static final int DRINK = 21;
    }

    // UI-related constants
    public static final int SEPARATOR_LINE_LENGTH = 90;
    public static final String SEPARATOR_LINE = "-".repeat(SEPARATOR_LINE_LENGTH);
}
```

---

### **Summary of Changes**
1. Made `line` final and dynamically generated it.
2. Grouped related constants (`foodLine` and `drinkLine`) into a nested class.
3. Improved field names for better readability.
4. Added a private constructor to prevent instantiation.
5. Ensured all fields are `final` for immutability.

---

Let me know if you’d like further refinements or additional analysis! 😊

### Model Package

### View Package

## **Class Name**

### **Overview**

### **Key Responsibilities**
1. 
2. 
3. 
etc

### **Constructor**
- 

### ** Methods**
1. **Method Name**
  - Description
2. **Method Name**
  - Description
3. **Method Name**
  - Description
etc

### ** Potential flaws and possible changes









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
island - <a href="https://www.flaticon.com/free-icons/island" title="island icons">Island icons created by Freepik - Flaticon</a>
column - <a href="https://www.flaticon.com/free-icons/pillar" title="pillar icons">Pillar icons created by Freepik - Flaticon</a>
desert - <a href="https://www.flaticon.com/free-icons/desert" title="desert icons">Desert icons created by Flat Icons - Flaticon</a>
castledoor - <a href="https://www.flaticon.com/free-icons/magic" title="magic icons">Magic icons created by Freepik - Flaticon</a>
flowers - <a href="https://www.flaticon.com/free-icons/flower" title="flower icons">Flower icons created by Freepik - Flaticon</a>
brokenchair - <a href="https://www.flaticon.com/free-icons/damaged" title="Damaged icons">Damaged icons created by juicy_fish - Flaticon</a>
village - <a href="https://www.flaticon.com/free-icons/neighborhood" title="neighborhood icons">Neighborhood icons created by itim2101 - Flaticon</a>
swamp - <a href="https://www.flaticon.com/free-icons/grass" title="grass icons">Grass icons created by Freepik - Flaticon</a>
stonetree - <a href="https://www.flaticon.com/free-icons/trees" title="trees icons">Trees icons created by BabyCorn - Flaticon</a>
stumphouse - <a href="https://www.flaticon.com/free-icons/tree-stump" title="tree stump icons">Tree stump icons created by Freepik - Flaticon</a>
path - <a href="https://www.flaticon.com/free-icons/path" title="path icons">Path icons created by Freepik - Flaticon</a>
adventurer - <a href="https://www.flaticon.com/free-icons/rpg" title="rpg icons">Rpg icons created by max.icons - Flaticon</a>
