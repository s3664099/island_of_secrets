/*
Title: Island of Secrets Miscellaneous Commands
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 18 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Commands;

import java.util.Random;

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
	private final Random rand = new Random();
	
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
	
	public ActionResult fill() {
		game.addMessage("Not sure that can be done.",true,true);
		ActionResult result = new ActionResult(game,player);
		
		if (isFillJug()) {
			result = fillJug();
		} else if (isFillJugWater()) {
			result = fillJugWater();
		} else if (isJugFull()) {
			result = jugFull();
		}
		return result;
	}
	
	public ActionResult ride() {
		
		game.addMessage("How?",true,true);
		ActionResult result = new ActionResult(game,player);
		if (isRideBeast()) {
			result = rideBeast();
		} else if (isAlreadyRidingBeast()) {
			result = alreadyRidingBeast();
		} 
		return result;
	}
	
	public ActionResult open() {
		game.addMessage("I'm unable to do that",true,true);
		ActionResult result = new ActionResult(game,player);
		
		if (isOpenChest()) {
			result = openChest();
		}
		
		if (isOpenTrapdoor()) {
			result = openTrapdoor();
		}
		return result;
	}
		
	public ActionResult swim() {
		
		ActionResult result = cannotSwim();
		
		if (checkCanSwim()) {
			result = canSwim();
		}
		
		return result;
	}

	public ActionResult shelter() {
		game.addMessage("Not possible at the moment.",true,true);
		ActionResult result = new ActionResult(game,player);
		
		if(isStormBlowing()) {
			result = shelterFromStorm();
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
	
	private boolean isFillJug() {
		boolean fillJug = false;
		if(codedCommand.equals(GameEntities.CODE_FILL_JUG)) {
			fillJug = true;
		}
		return fillJug;
	}
	
	private boolean isFillJugWater() {
		boolean fillJugWater = false;
		if(codedCommand.equals(GameEntities.CODE_FILL_JUG_WATER)) {
			fillJugWater = true;
		}
		return fillJugWater;
	}
	
	private boolean isJugFull() {
		boolean jugFull = false;
		if(codedCommand.substring(0,2).equals(GameEntities.CODE_JUG_FULL)) {
			jugFull = true;
		}
		return jugFull;
	}
	
	private boolean isRideBeast() {
		boolean ridingBeast = false;
		if(codedCommand.substring(0,4).equals(GameEntities.CODE_RIDE_BEAST)) {
			ridingBeast = true;
		}
		return ridingBeast;
	}
	
	private boolean isAlreadyRidingBeast() {
		boolean alreadyRidingBeast = false;
		if(codedCommand.substring(0,4).equals(GameEntities.CODE_RIDING_BEAST)) {
			alreadyRidingBeast = true;
		}
		return alreadyRidingBeast;
	}
	
	private boolean isOpenChest() {
		boolean openingChest = false;
		if(codedCommand.equals(GameEntities.CODE_OPEN_CHEST)) {
			openingChest = true;
		}
		return openingChest;
	}
	
	private boolean isOpenTrapdoor() {
		boolean openingTrapdoor = false;
		if(codedCommand.equals(GameEntities.CODE_OPEN_TRAPDOOR)) {
			openingTrapdoor = true;
		}
		return openingTrapdoor;
	}
	
	private boolean isStormBlowing() {
		boolean stormBlowing = false;
		if(game.getItem(GameEntities.ITEM_STORM).getItemFlag()<0) {
			stormBlowing = true;
		}
		return stormBlowing;
	}
	
	private boolean checkCanSwim() {
		boolean canSwim = false;
		
		if (player.getRoom()==GameEntities.ROOM_STOREROOM && game.getItem(GameEntities.ITEM_TRAPDOOR).getItemFlag()==0) {
			canSwim = true;
		}
		return canSwim;
	}
	
	private Game getDetails() {
		
		String items = getItemDetails();
		String infoDisplay = "Info: "+"Food: "+((int) player.getStat("food"));
		infoDisplay += "  Drink: "+((int) player.getStat("drink"));
		game.addMessage(infoDisplay,true,false);
		
		if(hasItems) {
			game.addMessage("Items: "+items,false,true);
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
	
	private ActionResult fillJug() {
		game.getItem(GameEntities.ITEM_JUG).setItemFlag(-1);
		game.addMessage("Filled",true,true);
		game.getItem(GameEntities.ITEM_JUG).setItemName("A jug full of bubbling green liquid");
		return new ActionResult(game,player);
	}
	
	private ActionResult fillJugWater() {
		game.addMessage("The water streams out of the jug",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult jugFull() {
		if (game.getItem(GameEntities.ITEM_JUG).getItemFlag()==-1)  {
			if (player.getRoom()==GameEntities.ROOM_HUT || 
				player.getRoom()==GameEntities.ROOM_BRANCHES) {
				game.addMessage("The jug is already full",true,true);
			}
		}
		return new ActionResult(game,player);
	}
	
	private ActionResult rideBeast() {
		int nounNumber = command.getNounNumber();
		game.getItem(nounNumber).setItemFlag(-1);
		game.addMessage("It allows you to ride.",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult alreadyRidingBeast() {
		game.addMessage("You are already riding the beast.",true,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult openChest() {
		game.addMessage("The chest opens. There is something inside",true,true);
		game.getItem(6).setItemFlag(9);
		game.getItem(5).setItemFlag(9);
		game.getItem(15).setItemFlag(9);
		game.getItem(26).setItemFlag(1);
		return new ActionResult(game,player);
	}
	
	private ActionResult openTrapdoor() {
		game.addMessage("The trapdoor creaks",true,true);
		game.getItem(29).setItemFlag(0);
		player.setStat("wisdom",(int) player.getStat("wisdom")+3);
		return new ActionResult(game,player);
	}
	
	private ActionResult cannotSwim() {
		game.addMessage("You can't swim here!",true,true);
		player.setStat("wisdom",(int) player.getStat("wisdom")-1);
		return new ActionResult(game,player);
	}
	
	private ActionResult canSwim() {
		game.addMessage("You dive into the water",true,true);
		player.setPlayerStateStartSwimming();
		player.setRoom(rand.nextInt(5)+1);
		return new ActionResult(game,player);
	}
	
	private ActionResult shelterFromStorm() {
		int commandLength = command.getCommand().split(" ").length;
		String[] commands = command.getSplitFullCommand();
		
		if (commandLength>1) {
			
			String shelterLocation = commands[1];
			game.addMessage("",true,true);
			
			if (commands.length>2 && commands[1].equals("in")) {
				shelterLocation = commands[2];
			}
			
			if (shelterLocation.equals("shack")) {
				player.setRoom(GameEntities.ROOM_GRANDPAS_SHACK);
			} else if (shelterLocation.equals("cave")) {
				player.setRoom(GameEntities.ROOM_LAIR);
			} else if (shelterLocation.equals("cabin")) {
				player.setRoom(GameEntities.ROOM_HUT);
			} else {
				game.addMessage("I'm sorry, I do not know that place",true,true);
			}
			
		} else {
			game.addMessage("You can shelter in:",true,true);
			game.setShelterGameState();
		}
		return new ActionResult(game,player);
	}
}

/* 31 May 2025 - Created File
 * 1 June 2025 - Added help & polish commands
 * 2 June 2025 - Added Speak command
 * 8 June 2025 - Added fill command
 * 9 June 2025 - Added ride and open commands
 * 10 June 2025 - Added swim & shelter commands
 * 14 July 2025 - Updated info section to prevent display from breaking
 * 18 July 2025 - Fixed problem with not setting the swimming flag in the storeroom
 */