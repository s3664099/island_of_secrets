/*
Title: Island of Secrets Command Parser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 1 May 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.Item;
import Data.RawData;
import Game.Game;

public class CommandParser {
	
	private final CommandNormaliser normaliser;
	
	public CommandParser() {
		normaliser = new CommandNormaliser();
	}
	
	public ParsedCommand parse(String rawInput, Game game, int room) {
		
		rawInput = normaliser.normalise(rawInput);
		String[] splitCommand = splitCommand(rawInput);
		int verbNumber = getVerbNumber(splitCommand[0]);
		int nounNumber = getNounNumber(splitCommand[1]);
		String codedCommand = codeCommand(splitCommand,nounNumber,game,room);
		
		return new ParsedCommand(verbNumber,nounNumber,codedCommand,splitCommand);
	}
	
	private String[] splitCommand(String rawInput) {
		
		String[] splitCommand = {"",""};
		String[] commands = rawInput.split(" ");
		splitCommand[0] = commands[0];
		
		if(commands.length>1) {
			splitCommand[1] = rawInput.substring(commands[0].length(),rawInput.length());
		}
		
		return splitCommand;
	}
	
	private int getVerbNumber(String verb) {
		
		int verbNumber = Constants.NUMBER_OF_VERBS+1;
		int verbCount = 0;
		
		for (String command:RawData.getVerbs()) {
			verbCount ++;
			
			if (verb.equals(command)) {
				verbNumber = verbCount;
			}
		}
		
		return verbNumber;
	}
	
	private int getNounNumber(String noun) {
		
		int nounNumber = Constants.NUMBER_OF_NOUNS;
				
		//Only called if more than two words
		if (noun.length()>1) {
			
			int nounCount = 0;
			for (String command:RawData.getNouns()) {
				nounCount ++;
								
				if (noun.equals(command)) {
					nounNumber = nounCount;
				}
			}
		} else {
			nounNumber = -1;
		}
		return nounNumber;
	}
	
	private String codeCommand(String[] splitCommand, int nounNumber, Game game, int room) {
		
		Item item = game.getItem(nounNumber);
		String codedNoun = String.format("%d%d%d%d",nounNumber,Math.abs(item.getItemLocation()),
		Math.abs(item.getItemFlag()),room);
		codedNoun = String.valueOf(Integer.parseInt(codedNoun.trim()));
		
		return codedNoun;
	}
}

/* 28 April 2025 - Created File
 * 30 April 2025 - Started building parser
 * 1 May 2025 - Completed parser
 */