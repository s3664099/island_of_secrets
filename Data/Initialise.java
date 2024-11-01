/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.1
Date: 30 October 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class Initialise {
	
	private int noRooms = 80;
	private int noItems = 51;
	private Location[] locationList = new Location[noRooms+1];
	private Item[] itemList = new Item[noItems+1];
	
	
	public Initialise() {
		
		locationList[0] = null;
		itemList[0] = null;
				
		for (int roomNumber=0;roomNumber<noRooms;roomNumber++) {
			System.out.printf("Room Number %d %s %n",roomNumber,RawData.getLocation(roomNumber));
			Location newLocation = new Location(RawData.getLocation(roomNumber),
												RawData.getPrepositions(),
												RawData.getDescription(roomNumber));
			locationList[roomNumber+1] = newLocation;
		}
		
		for (int itemNumber=1;itemNumber<noItems;itemNumber++) {
			System.out.printf("Item Number %d %s %n", itemNumber,RawData.getObjects(itemNumber));
			Item newItem == new Item(RawData.getObjects(itemNumber),)
		}
	}
}

/* 30 October 2024 - Created File
 * 31 October 2024 - Added description to the locations.
 * 				   - Adjusted way to extract rooms
 */