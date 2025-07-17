/*
Title: Island of Secrets Special Item Handler Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 17 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import Data.GameEntities;
import Data.Item;
import Data.Location;

public class SpecialItemHandler implements Serializable {

	private static final long serialVersionUID = -3392796825592359959L;

	private Map<Integer, String> itemDescriptions = new HashMap<>();	
	private int FLAG_HIDDEN = 9;
	
	public SpecialItemHandler() {
		itemDescriptions.put(45,"A tree bristling with apples");
		itemDescriptions.put(27,"A torch hanging in a bracket on the wall");
		itemDescriptions.put(44, "A coffee table against the wall, an open oak chest");
		itemDescriptions.put(67,"A piece of flint stuck in the crack");
		itemDescriptions.put(60, "A map, along with a collection of papers which seem to make up a diary");
		itemDescriptions.put(11, "A parchment stuck amongst the mushrooms");
	}
	
	public String getSpecialItems(int roomNumber,Item[] itemList, Location[] locationList) {
		
		String description = itemDescriptions.getOrDefault(roomNumber,"");
		
		if (roomNumber == GameEntities.ROOM_GRANDPAS_SHACK && itemList[GameEntities.ITEM_CHEST].getItemFlag() !=1) {
			description = description.split(", ")[0];
		} else if ((roomNumber == GameEntities.ROOM_ENTRANCE_CHAMBER 
				&& (itemList[GameEntities.ITEM_TORCH].getItemLocation() != GameEntities.ROOM_ENTRANCE_CHAMBER 
				|| itemList[GameEntities.ITEM_TORCH].getItemFlag() != FLAG_HIDDEN ))
				|| (roomNumber == GameEntities.ROOM_PYRAMID_SPLIT 
				&& (itemList[GameEntities.ITEM_FLINT].getItemLocation() != GameEntities.ROOM_PYRAMID_SPLIT 
				|| itemList[GameEntities.ITEM_FLINT].getItemFlag() != FLAG_HIDDEN))
				|| (roomNumber == GameEntities.ROOM_OUTSIDE_HUT && !locationList[GameEntities.ROOM_OUTSIDE_HUT].getViewed()
				|| (roomNumber == GameEntities.ROOM_LAIR && 
					(itemList[GameEntities.ITEM_PARCHMENT].getItemLocation() != GameEntities.ROOM_LAIR)))) {
			description = "";
		}
		return description;
	}	
}

/* 16 March 2025 - Created file
 * 17 March 2025 - Made class serialisable
 * 16 July 2025 - Fixed error with flint and added parchment
 * 17 July 2025 - Changed to GameEntities.
 */
