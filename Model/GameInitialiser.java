/*
Title: Island of Secrets Game Initialiser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 13 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.logging.Logger;

import Data.Constants;
import Data.Item;
import Data.Location;
import Data.RawData;

public final class GameInitialiser {

	private static final Logger logger = Logger.getLogger(GameInitialiser.class.getName());
	
	// Prevent instantiation
    private GameInitialiser() {  
        throw new AssertionError("Utility class should not be instantiated");
    }
	
    public static Game initialiseGame() {
    	return new Game(createLocations(),createItems(),new SpecialExitHandler());
    }
    
    private static Location[] createLocations() {
    	Location[] locations = new Location[Constants.NUMBER_OF_ROOMS+1];

    	//Room 0 is unused
    	locations[0] = null;
    	
		//Initialise locations
		// Start from 1 since 0 is unused
		for (int roomId=1;roomId<= Constants.NUMBER_OF_ROOMS;roomId++) {
			
			//Adjusted for 0-based raw data
			locations[roomId] = new Location(RawData.getLocation(roomId-1),
												RawData.getPrepositions(),
												RawData.getImage(roomId-1));
			
			final int id = roomId;
			logger.fine(() -> String.format("Initializing room %d", id));
		}
		
		return locations;
    }
    
	public static Item[] createItems() {
		
		//Initialise Items
		Item[] items = new Item[Constants.NUMBER_OF_NOUNS+1];
		
		//Item 0 is unused
		items[0] = null; 
		
		//Builds the item objects
		for (int itemId=1;itemId<=Constants.NUMBER_OF_NOUNS;itemId++) {
			System.out.println(itemId);
			
			//Checks the name to give the item
			String itemName = (itemId < Constants.NUMBER_OF_ITEMS)
					? RawData.getObjects(itemId):"";
			items[itemId] = new Item(RawData.getItemFlag(itemId),
										 RawData.getItemLocation(itemId),
										 itemName);
			
			final int id = itemId;
			logger.fine(() -> String.format("Initializing item %d", id));
		}
				
		return items;
	}
}

/* 15 March 2025 - Created File
 * 4 April 2025 - Fixed issue with first location not displaying
 * 13 April 2025 - Updated code based on DeepSeek
 */