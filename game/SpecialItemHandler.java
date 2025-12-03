/*
Title: Island of Secrets Special Item Handler Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
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

/**
 * Computes special item descriptions for specific rooms based on game state.
 * <br>
 * This handler returns contextual descriptions (e.g., items mounted, hidden, or revealed)
 * depending on item flags/locations and whether a room has been viewed.
 * If no special description should be shown, an empty string is returned.
 */
public class SpecialItemHandler implements Serializable {

	private static final long serialVersionUID = -3392796825592359959L;

    /** Static mapping of room number to its base special description. */
	private static final Map<Integer, String> itemDescriptions = new HashMap<>();
	
    /**
     * Alternative description for Grandpa's shack when the chest is not open.
     * Used instead of splitting strings inline.
     */
	private static final String shackDescription = "A coffee table against the wall";
	private static final String treeDescription = "An apple tree with no apples";

    /**
     * Initializes the static special descriptions for known rooms.
     * Uses {@link GameEntities} room constants as keys.
     */
	public SpecialItemHandler() {
		itemDescriptions.put(GameEntities.ROOM_CLEARING,"A tree bristling with apples");
		itemDescriptions.put(GameEntities.ROOM_ENTRANCE_CHAMBER,"A torch hanging in a bracket on the wall");
		itemDescriptions.put(GameEntities.ROOM_GRANDPAS_SHACK, "A coffee table against the wall, an open oak chest");
		itemDescriptions.put(GameEntities.ROOM_PYRAMID_SPLIT,"A piece of flint stuck in the crack");
		itemDescriptions.put(GameEntities.ROOM_OUTSIDE_HUT, "A map, along with a collection of papers which seem to make up a diary");
		itemDescriptions.put(GameEntities.ROOM_SNELM_LAIR, "A parchment stuck amongst the mushrooms");
	}
	
    /**
     * Returns the context-sensitive special item description for a room.
     * <ul>
     *   <li>If the room has no special description, returns an empty string.</li>
     *   <li>If a special item is not present/visible (based on item flags/locations or room viewed state), returns an empty string.</li>
     *   <li>Grandpa's shack returns a shorter description when the chest is not open.</li>
     * </ul>
     *
     * @param roomNumber   the current room number
     * @param itemList     the array of {@link Item} instances (indexed by {@link GameEntities} item constants)
     * @param locationList the array of {@link Location} instances (indexed by room number)
     * @param appleCount   the number of apples on the tree
     * @return the special description for the room, or an empty string if none should be shown
     */
	public String getSpecialItems(int roomNumber,Item[] itemList, Location[] locationList,int appleCount) {
		
		String description = itemDescriptions.getOrDefault(roomNumber,"");
		
		if (shouldSplitDescription(roomNumber,itemList)) {
			description = shackDescription;
		} else if (shouldChangeApple(roomNumber,appleCount)) {
			description =  treeDescription;
		} else if (shouldHideTorch(roomNumber,itemList)
				|| (shouldHideFlint(roomNumber,itemList))
				|| (shouldHidePapers(roomNumber,locationList))
				|| (shouldHideParchment(roomNumber,itemList))) {
			description = "";
		}
		return description;
	}
	
    /**
     * Determines whether the Grandpa's shack description should be shortened.
     * The description is shortened when the chest item flag != 1.
     *
     * @param roomNumber the room number
     * @param itemList   the item array
     * @return true if the shortened shack description should be used
     */
	private boolean shouldSplitDescription(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_GRANDPAS_SHACK && itemList[GameEntities.ITEM_CHEST].getItemFlag() !=1);
	}
	
    /**
     * Determines whether the torch description in the entrance chamber should be hidden.
     * Hidden if the torch item is visible {@code Constants.FLAG_HIDDEN}.
     *
     * @param roomNumber the room number
     * @param itemList   the item array
     * @return true if the torch description should be hidden
     */
	private boolean shouldHideTorch(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_ENTRANCE_CHAMBER 
				&& (itemList[GameEntities.ITEM_TORCH].getItemLocation() != GameEntities.ROOM_ENTRANCE_CHAMBER 
				|| itemList[GameEntities.ITEM_TORCH].getItemFlag() != Constants.FLAG_HIDDEN));
	}
	
    /**
     * Determines whether the flint description in the pyramid split should be hidden.
     * Hidden if the flint item is visible {@code Constants.FLAG_HIDDEN}.
     *
     * @param roomNumber the room number
     * @param itemList   the item array
     * @return true if the flint description should be hidden
     */
	private boolean shouldHideFlint(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_PYRAMID_SPLIT 
				&& (itemList[GameEntities.ITEM_FLINT].getItemLocation() != GameEntities.ROOM_PYRAMID_SPLIT 
				|| itemList[GameEntities.ITEM_FLINT].getItemFlag() != Constants.FLAG_HIDDEN));
	}

    /**
     * Determines whether the papers/map description outside the hut should be hidden.
     * Hidden until the room has been viewed.
     *
     * @param roomNumber   the room number
     * @param locationList the location array
     * @return true if the papers description should be hidden
     */
	private boolean shouldHidePapers(int roomNumber,Location[] locationList) {
		return (roomNumber == GameEntities.ROOM_OUTSIDE_HUT && !locationList[GameEntities.ROOM_OUTSIDE_HUT].getViewed());
	}
	
    /**
     * Determines whether the parchment description in the lair should be hidden.
     * Hidden if the parchment iitem is visible
     *
     * @param roomNumber the room number
     * @param itemList   the item array
     * @return true if the parchment description should be hidden
     */
	private boolean shouldHideParchment(int roomNumber,Item[] itemList) {
		return (roomNumber == GameEntities.ROOM_SNELM_LAIR && 
				(itemList[GameEntities.ITEM_PARCHMENT].getItemLocation() != GameEntities.ROOM_SNELM_LAIR
				|| itemList[GameEntities.ITEM_PARCHMENT].getItemFlag() != 9));
	}
	
    /**
     * Determines which description of the apple tree is displayed.
     *
     * @param roomNumber the room number
     * @param appleCount the number of apples on the tree
     * @return true if there are no apples
     */
	private boolean shouldChangeApple(int roomNumber,int appleCount) {
		return (roomNumber == GameEntities.ROOM_CLEARING && appleCount == 0);
	}
}

/* 16 March 2025 - Created file
 * 17 March 2025 - Made class serialisable
 * 16 July 2025 - Fixed error with flint and added parchment
 * 17 July 2025 - Changed to GameEntities.
 * 22 August 2025 - Updated class to make it more readable. Added JavaDocs
 * 9 October 2025 - Added changing description if no apples are on the tree.
 * 				  - Fixed display for the parchment
 * 12 October 2025 - Fixed problem with operator precedence in special items for flint not displaying
 * 3 December 2025 - Increased version number
 */
