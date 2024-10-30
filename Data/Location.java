/*
Title: Island of Secrets Location Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 30 October 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class Location {
	
	private String name;
	private String description;
	private int[] exits = new int[4];
	private boolean visited = false;
	
	public Location(String name, String[] prepositions) {		
		int prep = Integer.parseInt(name.substring(0,1));
		this.name = String.format("%s %s",prepositions[prep-1],name.substring(1,name.length()-4));
		System.out.println(name);
		int x=0;
		for (int i=name.length()-4;i<name.length();i++) {
			System.out.println(name.substring(i,i+1));
			exits[x] = Integer.parseInt(name.substring(i,i+1));
			x++;
		}
	}
	
	public void displayLocation() {
		System.out.println(this.name);
	}

}
/* 30 October 2024 - Created File
 * 
*/