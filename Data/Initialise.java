/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 30 October 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package Data;

public class Initialise {
	
	private int noRooms = 81;
	private Location[] locationList = new Location[noRooms];
	
	public Initialise() {
		
		locationList[0] = null;
		int roomNumber = 1;
		
		for (String location:RawData.getLocations()) {
			System.out.printf("Room Numner %d%n",roomNumber);
			Location newLocation = new Location(location,RawData.getPrepositions());
			locationList[roomNumber] = newLocation;
			roomNumber ++;
		}
	}
}

/*	30 October 2024 - Created File
*/