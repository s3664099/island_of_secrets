/*
Title: Island of Secrets Command Parser
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import commands.Consume;
import commands.Move;
import data.Constants;
import data.GameEntities;
import data.Item;
import data.RawData;
import game.Game;

/**
 * CommandParser is responsible for parsing raw user input into structured
 * {@link ParsedCommand} objects that the game engine can interpret.
 * <p>
 * It handles normalization of shorthand commands, identification of verbs and nouns,
 * and special cases such as movement, eating, and looking at objects.
 */
public class CommandParser {
		
	private final CommandNormaliser normaliser;
	
	/**
	 * Constructs a new {@code CommandParser} with its own internal
	 * {@link CommandNormaliser}.
	 */
	public CommandParser() {
		normaliser = new CommandNormaliser();
	}
	
	/**
	 * Parses the raw input string into a {@link ParsedCommand}, performing
	 * normalization, verb/noun lookup, and context-sensitive adjustments.
	 *
	 * @param rawInput the raw user input string
	 * @param game the current game instance, used for item lookups and state checks
	 * @param room the current room identifier where the command is issued
	 * @return a fully parsed {@link ParsedCommand} instance
	 */
	public ParsedCommand parse(String rawInput, Game game, int room) {
		
		rawInput = normaliser.normalise(rawInput);
		rawInput = parseMovement(rawInput);
		String[] splitCommand = splitCommand(rawInput);
		int verbNumber = getVerbNumber(splitCommand[0]);
		
		if (isLily(splitCommand[1].trim())) {
			splitCommand[1] = splitCommand[1].replace(GameEntities.NOUN_LILY, GameEntities.NOUN_FLOWER);
			rawInput = rawInput.replace(GameEntities.NOUN_LILY, GameEntities.NOUN_FLOWER);
		}
			
		if (game.isGiveState()) {
			boolean giveResponse = false;
			if(splitCommand[0].equals(GameEntities.WORD_TO)) {
				rawInput = GameEntities.WORD_GIVE+GameEntities.SPACE+game.getGiveNoun()+GameEntities.SPACE+rawInput;
				giveResponse = true;
			} else if (verbNumber == GameEntities.CMD_NO_VERB) {
				rawInput = GameEntities.WORD_GIVE+GameEntities.SPACE+game.getGiveNoun()+GameEntities.SPACE
						+GameEntities.WORD_TO+GameEntities.SPACE+rawInput;
				giveResponse = true;
			}
			
			if (giveResponse) {
				splitCommand = splitCommand(rawInput);
				verbNumber = getVerbNumber(splitCommand[0]);
			}
		}
		
		splitCommand[1] = splitCommand[1].trim();
		int nounNumber = getNounNumber(splitCommand[1],verbNumber);
		String codedCommand = codeCommand(splitCommand,nounNumber,game,room);
		ParsedCommand command = new ParsedCommand(verbNumber,nounNumber,codedCommand,splitCommand,rawInput);

		if (splitCommand[0].equals(GameEntities.WORD_LOOK)) {
			command = parseLook(splitCommand,command,room);
		} else if (command.checkMoveState()) {
			command = parseMove(command,room);
		} else if (command.checkEat()) {
			command = parseEat(command);
		}
		
		return command;
	}
	
	/**
	 * Splits a raw command string into verb and noun components.
	 * <p>
	 * Always returns an array of length 2. The first element is the verb,
	 * the second is the remainder (noun phrase or empty string).
	 *
	 * @param rawInput the normalized command string
	 * @return a two-element array: [verb, noun phrase]
	 */
	private String[] splitCommand(String rawInput) {
		
		String[] splitCommand = {"",""};
		String[] commands = rawInput.split(GameEntities.SPACE);
		splitCommand[0] = commands[0];
		
		if(commands.length>1) {
			splitCommand[1] = rawInput.substring(commands[0].length(),rawInput.length());
			if (isBoatman(splitCommand[1])) {
				splitCommand[1] = setBoat();
			}
		}
		
		return splitCommand;
	}
	
	/**
	 * Retrieves the verb index corresponding to a given verb string.
	 * <p>
	 * Verbs are matched against the list provided by {@link RawData#getVerbs()}.
	 *
	 * @param verb the verb string to look up
	 * @return the verb number if found, otherwise {@code Constants.NUMBER_OF_VERBS + 1}
	 */
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
	
	/**
	 * Retrieves the noun index corresponding to a given noun string.
	 * <p>
	 * Handles single-word and multi-word nouns, as well as special cases such
	 * as directional movement commands.
	 *
	 * @param noun the noun string to look up
	 * @param verbNumber the associated verb number, used for context-sensitive resolution
	 * @return the noun number if found, otherwise -1
	 */
	private int getNounNumber(String noun,int verbNumber) {

		if (isLily(noun)) {
			noun = setFlower();
		} else if (isBoatman(noun)) {
			noun = setBoat();
		}
		
		int nounNumber = Constants.NUMBER_OF_NOUNS;
				
		//Only called if more than two words
		if (noun.length()>1) {
			//Does not contain more than one word?
			if (noun.split(GameEntities.SPACE).length>1) {
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
			} else if (verbNumber==GameEntities.CMD_GO && nounNumber>0 && nounNumber<7) {
				nounNumber = 8;
			}
		}
				
		return nounNumber;
	}
	
	/**
	 * Encodes a command into a numeric string representation, including the
	 * noun number, item location, item flag, and current room.
	 *
	 * @param splitCommand the split verb/noun components
	 * @param nounNumber the resolved noun number
	 * @param game the current game instance
	 * @param room the current room identifier
	 * @return the coded command string, or empty string if noun is invalid
	 */
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
	
	/**
	 * Special-case parsing for the "look" command.
	 * <p>
	 * Converts "look" into "examine" and adjusts noun resolution, particularly
	 * for context-sensitive cases like looking into a well.
	 *
	 * @param splitCommand the split verb/noun components
	 * @param command the partially parsed command
	 * @param room the current room identifier
	 * @return a new {@link ParsedCommand} reflecting the look/examine action
	 */
	private ParsedCommand parseLook(String[] splitCommand,ParsedCommand command,int room) {
			
		if (splitCommand[1].length()==0) {
			splitCommand[1] = GameEntities.WORD_ROOM;
		}
		
		if (splitCommand[1].equals(GameEntities.WORD_WELL) && room==GameEntities.ROOM_WELL) {
			splitCommand[1]=GameEntities.WORD_ROOM;
		}
						
		splitCommand[0] = GameEntities.WORD_EXAMINE;
		int verbNumber = getVerbNumber(splitCommand[0]);
		
		return new ParsedCommand(verbNumber,command.getNounNumber(),command.getCodedCommand(),
								splitCommand,command.getCommand());
	}
	
	/**
	 * Delegates movement command parsing to the {@link Move} class.
	 *
	 * @param command the parsed command to refine
	 * @param room the current room identifier
	 * @return the updated {@link ParsedCommand} after movement parsing
	 */
	private ParsedCommand parseMove(ParsedCommand command,int room) {
		return new Move().normaliseMoveCommand(command, room);
	}
	
	/**
	 * Delegates "eat" command parsing to the {@link Consume} class.
	 *
	 * @param command the parsed command to refine
	 * @return the updated {@link ParsedCommand} after consumption parsing
	 */
	private ParsedCommand parseEat(ParsedCommand command) {
		return new Consume(command).parseEat();
	}
	
	/**
	 * Helper class that normalizes shorthand or alternative user inputs
	 * into canonical command forms.
	 */
	private class CommandNormaliser {
		
		/**
		 * Converts shorthand, synonyms, or alternate command forms into
		 * normalized commands understood by the parser.
		 * <p>
		 * For example, "u" or "up" become "go up", "north" becomes "n", etc.
		 *
		 * @param input the raw user input
		 * @return the normalized command string
		 */
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
	
	/**
	 * Ensures movement-related commands ("in", "out", "up", "down") are prefixed
	 * with "go" for consistency.
	 *
	 * @param command the input command string
	 * @return the normalized movement command string
	 */
	public String parseMovement(String command) {
		
		if (command.equals("in") || command.equals("out") ||
			command.equals("up") || command.equals("down")) {
			command = "go "+command;
		}
				
		return command;
	}
	
	private boolean isLily(String noun) {
		return noun.contains(GameEntities.NOUN_LILY);
	}
	
	private boolean isBoatman(String noun) {
		return noun.equals(GameEntities.NOUN_BOATMAN);
	}

	private String setFlower() {
		return GameEntities.NOUN_FLOWER;
	}
	
	private String setBoat() {
		return GameEntities.NOUN_BOAT;
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
 * 2 July 2025 - Added code to handle response to give
 * 23 July 2025 - Fixed parse look so can use look command
 * 25 August 2025 - Removed some of the magic variables. Added JavaDocs
 * 5 Sepember 2025 - Updated based on changes to consume
 * 29 October 2025 - Added validation to change lily to flower
 * 5 November 2025 - Added switch for boatman to boat
 * 3 December 2025 - Change lily to flower if being used.
 * 					- Increased version number
 */