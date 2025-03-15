/*
Title: Island of Secrets Quit Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 15 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.HashMap;
import java.util.Map;

public class SpecialExitHandler {

	//Map holds an array of 2 strings - direction & special direction name
	private Map<Integer, String[]> specialExits = new HashMap();

	private int TRAPDOOR = 29;
	private int TRAPDOOR_ROOM = 51;
	
	public SpecialExitHandler() {
		
		//Load Special Exits
	    specialExits.put(51, new String[]{"","There is a door to the east"});
	    specialExits.put(12, new String[]{"West","You can also go west into the cave"});
	    specialExits.put(53, new String[]{"West","You can also go west into the hut"});
	    specialExits.put(45, new String[]{"West","You can also go west into the hut"});
	    specialExits.put(70, new String[]{"North","You can also go north into the hut"});
	    specialExits.put(37, new String[]{"North","You can also go north into the portal"});
	    specialExits.put(11, new String[]{"North","You can also go north out of the lair"});
	    specialExits.put(41, new String[]{"North","You can also go north out of the hut"});
	    specialExits.put(43, new String[]{"North","You can also go north out of the cabin"});
	    specialExits.put(66, new String[]{"North","You can also go north down of the pyramid"});
	    specialExits.put(60, new String[]{"South","You can go south out of the hut"});
	    specialExits.put(56, new String[]{"South" ,"You can also go south up the pyramid"});
	    specialExits.put(44, new String[]{"East","You can go east out of the shack"});
	    specialExits.put(52, new String[]{"East","You can go east out of the hall"});
	}
	
	public boolean displayExit(int roomNumber,String exit) {
		
		boolean displayExit = true;
		String[] exitDescriptions =  specialExits.getOrDefault(roomNumber, new String[] {"",""});
		
		if (exitDescriptions[0].equals(exit)) {
			displayExit = false;
		}
		
		return displayExit;
	}
}

/* 15 March 2025 - Created File
 * 
 */
