/*
Title: Island of Secrets Special Exit Handler
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303

Remove magic numbers, and directions and use the GameEntities, plus special ones for the direction names (since already used in Game Entities)
*/

package game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import data.GameEntities;
import data.Item;

/**
 * Handles special exits for certain rooms in the adventure game.
 * <p>
 * Unlike normal exits, which are handled in a generic way, some rooms
 * have unique exits or additional descriptions that require custom logic.
 * This class manages those cases, ensuring the player receives the correct
 * directional and descriptive information when navigating.
 * </p>
 * 
 * <p>
 * Each special exit is stored in a {@link Map}, keyed by the room number
 * (from {@link GameEntities}). The value is a two-element {@code String[]},
 * where:
 * <ul>
 *   <li>Index 0 → The direction to suppress (the normal exit should not display).</li>
 *   <li>Index 1 → The description to display for the special exit.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * For example:  
 * {@code {WEST, "You can also go west into the cave"}}  
 * suppresses the default west exit, and instead provides a custom description.
 * </p>
 * 
 * This class is {@link Serializable} for compatibility with saved game states.
 */
public class SpecialExitHandler implements Serializable {

	private static final long serialVersionUID = 7662068425968354288L;

    /**
     * Stores special exits mapped to room numbers.
     * Each entry is a 2-element array:
     * <ul>
     *   <li>Index 0 → Exit direction (suppressed).</li>
     *   <li>Index 1 → Exit description (displayed).</li>
     * </ul>
     */
	private Map<Integer, String[]> specialExits = new HashMap<Integer, String[]>();

	 /** Extra description appended when a trapdoor is present. */
	private static final String TRAPDOOR_DESCRIPTION = " and a closed trapdoor in the floor";
	
    /** Common exit direction labels. */
	private static final String NORTH = "North";
	private static final String SOUTH = "South";
	private static final String EAST = "East";
	private static final String WEST = "West";
	
    /**
     * Constructs and populates the special exit mapping.
     * <p>
     * Loads all predefined room-to-exit associations from {@link GameEntities}.
     * These define the special cases where normal exits should be hidden
     * and replaced with custom descriptions.
     * </p>
     */
	public SpecialExitHandler() {
	    specialExits.put(GameEntities.ROOM_STOREROOM, new String[]{"","There is a door to the east"});
	    specialExits.put(GameEntities.ROOM_CAVE, new String[]{WEST,"You can also go west into the cave"});
	    specialExits.put(GameEntities.ROOM_BUILDING, new String[]{WEST,"You can also go west into the hut"});
	    specialExits.put(GameEntities.ROOM_CLEARING, new String[]{WEST,"You can also go west into the hut"});
	    specialExits.put(GameEntities.ROOM_ABODE_HUT, new String[]{NORTH,"You can also go north into the hut"});
	    specialExits.put(GameEntities.ROOM_CASTLE_WALL, new String[]{NORTH,"You can also go north into the portal"});
	    specialExits.put(GameEntities.ROOM_LAIR, new String[]{NORTH,"You can also go east out of the lair"});
	    specialExits.put(GameEntities.ROOM_HUT, new String[]{NORTH,"You can also go north out of the hut"});
	    specialExits.put(GameEntities.ROOM_LOGMAN_HUT, new String[]{NORTH,"You can also go north out of the cabin"});
	    specialExits.put(GameEntities.ROOM_PYRAMID_ROOF, new String[]{NORTH,"You can also go north down of the pyramid"});
	    specialExits.put(GameEntities.ROOM_OUTSIDE_HUT, new String[]{SOUTH,"You can go south out of the hut"});
	    specialExits.put(GameEntities.ROOM_PYRAMID_STEP, new String[]{SOUTH,"You can also go south up the pyramid"});
	    specialExits.put(GameEntities.ROOM_GRANDPAS_SHACK, new String[]{EAST,"You can go east out of the shack"});
	    specialExits.put(GameEntities.ROOM_LOGMAN_HALL, new String[]{EAST,"You can go east out of the hall"});
	}
	
    /**
     * Determines whether a normal exit should be displayed.
     * <p>
     * If the given room has a special exit that overrides the specified direction,
     * this method will return {@code false} to prevent duplication.
     * Otherwise, the normal exit is shown.
     * </p>
     * 
     * @param roomNumber the ID of the current room
     * @param exit       the exit direction under consideration
     * @return {@code true} if the normal exit should be displayed,  
     *         {@code false} if it is suppressed by a special exit
     */
	public boolean displayExit(int roomNumber,String exit) {
		
		String[] exitDescriptions =  specialExits.getOrDefault(roomNumber, new String[] {"",""});
		return !exitDescriptions[0].equals(exit);
	}
	
    /**
     * Retrieves the special exit description for a given room.
     * <p>
     * If the room has a custom exit description, that string is returned.
     * In the storeroom, if the trapdoor is still closed (based on the item state),
     * an additional line about the trapdoor is appended.
     * </p>
     * 
     * @param roomNumber the ID of the current room
     * @param itemList   the list of all items, used to check trapdoor state
     * @return the custom exit description, or an empty string if none exists
     */
	public String getSpecialExit(int roomNumber, Item[] itemList) {
		
		String[] exitDescriptions = specialExits.getOrDefault(roomNumber, new String[]{"", ""});
		String baseDescription = exitDescriptions[1];
		
		//Is player in the trapdoor room
		if (roomNumber == GameEntities.ROOM_STOREROOM && itemList[GameEntities.ITEM_TRAPDOOR].getItemFlag() !=0) {
			baseDescription += TRAPDOOR_DESCRIPTION;
		}
		
		return baseDescription;
	}
}

/* 15 March 2025 - Created File
 * 17 March 2025 - Made class serialisable
 * 6 July 2025 - Updated directions out of cave
 * 18 July 2025 - Move hardcoded trapdoor section to GameEntities.
 * 23 August 2025 - Updated Class by removing magic numbers. Added JavaDocs
 * 3 December 2025 - Increased version number
 */
