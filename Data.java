/*
Title: Island of Secrets Data Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 0.1
Date: 7 September 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

public class Data {
	
	private String[] data;
	private int[] intData;
	
	public Data(String[] data) {
		this.data = data;
	}
	
	public Data(String intData,int mod) {
		
		this.intData = new int[intData.length()];
		
		for (int x=0;x<intData.length();x++) {
			this.intData[x] = ((int) intData.charAt(x))-mod;
		}
	}
	
	public Data(String data,int len,boolean temp) {
		
		this.data = new String[data.length()/3];
		
		for (int x=0;x<data.length();x+=3) {
			this.data[x/3] = data.substring(x,x+3);			
		}
	}
	
	public String getStringData(int position) {
		
		return this.data[position-1];
	}
	
	public int getIntData(int position) {
		return this.intData[position-1];
	}
	
	public void updateIntData(int position,int newInt) {
		this.intData[position-1] = newInt;
	}
	
	public void modifyIntData() {
		for (int x=0;x<this.intData.length;x++) {
			if(this.intData[x]>127) {
				this.intData[x] = this.intData[x]-96;
			}
		}
	}
	
	public void getLength() {
		System.out.println(this.data.length);
	}
	
	public int getDataLength() {
		return this.data.length;
	}
	
	public void displayData() {
		
		for (int x=0;x<this.data.length;x++) {
			System.out.printf("%d %d) %s %n",x,x+1,this.data[x]);
		}
	}
}
/*
7 September 2024 - Created File
8 September 2024 - Added Constructors for other data styles
26 October 2024 - Updated the names of the method
*/