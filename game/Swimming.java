/*
Title: Island of Secrets Initialise Swimming Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 20 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.util.Random;

public class Swimming {
	
	private int swimming = 0;
	private int swimPosition = 0;
	private Random rand = new Random();

	public Swimming() {
		this.swimming = rand.nextInt(5)+1;
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
 * 20 July 2025 - Moved random position here.
 */
