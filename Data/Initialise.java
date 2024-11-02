/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.2
Date: 1 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class Initialise {
	
	private int noRooms = Constants.noRooms;
	private int noItems = Constants.noItems;
	private Location[] locationList = new Location[noRooms+1];
	private Item[] itemList = new Item[noItems+1];
	
	public Initialise() {
		
		//Create Second Class to hold player data
		//Update the readme when the initialisation has been completed
		
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
}

/* 30 October 2024 - Created File
 * 31 October 2024 - Added description to the locations.
 * 				   - Adjusted way to extract rooms
 * 1 November 2024 - Added the items
 */