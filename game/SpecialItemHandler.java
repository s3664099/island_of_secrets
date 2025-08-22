/*
Title: Island of Secrets Special Item Handler Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 22 August 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import data.Constants;
import data.GameEntities;
import data.Item;
import data.Location;

public class SpecialItemHandler implements Serializable {

	private static final long serialVersionUID = -3392796825592359959L;

	private static final Map<Integer, String> itemDescriptions = new HashMap<>();	
	private static final String shackDescription = "A coffee table against the wall,";

	
	public SpecialItemHandler() {
		itemDescriptions.put(GameEntities.ROOM_CLEARING,"A tree bristling with apples");
		itemDescriptions.put(GameEntities.ROOM_ENTRANCE_CHAMBER,"A torch hanging in a bracket on the wall");
		itemDescriptions.put(GameEntities.ROOM_GRANDPAS_SHACK, "A coffee table against the wall, an open oak chest");
		itemDescriptions.put(GameEntities.ROOM_PYRAMID_SPLIT,"A piece of flint stuck in the crack");
		itemDescriptions.put(GameEntities.ROOM_OUTSIDE_HUT, "A map, along with a collection of papers which seem to make up a diary");
		itemDescriptions.put(GameEntities.ROOM_LAIR, "A parchment stuck amongst the mushrooms");
	}
	
	public String getSpecialItems(int roomNumber,Item[] itemList, Location[] locationList) {
		
		String description = itemDescriptions.getOrDefault(roomNumber,"");
		
		if (shouldSplitDescription(roomNumber,itemList)) {
			description = shackDescription;
		} else if (shouldHideTorch(roomNumber,itemList)
				|| (shouldHideFlint(roomNumber,itemList))
				|| (shouldHidePapers(roomNumber,locationList))
				|| (shouldHideParchment(roomNumber,itemList))) {
			description = "";
		}
		return description;
	}
	
	public boolean shouldSplitDescription(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_GRANDPAS_SHACK && itemList[GameEntities.ITEM_CHEST].getItemFlag() !=1);
	}
	
	public boolean shouldHideTorch(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_ENTRANCE_CHAMBER 
				&& (itemList[GameEntities.ITEM_TORCH].getItemLocation() != GameEntities.ROOM_ENTRANCE_CHAMBER 
				|| itemList[GameEntities.ITEM_TORCH].getItemFlag() != Constants.FLAG_HIDDEN));
	}
	
	public boolean shouldHideFlint(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_PYRAMID_SPLIT 
				&& (itemList[GameEntities.ITEM_FLINT].getItemLocation() != GameEntities.ROOM_PYRAMID_SPLIT 
				|| itemList[GameEntities.ITEM_FLINT].getItemFlag() != Constants.FLAG_HIDDEN));
	}
	
	public boolean shouldHidePapers(int roomNumber,Location[] locationList) {
		return (roomNumber == GameEntities.ROOM_OUTSIDE_HUT && !locationList[GameEntities.ROOM_OUTSIDE_HUT].getViewed());
	}
	
	public boolean shouldHideParchment(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_LAIR && 
				(itemList[GameEntities.ITEM_PARCHMENT].getItemLocation() != GameEntities.ROOM_LAIR)));
	}
}

/* 16 March 2025 - Created file
 * 17 March 2025 - Made class serialisable
 * 16 July 2025 - Fixed error with flint and added parchment
 * 17 July 2025 - Changed to GameEntities.
 * 22 August 2025 - Updated class to make it more readable
 */
