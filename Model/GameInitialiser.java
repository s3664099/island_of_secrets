/*
Title: Island of Secrets Game Initialiser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 15 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Data.Constants;
import Data.Item;
import Data.Location;
import Data.RawData;

public class GameInitialiser {

	public static Game initialiseGame() {
		
		int noRooms = Constants.NUMBER_OF_ROOMS;
		int noItems = Constants.NUMBER_OF_NOUNS;
		
		Location[] locations = new Location[noRooms+1];
		locations[0] = null; //Room 0 is unused
		
		//Initialise locations
		for (int roomNumber=1;roomNumber<noRooms;roomNumber++) {
						
			locations[roomNumber+1] = new Location(RawData.getLocation(roomNumber),
												RawData.getPrepositions(),
												RawData.getImage(roomNumber));
		}
		
		//Initialise Items
		Item[] items = new Item[noItems+1];
		items[0] = null; //Item 0 is unused

		//Builds the item objects
		for (int itemNumber=1;itemNumber<=noItems;itemNumber++) {
			
			//Checks the name to give the item
			String itemName = (itemNumber <= Constants.NUMBER_OF_ITEMS)
					? RawData.getObjects(itemNumber):"";
						
			items[itemNumber] = new Item(RawData.getItemFlag(itemNumber),
										 RawData.getItemLocation(itemNumber),
										 itemName);			
		}
				
		return new Game(locations,items,new SpecialExitHandler());
	}
}

/* 15 March 2025 - Created File
 */