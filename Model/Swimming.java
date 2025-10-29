/*
Title: Island of Secrets Initialise Swimming Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 15 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

public class Swimming {
	
	private int swimming = 0;
	private int swimPosition = 0;

	public Swimming(int swimming) {
		this.swimming = swimming;
	}
	
	public void swim() {
		this.swimPosition++;
	}
	
	public boolean checkPosition(float strength) {
		
		boolean checked = false;
		
		if ((this.swimming/2)<this.swimPosition && strength>0) {
			checked = true;
		}
		
		return checked;
	}
}

/*15 March 2025 - Created File
 */
