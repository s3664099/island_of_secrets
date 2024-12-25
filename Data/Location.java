/*
Title: Island of Secrets Location Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.0
Date: 23 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

import java.io.Serializable;

public class Location implements Serializable {
	
	private String name;
	private boolean[] exits = new boolean[4];
	private boolean visited = false;
	private int roomType;
	
	public Location(String name, String[] prepositions) {		

		int prep = Integer.parseInt(name.substring(0,1));
		this.name = String.format("%s %s",prepositions[prep-1],name.substring(1,name.length()-4));
		int x=0;

		//Determines the exits from the room
		for (int i=name.length()-4;i<name.length();i++) {
			
			if (name.substring(i,i+1).equals("0")) {
				exits[x] = true;
			} else {
				exits[x] = false;
			}
			x++;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean[] getExits() {
		return this.exits;
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
*/