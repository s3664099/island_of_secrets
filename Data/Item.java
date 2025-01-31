/*
Title: Island of Secrets Item Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.0
Date: 31 January 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

import java.io.Serializable;

public class Item implements Serializable {
	
	int itemFlag;
	int itemLocation;
	String item;
	
	public Item(char flag,char location, String item) {
				
		//Converts the strings to the appropriate int
		this.itemFlag = ((int) flag)-48;
		this.itemLocation = ((int) location)-32;
		
		if (this.itemLocation>127) {
			this.itemLocation -= 96;
		}
				
		//Saves the descriptions
		this.item = item;
	}
	
	public String getItem() {
		return this.item;
	}
	
	//Checks if the item is present at the location
	public boolean checkLocation(int location) {
		
		boolean itemPresent = false;
		
		if (location == this.itemLocation)  {
			itemPresent = true;
		}
		
		return itemPresent;
	}
	
	public int getFlag() {
		return this.itemFlag;
	}
	
	public int getLocation() {
		return this.itemLocation;
	}
	
	public void setLocation(int newLocation) {
		this.itemLocation = newLocation;
	}
	
	public void setFlag(int flag) {
		this.itemFlag = flag;
	}

}

/*
 * 31 October 2024 - Created File
 * 4 November 2024 - Added code to retrieve name & location
 * 5 November 2024 - Added section to retrieve item flag
 * 10 November 2024 - Removed description field
 * 11 November 2024 - Added method to retrieve the item location
 * 17 November 2024 - Added setters for location & flag
 * 9 December 2024 - Made class serializable
 * 23 December 2024 - Updated to version 2.
 * 31 January 2025 - Completed Testing and increased version
 */