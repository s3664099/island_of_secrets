/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 9 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

public class Commands {
	
	String[] splitCommand = {"",""};
	int verbNo;
	int nounNo;
	
	public Commands(String command) {
				
		command = command.toLowerCase();
		String[] commands = command.split(" ");
		splitCommand[0] = commands[0];
		
		if (commands.length>1) {
			splitCommand[1] = command.substring(commands[0].length()).trim();
		}
		
		
	}
	
	private int getVerbNumber(String verb) {
		
		int verbNumber = -1;
		//Goes through each of the verbs and matches. If match then returns the number
		//Otherwise returns -1 indicating 
		//240 FOR I=1 TO V
		//250 IF LEFT$(C$,3)=MID$(V$,3*(I-1)+1,3) THEN LET A=I
		//260 NEXT I
		//270 GOSUB 760
				
		return verbNumber;
	}
	
	private int getNounNumber(String noun) {
		
		int nounNumber = -1;
		//Goes through two sets of nouns - simply and full
		//If simply found all good if not checks for full
		//760 IF LEN(X$)<3 THEN LET X$=X$+"???"
		//		770 FOR I=1 TO W
		//		780 IF LEFT$(X$,3)=MID$(Z$,3*(I-1)+1,3) THEN LET O=I
		//		790 NEXT I:IF O=0 THEN LET O=52
		//		800 RETURN
		
		return nounNumber;
	}
}

/* 9 November 2024 - Created method
 *
 */