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

### **Overview**
The `Item` class represents an in-game item, including its properties such as name, location, flag, and whether wisdom has been acquired. It is designed to be serializable, allowing the game state to be saved and loaded.

### **Purpose**
- **Encapsulate Item Data**: Stores and manages data related to in-game items, such as their name, location, and flags.
- **Support Serialization**: Implements `Serializable` to enable saving and loading of game state.
- **Provide Utility Methods**: Offers methods to check if an item is at a specific location, update its properties, and retrieve its state.

### **Key Components**

#### **1. Instance Variables**
- **`itemFlag`**: An integer representing the item’s flag (e.g., properties or state).
- **`itemLocation`**: An integer representing the item’s current location in the game world.
- **`itemName`**: A string describing the item’s name or description.
- **`wisdomAcquired`**: A boolean indicating whether wisdom has been acquired from the item.
- **Constants**: 
  - `FLAG_OFFSET`: ASCII value of `'0'`, used for converting flag characters to integers.
  - `LOCATION_OFFSET`: ASCII value of space, used for converting location characters to integers.
  - `LOCATION_ADJUSTMENT`: Adjustment value for location values greater than 127.
  - `ASCII_MAX`: Maximum ASCII value before adjustment is needed.

#### **2. Methods**
- **Constructor**:
  - `Item(char flag, char location, String itemName)`: Initializes an item with a flag, location, and name.
- **Getters and Setters**:
  - `getItemName()`, `setItemName(String)`: Retrieve or update the item’s name.
  - `getItemFlag()`, `setItemFlag(int)`: Retrieve or update the item’s flag.
  - `getItemLocation()`, `setItemLocation(int)`: Retrieve or update the item’s location.
  - `setWisdomAcquired(boolean)`, `hasWisdomAcquired()`: Set or check whether wisdom has been acquired.
- **Utility Methods**:
  - `isAtLocation(int)`: Checks if the item is at a specific location.
- **`toString()`**:
  - Provides a string representation of the item for debugging or logging.

### **Usage**
To use the `Item` class, create an instance with the required parameters and interact with it using the provided methods. For example:

```java
// Create an item
Item apple = new Item('1', 'A', "Shiny Apple");

// Check if the item is at a specific location
if (apple.isAtLocation(10)) {
    System.out.println("The apple is at location 10.");
}

// Update the item's location
apple.setItemLocation(20);

// Check if wisdom has been acquired
if (apple.hasWisdomAcquired()) {
    System.out.println("Wisdom has been acquired from the apple.");
}
```

### **Best Practices**
1. **Encapsulation**:
   - Use the provided getter and setter methods to access or modify the item’s properties.
2. **Validation**:
   - Ensure that location and flag values are within valid ranges when setting them.
3. **Serialization**:
   - Update the `serialVersionUID` if the class structure changes significantly to avoid compatibility issues during deserialization.
4. **Debugging**:
   - Use the `toString()` method for debugging or logging to inspect the item’s state.

### **Example**
Here’s an example of how the `Item` class might be used in the game:

```java
// Create an item
Item torch = new Item('2', 'B', "Flickering Torch");

// Print the item's details
System.out.println(torch.toString()); // Output: Item{description='Flickering Torch', location=34, flag=2, wisdomGained=false}

// Update the item's location
torch.setItemLocation(50);

// Check if the item is at a specific location
if (torch.isAtLocation(50)) {
    System.out.println("The torch is at location 50.");
}

// Mark wisdom as acquired
torch.setWisdomAcquired(true);
```

### **Why Use This Class?**
- **Centralized Item Management**: Encapsulates all data and behavior related to in-game items in one place.
- **Flexibility**: Allows items to be dynamically updated during gameplay (e.g., changing location or flags).
- **Serialization Support**: Enables saving and loading of game state, ensuring persistence across sessions.
- **Debugging and Logging**: The `toString()` method provides a convenient way to inspect item states.

---

## **Location**

### **Overview**
The `Location` class represents a location in the game, including its name, exits, and state (e.g., whether it has been visited or viewed). It is designed to be serializable, allowing the game state to be saved and loaded.

### **Purpose**
- **Encapsulate Location Data**: Stores and manages data related to in-game locations, such as their name, exits, and state.
- **Support Serialization**: Implements `Serializable` to enable saving and loading of game state.
- **Provide Utility Methods**: Offers methods to check and update the state of the location (e.g., visited, viewed).

### **Key Components**

#### **1. Instance Variables**
- **`name`**: A string representing the name or description of the location.
- **`exits`**: A boolean array indicating the available exits (e.g., north, south, east, west).
- **`visited`**: A boolean indicating whether the location has been visited.
- **`viewed`**: A boolean indicating whether the location has been viewed.
- **`roomType`**: A string representing the type of the room (e.g., "forest", "cave").
- **Constants**:
  - `PREPOSITION_INDEX`: Index of the preposition in the `name` string.
  - `NAME_INDEX`: Index of the name in the `name` string.
  - `EXIT_START_INDEX`: Index where the exit data starts in the `name` string.

#### **2. Methods**
- **Constructor**:
  - `Location(String name, String[] prepositions, String roomType)`: Initializes a location with a name, prepositions, and room type.
- **Getters**:
  - `getName()`: Returns the location’s name.
  - `getExits()`: Returns the array of available exits.
  - `getVisited()`: Returns whether the location has been visited.
  - `getViewed()`: Returns whether the location has been viewed.
  - `getRoomType()`: Returns the room type.
- **Setters**:
  - `setVisited()`: Marks the location as visited.
  - `setViewed()`: Marks the location as viewed.
- **`toString()`**:
  - Provides a string representation of the location for debugging or logging.

### **Usage**
To use the `Location` class, create an instance with the required parameters and interact with it using the provided methods. For example:

```java
// Create a location
String[] prepositions = {"by", "facing", "at", "in", "outside", "beneath", "on"};
Location forest = new Location("4the furthest depth of the forest1001", prepositions, "forest");

// Check if the location has been visited
if (forest.getVisited()) {
    System.out.println("The forest has been visited.");
}

// Mark the location as viewed
forest.setViewed();

// Print the location's details
System.out.println(forest.toString()); // Output: Location{name='by the furthest depth of the forest', exits=[true, false, false, true], visited=false, viewed=true, roomType='forest'}
```

### **Best Practices**
1. **Encapsulation**:
   - Use the provided getter and setter methods to access or modify the location’s properties.
2. **Validation**:
   - Ensure that input parameters (e.g., `name`, `prepositions`, `roomType`) are validated in the constructor to prevent runtime errors.
3. **Serialization**:
   - Update the `serialVersionUID` if the class structure changes significantly to avoid compatibility issues during deserialization.
4. **Debugging**:
   - Use the `toString()` method for debugging or logging to inspect the location’s state.

### **Example**
Here’s an example of how the `Location` class might be used in the game:

```java
// Create a location
String[] prepositions = {"by", "facing", "at", "in", "outside", "beneath", "on"};
Location cave = new Location("2a dark cave0000", prepositions, "cave");

// Print the location's details
System.out.println(cave.toString()); // Output: Location{name='facing a dark cave', exits=[false, false, false, false], visited=false, viewed=false, roomType='cave'}

// Mark the location as visited
cave.setVisited();

// Check if the location has been viewed
if (!cave.getViewed()) {
    System.out.println("The cave has not been viewed.");
}
```

### **Why Use This Class?**
- **Centralized Location Management**: Encapsulates all data and behavior related to in-game locations in one place.
- **Flexibility**: Allows locations to be dynamically updated during gameplay (e.g., marking as visited or viewed).
- **Serialization Support**: Enables saving and loading of game state, ensuring persistence across sessions.
- **Debugging and Logging**: The `toString()` method provides a convenient way to inspect location states.

---

## **RawData Class**

### **Overview**
The `RawData` class is a **data repository** that stores and provides access to static game data, such as locations, objects, verbs, nouns, and other configuration values. It serves as a centralized place for all hardcoded game data, making it easier to manage and update.

### **Purpose**
- **Centralized Data Storage**: Stores all static game data in one place for easy access and maintenance.
- **Data Retrieval**: Provides methods to retrieve specific data (e.g., locations, objects, verbs) based on input parameters.
- **Error Handling**: Includes input validation to prevent runtime errors when accessing data.

### **Key Components**

#### **1. Data Arrays**
- **`LOCATION_TYPES`**: Maps each location to a type (e.g., forest, cave, factory).
- **`LOCATION_IMAGE`**: Stores the image names associated with each location type.
- **`LOCATIONS`**: Contains descriptions and metadata for each game location.
- **`OBJECTS`**: Lists all objects in the game, including carriable items, food, and drink.
- **`VERBS`**: Defines the actions players can perform.
- **`NOUNS`**: Lists the objects or directions players can interact with.
- **`ITEM_LOCATION`**: Encodes the location of each item in the game.
- **`ITEM_FLAG`**: Encodes flags or properties for each item.
- **`PREPOSITIONS`**: Lists prepositions used in the game (e.g., "by", "in", "on").

#### **2. Methods**
- **`getLocation(int number)`**: Retrieves the description of a specific location.
- **`getImage(int number)`**: Retrieves the image name for a specific location type.
- **`getObjects(int number)`**: Retrieves the description of a specific object.
- **`getPrepositions()`**: Returns the list of prepositions.
- **`getItemLocation(int number)`**: Retrieves the location code for a specific item.
- **`getItemFlag(int number)`**: Retrieves the flag for a specific item.
- **`getVerbs()`**: Returns the list of verbs.
- **`getNouns()`**: Returns the list of nouns.

### **Usage**

#### **Accessing Data**
To retrieve data from the `RawData` class, use the provided static methods. For example:

```java
// Get the description of location 5
String location = RawData.getLocation(5);

// Get the image name for location type 2
String image = RawData.getImage(2);

// Get the description of object 10
String object = RawData.getObjects(10);

// Get the list of verbs
String[] verbs = RawData.getVerbs();
```

#### **Error Handling**
All methods include input validation to ensure the input `number` is within the valid range. If the input is invalid, an `IllegalArgumentException` is thrown with a descriptive error message.

### **Best Practices**
1. **Avoid Modifying Data**:
   - All fields are `private` and `final`, meaning they cannot be modified after initialization. This ensures data integrity.
2. **Use Constants**:
   - When accessing data, use constants or enums (if available) to avoid hardcoding indices.
3. **Extend with External Data**:
   - If the game grows in complexity, consider moving hardcoded data to external files (e.g., JSON or XML) for easier maintenance.

### **Example**
Here’s an example of how the `RawData` class might be used in the game:

```java
// Retrieve and display location information
int locationId = 5;
String locationDescription = RawData.getLocation(locationId);
String locationImage = RawData.getImage(locationId);
System.out.println("Location: " + locationDescription);
System.out.println("Image: " + locationImage);

// Retrieve and display object information
int objectId = 10;
String objectDescription = RawData.getObjects(objectId);
System.out.println("Object: " + objectDescription);
```

### **Why Use This Class?**
- **Centralized Data Management**: All game data is stored in one place, making it easy to update and maintain.
- **Improved Code Quality**: Reduces the risk of errors caused by hardcoding data in multiple places.
- **Scalability**: Makes it easier to add new data or extend the game in the future.


### Model

## **CommandProcess**

## **Commands**

## **Game**

## **GameEngine**

## **GameInitialiser**

---

### **Overview**
The `GameInitialiser` class is a utility class designed to initialize and set up the core components of a text-based adventure game. It prepares the game environment by creating and configuring locations (rooms) and items, which are essential for the game world. This class ensures that the game starts with all necessary objects and locations properly set up.

### **Purpose**
The primary purpose of the `GameInitialiser` class is to:
- Initialize the game's locations (rooms) using data from the `RawData` class.
- Initialize the game's items, assigning them properties such as names, locations, and flags.
- Return a fully configured `Game` object that contains all the initialized locations and items, ready for gameplay.

### **Key Components**

#### **1. Instance Variables**
The `GameInitialiser` class does not have any instance variables. It operates as a static utility class, meaning all its functionality is provided through static methods.

#### **2. Methods**
- **`public static Game initialiseGame()`**:  
  This is the main method of the class. It performs the following tasks:
  1. Initializes an array of `Location` objects based on the number of rooms defined in `Constants.NUMBER_OF_ROOMS`.
  2. Populates each location with data from the `RawData` class, including descriptions, prepositions, and images.
  3. Initializes an array of `Item` objects based on the number of items defined in `Constants.NUMBER_OF_NOUNS`.
  4. Assigns properties to each item, such as its name, location, and flags, using data from the `RawData` class.
  5. Returns a `Game` object containing the initialized locations and items.

### **Usage**
To use the `GameInitialiser` class, simply call the `initialiseGame()` method. This method will return a fully initialized `Game` object, which can then be used to start the game.

```java
// Initialize the game
Game game = GameInitialiser.initialiseGame();

// Use the game object to start the game or perform other operations
```

### **Best Practices**
1. **Encapsulation:** The `GameInitialiser` class encapsulates the initialization logic, making it easy to modify or extend the game setup process without affecting other parts of the code.
2. **Separation of Concerns:** By separating the initialization logic into its own class, the codebase remains modular and easier to maintain.
3. **Use of Constants:** The class relies on constants (e.g., `Constants.NUMBER_OF_ROOMS`) to define the size of the game world, ensuring consistency and flexibility.
4. **Null Index Handling:** The first index of the `locations` and `items` arrays is intentionally set to `null` to align with the game's numbering scheme. Ensure this is respected when accessing these arrays.

### **Example**
Here’s an example of how the `GameInitialiser` class might be used in a game:

```java
public class Main {
    public static void main(String[] args) {
        // Initialize the game
        Game game = GameInitialiser.initialiseGame();

        // Start the game loop or perform other game logic
        // ...
    }
}
```

### **Why Use This Class?**
- **Simplifies Game Setup:** The `GameInitialiser` class handles the complex process of setting up the game world, allowing developers to focus on other aspects of the game.
- **Reusability:** The class can be reused across different parts of the game or in future projects with similar requirements.
- **Maintainability:** By centralizing the initialization logic, the codebase becomes easier to maintain and debug.
- **Scalability:** The class is designed to work with dynamic data (e.g., `RawData`), making it easy to scale the game world by simply updating the data sources.

---

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

### **Overview**
The `Player` class represents the player in the game, managing their attributes (e.g., strength, wisdom, time remaining) and state (e.g., current room, food, drink). It uses a `Map` to store and manage player stats dynamically, making it flexible and extensible. The class is serializable, allowing the game state to be saved and loaded.

### **Purpose**
- **Encapsulate Player Data**: Stores and manages the player’s attributes and state using a `Map` for flexibility.
- **Support Serialization**: Implements `Serializable` to enable saving and loading of game state.
- **Provide Utility Methods**: Offers methods to update and retrieve the player’s state (e.g., strength, wisdom, room).

### **Key Components**

#### **1. Instance Variables**
- **`room`**: The player’s current room.
- **`roomToDisplay`**: The room to display (used for special cases like random room display).
- **`stats`**: A `Map<String, Object>` storing the player’s attributes (e.g., strength, wisdom, time remaining, weight, food, drink).
- **`rand`**: A `Random` object for generating random values.
- **`panelFlag`**: A flag for UI state (e.g., normal screen, give screen, lightning flashes).
- **`isSwimming`**: A boolean indicating whether the player is swimming.
- **`RANDOM_ROOM_TRIGGER`**: A constant representing the room that triggers random room display.

#### **2. Methods**
- **`getDisplayRoom()`**: Returns the room to display.
- **`updateDisplayRoom()`**: Updates the room to display (e.g., for random room logic).
- **`turnUpdateStats()`**: Updates the player’s stats at the end of each turn (e.g., reduces time and strength).
- **`getStrengthWisdom()`**: Returns the combined value of strength and wisdom.
- **Getters and Setters**:
  - `getRoom()`, `setRoom(int)`: Retrieve or update the player’s current room.
  - `getStat(String)`, `setStat(String, Object)`, `reduceStat(String)`: Manage the player’s stats dynamically.
  - `getPanelFlag()`, `setPanelFlag(int)`: Manage the UI state flag.
  - `getSwimming()`, `setSwimming(boolean)`: Manage the player’s swimming state.
- **`toString` Methods**:
  - `toStringStatus()`: Returns a formatted string with the player’s strength and wisdom.
  - `toStringTimeRemaining()`: Returns a formatted string with the player’s remaining time.
  - `toString()`: Returns a detailed string representation of the player’s state for debugging.

### **Usage**
To use the `Player` class, create an instance and interact with it using the provided methods. For example:

```java
// Create a player
Player player = new Player();

// Update the player's room
player.setRoom(20);

// Print the player's status
System.out.println(player.toStringStatus()); // Output: Strength: 100.00         Wisdom: 35

// Update the player's state
player.turnUpdateStats();

// Check if the player is swimming
player.setSwimming(true);
if (player.getSwimming()) {
    System.out.println("The player is swimming.");
}
```

### **Best Practices**
1. **Encapsulation**:
   - Use the provided getter and setter methods to access or modify the player’s attributes.
2. **Validation**:
   - Ensure that attribute values (e.g., room, strength, wisdom) are within valid ranges when setting them.
3. **Serialization**:
   - Update the `serialVersionUID` if the class structure changes significantly to avoid compatibility issues during deserialization.
4. **Debugging**:
   - Use the `toString()` method for debugging or logging to inspect the player’s state.

### **Example**
Here’s an example of how the `Player` class might be used in the game:

```java
// Create a player
Player player = new Player();

// Update the player's room
player.setRoom(20);

// Print the player's status
System.out.println(player.toStringStatus()); // Output: Strength: 100.00         Wisdom: 35

// Update the player's state
player.turnUpdateStats();

// Check if the player is swimming
player.setSwimming(true);
if (player.getSwimming()) {
    System.out.println("The player is swimming.");
}

// Print the player's details
System.out.println(player.toString()); // Output: Player{room=20, strength=100.0, wisdom=35, timeRemaining=999, weight=0, food=2, drink=2}
```

### **Why Use This Class?**
- **Centralized Player Management**: Encapsulates all data and behavior related to the player in one place.
- **Flexibility**: Uses a `Map` to store stats, making it easy to add or modify attributes dynamically.
- **Serialization Support**: Enables saving and loading of game state, ensuring persistence across sessions.
- **Utility Methods**: Provides methods to manage and retrieve the player’s state, making it easier to implement game logic.

---

## **Swimming**

### **Overview**
The `Swimming` class manages the player’s swimming state, including their position and progress while swimming. It is designed to handle the logic for swimming mechanics in the game.

### **Purpose**
- **Encapsulate Swimming Logic**: Tracks the player’s swimming progress and checks if they have reached a specific position.
- **Support Game Mechanics**: Provides methods to update and check the player’s swimming state.

### **Key Components**

#### **1. Instance Variables**
- **`swimming`**: Represents the room or context in which the player is swimming.
- **`swimPosition`**: Tracks the player’s progress while swimming.

#### **2. Methods**
- **Constructor**:
  - `Swimming(int swimming)`: Initializes the swimming state with the specified room or context.
- **`swim()`**: Increments the player’s swimming position.
- **`checkPosition(float strength)`**: Checks if the player has reached a specific swimming position and has sufficient strength to continue.

### **Usage**
To use the `Swimming` class, create an instance and interact with it using the provided methods. For example:

```java
// Create a swimming instance
Swimming swimming = new Swimming(10);

// Update the player's swimming position
swimming.swim();

// Check if the player has reached the swimming position
if (swimming.checkPosition(50.0f)) {
    System.out.println("The player has reached the swimming position.");
}
```

### **Best Practices**
1. **Encapsulation**:
   - Use the provided methods to interact with the swimming state, avoiding direct manipulation of instance variables.
2. **Validation**:
   - Ensure that the `strength` parameter passed to `checkPosition` is valid (e.g., non-negative).
3. **Integration**:
   - Integrate the `Swimming` class with the `Player` class to manage the player’s swimming state during gameplay.

### **Example**
Here’s an example of how the `Swimming` class might be used in the game:

```java
// Create a swimming instance
Swimming swimming = new Swimming(10);

// Simulate swimming progress
for (int i = 0; i < 6; i++) {
    swimming.swim();
}

// Check if the player has reached the swimming position
if (swimming.checkPosition(50.0f)) {
    System.out.println("The player has reached the swimming position.");
} else {
    System.out.println("The player is still swimming.");
}
```

### **Why Use This Class?**
- **Encapsulation of Swimming Logic**: Keeps swimming-related logic separate from other player mechanics, improving code organization.
- **Reusability**: Can be reused in different parts of the game where swimming mechanics are needed.
- **Simplicity**: Provides a straightforward way to track and check swimming progress.

---

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
