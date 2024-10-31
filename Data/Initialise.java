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
	private Location[] locationList = new Location[noRooms];
	private Item[] itemList = new Item[noItems];
	
	
	public Initialise() {
		
		locationList[0] = null;
				
		for (int roomNumber=0;roomNumber<noRooms;roomNumber++) {
			System.out.printf("Room Numner %d%n",roomNumber);
			Location newLocation = new Location(RawData.getLocation(roomNumber),
												RawData.getPrepositions(),
												RawData.getDescription(roomNumber));
			locationList[roomNumber] = newLocation;
		}
	}
}

/* 30 October 2024 - Created File
 * 31 October 2024 - Added description to the locations.
 * 				   - Adjusted way to extract rooms
 */