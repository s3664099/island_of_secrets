/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.3
Date: 3 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Data.Constants;
import Data.Item;
import Data.Location;
import Data.RawData;

public class Game {
	
	private int noRooms = Constants.noRooms;
	private int noItems = Constants.noItems;
	private Location[] locationList = new Location[noRooms+1];
	private Item[] itemList = new Item[noItems+1];
	private String message = "Let your quest begin";

	public Game() {
				
		locationList[0] = null;
		itemList[0] = null;
		
		//Builds the location objects
		for (int roomNumber=0;roomNumber<noRooms;roomNumber++) {

			Location newLocation = new Location(RawData.getLocation(roomNumber),
												RawData.getPrepositions(),
												RawData.getDescription(roomNumber));
			locationList[roomNumber+1] = newLocation;
		}
		
		//Builds the item objects
		for (int itemNumber=1;itemNumber<noItems;itemNumber++) {

			Item newItem = new Item(RawData.getItemFlag(itemNumber),
									RawData.getItemLocation(itemNumber),
									RawData.getObjects(itemNumber),
									RawData.getObjectDescription(itemNumber));
		}
	}
	
	public Location getRoom(int roomNumber) {
		return this.locationList[roomNumber];
	}
}

/* 30 October 2024 - Created File
 * 31 October 2024 - Added description to the locations.
 * 				   - Adjusted way to extract rooms
 * 1 November 2024 - Added the items
 * 3 November 2024 - Added method to retrieve the player's current location
 */