/*
Title: Island of Secrets Item Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

import java.io.Serializable;

public class Item implements Serializable {
	
	private static final long serialVersionUID = -2697850646469797958L;
	int itemFlag;
	int itemLocation;
	String item;
	boolean wisdomGained = false;
	
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
	
	public void setDescription(String description) {
		this.item = description;
	}
	
	public void setWisdomGain() {
		this.wisdomGained = !this.wisdomGained;
	}
	
	public boolean checkWisdomGain() {
		return this.wisdomGained;
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
 * 1 February 2025 - Added serialisable id and also option to change item description
 * 				   - Added flag to make sure wisdom is only gained once.
 * 5 March 2025 - Increased to v4.0
 */