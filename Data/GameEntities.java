/*
Title: Island of Secrets Constant Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 24 May 2025
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
	public static final int ROOM_CARRYING = 0;
	public static final int ROOM_FOREST = 1;
	public static final int ROOM_SANCTUM = 10;
	public static final int ROOM_CAVE = 12;
	public static final int ROOM_ROCKY_PATH = 25;
	public static final int ROOM_ENTRANCE_CHAMBER = 27;
	public static final int ROOM_WITH_HANDS = 28;
	public static final int ROOM_CLEARING = 45;
	public static final int ROOM_CASTLE_ENTRANCE = 47;
	public static final int ROOM_STOREROOM = 51;
	public static final int ROOM_BUILDING = 53;
	public static final int ROOM_ABODE_HUT = 70;
	public static final int ROOM_DESTROYED = 81;
	
	// === Items ===
	public static final int ITEM_APPLE = 1;
	public static final int ITEM_JUG = 4;
	public static final int ITEM_TORCH = 7;
	public static final int ITEM_ROPE = 10;
	public static final int ITEM_STAFF = 20;
	public static final int ITEM_BEAST = 16;
	public static final int ITEM_MUSHROOM = 20;
	public static final int ITEM_WATER = 24;
	public static final int ITEM_VILLAGER = 30;
	public static final int ITEM_SWAMPMAN = 32;
	public static final int ITEM_OMEGAN = 39;
	public static final int ITEM_SNAKE = 40;
	public static final int ITEM_LOGMEN = 41;
	public static final int ITEM_SCAVENGER = 42;
	public static final int ITEM_MEDIAN = 43;
	public static final int ITEM_ROCKS = 44;
	
	// === Commands ===
	public static final int CMD_DROP = 9;
	public static final int CMD_PICK = 15;
	public static final int CMD_CATCH = 29;
	
	// === Codes ===
	public static final String CODE_CLOAK = "3810010";
	public static final String CODE_EGG = "246046";
	public static final String CODE_EVIL_BOOKS = "3450050";
	public static final String CODE_TORCH = "701";
	
	// === Directions ===
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;
	
}

/* 21 May 2025 - Created File
 * 22 May 2025 - Moved constants from Move function
 * 23 May 2025 - Edited locations to start with ROOM,ITEM and CMD (Command)
 * 24 May 2025 - Added codes for take
 */