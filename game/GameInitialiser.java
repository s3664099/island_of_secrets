/*
Title: Island of Secrets Game Initialiser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.util.logging.Level;
import java.util.logging.Logger;

import data.Constants;
import data.Item;
import data.Location;
import data.RawData;

/**
 * Utility class responsible for initializing the core game data.
 * <p>
 * The {@code GameInitialiser} sets up all locations and items
 * based on static {@link RawData} definitions and assembles them
 * into a {@link Game} object ready for play.
 * <p>
 * This class is non-instantiable and provides only static methods.
 */
public final class GameInitialiser {

	private static final Logger logger = Logger.getLogger(GameInitialiser.class.getName());
	
    /**
     * Private constructor to prevent instantiation.
     *
     * Calling this constructor will throw an {@link AssertionError}
     * as this class is intended to be used only in a static context.
     */
    private GameInitialiser() {  
        throw new AssertionError("Utility class should not be instantiated");
    }
	
    /**
     * Creates and returns a fully initialized {@link Game} object.
     *
     * The game will contain all locations, items, and a {@link SpecialExitHandler}.
     *
     * @return the initialized {@code Game} object
     */
    public static Game initialiseGame() {
    	return new Game(createLocations(),createItems(),new SpecialExitHandler());
    }
    
    /**
     * Creates and initializes all game locations.
     *
     * Location arrays are indexed starting at 1 (index 0 is unused),
     * matching the design of the original BASIC implementation.
     *
     * @return an array of {@link Location} objects
     */
    private static Location[] createLocations() {
    	Location[] locations = new Location[Constants.NUMBER_OF_ROOMS+1];

    	//Room 0 is unused
    	locations[0] = null;
    	
    	// Initialize locations starting from 1
		for (int roomId=1;roomId<= Constants.NUMBER_OF_ROOMS;roomId++) {
			locations[roomId] = new Location(RawData.getLocation(roomId-1),
												RawData.getPrepositions(),
												RawData.getImage(roomId-1));
			final int id = roomId;
			logger.log(Level.FINE, String.format("Initializing room %d", id));
		}
		return locations;
    }
    
    /**
     * Creates and initializes all game items.
     *
     * Item arrays are indexed starting at 1 (index 0 is unused),
     * matching the design of the original BASIC implementation.
     *
     * @return an array of {@link Item} objects
     */
	public static Item[] createItems() {
		
		Item[] items = new Item[Constants.NUMBER_OF_NOUNS+1];
		
		//Item 0 is unused
		items[0] = null; 
		
		//Builds the item objects
		for (int itemId=1;itemId<=Constants.NUMBER_OF_NOUNS;itemId++) {
			String itemName = (itemId < Constants.NUMBER_OF_ITEMS+1)
					? RawData.getObjects(itemId):"";
			items[itemId] = new Item(RawData.getItemFlag(itemId),
										 RawData.getItemLocation(itemId),
										 itemName);
			final int id = itemId;
			logger.log(Level.FINE, String.format("Initializing item %d, %s", id,itemName));
		}	
		return items;
	}
}

/* 15 March 2025 - Created File
 * 4 April 2025 - Fixed issue with first location not displaying
 * 13 April 2025 - Updated code based on DeepSeek
 * 13 July 2025 - Increase size of Items when read
 * 16 August 2025 - Added Java Docs
 * 3 December 2025 - Increased version number
 */