/*
Title: Island of Secrets Location Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package data;

import java.io.Serializable;
import java.util.Arrays;

public class Location implements Serializable {
	
	private static final long serialVersionUID = 7421397108414613755L;
	
	//Constants for string parsing
	private static final int PREPOSITION_INDEX = 0;
	private static final int NAME_INDEX = 1;
	private static final int EXIT_START_INDEX = 4;
	
	private final String name;
	private final boolean[] exits = new boolean[4];
	private boolean visited = false;
	private boolean viewed = false;
	private String roomType;
	
    /**
     * Constructs a location with the specified name, prepositions, and room type.
     *
     * @param name         The name of the location (e.g., "4the furthest depth of the forest1001").
     * @param prepositions The array of prepositions used to format the name.
     * @param roomType     The type of the room (e.g., "forest", "cave").
     * @throws IllegalArgumentException If the input parameters are invalid.
     */
	public Location(String name, String[] prepositions, String roomType) {		

		//Validate inputs
		if (name == null||name.length()<5) {
			throw new IllegalArgumentException("Invalid name format");
		}
		
		if (prepositions ==  null||prepositions.length==0) {
			throw new IllegalArgumentException("Prepositions array cannot be null or empty");
		}
		
		if (roomType == null||roomType.isEmpty()) {
			throw new IllegalArgumentException("Room type cannot be null or empty");
		}
				
		//Parse the name
		int prep = Integer.parseInt(name.substring(PREPOSITION_INDEX,NAME_INDEX));
		this.name = String.format("%s %s",prepositions[prep-1],name.substring(1,name.length()-EXIT_START_INDEX));
		
		//Parse the exits
		String exitString = name.substring(name.length()-EXIT_START_INDEX);
		for (int i=0;i<4;i++) {
			exits[i] = exitString.charAt(i) == '0';
		}
		
		this.roomType = roomType;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean[] getExits() {
		return this.exits;
	}
	
	public void setVisited() {
		this.visited = true;
	}
	
	public boolean getVisited() {
		return this.visited;
	}
	
	public void setViewed() {
		this.viewed = true;
	}
	
	public boolean getViewed() {
		return this.viewed;
	}
	
	public String getRoomType() {
		return this.roomType;
	}
	
	@Override
	public String toString() {
	    return "Location{" +
	            "name='" + name + '\'' +
	            ", exits=" + Arrays.toString(exits) +
	            ", visited=" + visited +
	            ", viewed=" + viewed +
	            ", roomType='" + roomType + '\'' +
	            '}';
	}
}
/* 30 October 2024 - Created File
 * 31 October 2024 - Added description
 * 1 November 2024 - Removed testing lines
 * 3 November 2024 - Added method to retrieve the location name & exits
 * 10 November 2024 - Removed description field
 * 9 December 2024 - Made class serializable
 * 23 December 2024 - Updated to version 2.
 * 25 December 2024 - Added roomType variable for the map. Changed the exits to booleans
 * 31 January 2025 - Completed Testing and increased version
 * 1 February 2025 - Added serialisable Id
 * 2 February 2025 - Added methods for the map panel.
 * 5 February 2025 - Added getter for visited
 * 21 February 2025 - Added viewed boolean
 * 5 March 2025 - Increased to v4.0
 * 9 March 2025 - Updated class based on recommendations
 * 3 December 2025 - Increased version number
*/