/*
Title: Island of Secrets Item Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 31 October 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class Item {
	
	int itemFlag;
	int itemLocation;
	String item;
	String description;
	
	public Item(char flag,char location, String item, String description) {
		
		//Converts the strings to the appropriate int
		this.itemFlag = ((int) flag)-48;
		this.itemLocation = ((int) location)-32;
		
		if (this.itemLocation>127) {
			this.itemLocation -= 96;
		}
		
		//Saves the descriptions
		this.item = item;
		this.description = description;
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

}
/*
 * 
 */