/*
Title: Island of Secrets Constant Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 28 June 2025
Source: https://archive.org/details/island-of-secrets_202303

This class is designed to hold the constants. They have been made public since they
do not change, and it makes them easily accessible
*/

package Data;

public class Constants {

    // Prevent instantiation (private constructor)
    private Constants() {
        throw new UnsupportedOperationException("Constants - Utility class");
    }
	
	//Game related constants
	public static final int NUMBER_OF_ROOMS = 80;
	public static final int NUMBER_OF_ITEMS = 43;
	public static final int NUMBER_OF_VERBS = 42;
	public static final int NUMBER_OF_NOUNS = 52;
	public static final int NUMBER_EXITS = 4;
	
	//Panel Related constants
	public static final int MESSAGE_LENGTH = 60;
	
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
	
	//Constants for the player starting values
	public static final float STARTING_STRENGTH = 100;
	public static final int STARTING_WISDOM = 35;
	public static final int STARTING_TIME = 1000;
	
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final String EAST = "East";
	public static final String WEST = "West";
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
*/