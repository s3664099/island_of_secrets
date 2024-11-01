/*
Title: Island of Secrets Location Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.2
Date: 1 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class Location {
	
	private String name;
	private String description;
	private int[] exits = new int[4];
	private boolean visited = false;
	
	public Location(String name, String[] prepositions, String description) {		
		int prep = Integer.parseInt(name.substring(0,1));
		this.name = String.format("%s %s",prepositions[prep-1],name.substring(1,name.length()-4));
		this.description = description;
		int x=0;
		for (int i=name.length()-4;i<name.length();i++) {
			exits[x] = Integer.parseInt(name.substring(i,i+1));
			x++;
		}
	}
}
/* 30 October 2024 - Created File
 * 31 October 2024 - Added description
 * 1 November 2024 - Removed testing lines
*/