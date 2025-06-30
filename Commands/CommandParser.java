/*
Title: Island of Secrets Command Parser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.13
Date: 29 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.GameEntities;
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
		rawInput = parseMovement(rawInput);
		
		//Separate parse give here. If it is set to give:
			//Goes to separate function
			//Splits command, if first word 'to' or single word not a verb sets it as 'give x to x'
			//Otherwise returns it as a normal command
			//Resets the give status in game
		
		String[] splitCommand = splitCommand(rawInput);
		splitCommand[1] = splitCommand[1].trim();
		int verbNumber = getVerbNumber(splitCommand[0]);
		int nounNumber = getNounNumber(splitCommand[1],verbNumber);
		String codedCommand = codeCommand(splitCommand,nounNumber,game,room);
		ParsedCommand command = new ParsedCommand(verbNumber,nounNumber,codedCommand,splitCommand,rawInput);

		if (splitCommand[0].equals("look")) {
			command = parseLook(splitCommand,command,room);
		} else if (command.checkMoveState()) {
			command = parseMove(command,room);
		} else if (command.checkEat()) {
			command = parseEat(command);
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
	
	private int getNounNumber(String noun,int verbNumber) {
		
		int nounNumber = Constants.NUMBER_OF_NOUNS;
				
		//Only called if more than two words
		if (noun.length()>1) {
			
			//Does not contain more than one word?
			if (noun.split(" ").length>1) {
				noun = noun.split(" ")[0];
			}
			
			int nounCount = 0;
			for (String command:RawData.getNouns()) {
				nounCount ++;
				if (noun.equals(command)) {
					nounNumber = nounCount;
				}
			}
		} else {
			nounNumber = -1;
			
			if(verbNumber>0 && verbNumber<5) {
				nounNumber = new Move().parseSingleDirection(nounNumber, verbNumber);
			}
		}
		return nounNumber;
	}
	
	private String codeCommand(String[] splitCommand, int nounNumber, Game game, int room) {
		
		String codedCommand = "";
		
		if (nounNumber != -1) {
			Item item = game.getItem(nounNumber);
			codedCommand = String.format("%d%d%d%d",nounNumber,Math.abs(item.getItemLocation()),
										 Math.abs(item.getItemFlag()),room);
			codedCommand = String.valueOf(Integer.parseInt(codedCommand.trim()));
		}
		
		return codedCommand;
	}
		
	private ParsedCommand parseLook(String[] splitCommand,ParsedCommand command,int room) {
			
		if (splitCommand[1].length()==0) {
			splitCommand[1] = "room";
		}
		
		if (splitCommand[1].equals("well") && room==GameEntities.ROOM_WELL) {
			splitCommand[1]="room";
		}
				
		splitCommand[0] = "examine";
		int verbNumber = GameEntities.CMD_SCRATCH;
		
		return new ParsedCommand(verbNumber,command.getNounNumber(),command.getCodedCommand(),
								splitCommand,command.getCommand());
	}
	
	private ParsedCommand parseMove(ParsedCommand command,int room) {
		return new Move().parseMove(command, room);
	}
	
	private ParsedCommand parseEat(ParsedCommand command) {
		return new Consume().parseEat(command);
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
	
	public String parseMovement(String command) {
		
		if (command.equals("in") || command.equals("out") ||
			command.equals("up") || command.equals("down")) {
			command = "go "+command;
		}
				
		return command;
		
	}
}

/* 28 April 2025 - Created File
 * 30 April 2025 - Started building parser
 * 1 May 2025 - Completed parser
 * 2 May 2025 - Updated for command validator
 * 3 May 2025 - Added Parse Look method
 * 4 May 2025 - Added Parse Move method
 * 22 May 2025 - Moved CommandNormaliser here as private class
 * 28 May 2025 - Added parsing for eating
 * 2 June 2025 - Added further parsing for examine
 * 22 June 2025 - Fixed problem where negative nounNumber blocked program.
 * 23 June 2025 - Stripped whitespace from noun
 * 24 June 2025 - Added parser for single command movement commands
 * 29 June 2025 - Fixed problem with multiple words in noun.
 */