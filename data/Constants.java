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

public class Constants {

    // Prevent instantiation (private constructor)
    private Constants() {
        throw new UnsupportedOperationException("Constants - Utility class");
    }
	
	//Game related constants
	public static final int NUMBER_OF_ROOMS = 80;
	public static final int NUMBER_OF_ITEMS = 43;
	public static final int NUMBER_OF_VERBS = 43;
	public static final int NUMBER_OF_NOUNS = 52;
	public static final int NUMBER_EXITS = 4;
	public static final int FLAG_HIDDEN = 9;
	
	//Panel Related constants
	public static final int MESSAGE_LENGTH = 100;
	
	//Threshold for item categories in the item list
	public static final int MAX_CARRIABLE_ITEMS = 24; // Items with IDs <= 24 are carriable
	public static final int FOOD_THRESHOLD = 16; // Items with IDs >16 are food
	public static final int DRINK_THRESHOLD = 21; // Items with IDs > 21 are drinks
	public static final int LINE_LENGTH = 90;
	
	//Constants for the Game Class
	public static final int START_LOCATION = 23;
	public static final int RANDOM_ROOM = 39;
	public static final int RANDOM_EXIT_COMBO = 5;
	public static final int NUMBER_RESPONSES = 2;
	public static final int INITIAL_APPLE_COUNT = 3;
	public static final int INITIAL_START_COUNT = 2;
	
	//Constants for the player starting values
	public static final float STARTING_STRENGTH = 100;
	public static final int STARTING_WISDOM = 35;
	public static final int STARTING_TIME = 1000;
	
	private static final String NORTH = "North";
	private static final String SOUTH = "South";
	private static final String EAST = "East";
	private static final String WEST = "West";
	public static final String[] DIRECTIONS = {NORTH, SOUTH, EAST,WEST};

	public static final String STAT_STRENGTH = "strength";
	public static final String STAT_WISDOM = "wisdom";
	public static final String STAT_TIME = "timeRemaining";
}

/* 1 November 2024 - Created File
 * 2 November 2024 - Added the line
 * 11 November 2024 - Updated noNoun
 * 23 December 2024 - Updated to version 2.
 * 15 January 2025  - Added a constant for the line length
 * 31 January 2025 - Completed Testing and increased version
 * 5 March 2025 - Increased to v4.0
 * 9 March 2025 - Updated code based on recommendations
 * 16 March 2025 - Moved NUMBER_OF_EXITS here
 * 25 June 2025 - Added constants for player starting values
 * 28 June 2025 - Changed type for strength from int to float
 * 14 July 2025 - Increased size of message
 * 19 August 2025 - Added more constants from the game class
 * 20 August 2025 - Added hidden flag
 * 6 November 2025 - Increased number of verbs by 1
 * 3 December 2025 - Increased version number
*/