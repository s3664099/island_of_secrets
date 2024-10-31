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
	
	public Item(int flag,int location, String item, String description) {
		
		//Convert the location here
		
		this.itemFlag = flag;
		this.itemLocation = location;
		this.item = item;
		this.description = description;
	}

}
/* 31 October 2024 - Create class
 * 
 */