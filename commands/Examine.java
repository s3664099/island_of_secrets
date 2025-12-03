/*
Title: Island of Secrets Examine Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package commands;

import java.util.Random;

import command_process.ActionResult;
import command_process.ParsedCommand;
import data.GameEntities;
import game.Game;
import game.Player;

/**
 * Handles the "examine" action for a player within the game.
 * <p>
 * This class evaluates the player's current command, room, and context to
 * determine what description or outcome should be produced. Depending on
 * the command, the player may receive narrative text, discover items, or
 * unlock clues that progress the game.
 */
public class Examine {
	
	private final Game game;
	private final Player player;
	private final String codedCommand;
	private final String verb;
	private final String noun;
	private final int playerRoom;
	private final Random rand = new Random();
	
    /**
     * Creates a new Examine action handler.
     *
     * @param game    the current game instance
     * @param player  the player performing the action
     * @param command the parsed player command containing verb/noun and encoded form
     */
	public Examine(Game game, Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.codedCommand = command.getCodedCommand();
		this.verb = command.getSplitTwoCommand()[0];
		this.noun = command.getSplitTwoCommand()[1];
		this.playerRoom = player.getRoom();
	}
		
    /**
     * Executes the examine action, branching based on the command context.
     * <p>
     * This may trigger different narrative responses such as examining
     * chests, tables, columns, rooms, parchments, or papers.
     *
     * @return the result of the action, including validity and updated game state
     */
	public ActionResult examine() {
		
		game.addMessage("Examine the book for clues",true,true);
		ActionResult result = new ActionResult(game,player,false);
		
		if(isReadParchment() ) {
			result = readParchment();
		} else if (isChestClosed()) {
			result = lookClosedChest();
		} else if (isChestOpen()) {
			result = lookOpenChest();
		} else if (isExamineTable()) {
			result = examineTable();
		} else if (isExamineTableInRoom()) {
			result = examineTableNotInRoom();
		} else if (isExamineColumn()) {
			result = examineColumn();
		} else if (isExamineRoom()) {
			result = examineRoom();
		} else if (isReadMap()) {
			result = readMap();
		} else if (isExaminePapers()) {
			result = examinePapers();
		}
		
		return result;
	}
	
    // --- Condition Checks ---

    /** @return true if the command indicates reading a parchment */
	private boolean isReadParchment() {
		return codedCommand.substring(0,3).equals(GameEntities.CODE_READ_PARCHMENT);
	}
	
    /** @return true if the closed chest is being examined */
	private boolean isChestClosed() {
		return codedCommand.equals(GameEntities.CODE_CHEST_CLOSED) && verb.equals("examine");
	}
	
    /** @return true if the open chest is being examined */
	private boolean isChestOpen() {
		return codedCommand.equals(GameEntities.CODE_CHEST_OPEN) && verb.equals("examine");
	}
	
    /** @return true if the table in Grandpa's shack is being examined */
	private boolean isExamineTable() {
		return noun.equals("table") && playerRoom==GameEntities.ROOM_GRANDPAS_SHACK && verb.equals("examine");
	}
	
    /** @return true if the table is being examined and not in Grandpa's Shack */
	private boolean isExamineTableInRoom() {
		return noun.equals("table") && playerRoom!=GameEntities.ROOM_GRANDPAS_SHACK && verb.equals("examine");
	}
	
    /** @return true if the column in the column room is being examined */
	private boolean isExamineColumn() {
		return noun.equals("column") && playerRoom==GameEntities.ROOM_COLUMN && verb.equals("examine");
	}
	
    /** @return true if the command is "examine room" */
	private boolean isExamineRoom() {
		return verb.equals("examine") && noun.equals(GameEntities.NOUN_ROOM);
	}
	

    /** @return true if the player is at a pyramid-related location */
	private boolean isPyramid() {
		return playerRoom==GameEntities.ROOM_PYRAMID_EDGE || 
				playerRoom==GameEntities.ROOM_PYRAMID_ROOF || 
				playerRoom==GameEntities.ROOM_PYRAMID_SPLIT;
	}
	
    /** @return true if the player is at the well location */
	private boolean isWell() {
		return playerRoom==GameEntities.ROOM_WELL;
	}
	
    /** @return true if the player is in one of the stone village rooms */
	private boolean isStoneVillage() {
		return playerRoom==GameEntities.ROOM_VILLAGE_ENTRANCE || 
				playerRoom==GameEntities.ROOM_VILLAGE_PETRIFIED||
				playerRoom==GameEntities.ROOM_VILLAGE_REMAINS;
	}
	
    /** @return true if the player is attempting to read the map outside the hut */
	private boolean isReadMap() {
		return noun.equals(GameEntities.NOUN_MAP) && player.getRoom()==GameEntities.ROOM_OUTSIDE_HUT && 
				game.getRoom(player.getRoom()).getViewed();
	}
	
    /** @return true if the player is examining papers or a diary outside the hut */
	private boolean isExaminePapers() {
		return (noun.equals(GameEntities.NOUN_PAPERS) || noun.equals(GameEntities.NOUN_DIARY)) && 
				player.getRoom()==GameEntities.ROOM_OUTSIDE_HUT && 
				game.getRoom(playerRoom).getViewed();
	}
	
    // --- Action Handlers ---

    /** @return result of reading a parchment, including narrative clue */
	private ActionResult readParchment() {
		game.addMessage("Remember Aladin. It Worked for him.",true,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining a closed chest */
	private ActionResult lookClosedChest() {
		game.addMessage("The chest is closed",true,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining an open chest, possibly revealing items */
	private ActionResult lookOpenChest() {
		
		boolean ragSeen = false;
		game.addMessage("The chest if full of Grandpa's old stuff. On the lid is parchment that says",true,true);
		game.addMessage("'Use the rag if it looks a bit dim'", false,true);
		
		String chestContents = "";
		
		if (game.getItem(GameEntities.ITEM_RAG).getItemLocation()==GameEntities.ROOM_GRANDPAS_SHACK && 
			game.getItem(GameEntities.ITEM_RAG).getItemFlag()==9) {
			chestContents += " The chest contains a dirty old rag";
			game.getItem(GameEntities.ITEM_RAG).setItemFlag(0);
			ragSeen = true;
		}
		
		if (game.getItem(GameEntities.ITEM_HAMMER).getItemLocation()==GameEntities.ROOM_GRANDPAS_SHACK && 
			game.getItem(GameEntities.ITEM_HAMMER).getItemFlag()==9) {
			
			if (!ragSeen) {
				chestContents = "The chest contains ";
			} else {
				chestContents += " and";
			}	
			
			game.addMessage(chestContents+" a geologist's hammer",false,true);
			game.getItem(GameEntities.ITEM_HAMMER).setItemFlag(0);
		}
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the table, possibly revealing bread or bottle */
	private ActionResult examineTable() {
		boolean breadSeen = false;
		String tableContents = "";
		game.addMessage("The coffee table looks like it has been better days.",true,true);
		
		if (game.getItem(GameEntities.ITEM_BREAD).getItemLocation()==GameEntities.ROOM_GRANDPAS_SHACK && 
			game.getItem(GameEntities.ITEM_BREAD).getItemFlag()==9) {
			tableContents = "On the table is a loaf of bread";
			game.getItem(GameEntities.ITEM_BREAD).setItemFlag(0);
			breadSeen = true;
		}
		
		if (game.getItem(GameEntities.ITEM_BOTTLE).getItemLocation()==GameEntities.ROOM_GRANDPAS_SHACK && 
			game.getItem(GameEntities.ITEM_BOTTLE).getItemFlag()==9) {
			if (!breadSeen) {
				tableContents = "On the table is";
			} else {
				tableContents += " and";
			}
			game.addMessage(tableContents+" a bottle of water",false,true);
			game.getItem(GameEntities.ITEM_BOTTLE).setItemFlag(0);
		}
		
		return new ActionResult(game,player,true);
	}
	
	private ActionResult examineTableNotInRoom() {
		game.addMessage("There is no table here.",true,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the column, showing inscription */
	private ActionResult examineColumn() {
		game.addMessage("At the bottom of the column are the words 'remember old times'",true,true);
		return new ActionResult(game,player,true);
	}
	

    /** 
     * Examines the current room. 
     * May delegate to pyramid, well, village, sanctum, column, hut, or clone factory descriptions.
     *
     * @return result of examining the room
     */
	private ActionResult examineRoom() {
		game.addMessage("There doesn't seem anything out of the ordinary here",true,true);
		ActionResult result = new ActionResult(game,player,false);
		if (isPyramid()) {
			result = examinePyramid();
		} else if (isWell()) {
			result = examineWell();
		} else if (isStoneVillage()) {
			result = examineVillage();
		} else if (player.getRoom()==GameEntities.ROOM_CLONE_FACTORY) {
			result = examineCloneFactory();
		} else if (player.getRoom()==GameEntities.ROOM_SANCTUM) {
			result = examineSanctum();
		} else if (player.getRoom()==GameEntities.ROOM_COLUMN) {
			result = examineColumnRoom();
		} else if (player.getRoom()==GameEntities.ROOM_OUTSIDE_HUT) {
			result = examineAbodeHut();
		}
		return result;
	}
	
    /** @return result of examining the pyramid location */
	private ActionResult examinePyramid() {
		game.addMessage("You can see quite a distance from here. To the north a forest rises into ragged peaks",true,true);
		game.addMessage("while to the west you can see a log village on a lake. To the south is a swamp, while",false,true);
		game.addMessage("blasted lands disappear to the east. In the middle of a lake, shrouded in mist, appears",false,true);
		game.addMessage("to be an ancient castle.",false,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the well */
	private ActionResult examineWell() {
		game.addMessage("The well emits deathly energy. Surrounding the well are incorporeal creatures attempting",true,true);
		game.addMessage("to add you to their number",false,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the stone village */
	private ActionResult examineVillage() {
		game.addMessage("You see a village that appears to have been frozen in time, with buildings and",true,true);
		game.addMessage("inhabitants having been turned to stone. The silence is eerie, and the swamp",false,true);
		game.addMessage("seems to be ever so slowly enveloping it.",false,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the clone factory */
	private ActionResult examineCloneFactory() {
		game.addMessage("This room has rows and rows of pods with glass lids containing what appears",true,true);
		game.addMessage("appears to be identical people fast asleep, or even in a coma. However a number",false,true);
		game.addMessage("appear to be cracked, or even broken, and the bodies inside are either corposes or",false,true);
		game.addMessage("have rotted away. A foul, almost toxic, smell seems to be present.",false,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the sanctum */
	private ActionResult examineSanctum() {
		game.addMessage("This room has an evil presence in it, with strange symbols on the floor and wall",true,true);
		game.addMessage("Shadows seem to flicker across the wall, and the floor is covered in a crest, from",false,true);
		game.addMessage("long forgotten family. A crystaline glass window looks out over the island.",false,true);
		return new ActionResult(game,player,true);
	}
	

    /** @return result of examining the column room */
	private ActionResult examineColumnRoom() {
		game.addMessage("The column looks like it has seen better days. It is crumbling and appears that a",true,true);
		game.addMessage("peice could easily be removed if you had the right equipment. There is a message",false,true);
		game.addMessage("inscribed at the base of the column.",false,true);
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining the hut outside the abode */
	private ActionResult examineAbodeHut() {
		game.addMessage("This hut looks like it has been well used, but hasn't been occupied for a long time.",true,true);
		game.addMessage("Whoever lived here, or worked from here, must have been some sort of scholar,",false,true);
		game.addMessage("considering the contents. There is a desk that is covered in papers, which includes",false,true); 
		game.addMessage("what looks like a map.",false,true);
		game.getRoom(player.getRoom()).setViewed();
		return new ActionResult(game,player,true);
	}
	
    /** @return result of reading the map, unlocking new rooms on the map */
	private ActionResult readMap() {
		game.addMessage("The map looks like it is of a castle located on an island",true,true);
		game.getRoom(57).setVisited();
		
		for (int x=0;x<5;x++) {
			for (int y=7;y<11;y++) {
				game.getRoom((x*10)+y).setVisited();
			}
		}
		return new ActionResult(game,player,true);
	}
	
    /** @return result of examining papers, possibly revealing random clues */
	private ActionResult examinePapers() {
		game.addMessage("The papers look like the belong to somebody by the name Median. It chronicles his search",true,true);
		game.addMessage("for somebody name Omegan who has poisoned the land. It looks like he is also seeing a cure",false,true);
		game.addMessage("for this poison. In addition, you notice the following",false,true);
		game.addMessage(getClue(rand.nextInt(10)), false, true);
		
		return new ActionResult(game,player,true);
	}
	
    /**
     * Retrieves a clue based on a random number.
     *
     * @param clueNumber index of the clue to retrieve
     * @return textual clue string
     */
	private String getClue(int clueNumber) {
		String clue = "'I think it is time to head to the island. I hope I remember.'";
		
		if(clueNumber == 0) {
			clue = "'Only those of strong will can resist the boatman'";
		} else if (clueNumber == 1) {
			clue = "'The Sagemaster rewards those who help her'";
		} else if (clueNumber == 2) {
			clue = "'The stone's mouth needs some polishing'";
		} else if (clueNumber == 3) {
			clue = "'The hands fear bright light'";
		} else if (clueNumber == 4) {
			clue = "'Words will stop the stone'";
		} else if (clueNumber == 5) {
			clue = "'Omegan's Cloak is his strength'";
		} else if (clueNumber == 6) {
			clue = "'The Dactyl could be useful'";
		}
		
		return clue;
	}
}
/* 2 June 2025 - Created File
 * 4 June 2025 - Added examine chest and table
 * 5 June 2025 - Added examine rooms and began splitting into separate methods
 * 6 June 2025 - Finished Examine Room. Added Read Map
 * 8 June 2025 - Finished examine function with examine papers
 * 2 September 2025 - Updated based on new ActionResult
 * 13 October 2025 - Tightened the contents of the chest
 * 3 December 2025 - Increased version number
*/