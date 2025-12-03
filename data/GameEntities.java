/*
Title: Island of Secrets Constant Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303

This class is designed to hold the constants. They have been made public since they
do not change, and it makes them easily accessible
*/

package data;

public class GameEntities {

    // Prevent instantiation (private constructor)
    private GameEntities() {
        throw new UnsupportedOperationException("Constants - Utility class");
    }

	// === Rooms ===
	public static final int ROOM_CARRYING = 0;
	public static final int ROOM_FOREST = 1;
	public static final int ROOM_VATS = 8;
	public static final int ROOM_BATTLEMENTS = 9;
	public static final int ROOM_SANCTUM = 10;
	public static final int ROOM_LAIR = 11;
	public static final int ROOM_CAVE = 12;
	public static final int ROOM_BRANCHES = 13;
	public static final int ROOM_THICKET = 14;
	public static final int ROOM_STONE = 15;
	public static final int ROOM_CLONE_FACTORY = 17;
	public static final int ROOM_CLONE_ROOM = 18;
	public static final int ROOM_WELL = 19;
	public static final int ROOM_SNELM_LAIR = 21;
	public static final int ROOM_PATH =  23;
	public static final int ROOM_ROCKY_PATH = 25;
	public static final int ROOM_ENTRANCE_CHAMBER = 27;
	public static final int ROOM_WITH_HANDS = 28;
	public static final int ROOM_POOL = 32;
	public static final int ROOM_JETTY = 33;
	public static final int ROOM_DUNES = 34;
	public static final int ROOM_CASTLE_WALL = 37;
	public static final int ROOM_HUT = 41;
	public static final int ROOM_TABLE = 42;
	public static final int ROOM_LOGMAN_HUT = 43;
	public static final int ROOM_GRANDPAS_SHACK = 44;
	public static final int ROOM_CLEARING = 45;
	public static final int ROOM_CASTLE_ENTRANCE = 47;
	public static final int ROOM_STOREROOM = 51;
	public static final int ROOM_LOGMAN_HALL = 52;
	public static final int ROOM_BUILDING = 53;
	public static final int ROOM_PYRAMID_STEP = 56;
	public static final int ROOM_ISLAND = 57;
	public static final int ROOM_COLUMN = 58;
	public static final int ROOM_OUTSIDE_HUT = 60;
	public static final int ROOM_ORCHIDS = 61;
	public static final int ROOM_LOG_BRIDGE = 63;
	public static final int ROOM_PYRAMID_EDGE = 65;
	public static final int ROOM_PYRAMID_ROOF = 66;
	public static final int ROOM_PYRAMID_SPLIT = 67;
	public static final int ROOM_ABODE_HUT = 70;
	public static final int ROOM_BRIDGE = 73;
	public static final int ROOM_VILLAGE_PETRIFIED = 74;
	public static final int ROOM_VILLAGE_REMAINS = 75;
	public static final int ROOM_VILLAGE_ENTRANCE = 76;
	public static final int ROOM_DESTROYED = 81;
	
	// === Items ===
	public static final int ITEM_APPLE = 1;
	public static final int ITEM_EGG = 2;
	public static final int ITEM_LILY = 3;
	public static final int ITEM_JUG = 4;
	public static final int ITEM_RAG = 5;
	public static final int ITEM_PARCHMENT = 6;
	public static final int ITEM_TORCH = 7;
	public static final int ITEM_PEBBLE = 8;
	public static final int ITEM_AXE = 9;
	public static final int ITEM_ROPE = 10;
	public static final int ITEM_STAFF = 11;
	public static final int ITEM_CHIP = 12;
	public static final int ITEM_COAL = 13;
	public static final int ITEM_FLINT = 14;
	public static final int ITEM_HAMMER = 15;
	public static final int ITEM_BEAST = 16;
	public static final int ITEM_BREAD = 17;
	public static final int ITEM_MUSHROOM = 20;
	public static final int ITEM_BOTTLE = 21;
	public static final int ITEM_WINE = 22;
	public static final int ITEM_SAP = 23;
	public static final int ITEM_WATER = 24;
	public static final int ITEM_BOAT = 25;
	public static final int ITEM_CHEST = 26;
	public static final int ITEM_FRACTURE = 27;
	public static final int ITEM_STONE = 28;
	public static final int ITEM_TRAPDOOR = 29;
	public static final int ITEM_VILLAGER = 30;
	public static final int ITEM_LIQUID = 31;
	public static final int ITEM_SWAMPMAN = 32;
	public static final int ITEM_BOOKS = 34;
	public static final int ITEM_STORM = 36;
	public static final int ITEM_CLOAK = 38;
	public static final int ITEM_OMEGAN = 39;
	public static final int ITEM_SNAKE = 40;
	public static final int ITEM_LOGMEN = 41;
	public static final int ITEM_SCAVENGER = 42;
	public static final int ITEM_MEDIAN = 43;
	public static final int ITEM_ROCKS = 44;
	public static final int ITEM_DACTYL = 46;
	public static final int ITEM_FLAG_49 = 49;

	// === Commands ===
	public static final int MOVE_BOTTOM = 0;
	public static final int MOVE_DIRECTION = 5;
	public static final int MOVE_NOT_DIRECTION = 4;
	public static final int MOVE_TOP = 6;
	public static final int CHOP_BOTTOM = 15;
	public static final int CHOP_TOP = 20;
	public static final int ATTACK_BOTTOM = 19;
	public static final int ATTACK_TOP = 24;
	
	public static final int CMD_NORTH = 1;
	public static final int CMD_SOUTH = 2;
	public static final int CMD_EAST = 3;
	public static final int CMD_WEST =4;
	public static final int CMD_GO = 5;
	public static final int CMD_TAKE = 6;
	public static final int CMD_GET = 7;
	public static final int CMD_GIVE = 8;
	public static final int CMD_DROP = 9;
	public static final int CMD_LEAVE = 10;
	public static final int CMD_EAT = 11;
	public static final int CMD_DRINK = 12;
	public static final int CMD_RIDE = 13;
	public static final int CMD_OPEN = 14;
	public static final int CMD_PICK = 15;
	public static final int CMD_TAP = 18;
	public static final int CMD_BREAK = 19;
	public static final int CMD_KILL = 24;
	public static final int CMD_SWIM = 25;
	public static final int CMD_SHELTER = 26;
	public static final int CMD_HELP = 27;
	public static final int CMD_SCRATCH = 28;
	public static final int CMD_CATCH = 29;
	public static final int CMD_RUB = 30;
	public static final int CMD_POLISH = 31;
	public static final int CMD_READ = 32;
	public static final int CMD_EXAMINE = 33;
	public static final int CMD_FILL = 34;
	public static final int CMD_SAY = 35;
	public static final int CMD_WAIT = 36;
	public static final int CMD_REST = 37;
	public static final int CMD_WAVE = 38;
	public static final int CMD_INFO = 39;
	public static final int CMD_LOAD = 40;
	public static final int CMD_SAVE = 41;
	public static final int CMD_QUIT = 42;
	public static final int CMD_RESTART = 43;
	public static final int CMD_NO_VERB = Constants.NUMBER_OF_VERBS+1;
	
	public static final String WORD_EXAMINE = "examine";
	public static final String WORD_GIVE = "give";
	public static final String WORD_LOOK = "look";
	public static final String WORD_ROOM = "room";
	public static final String WORD_TO = "to";
	public static final String WORD_WELL = "well";
	public static final String SPACE = " ";
	public static final String NOUN_TABLE = "table";
	public static final String NOUN_ROOM = "room";
	public static final String NOUN_MAP = "map";
	public static final String NOUN_PAPERS = "papers";
	public static final String NOUN_DIARY = "diary";
	public static final String NOUN_APPLE = "apple";
	public static final String NOUN_LILY = "lily";
	public static final String NOUN_FLOWER = "flower";
	public static final String NOUN_WINE = "wine";
	public static final String NOUN_BOATMAN = "boatman";
	public static final String NOUN_BOAT = "boat";
	public static final String NOUN_FOOD = "food";
	public static final String NOUN_DRINK = "drink";
	public static final String NOUN_STONE = "stone";
	public static final String NOUN_HUT = "hut";
	public static final String NOUN_SHACK = "shack";
	public static final String NOUN_CAVE = "cave";
	public static final String NOUN_STONEYWORDS = "stony words";
	public static final String NOUN_REMEMBEROLDTIMES = "remember old times";
	public static final String SHELTER_MESSAGE = "You reach shelter";
	
	// === Codes ===
	public static final String CODE_CLOAK = "3810010";
	public static final String CODE_EGG = "246046";
	public static final String CODE_EVIL_BOOKS = "3450050";
	public static final String CODE_TORCH_DIM = "700";
	public static final String CODE_TORCH_BRIGHT = "701";
	public static final String CODE_SNAKE = "10045";
	public static final String CODE_VILLAGER = "2413075";
	public static final String CODE_LILY = "300";
	public static final String CODE_CHIP = "120";
	public static final String CODE_PEBBLE = "80";
	public static final String CODE_JUG = "40";
	public static final String CODE_IN_LAIR = "500012";
	public static final String CODE_IN_LOG_HUT = "500053";
	public static final String CODE_IN_SHACK = "500045";
	public static final String CODE_IN_ABODE_HUT = "500070";
	public static final String CODE_IN_PORTAL = "500037";
	public static final String CODE_OUT_LAIR = "510011";
	public static final String CODE_OUT_LOG_HUT = "510041";
	public static final String CODE_OUT_LOG_CABIN = "510043";
	public static final String CODE_DOWN_STOREROOM = "490051";
	public static final String CODE_DOWN_PYRAMID = "490066";
	public static final String CODE_DOWN_TRAPDOOR = "490051"; 
	public static final String CODE_ENTER_TRAPDOOR = "2951051";
	public static final String CODE_CLOSED_TRAPDOOR = "2951151";
	public static final String CODE_OUT_ABODE_HUT = "510060";
	public static final String CODE_UP_PYRAMID = "480056";
	public static final String CODE_OUT_SHACK = "510044";
	public static final String CODE_OUT_HALL = "510052";
	public static final String CODE_SCRATCH_SAGE = "3371071";
	public static final String CODE_HELP_VILLAGER = "3075075";
	public static final String CODE_HELP_SAGE = "3371071";
	public static final String CODE_READ_PARCHMENT = "600";
	public static final String CODE_CHEST_CLOSED = "2644044";
	public static final String CODE_CHEST_OPEN = "2644144";
	public static final String CODE_FILL_JUG = "40041";
	public static final String CODE_FILL_JUG_WATER = "40013";
	public static final String CODE_JUG_FULL = "40";
	public static final String CODE_RIDE_BEAST = "1600";
	public static final String CODE_RIDING_BEAST = "1601";
	public static final String CODE_OPEN_CHEST = "2644044";
	public static final String CODE_OPEN_TRAPDOOR = "2951151";
	public static final String CODE_CHOPPING_ROOTS = "3577077";
	public static final String CODE_BREAK_COLUMN_ONE = "1258158";
	public static final String CODE_BREAK_COLUMN_TWO = "2758158";
	public static final String CODE_HAS_STAFF = "1100";
	public static final String CODE_HAS_FLINT = "1400";
	
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
 * 25 May 2025 - Added codes for give
 * 30 May 2025 - Added codes for rest
 * 1 June 2025 - Added codes for help
 * 2 June 2025 - Added codes for speak
 * 4 June 2025 - Started adding codes for examine
 * 5 June 2025 - Started adding codes for examine room
 * 6 June 2025 - Added more codes for Examine Room
 * 8 June 2025 - Added codes for fill
 * 9 June 2025 - Added codes for ride & open
 * 10 June 2025 - Added codes for swim
 * 12 June 2025 - Added codes to break column & break staff
 * 13 June 2025 - Added codes for tap.
 * 15 June 2025 - Added flint code
 * 16 June 2025 - Added codes for strike flint
 * 20 June 2025 - Added orchids room
 * 21 June 2025 - Added codes for post action
 * 22 June 2025 - Added codes for post move events
 * 6 July 2025 - Fixed issue with going into and out of the hut
 * 16 July 2025 - Added parchment
 * 17 July 2025 - Added Torch & Flint
 * 20 July 2025 - Added code for go trapdoor
 * 21 July 2025 - Added code for go command
 * 23 August 2025 - Added further variables
 * 24 August 2025 - Added remaining commands
 * 2 November 2025 - Added noun room, map, and papers
 * 6 November 2025 - Increased for a Restart option
 * 8 November 2025 - Added nouns for food & drink
 * 30 November 2025 - Changed cabin to hut
 * 3 December 2025 - Increased version number
 */