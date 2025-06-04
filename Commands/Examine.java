/*
Title: Island of Secrets Examine Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 4 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.GameEntities;
import Game.Game;
import Game.Player;

public class Examine {
	
	private final Game game;
	private final Player player;
	private final ParsedCommand command;
	private final String codedCommand;
	private final String noun;
	
	public Examine(Game game, Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
		this.codedCommand = command.getCodedCommand();
		this.noun = command.getSplitTwoCommand()[0];
	}
	
	public ActionResult examine() {
		
		game.addMessage("Examine the book for clues",true,true);
		ActionResult result = new ActionResult(game,player);
		
		if(isReadParchment() ) {
			result = readParchment();
		} else if (isChestClosed()) {
			result = lookClosedChest();
		} else if (isChestOpen()) {
			result = lookOpenChest();
		}
		
		return result;
	}
	
	private boolean isReadParchment() {
		boolean readParchment = false;
		if (codedCommand.substring(0,3).equals(GameEntities.CODE_READ_PARCHMENT)) {
			readParchment = true;
		}
		return readParchment;
	}
	
	private boolean isChestClosed() {
		boolean chestClosed = false;
		if (codedCommand.equals(GameEntities.CODE_CHEST_CLOSED) && noun.equals("examine")) {
			chestClosed = true;
		}
		return chestClosed;
	}
	
	private boolean isChestOpen() {
		boolean chestOpen = false;
		if (codedCommand.equals(GameEntities.CODE_CHEST_OPEN) && noun.equals("examine")) {
			chestOpen = true;
		}
		return chestOpen;
	}
	
	private ActionResult readParchment() {
		game.addMessage("Remember Aladin. It Worked for him.",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult lookClosedChest() {
		game.addMessage("The chest is closed",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult lookOpenChest() {
		
		boolean ragSeen = false;
		game.addMessage("The chest if full of Grandpa's old stuff. On the lid is parchment that says",true,true);
		game.addMessage("'Use the rag if it looks a bit dim'", false,true);
		
		if (game.getItem(GameEntities.ITEM_RAG).getItemLocation()==GameEntities.ROOM_GRANDPAS_SHACK && 
			game.getItem(GameEntities.ITEM_RAG).getItemFlag()==9) {
			game.addMessage(" The chest contains a dirty old rag",false,true);
			game.getItem(GameEntities.ITEM_RAG).setItemFlag(0);
			ragSeen = true;
		}
		
		if (game.getItem(GameEntities.ITEM_HAMMER).getItemLocation()==GameEntities.ROOM_GRANDPAS_SHACK && 
			game.getItem(GameEntities.ITEM_HAMMER).getItemFlag()==9) {
			
			String hammer = "";

			if (!ragSeen) {
				hammer = "The chest contains ";
			} else {
				hammer = " and";
			}	
			
			game.addMessage(hammer+" a geologist's hammer",false,true);
			game.getItem(GameEntities.ITEM_HAMMER).setItemFlag(0);
		}
		return new ActionResult(game,player);
	}

	/*
	 * 		
		
		

				
		//Read the parchment
		if () {
			
			
		//Examining the chest
		} else if (code.equals("2644044") && command[0].equals("examine")) {
			
		} else if (code.equals("2644144") && command[0].equals("examine")) {

		} else if (command[1].equals("table") && player.getRoom()==44 && command[0].equals("examine")) {
			
			game.addMessage("The coffee table looks like it has been better days.",true,true);
			boolean breadSeen = false;
			
			if (game.getItem(17).getItemLocation()==44 && game.getItem(17).getItemFlag()==9) {
				game.addMessage("On the table is a loaf of bread",false,true);
				game.getItem(17).setItemFlag(0);
				breadSeen = true;
			}
			
			if (game.getItem(21).getItemLocation()==44 && game.getItem(21).getItemFlag()==9) {
				
				String water = "";
				
				if (!breadSeen) {
					water = "On the table is";
				} else {
					water = " and";
				}
				
				game.addMessage(water+" a bottle of water",false,true);
				game.getItem(21).setItemFlag(0);
			}
		} else if (command[1].equals("column") && player.getRoom()==58 && command[0].equals("examine")) {
			game.addMessage("At the bottom of the column are the words 'remember old times'",true,true);
		} else if (command[0].equals("examine") && command[1].equals("room")) {
			game.addMessage("There doesn't seem anything out of the ordinary here",true,true);
			
			if (player.getRoom()==65 || player.getRoom()==66 || player.getRoom()==67) {
				game.addMessage("You can see quite a distance from here. To the north a forest rises into ragged peaks",true,true);
				game.addMessage("while to the west you can see a log village on a lake. The the south is a swamp, while",false,true);
				game.addMessage("blasted lands disappear to the east. In the middle of a lake, shrouded in mist, appears",false,true);
				game.addMessage("to be an ancient castle.",false,true);
			} else if (player.getRoom()==19) {
				game.addMessage("The well emits deathly energy. Surrounding the well are incorporeal creatures attempting",true,true);
				game.addMessage("to add you to their number",false,true);
			} else if (player.getRoom()==74 || player.getRoom()==75||player.getRoom()==76) {
				game.addMessage("You see a village that appears to have been frozen in time, with buildings and",true,true);
				game.addMessage("inhabitants having been turned to stone. The silence is eerie, and the swamp",false,true);
				game.addMessage("seems to be ever so slowly enveloping it.",false,true);
			} else if (player.getRoom()==17) {
				game.addMessage("This room has rows and rows of pods with glass lids containing what appears",true,true);
				game.addMessage("appears to be identical people fast asleep, or even in a coma. However a number",false,true);
				game.addMessage("appear to be cracked, or even broken, and the bodies inside are either corposes or",false,true);
				game.addMessage("have rotted away. A foul, almost toxic, smell seems to be present.",false,true);
			} else if (player.getRoom()==10) {
				game.addMessage("This room has an evil presence in it, with strange symbols on the floor and wall",true,true);
				game.addMessage("Shadows seem to flicker across the wall, and the floor is covered in a crest, from",false,true);
				game.addMessage("long forgotten family. A crystaline glass window looks out over the island.",false,true);
			} else if (player.getRoom()==58) {
				game.addMessage("The column looks like it has seen better days. It is crumbling and appears that a",true,true);
				game.addMessage("peice could easily be removed if you had the right equipment. There is a message",false,true);
				game.addMessage("inscribed at the base of the column.",false,true);
			} else if (player.getRoom()==60) {
				game.addMessage("This hut looks like it has been well used, but hasn't been occupied for a long time.",true,true);
				game.addMessage("Whoever lived here, or worked from here, must have been some sort of scholar,",false,true);
				game.addMessage("considering the contents. There is a desk that is covered in papers, which includes",false,true); 
				game.addMessage("what looks like a map.",false,true);
				game.getRoom(player.getRoom()).setViewed();
			}
		} else if (command[1].equals("map") && player.getRoom()==60 && game.getRoom(player.getRoom()).getViewed()) {
				
			game.addMessage("The map looks like it is of a castle located on an island",true,true);
			game.getRoom(57).setVisited();
			
			for (int x=0;x<5;x++) {
				for (int y=7;y<11;y++) {
					game.getRoom((x*10)+y).setVisited();
				}
			}
				
		} else if ((command[1].equals("papers") || command[1].equals("diary")) && player.getRoom()==60 && game.getRoom(player.getRoom()).getViewed()) {
		
			game.addMessage("The papers look like the belong to somebody by the name Median. It chronicles his search",true,true);
			game.addMessage("for somebody name Omegan who has poisoned the land. It looks like he is also seeing a cure",false,true);
			game.addMessage("for this poison. In addition, you notice the following",false,true);
			
			int x=rand.nextInt(10);			

			switch (x) {
				case 0:
					game.addMessage("'Only those of strong will can resist the boatman'",false,true);
					break;
				case 1:
					game.addMessage("'The Sagemaster rewards those who help her'",false,true);
					break;
				case 2:
					game.addMessage("'The stone's mouth needs some polishing'",false,true);
					break;
				case 3:
					game.addMessage("'The hands fear bright light'",false,true);
					break;
				case 4:
					game.addMessage("'Words will stop the stone'",false,true);
					break;
				case 5:
					game.addMessage("'Omegan's Cloak is his strength'",false,true);
					break;
				case 6:
					game.addMessage("'The Dactyl could be useful'",false,true);
					break;
				default:
					game.addMessage("'I think it is time to head to the island. I hope I remember.'",false,true);
			}	
		}
	 */
}
/* 2 June 2025 - Created File
 * 4 June 2025 - Added examine chest
*/