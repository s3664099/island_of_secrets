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

---

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

---

## Program Structure ##

### Default Package

This package is only used to start the game, and only holds the class that launches the game.

## **Start Class**

### **Overview**
The `Start` class is the **entry point** for the game application. It is responsible for launching the game, handling errors, and logging key events during startup.

### **Key Responsibilities**
1. **Launch the Game**:
   - Initializes the game by creating an instance of the `Main` class and calling its `startGame()` method.
2. **Error Handling**:
   - Catches and logs any errors that occur during game startup, ensuring the application fails gracefully.
3. **Logging**:
   - Logs important events, such as the start of the application and successful game initialization.

### **How It Works**
- The `main` method:
  - Logs the start of the application.
  - Creates an instance of the `Main` class and starts the game.
  - Logs the successful start of the game.
  - Catches and logs any errors that occur during this process.

### **Usage**
The `Start` class is the first class executed when the game is launched. It does not require any configuration or modification unless the game's startup process changes.

---

### Controller

The controller package is a part of the MVP pattern and holds all of the classes that execute the commands. This does not process the commands but are rather classes that are tied to buttons or text input boxes, and basically retrieve the action and interpretates it, and then sends it to the specific model class.

## **BookButton**  

## **CommandButton**

## **CommandListener**

## **GameButton**

## **LoadGameButton**    

## **MapButton**

## **QuitButton**

## **SearchGameButton**

## **ShelterButton**     

### Data

## **Constants Class**

### **Overview**
The `Constants` class is a **centralized place** for storing fixed values used throughout the game. These values include game configuration settings, item limits, and other shared data. The class is designed to be **static** and **immutable**, meaning its values cannot be changed once defined.

### **Purpose**
- **Simplify Maintenance**: By storing all constants in one place, it’s easier to update values without searching through the entire codebase.
- **Improve Readability**: Constants with descriptive names make the code easier to understand.
- **Ensure Consistency**: Using constants prevents hardcoding values in multiple places, reducing the risk of errors.

### **Key Constants**
1. **Game Configuration**:
   - `NUMBER_OF_ROOMS`: Total number of rooms in the game.
   - `NUMBER_OF_ITEMS`: Total number of items in the game.
   - `NUMBER_OF_VERBS`: Total number of verbs (actions) available.
   - `NUMBER_OF_NOUNS`: Total number of nouns (objects) available.

2. **Item Limits**:
   - `MAX_CARRIABLE_ITEMS`: Maximum number of items the player can carry.
   - `FOOD_THRESHOLD`: Items with IDs above this value are considered food.
   - `DRINK_THRESHOLD`: Items with IDs above this value are considered drinks.

### **Usage**
- The `Constants` class is used throughout the game to access shared values. For example:
  ```java
  if (itemId <= Constants.MAX_CARRIABLE_ITEMS) {
      System.out.println("This item can be carried.");
  }
  ```

- Since all fields are `public static final`, they can be accessed directly without creating an instance of the class:
  ```java
  int totalRooms = Constants.NUMBER_OF_ROOMS;
  ```

### **Best Practices**
- **Do Not Modify**: The values in this class are fixed and should not be changed at runtime.
- **Add New Constants**: If the game requires additional fixed values, add them to this class to maintain consistency.
- **Use Descriptive Names**: When adding new constants, choose names that clearly describe their purpose.

---

## **Item**

## **Location**

## **RawData**      

### Model

## **CommandProcess**

## **Commands**

## **Game**

## **GameEngine**

## **Main Class**

### **Overview**
The `Main` class is responsible for setting up and launching the game. It initializes the game data, creates the player, and starts the game UI in a way that ensures smooth performance.

### **Key Responsibilities**
1. **Game Setup**:
   - Initializes the game data, player, and game engine.
2. **UI Launch**:
   - Starts the game UI using Swing's event dispatch thread to avoid blocking the main thread.
3. **Error Handling**:
   - Catches and handles errors during game setup and UI launch, ensuring the game fails gracefully if something goes wrong.

### **How It Works**
- The `startGame()` method:
  - Creates the game data, player, and game engine.
  - Launches the game UI in a separate thread.
  - Handles any errors that occur during this process, printing helpful error messages.

### **Usage**
The `Main` class is used by the `Start` class to initialize and launch the game. It does not need to be directly instantiated or modified unless the game's initialization logic changes.

---

## **Player**

## **Test**  

### View

## **GameFrame**

## **GamePanel**

## **Lightning Panel**

## **MapPanel**

## **MessagePanel**          

---

## **Class Name**

### **Overview**

### **Key Responsibilities**
1. 
2. 
3. 
etc

### **Instance Variables**


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
