/*
Title: Island of Secrets Command Normaliser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 30 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

public class CommandNormaliser {

	public String normalise(String input) {

		input = input.toLowerCase();
		
		if (input.equals("u") || input.equals("up")) {
			input = "go up";
		} else if (input.equals("d") || input.equals("down")) {
			input = "go down";
		} else if (input.equals("i") || input.equals("enter") ||
				input.equals("inside") || input.equals("go inside")) {
			input = "go in";
		} else if (input.equals("o") || input.equals("exit") ||				
				input.equals("outside") || input.equals("go outside")) {
			input = "go out";
		} else if (input.equals("north")) {
			input = "n";
		} else if (input.equals("south")) {
			input = "s";
		} else if (input.equals("east")) {
			input = "e";
		} else if (input.equals("west")) {
			input = "w";
		}
				
		return input;
	}
	
}

/* 30 April 2025 - Created File
 */