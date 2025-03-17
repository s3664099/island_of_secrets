/*
Title: Island of Secrets Special Item Handler Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 16 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.HashMap;
import java.util.Map;

import Data.Item;
import Data.Location;

public class SpecialItemHandler {

	private Map<Integer, String> itemDescriptions = new HashMap<>();
	
	private int ENTRANCE_CHAMBER = 27;
	private int GRANDPAS_HUT = 44;
	private int PYRAMID = 67;
	private int ABODE_HUT = 60;
	
	private int TORCH = 7;
	private int CHEST = 26;
	private int FLINT = 14;
	
	private int FLAG_HIDDEN = 9;
	
	
	public SpecialItemHandler() {
		itemDescriptions.put(45,"A tree bristling with apples");
		itemDescriptions.put(27,"A torch hanging in a bracket on the wall");
		itemDescriptions.put(44, "A coffee table against the wall, an open oak chest");
		itemDescriptions.put(67,"A piece of flint stuck n the crack");
		itemDescriptions.put(60, "A map, along with a collection of papers which seem to make up a diary");
	}
	
	public String getSpecialItems(int roomNumber,Item[] itemList, Location[] locationList) {
		
		String description = itemDescriptions.getOrDefault(roomNumber,"");
		
		if (roomNumber == GRANDPAS_HUT && itemList[CHEST].getItemFlag() !=1) {
			description = description.split(", ")[0];
		} else if ((roomNumber == ENTRANCE_CHAMBER && (itemList[TORCH].getItemLocation() != ENTRANCE_CHAMBER || itemList[TORCH].getItemFlag() != FLAG_HIDDEN ))
				|| (roomNumber == PYRAMID && (itemList[FLINT].getItemLocation() != PYRAMID || itemList[FLINT].getItemFlag() != FLAG_HIDDEN))
				|| (roomNumber == ABODE_HUT && !locationList[ABODE_HUT].getViewed())) {
			description = "";
		}
		return description;
	}	
}

/* 16 March 2025 - Created file
 */
