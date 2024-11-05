/*
Title: Island of Secrets Initialise Game Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.5
Date: 4 November 2024
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
			itemList[itemNumber] = newItem;;
		}
	}
	
	public String getRoomName(int roomNumber) {
		return this.locationList[roomNumber].getName();
	}
	
	//Goes through the items and checks what is present
	public String getItems(int roomNumber) {
				
		int count = 0;
		String items = "";
		
		//Goes through each of the items
		for (Item item:itemList) {
			if(item != null) {
				
				//If the items are visible display them.
				if(item.checkLocation(roomNumber) || item.getFlag()<1) {
					
					count ++;
					if (count>1) {
						items = String.format("%s, %s",items,item.getItem());
					} else {
						items = String.format("%s %s",items,item.getItem());
					}
				}
			}
		}
		
		if (count>0) {
			items = String.format("%s %s","You see:",items);
		}
		
		return items;
	}
	
	public String getExits(int roomNumber) {
		
		int[] exitNumbers = locationList[roomNumber].getExits();
		String exits = "";
		
		if (exitNumbers[0] == 0) {
			exits = addExit("North",exits);
		}
		
		if (exitNumbers[1] == 0) {
			exits = addExit("South",exits);
		}
		
		if (exitNumbers[2] == 0) {
			exits = addExit("East",exits);
		}
		
		if (exitNumbers[3] == 0) {
			exits = addExit("West",exits);
		}
		
		if (exits.length()>0) {
			exits = String.format("You can go:%s",exits);
		}
		
		return exits;
	}
	
	//Checks to see if an exit has already been added
	private String addExit(String exit, String exits) {
		
		if (exits.length()>0) {
			exits = String.format("%s, %s",exits,exit);
		} else {
			exits = String.format("%s %s",exits,exit);
		}
		
		return exits;
	}
}

/* 30 October 2024 - Created File
 * 31 October 2024 - Added description to the locations.
 * 				   - Adjusted way to extract rooms
 * 1 November 2024 - Added the items
 * 3 November 2024 - Added method to retrieve the player's current location
 * 4 November 2024 - Added method to retrieve items at the player's location\
 * 5 November 2024 - Updated get items method. Added get exits method
 */