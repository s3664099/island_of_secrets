/*
Title: Island of Secrets Constant Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 21 May 2025
Source: https://archive.org/details/island-of-secrets_202303

This class is designed to hold the constants. They have been made public since they
do not change, and it makes them easily accessible
*/

package Data;

public class GameEntities {

    // Prevent instantiation (private constructor)
    private GameEntities() {
        throw new UnsupportedOperationException("Constants - Utility class");
    }

	// === Rooms ===
	public static final int CARRYING = 0;
	public static final int FOREST = 1;
	public static final int SANCTUM = 10;
	public static final int CAVE = 12;
	public static final int ROCKY_PATH = 25;
	public static final int ENTRANCE_CHAMBER = 27;
	public static final int ROOM_WITH_HANDS = 28;
	public static final int CLEARING = 45;
	public static final int CASTLE_ENTRANCE = 47;
	public static final int STOREROOM = 51;
	public static final int BUILDING = 53;
	public static final int ABODE_HUT = 70;
	public static final int DESTROYED = 81;
	
	// === Items ===
	public static final int APPLE = 1;
	public static final int JUG = 4;
	public static final int TORCH = 7;
	public static final int ROPE = 10;
	public static final int STAFF = 20;
	public static final int BEAST = 16;
	public static final int MUSHROOM = 20;
	public static final int WATER = 24;
	public static final int VILLAGER = 30;
	public static final int SWAMPMAN = 32;
	public static final int OMEGAN = 39;
	public static final int SNAKE = 40;
	public static final int LOGMEN = 41;
	public static final int SCAVENGER = 42;
	public static final int MEDIAN = 43;
	public static final int ROCKS = 44;
	
	// === Commands ===
	public static final int DROP = 9;
	public static final int PICK = 15;
	public static final int CATCH = 29;
	
	// === Directions ===
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;
	
}

/* 21 May 2025 - Created File
 * 22 May 2025 - Moved constants from Move function
 */