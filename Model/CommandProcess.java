/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.4
Date: 13 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Data.Constants;
import Data.Item;
import Data.RawData;

public class CommandProcess {
	
	private String[] splitCommand = {"",""};
	private String[] commands;
	private int verbNo;
	private int nounNo;
	private String codedCommand;
	private Commands command;
	
	public CommandProcess(String command,Game game) {
				
		command = command.toLowerCase();
		commands = command.split(" ");
		splitCommand[0] = commands[0];
		
		if (commands.length>1) {
			splitCommand[1] = command.substring(commands[0].length()).trim();
		} else {
			game.setMessage("Most commands need two words");
		}
	}
	
	public int getVerbNumber() {
		
		int verbNumber = Constants.noVerbs+1;
		int verbCount = 0;
		
		for (String command:RawData.getVerbs()) {
			verbCount ++;
			
			if (splitCommand[0].toLowerCase().equals(command)) {
				verbNumber = verbCount;
				this.verbNo = verbCount;
			}
		}
						
		return verbNumber;
	}
	
	public int getNounNumber() {
		
		int nounNumber = 52;
		int nounCount = 0;
				
		//Only called if more than two words
		if (commands.length>1) {
			for (String command:RawData.getNouns()) {
				nounCount ++;
								
				if (splitCommand[1].toLowerCase().equals(command)) {
					nounNumber = nounCount;
					this.nounNo = nounCount;
				}
			}
		} else {
			nounNumber = -1;
		}
		
		return nounNumber;
	}
	
	public String codeCommand(int room, int nounNumber, Item item) {
		
		String codedNoun = String.format("%d%d%d%d",nounNumber,item.getLocation(),item.getFlag(),room);
		codedNoun = String.valueOf(Integer.parseInt(codedNoun.trim()));
		this.codedCommand = codedNoun;
				
		return codedNoun;
	}
	
	public void executeCommand(Game game,Player player) {
		
		this.command = new Commands(verbNo,nounNo,codedCommand);
		
		//Movement Command
		if ((verbNo>0 || verbNo<6)) {
			this.command.move(game,player);
		}
		
	}
}

/* 9 November 2024 - Created method
 * 10 November 2024 - Added the verb count method
 * 11 November 2024 - Added the noun count method
 * 					- Got the command splitting working and sending correct errors
 * 					- Added method to process the coded command.
 * 12 November 2024 - Completed the codeCommand method
 * 13 November 2024 - Stored the variables 
 */