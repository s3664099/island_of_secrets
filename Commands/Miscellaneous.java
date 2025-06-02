/*
Title: Island of Secrets Miscellaneous Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 3 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import Data.Constants;
import Data.GameEntities;
import Game.Game;
import Game.Player;

public class Miscellaneous {
	
	private final Game game;
	private final Player player;
	private final ParsedCommand command;
	private final String codedCommand;
	private final int verbNumber;
	private boolean hasItems = false;
	
	public Miscellaneous(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.command = null;
		this.codedCommand = null;
		this.verbNumber = -99;
	}
	
	public Miscellaneous(Game game,Player player, ParsedCommand command) {
		this.game = game;
		this.player = player;
		this.command = command;
		this.codedCommand = command.getCodedCommand();
		this.verbNumber = command.getVerbNumber();
	}
	
	public ActionResult info() {
		Game game = getDetails();
		return new ActionResult(game,player);
	}
	
	public ActionResult wave() {
		
		ActionResult result = new ActionResult();
		if (isBoatmanPresent()) {
			game.addMessage("The boatman waves back.",true,true);
			result = new ActionResult(game,player);
		} else if (isWavingTorch()) {
			result = waveTorch();
		}
		
		return result;
	}
	
	public ActionResult help() {
		
		game.addMessage("?!?",true,true);
		ActionResult result = new ActionResult(game,player);
		
		if(isScratchSage()) {
			result = scratchSage();
		} else if (isHelpVillagerSage()) {
			game.addMessage("How will you do that?",true,true);
			result = new ActionResult(game,player);
		}
		return result;
	}
	
	public ActionResult polish() {
		
		game.addMessage("A-dub-dub",true,true);
		ActionResult result = new ActionResult(game,player);
		
		//Rub the stone
		if (isPolishStone()) {
			result = polishStone();			
		} else if (isPolishStoneAgain()) {
			result = stonePolished();
		}
		return result;
	}
		
	public ActionResult speak() {
		
		String noun = command.getSplitTwoCommand()[1];
		game.addMessage(noun,true,true);
		ActionResult result = new ActionResult(game,player);
		
		if(isSayStonyWords(noun)) {
			result = sayStonyWords();
		} else if (isSpeakScavenger(noun)) {
			result = speakScavenger();
		}
		
		return result;
	}
	
	private boolean isBoatmanPresent() {
		
		boolean boatmanPresent = false;
		if(game.getItem(GameEntities.ITEM_BOAT).isAtLocation(player.getRoom()) &&
		   !isWavingTorch()) {
			boatmanPresent = true;
		}
		return boatmanPresent;
	}
	
	private boolean isWavingTorch() {
		boolean wavingTorch = false;
		if(codedCommand.substring(0,3).equals(GameEntities.CODE_TORCH_DIM)) {
			wavingTorch = true;
		}
		return wavingTorch;
	}
	
	private boolean isScratchSage() {
		boolean isScratchSage = false;
		if (codedCommand.equals(GameEntities.CODE_SCRATCH_SAGE) 
			&& verbNumber == GameEntities.CMD_SCRATCH) {
			isScratchSage = true;
		}
		return isScratchSage;
	}
	
	private boolean isHelpVillagerSage() {
		boolean helpVillageSage = false;
		if (codedCommand.equals(GameEntities.CODE_HELP_VILLAGER) || 
			codedCommand.equals(GameEntities.CODE_HELP_SAGE)) {
			helpVillageSage = true;
		}
		return helpVillageSage;
	}
	
	private boolean isPolishStone() {
		boolean polishStone = false;
		if (command.getSplitTwoCommand()[1].length()>0) {
			String noun = command.getSplitTwoCommand()[1];
			if(noun.equals("stone") && player.getRoom()==GameEntities.ROOM_STONE 
			   && game.getItem(GameEntities.ITEM_STONE).getItemFlag()==1
			   && game.getItem(GameEntities.ITEM_RAG).getItemLocation()==GameEntities.ROOM_CARRYING) {
				polishStone = true;
			}
		}
		return polishStone;
	}
	
	private boolean isPolishStoneAgain() {
		boolean polishStone = false;
		if(codedCommand.substring(0,4).equals("2815") && player.getRoom()==GameEntities.ROOM_STONE
			&& game.getItem(GameEntities.ITEM_STONE).getItemFlag()==0
			&& game.getItem(GameEntities.ITEM_RAG).getItemLocation()==GameEntities.ROOM_CARRYING) {
			polishStone = true;
		}
		return polishStone;
	}
	
	private boolean isSayStonyWords(String noun) {
		
		boolean sayStonyWords = false;
		if(noun.toLowerCase().equals("stony words") && player.getRoom()==GameEntities.ROOM_CASTLE_ENTRANCE &&
			game.getItem(GameEntities.ITEM_PEBBLE).getItemFlag()==0) {
			sayStonyWords = true;
		}
		return sayStonyWords;
	}
	
	private boolean isSpeakScavenger(String noun) {
		boolean speakScavenger = false;
		if (noun.toLowerCase().equals("remember old times") && 
			player.getRoom()==game.getItem(GameEntities.ITEM_SCAVENGER).getItemLocation() && 
			game.getItem(GameEntities.ITEM_LILY).getItemLocation()==GameEntities.ROOM_DESTROYED &&
			game.getItem(GameEntities.ITEM_CHIP).getItemLocation()==GameEntities.ROOM_DESTROYED) {
			speakScavenger = true;
		}
		return speakScavenger;
	}
	
	private Game getDetails() {
		
		String items = getItemDetails();
		game.addMessage("Info - Items carried",true,false);
		game.addMessage("Food: "+((int) player.getStat("food")),false,false);
		game.addMessage("Drink: "+((int) player.getStat("drink")),false,false);
		
		if(hasItems) {
			game.addMessage("Items: "+items,false,false);
		}
		return game;
	}
	
	private String getItemDetails() {
		
		int itemLength = 0;
		String items = "";
		
		for (int i=1;i<Constants.MAX_CARRIABLE_ITEMS+1;i++) {
			
			if (game.getItem(i).isAtLocation(GameEntities.ROOM_CARRYING)) {
				hasItems = true;
				int extraLength = 1;
				
				if (itemLength>0) {
					items = items+", ";
					extraLength = 2;
				}
				items = items+game.getItem(i).getItemName();
				itemLength += game.getItem(i).getItemName().length()+extraLength;
			}
		}
		return items;
	}
	
	private ActionResult waveTorch() {
		game.getItem(GameEntities.ITEM_TORCH).setItemFlag(1);
		game.addMessage("The torch brightens.",true,true);
		
		if (player.getRoom()==GameEntities.ROOM_WITH_HANDS) {
			game.addMessage("The hands release you and retreat into the wall.",false,true);
		}
		
		game.getItem(GameEntities.ITEM_TORCH).setItemName("a brightly glowing torch");
		player.setStat("wisdom",(int) player.getStat("wisdom")+8);
		return new ActionResult(game,player);
	}
	
	private ActionResult scratchSage() {
		game.getItem(3).setItemFlag(0);
		game.addMessage("She nods slowly.",true,true);
		player.setStat("wisdom",(int) player.getStat("wisdom")+5);
		return new ActionResult(game,player);
	}
	
	private ActionResult polishStone() {
		game.getItem(28).setItemFlag(0);
		game.addMessage("Reflections stir within.",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult stonePolished() {
		game.getItem(GameEntities.ITEM_PEBBLE).setItemFlag(0);
		game.getItem(GameEntities.ITEM_PEBBLE).setItemFlag(GameEntities.ROOM_CARRYING);
		game.getItem(GameEntities.ITEM_STONE).setItemFlag(99);
		game.addMessage("The stone utters 'Stony Words'",true,true);
		game.addMessage("You are carrying something new",false,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult sayStonyWords() {
		game.addMessage("The stones are fixed.",false,false);
		game.getItem(GameEntities.ITEM_ROCKS).setItemFlag(1);
		return new ActionResult(game,player);
	}
	
	private ActionResult speakScavenger() {
		game.addMessage("He eats the flowers - and changes",false,false);
		game.getItem(GameEntities.ITEM_SCAVENGER).setItemFlag(1);
		game.getItem(GameEntities.ITEM_MEDIAN).setItemFlag(0);
		return new ActionResult(game,player);
	}
}

/* 31 May 2025 - Created File
 * 1 June 2025 - Added help & polish commands
 * 2 June 2025 - Added Speak command
 */