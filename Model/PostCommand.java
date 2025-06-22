/*
Title: Island of Secrets Post Command Functions
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 22 June 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import Commands.ActionResult;
import Data.Constants;
import Data.GameEntities;
import Game.Game;
import Game.Player;
import java.util.Random;

public class PostCommand {
	
	private final Game game;
	private final Player player;
	private final Random rand = new Random();
	private final int stormRand = rand.nextInt(3);
	
	public PostCommand(ActionResult result) {
		game = result.getGame();
		player = result.getPlayer();
	}
	
	public ActionResult postUpdates() {
		
		return new ActionResult(game,player);
	}
	
	public void updates(Game game, Player player) {
		
		ActionResult result = new ActionResult(game,player);
		
		if (isAtOrchids()) {
			result = atOrchids(result);
		}
		
		if(isAtThicket()) {
			result = atThicket(result);
		}
		
		if(needAdjustStorm()) {
			result = adjustStorm(result);
		}
			
		if (doesStormAppear()) {
			result = stormAppears(result);
		}

		if (doesPlayerHaveBeast()) {
			result = moveBeast(result);
		}
		
		if (doesOmeganMove()) {
			result = moveOmegan(result);
		} else if (isOmeganPresent()) {
			result = omeganAttacks(result);		
		} 
		
		if (isSwampManPresent()) {
			result = swampManNotPresent(result);
		}
		
		if (doesSwampManTalk()) {
			result = swampManTalks(result);
		}
					
		if (doesBoatmanAppear()) {
			result = boatmanAppears(result);
		}
		
		if (isAtWell()) {
			result = atWell(result);
		}
		
		if (!areLogmenPresent()) {
			result = moveLogmen(result);
		} else {
			result = logmenPresent(result);
		}
		
		if(isMedianFollowing()) {
			result = medianFollowing(result);
		}
		
		if(isMedianHint()) {
			result = medianHint(result);
		}
		
		if (isInVatRoom()) {
			result = inVatRoom(result);
		}
		
		if (isTooWeak()) {
			result = dropItems(result);
		}
				
		if(isAtClashingStones()) {
			result = atClashingStones(result);
		}
		
		if(isWinGame()) {
			result = winGame(result);
		}
		
		if(isLoseGame()) {
			result = loseGame(result);
		}
	}
	
	private boolean isAtOrchids() {
		boolean atOrchids = false;
		if(player.getRoom()==GameEntities.ROOM_ORCHIDS) {
			atOrchids = true;
		}
		return atOrchids;
	}
	
	private boolean isAtThicket() {
		boolean atThicket = false;
		if(player.getRoom()==GameEntities.ROOM_THICKET && rand.nextInt(3)==1) {
			atThicket = true;
		}
		return atThicket;
	}
	
	private boolean needAdjustStorm() {
		boolean adjustStorm = false;
		if(game.getItem(GameEntities.ITEM_STORM).getItemFlag()<1 && 
		   game.getItem(GameEntities.ITEM_WINE).getItemFlag() != -player.getRoom()) {
			adjustStorm = true;
		}
		return adjustStorm;
	}
	
	private boolean doesStormAppear() {
		boolean stormAppears = false;
		if((int) player.getStat("timeRemaining")<900 && 
			player.getRoom()==GameEntities.ROOM_PATH && 
			game.getItem(GameEntities.ITEM_STORM).getItemFlag()>0 && 
			stormRand ==2) {
			stormAppears = true;
		}
		return stormAppears;
	}
	
	private boolean doesPlayerHaveBeast() {
		boolean playerHasBeast = false;
		if(!game.getItem(GameEntities.ITEM_BEAST).isAtLocation(player.getRoom()) && 
			game.getItem(GameEntities.ITEM_BEAST).getItemLocation()>GameEntities.ROOM_CARRYING) {
			playerHasBeast = true;
		}
		return playerHasBeast;
	}
	
	private boolean doesOmeganMove() {
		boolean omeganMoves = false;
		if(!game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom())) {
			omeganMoves = true;
		}
		return omeganMoves;
	}
	
	private boolean isOmeganPresent() {
		boolean omeganPresent = false;
		if(game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom()) &&
		   !game.getItem(GameEntities.ITEM_MEDIAN).isAtLocation(player.getRoom()) &&
		   game.getItem(GameEntities.ITEM_COAL).getItemFlag()>-1) {
			omeganPresent = true;
		}
		return omeganPresent;
	}
	
	private boolean isSwampManPresent() {
		boolean swampManPresent = true;
		if(!game.getItem(GameEntities.ITEM_SWAMPMAN).isAtLocation(player.getRoom()) && 
			game.isRunningState()) {
			swampManPresent = false;
		}
		return swampManPresent;
	}
	
	private boolean doesSwampManTalk() {
		boolean swampManTalks = false;
		if(game.getItem(GameEntities.ITEM_SWAMPMAN).isAtLocation(player.getRoom()) && 
		   rand.nextInt(2)==1 &&
		   game.getItem(GameEntities.ITEM_SWAMPMAN).getItemFlag()==0) {
			swampManTalks = true;
		}
		return swampManTalks;
	}
	
	private boolean doesBoatmanAppear() {
		boolean boatmanAppears = false;
		if((player.getRoom()==GameEntities.ROOM_JETTY || 
			player.getRoom()==GameEntities.ROOM_ISLAND || 
			player.getRoom()==GameEntities.ROOM_BRIDGE) &&
			rand.nextInt(2)==1) {
			boatmanAppears = true;
		}
		return boatmanAppears;
	}
	
	private boolean isAtWell() {
		boolean atWell = false;
		if(player.getRoom()==GameEntities.ROOM_WELL && 
		   ((float) player.getStat("strength"))<70 && 
		   game.getItem(GameEntities.ITEM_MEDIAN).getItemFlag()==0 && 
		   rand.nextInt(4)==1) {
			atWell = true;
		}
		return atWell;
	}
	
	private boolean areLogmenPresent() {
		boolean logmenPresent = true;
		if(!game.getItem(GameEntities.ITEM_LOGMEN).isAtLocation(player.getRoom())) {
			logmenPresent = false;
		}
		return logmenPresent;
		
	}
	
	private boolean areLogmenUpset() {
		boolean logmenUpset = false;
		if(game.getItem(41).getItemFlag()<-4) {
			logmenUpset = true;
		}
		return logmenUpset;
	}
	
	private boolean isMedianFollowing() {
		boolean medianFollowing = false;
		if(game.getItem(GameEntities.ITEM_MEDIAN).getItemFlag()==0) {
			medianFollowing = true;
		}
		return medianFollowing;
	}
	
	private boolean isMedianHint() {
		boolean medianHint = false;
		if(game.getItem(GameEntities.ITEM_MEDIAN).getItemLocation()<GameEntities.ROOM_CLONE_ROOM && 
		   player.getRoom() != GameEntities.ROOM_BATTLEMENTS && 
		   player.getRoom() != GameEntities.ROOM_SANCTUM && 
		   game.getItem(GameEntities.ITEM_FLAG_49).getItemFlag()<1) {
			medianHint = true;
		}
		return medianHint;
	}
	
	private boolean isInVatRoom() {
		boolean inVatRoom = false;
		if(player.getRoom()==GameEntities.ROOM_CLONE_ROOM) {
			inVatRoom = true;
		}
		return inVatRoom;
	}
	
	private boolean isTooWeak() {
		
		boolean tooWeak = false;
		float str = (float) player.getStat("strength");
		int weight = (int) player.getStat("weight");
		if ((str-weight)<50) {
			tooWeak = true;
		}
		return tooWeak;
	}
	
	private boolean isAtClashingStones() {
		boolean atClashingStones = false;
		if (player.getRoom()==GameEntities.ROOM_CASTLE_ENTRANCE && 
			game.getItem(GameEntities.ITEM_PEBBLE).getItemFlag()>0) {
			atClashingStones = true;
		}
		return atClashingStones;
	}
	
	private boolean isWinGame() {
		boolean winGame = false;
		if (game.getItem(GameEntities.ITEM_PEBBLE).getItemFlag()+
			game.getItem(GameEntities.ITEM_STAFF).getItemFlag()+
			game.getItem(GameEntities.ITEM_COAL).getItemFlag()==-3) {
			winGame = true;
		}
		return winGame;
	}
	
	private boolean isLoseGame() {
		boolean loseGame = false;
		if ((int) player.getStat("timeRemaining")<0 || 
			(float) player.getStat("strength")<0 || 
			game.getItem(Constants.NUMBER_OF_NOUNS).getItemFlag()==1) {
			loseGame = true;
		}
		return loseGame;
	}
	
	private ActionResult atOrchids(ActionResult result) {
		Player player = result.getPlayer();
		player.setStat("wisdom",(int) player.getStat("wisdom")+rand.nextInt(2)+1);
		return new ActionResult(result.getGame(),player);
	}
	
	private ActionResult atThicket(ActionResult result) {
		Player player = result.getPlayer();
		Game game = result.getGame();
		player.setStat("strength",(float) player.getStat("strength")-1);
		game.addMessage("You are bitten.",false,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult adjustStorm(ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		game.getItem(GameEntities.ITEM_STORM).setItemFlag(game.getItem(GameEntities.ITEM_STORM).getItemFlag()+1);
		game.getItem(GameEntities.ITEM_STORM).setItemLocation(player.getRoom());
		player.setStat("strength",(float) player.getStat("strength")-1);
		return new ActionResult(game,player);
	}
	
	private ActionResult stormAppears(ActionResult result) {
		Game game = result.getGame();
		game.getItem(GameEntities.ITEM_STORM).setItemFlag(-(rand.nextInt(4)+6));
		game.addMessage(" A storm breaks overhead!",false,true);
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult moveBeast(ActionResult result) {
		Game game = result.getGame();
		game.getItem(GameEntities.ITEM_BEAST).setItemLocation(rand.nextInt(4)+1);
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult moveOmegan(ActionResult result) {		
		Game game = result.getGame();
		int part1 = 10 * (rand.nextInt(5)+1);
		int part2 = 7 * (rand.nextInt(3)+1);
		int newLocation = Math.min(part1+part2, 80);
		game.getItem(GameEntities.ITEM_OMEGAN).setItemLocation(newLocation);
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult omeganAttacks(ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		player.setStat("strength",(float) player.getStat("strength")-2);
		player.setStat("wisdom",(int) player.getStat("wisdom")-2);
		game.addMessage(" Omegan attack you!",false,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult swampManNotPresent(ActionResult result) {
		Game game = result.getGame();
		game.getItem(GameEntities.ITEM_SWAMPMAN).setItemLocation(76+rand.nextInt(2));
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult swampManTalks(ActionResult result) {
		Game game = result.getGame();
		game.getItem(GameEntities.ITEM_SWAMPMAN).setItemFlag(-1);
		game.addPanelMessage("The swampman tells his tale",true);
		game.addPanelMessage("Median can disable the equipment",false);
		
		if (game.getItem(GameEntities.ITEM_PEBBLE).isAtLocation(GameEntities.ROOM_CARRYING)) {
			game.addPanelMessage("and asks you for the pebble you carry.",false);
		}
		game.setMessageGameState();
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult boatmanAppears(ActionResult result) {
		Game game = result.getGame();
		game.getItem(25).setItemLocation(player.getRoom());
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult atWell(ActionResult result) {
		Game game = result.getGame();
		game.addMessage("Pushed into the pit",false,true);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult moveLogmen(ActionResult result) {
		Game game = result.getGame();
		game.getItem(41).setItemLocation(21+((rand.nextInt(3)+1)*10)+(rand.nextInt(2)+1));
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult logmenPresent(ActionResult result) {
		Game game = result.getGame();
		game.getItem(GameEntities.ITEM_LOGMEN).setItemFlag(game.getItem(GameEntities.ITEM_LOGMEN).getItemFlag()-1);
		
		//Upset the logmen
		if (areLogmenUpset()) {
			result = logmenUpset(new ActionResult(game,result.getPlayer()));
			game = result.getGame();
		}
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult logmenUpset(ActionResult result) {
		
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		String message = "The Logmen decide to have a little fun and";
		String messageTwo = "tie you up in a storeroom";
		game.getItem(GameEntities.ITEM_LOGMEN).setItemFlag(0);
		player.setStat("strength",(float) player.getStat("strength")-4);
		player.setStat("wisdom",(int) player.getStat("wisdom")-4);
		
		//Player located determines where end up
		if (player.getRoom()<GameEntities.ROOM_DUNES) {
			messageTwo = "throw you in the water";
			player.setRoom(GameEntities.ROOM_POOL);
		} else {
			player.setRoom(GameEntities.ROOM_STOREROOM);
		}
		
		game.setMessageGameState();
		game.addPanelMessage(message, true);
		game.addPanelMessage(messageTwo, false);
		
		//Do you lose items
		for (int i=3;i<5;i++) {
			if (game.getItem(i).getItemLocation()==GameEntities.ROOM_CARRYING) {
				game.getItem(i).setItemLocation(GameEntities.ROOM_TABLE);
			}
		}
		return new ActionResult(game,player);
	}
	
	private ActionResult medianFollowing(ActionResult result) {
		Game game = result.getGame();
		game.getItem(GameEntities.ITEM_MEDIAN).setItemLocation(player.getRoom());
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult medianHint(ActionResult result) {
		Game game = result.getGame();
		String messageOne = "Median can disable the equipment";
		game.setMessageGameState();
		game.addPanelMessage(messageOne,true);
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult inVatRoom(ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		player.setStat("strength",(float) player.getStat("strength")-1);
		game.addMessage("The gas leaking from the vats burns your lungs!",false,true);
		return new ActionResult(game,player);
	}
	
	private ActionResult dropItems(ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		int object = rand.nextInt(9)+1;
		if (game.getItem(object).isAtLocation(GameEntities.ROOM_CARRYING)) {
			game.getItem(object).setItemLocation(player.getRoom());
			game.addMessage(" You drop something.",false,false);
		}
		return new ActionResult(game,player);
	}
	
	private ActionResult atClashingStones(ActionResult result) {
		Game game = result.getGame();
		game.addMessage(" You can go no further",false,false);
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult winGame(ActionResult result) {
		Game game = result.getGame();
		String messageOne = "The world lives with new hope!";
		game.setMessageGameState();
		game.addPanelMessage(messageOne, false);
		game.addMessage("Your quest is over!",true,true);
		game.setEndGameState();
		return new ActionResult(game,result.getPlayer());
	}
	
	private ActionResult loseGame(ActionResult result) {
		Game game = result.getGame();
		game.addMessage( "You have failed, the evil one succeeds.",true,true);
		game.setEndGameState();
		return new ActionResult(game,result.getPlayer());
	}
}

/* 18 June 2025 - Created File
 * 20 June 2025 - Added atOrchids, atThicket and adjust storm
 * 21 June 2025 - Continued with the post move events, up to Omegan, now up to logmen
 * 22 June 2025 - Finished the post move events
 */
