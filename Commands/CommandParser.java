/*
Title: Island of Secrets Command Parser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 30 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.RawData;
import Game.Game;

public class CommandParser {
	
	private final CommandNormaliser normaliser;
	
	public CommandParser() {
		normaliser = new CommandNormaliser();
	}
	
	public ParsedCommand parse(String rawInput, Game game) {
		
		rawInput = normaliser.normalise(rawInput);
		String[] splitCommand = splitCommand(rawInput);
		int verbNumber = getVerbNumber(splitCommand[0]);
		int nounNumber = getNounNumber(splitCommand[1]);
		String codedCommand = codeCommand(splitCommand,nounNumber, game);
		
		return new ParsedCommand(verbNumber,nounNumber,codedCommand,splitCommand);
	}
	
	public String[] splitCommand(String rawInput) {
		
		String[] splitCommand = {"",""};
		String[] commands = rawInput.split(" ");
		splitCommand[0] = commands[0];
		
		if(commands.length>1) {
			splitCommand[1] = rawInput.substring(commands[0].length(),rawInput.length());
		}
		
		return splitCommand;
	}
	
	public int getVerbNumber(String verb) {
		
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
	
	public int getNounNumber(String noun) {
		
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
	
	private String codeCommand(String[] splitCommand, int nounNumber, Game game) {
		return "";
	}
}

/* 28 April 2025 - Created File
 * 30 April 2025 - Started building parser
 */