/*
Title: Island of Secrets Command Parser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 22 May 2025
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
		ParsedCommand command = new ParsedCommand(verbNumber,nounNumber,codedCommand,splitCommand,rawInput);
		
		if (splitCommand[0].equals("look")) {
			command = parseLook(splitCommand,command);
		} else if (command.checkMoveState()) {
			command = parseMove(command,room);
		}
		
		return command;
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
		
	private ParsedCommand parseLook(String[] splitCommand,ParsedCommand command) {
			
		if (splitCommand[1].length()==0) {
			splitCommand[1] = "room";
		}
		
		splitCommand[0] = "examine";
		int verbNumber = 33;
		
		return new ParsedCommand(verbNumber,command.getNounNumber(),command.getCodedCommand(),
								splitCommand,command.getCommand());
	}
	
	private ParsedCommand parseMove(ParsedCommand command,int room) {
		return new Move().parseMove(command, room);
	}
	
	private class CommandNormaliser {

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
}

/* 28 April 2025 - Created File
 * 30 April 2025 - Started building parser
 * 1 May 2025 - Completed parser
 * 2 May 2025 - Updated for command validator
 * 3 May 2025 - Added Parse Look method
 * 4 May 2025 - Added Parse Move method
 * 22 May 2025 - Moved CommandNormaliser here as private class
 */