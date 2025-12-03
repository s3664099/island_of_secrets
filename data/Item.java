/*
Title: Island of Secrets Item Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package data;

import java.io.Serializable;

public class Item implements Serializable {
	
	private static final long serialVersionUID = -2697850646469797958L;
	private int itemFlag;
	private int itemLocation;
	private String itemName;
	private boolean wisdomAcquired = false;
	
	private static final int FLAG_OFFSET = 48; // ASCII value of '0'
	private static final int LOCATION_OFFSET = 32; // ASCII value of space
	private static final int LOCATION_ADJUSTMENT = 96; // Adjustment for values > 127
	private static final int ASCII_MAX = 127; // Adjustment for values > 127
	
	/**
     * Constructs an Item with the specified flag, location, and description.
     *
     * @param flag     The flag character (e.g., from ITEM_FLAG).
     * @param location The location character (e.g., from ITEM_LOCATION).
     * @param description The item's description.
     */
	public Item(char flag,char location, String item) {
				
		//Converts the strings to the appropriate int
		this.itemFlag = ((int) flag)-FLAG_OFFSET;
		this.itemLocation = ((int) location)-LOCATION_OFFSET;
		
		if (this.itemLocation>ASCII_MAX) {
			this.itemLocation -= LOCATION_ADJUSTMENT;
		}
				
		//Saves the descriptions
		this.itemName = item;
	}
	
	public String getItemName() {
		return this.itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public int getItemFlag() {
		return this.itemFlag;
	}
	
	public void setItemFlag(int flag) {
		this.itemFlag = flag;
	}
	
	public int getItemLocation() {
		return this.itemLocation;
	}
	
	public void setItemLocation(int newLocation) {
		this.itemLocation = newLocation;
	}
	
	//Checks if the item is present at the location
	public boolean isAtLocation(int location) {
		
		boolean itemPresent = false;
		
		if (location == this.itemLocation)  {
			itemPresent = true;
		}
		
		return itemPresent;
	}	
			
	public void setWisdomAcquired(boolean wisdonAcquired) {
		this.wisdomAcquired = wisdonAcquired;
	}
	
	public boolean hasWisdonAcquired() {
		return this.wisdomAcquired;
	}
	
    @Override
    public String toString() {
        return "Item{" +
                "description='" + itemName + '\'' +
                ", location=" + itemLocation +
                ", flag=" + itemFlag +
                ", wisdomGained=" + wisdomAcquired +
                '}';
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
 * 3 December 2025 - Increased version number
 */