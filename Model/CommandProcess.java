/*
Title: Island of Secrets Command Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.8
Date: 30 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Data.Constants;
import Data.Item;
import Data.RawData;
import View.GamePanel;

public class CommandProcess {
	
	private String[] splitCommand = {"",""};
	private String[] commands;
	private String originalCommand;
	private int verbNo;
	private int nounNo;
	private String codedCommand;
	private Commands command;
	private int nounNumber;
	private Game game;
	
	public CommandProcess(String command,Game game) {
				
		command = command.toLowerCase();
		command = fixCommand(command);
		commands = command.split(" ");
		splitCommand[0] = commands[0];
		this.originalCommand = command;
		
		if (commands.length>1) {
			splitCommand[1] = command.substring(commands[0].length()).trim();
		} else {
			game.setMessage("Most commands need two words");
		}
	}
	
	public CommandProcess() {}
	
	private String fixCommand(String command) {
		
		if (command.equals("u") || command.equals("up")) {
			command = "go up";
		} else if (command.equals("d") || command.equals("down")) {
			command = "go down";
		} else if (command.equals("i") || command.equals("enter") ||
				command.equals("inside") || command.equals("go inside")) {
			command = "go in";
		} else if (command.equals("o") || command.equals("exit") ||				
				command.equals("outside") || command.equals("go outside")) {
			command = "go out";
		} else if (command.equals("north")) {
			command = "n";
		} else if (command.equals("south")) {
			command = "s";
		} else if (command.equals("east")) {
			command = "e";
		} else if (command.equals("west")) {
			command = "w";
		}
		
		return command;	
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
				
		//Only called if more than two words
		if (commands.length>1) {
			nounNumber = getNounNum(splitCommand[1].toLowerCase());
			this.nounNo = nounNumber;
		} else {
			nounNumber = -1;
		}
		
		return nounNumber;
	}
	
	private int getNounNum(String noun) {
		
		int nounCount = 0;
		int nounNumber = 52;
		
		for (String command:RawData.getNouns()) {
			nounCount ++;
							
			if (noun.equals(command)) {
				nounNumber = nounCount;
			}
		}
				
		return nounNumber;
	}
	
	public String codeCommand(int room, int nounNumber, Item item) {
			
		String codedNoun = String.format("%d%d%d%d",nounNumber,Math.abs(item.getLocation()),
										 item.getFlag(),room);
		codedNoun = String.valueOf(Integer.parseInt(codedNoun.trim()));
		this.codedCommand = codedNoun;
				
		return codedNoun;
	}
	
	//Executes the command
	public void executeCommand(Game game,Player player,int nounNumber) {
		
		nounNo = nounNumber;
		this.command = new Commands(verbNo,nounNumber,codedCommand,originalCommand);
				
		//Movement Command (verb only)
		if ((verbNo>0 && verbNo<5)) {
			this.command.move(game,player);
		
		//More than one verb
		} else if(commands.length>1) {
			
			//Go
			if (verbNo==5) {
				this.command.move(game,player);

			//Take Command (pick & catch included)
			} else if (verbNo == 6 || verbNo == 7 || verbNo == 15 || verbNo == 29) {
				this.command.take(game,player);
			
			//Give
			} else if (verbNo == 8) {
				this.command.give(game, player);
			}
		}
	}
	
	public void executeGive(Game game,Player player,int nounNumber, String subject,
							String codedNoun) {
		
		int objNumber = getNounNum(subject);
		
		if (subject.length()==0) {
			String itemName = game.getItem(objNumber).getItem();
			game.setMessage("Please enter who you will be giving the "+itemName+" to.");
		} else if (player.getRoom() != game.getItem(objNumber).getLocation()) {
			game.setMessage("The "+subject+" is not here.");
		} else {
			
			game.setMessage("It is refused.");
			
			//Removes the snake from the hut by giving it an apple
			if (codedNoun.equals("10045") && objNumber==40) {
				game.getItem(nounNumber).setLocation(81);
				game.getItem(objNumber).setFlag(1);
				game.setMessage("The snake uncoils");
			
			//Giving water to a villager (but must have some drink)
			} else if (codedNoun.equals("2413075") && objNumber==30 && player.getDrink()>1) {
				game.getItem(11).setFlag(0);
				game.setMessage("He offers his staff");
				player.adjustDrink(1);
			
			
			} else {
				
				//Give Lilyflower/Marble Chip to scavenger	
				if ((codedNoun.substring(0,3).equals("300") || 
					 codedNoun.substring(0,3).equals("120")) &&
					 objNumber == 42) {
					player.setWisdom(player.getWisdom()+10);
					game.getItem(nounNumber).setLocation(81);
				
				//Give jug to swampman
				} else if (codedNoun.substring(0,2).equals("40") && 
						   game.getItem(4).getFlag()<0 && objNumber == 32) {
					game.getItem(objNumber).setFlag(1);
					game.getItem(nounNumber).setLocation(81);
				
				//Give pebble to Median
				} else if (codedNoun.substring(0,2).equals("80") &&
						   objNumber == 43) {
					game.getItem(nounNumber).setLocation(81);
					//Gosub 1560
					/*
					 * 1560 LET A$="*HE TAKES IT ":IF R<>8 THEN LET A$+A$+"RUNS DOWN THE CORRIDOR,"
					 * 1570 GOSUB2740:A$="*AND CASTS IT INTO THE CHEMICAL VATS, PURIFYING THEM WITH"
					 * 1580 A$=A$+" A CLEAR BLUE LIGHT REACHING FAR INTO THE LAKES AND RIVERS BEYOND"
					 * 1590 LET F(8)=-1:GOSUB2750:GOSUB2760:GOSUB2760:RETURN
					 */
					
				}
			
			//We want the first three characters of codedNoun
			}
				
			
		}
	}
	/*
	
	
	
	1450 LET B$=LEFT$(B$,3):LET F$="IT IS REFUSED"
	
	
	
	
	1500 IF L(O)=81 OR (O=24 AND L(11)>0 AND G>0)THEN LET F$="IT IS ACCEPTED"
	1510 IF N=41 THEN LET L(O)=51:LET F$="IT IS TAKEN"
	1520 RETURN
	*/
}

/* 9 November 2024 - Created method
 * 10 November 2024 - Added the verb count method
 * 11 November 2024 - Added the noun count method
 * 					- Got the command splitting working and sending correct errors
 * 					- Added method to process the coded command.
 * 12 November 2024 - Completed the codeCommand method
 * 13 November 2024 - Stored the variables 
 * 14 November 2024 - Added more options for movement
 * 17 November 2024 - Added call to take method
 * 29 November 2024 - moved script to get noun value to separate script.
 * 					- Fixed problem with only verb command not displaying properly
 * 30 November 2024 - Continued building the give functionality
 */