/*
Title: Island of Secrets Data Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 0.0
Date: 7 September 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

public class Data {
	
	private String[] data;
	
	public Data(String[] data) {
		this.data = data;
	}
	
	public String retrieveData(int position) {
		
		String retrievedData = this.data[position-1];
		
		return retrievedData;
	}

}
