/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.2
Date: 11 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Data.RawData;

public class Commands {
	
	String[] splitCommand = {"",""};
	int verbNo;
	int nounNo;
	
	public Commands(String command,Game game) {
				
		command = command.toLowerCase();
		String[] commands = command.split(" ");
		splitCommand[0] = commands[0];
		
		if (commands.length>1) {
			splitCommand[1] = command.substring(commands[0].length()).trim();
		} else {
			game.setMessage("Most commands need two words");
		}
	}
	
	public int getVerbNumber() {
		
		int verbNumber = 0;
		int verbCount = 0;
		
		for (String command:RawData.getVerbs()) {
			verbCount ++;
			System.out.println(command);
			if (splitCommand[0].toLowerCase().equals(command)) {
				verbNumber = verbCount;
			}
		}
		
		System.out.println(verbNumber);
				
		return verbNumber;
	}
	
	public int getNounNumber() {
		
		int nounNumber = 52;
		int nounCount = 0;
		
		//Only called if more than two words
		if (splitCommand.length>1) {
			for (String command:RawData.getNouns()) {
				nounCount ++;
				System.out.println(command);
				if (splitCommand[1].toLowerCase().equals(command)) {
					nounNumber = nounCount;
				}
			}
		}
		
		return nounNumber;
	}
}

/* 9 November 2024 - Created method
 * 10 November 2024 - Added the verb count method
 * 11 November 2024 - Added the noun count method
 */