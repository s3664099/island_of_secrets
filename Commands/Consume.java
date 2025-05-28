/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 28 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;

public class Consume {

	public ParsedCommand parseEat(ParsedCommand command) {
		
		int nounNumber = command.getNounNumber();
		String noun = command.getSplitTwoCommand()[1];
		
		if (noun.equals("food")) {
			nounNumber = 17;
		}
		
		return new ParsedCommand(command.getVerbNumber(),nounNumber,command.getCodedCommand(),
				command.getSplitTwoCommand(),command.getCommand());
	}
	
	
	
}

/*		
		
		//Eating lillies (moved here since in original game code wouldn't reach)
		if (noun == 3 && game.getItem(3).getItemLocation()==0) {
			player.setStat("wisdom",(int) player.getStat("wisdom")-5);
			player.setStat("strength",(float) player.getStat("strength")-2);
			game.addMessage("They make you very ill",true,true);
		
		//Item unedible
		} else if ((noun<=Constants.FOOD_THRESHOLD || noun>=Constants.DRINK_THRESHOLD) 
			&& nounStr.length()>0) {
			game.addMessage("You can't "+command,true,true);
			player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		
		//Eat
		} else {
			game.addMessage("You have no food",true,true);
			
			if (((int) player.getStat("food")+1)>0) {
				player.setStat("food",((int) player.getStat("food"))-1);
				player.setStat("strength",(float) player.getStat("strength")+10);
				game.addMessage("Ok",true,true);
			}
		}
 */

/* 28 May 2025 - Created File
*/