/*
Title: Island of Secrets Post Command Functions
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import data.Constants;
import data.GameEntities;

import java.util.Random;

import command_process.ActionResult;

/**
 * Handles post-command updates in the adventure game.
 *
 * <p>{@code PostCommand} manages game state changes that occur automatically
 * after a player executes a command. This includes environmental effects,
 * NPC actions, item movements, and win/loss checks.</p>
 *
 * <p>The class is designed to be used after every player action to update the game state
 * according to the game's logic rules, including random events like storms,
 * attacks by Omegan, swamp man interactions, and other events tied to rooms
 * or items.</p>
 */
public class PostCommand {
	
	private Game game;
	private Player player;
	private final Random rand = new Random();
	private final int stormRand = rand.nextInt(3);

    /**
     * Constructs a {@code PostCommand} handler from an {@link ActionResult}.
     *
     * @param result the result of the previous player action containing game and player states
     */
	public PostCommand(ActionResult result) {
		game = result.getGame();
		player = result.getPlayer();
	}
	
    /**
     * Processes all post-command updates.
     *
     * <p>This method checks and executes all environmental effects,
     * NPC movements, item effects, win/lose conditions, and other
     * state changes that occur after a player command.</p>
     *
     * @return the updated {@link ActionResult} containing the game and player
     */	
	public ActionResult postUpdates() {

		if (isAtOrchids()) {atOrchids();}
		
		if (isAtThicket()) {atThicket();}
		
		if (needAdjustStorm()) {adjustStorm();}
		
		if (doesStormAppear()) {stormAppears();}

		if (doesPlayerHaveBeast()) {moveBeast();}
		
		if (doesOmeganMove()) {moveOmegan();} 
		else if (isOmeganPresent()) {omeganAttacks();} 
		
		if (isSwampManPresent()) {swampManNotPresent();}
		
		if (doesSwampManTalk()) {swampManTalks();}
					
		if (doesBoatmanAppear()) {boatmanAppears();}
		
		if (isAtWell()) {atWell();}
		
		if (!areLogmenPresent()) {moveLogmen();} 
		else {logmenPresent();}
		
		if (isMedianFollowing()) {medianFollowing();}
		
		if (isMedianHint()) {medianHint();}
		
		if (isInVatRoom()) {inVatRoom();}
		
		if (isTooWeak()) {dropItems();}
				
		if (isAtClashingStones()) {atClashingStones();}
		
		if (isWinGame()) {winGame();}
		
		if (isLoseGame()) {loseGame();}
		
		return new ActionResult(game,player,true);
	}

    // ================== Condition Checks ================== //
	
	private boolean isAtOrchids() {
		return player.getRoom()==GameEntities.ROOM_ORCHIDS;
	}
	
	private boolean isAtThicket() {
		return player.getRoom()==GameEntities.ROOM_THICKET && rand.nextInt(3)==1;
	}
	
	private boolean needAdjustStorm() {
		return (game.getItem(GameEntities.ITEM_STORM).getItemFlag()<1 && 
				game.getItem(GameEntities.ITEM_WINE).getItemFlag() != -player.getRoom() &&
				player.getRoom() != GameEntities.ROOM_GRANDPAS_SHACK &&
				player.getRoom() != GameEntities.ROOM_SNELM_LAIR &&
				player.getRoom() != GameEntities.ROOM_HUT);
	}
	
	private boolean doesStormAppear() {
		return (int) player.getStat("timeRemaining")<900 && 
				player.getRoom()==GameEntities.ROOM_PATH && 
				game.getItem(GameEntities.ITEM_STORM).getItemFlag()>0 && 
				stormRand ==2;
	}
	
	private boolean doesPlayerHaveBeast() {
		return !game.getItem(GameEntities.ITEM_BEAST).isAtLocation(player.getRoom()) && 
				game.getItem(GameEntities.ITEM_BEAST).getItemLocation()>GameEntities.ROOM_CARRYING;
	}
	
	private boolean doesOmeganMove() {
		return !game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom());
	}
	
	private boolean isOmeganPresent() {
		return game.getItem(GameEntities.ITEM_OMEGAN).isAtLocation(player.getRoom()) &&
			   !game.getItem(GameEntities.ITEM_MEDIAN).isAtLocation(player.getRoom()) &&
			   game.getItem(GameEntities.ITEM_COAL).getItemFlag()>-1;
	}
	
	private boolean isSwampManPresent() {
		return !game.getItem(GameEntities.ITEM_SWAMPMAN).isAtLocation(player.getRoom()) && 
				game.isRunningState();
	}
	
	private boolean doesSwampManTalk() {
		return game.getItem(GameEntities.ITEM_SWAMPMAN).isAtLocation(player.getRoom()) && 
			   rand.nextInt(2)==1 &&
			   game.getItem(GameEntities.ITEM_SWAMPMAN).getItemFlag()==0;
	}
	
	private boolean doesBoatmanAppear() {
		return (player.getRoom()==GameEntities.ROOM_JETTY || 
				player.getRoom()==GameEntities.ROOM_ISLAND || 
				player.getRoom()==GameEntities.ROOM_BRIDGE) &&
				rand.nextInt(2)==1;
	}
	
	private boolean isAtWell() {
		return player.getRoom()==GameEntities.ROOM_WELL && 
				   ((float) player.getStat("strength"))<70 && 
				   game.getItem(GameEntities.ITEM_MEDIAN).getItemFlag()==0 && 
				   rand.nextInt(4)==1;
	}
	
	private boolean areLogmenPresent() {
		return game.getItem(GameEntities.ITEM_LOGMEN).isAtLocation(player.getRoom());
		
	}
	
	private boolean areLogmenUpset() {
		return game.getItem(41).getItemFlag()<-4;
	}
	
	private boolean isMedianFollowing() {
		return game.getItem(GameEntities.ITEM_MEDIAN).getItemFlag()==0;
	}
	
	private boolean isMedianHint() {
		return game.getItem(GameEntities.ITEM_MEDIAN).getItemLocation()<GameEntities.ROOM_CLONE_ROOM && 
				   player.getRoom() != GameEntities.ROOM_BATTLEMENTS && 
				   player.getRoom() != GameEntities.ROOM_SANCTUM && 
				   game.getItem(GameEntities.ITEM_FLAG_49).getItemFlag()<1;
	}
	
	private boolean isInVatRoom() {
		return player.getRoom()==GameEntities.ROOM_CLONE_ROOM;
	}
	
	private boolean isTooWeak() {
		float str = (float) player.getStat("strength");
		int weight = (int) player.getStat("weight");
		return (str-weight)<50;
	}
	
	private boolean isAtClashingStones() {
		return player.getRoom()==GameEntities.ROOM_CASTLE_ENTRANCE && 
				game.getItem(GameEntities.ITEM_PEBBLE).getItemFlag()>0;
	}
	
	private boolean isWinGame() {
		return game.getItem(GameEntities.ITEM_PEBBLE).getItemFlag()+
				game.getItem(GameEntities.ITEM_STAFF).getItemFlag()+
				game.getItem(GameEntities.ITEM_COAL).getItemFlag()==-3;
	}
	
	private boolean isLoseGame() {
		return (int) player.getStat("timeRemaining")<0 || 
				(float) player.getStat("strength")<0 || 
				(game.getItem(Constants.NUMBER_OF_NOUNS).getItemFlag()==1 &&
				!game.isMessageState());
	}

    // ================== Actions ================== //
	
	private void atOrchids() {
		player.setStat("wisdom",(int) player.getStat("wisdom")+rand.nextInt(2)+1);
	}
	
	private void atThicket() {
		player.setStat("strength",(float) player.getStat("strength")-1);
		game.addMessage("You are bitten.",false,true);
	}
	
	private void adjustStorm() {
		game.getItem(GameEntities.ITEM_STORM).setItemFlag(game.getItem(GameEntities.ITEM_STORM).getItemFlag()+1);
		game.getItem(GameEntities.ITEM_STORM).setItemLocation(player.getRoom());
		player.setStat("strength",(float) player.getStat("strength")-1);
	}
	
	private void stormAppears() {
		game.getItem(GameEntities.ITEM_STORM).setItemFlag(-(rand.nextInt(4)+6));
		game.addMessage(" A storm breaks overhead!",false,true);
	}
	
	private void moveBeast() {
		game.getItem(GameEntities.ITEM_BEAST).setItemLocation(rand.nextInt(4)+1);
	}
	
	private void moveOmegan() {
		
		int newLocation = GameEntities.ROOM_GRANDPAS_SHACK;
		
		while (newLocation == GameEntities.ROOM_GRANDPAS_SHACK) {
			int part1 = 10 * (rand.nextInt(5)+1);
			int part2 = 7 * (rand.nextInt(3)+1);
			newLocation = Math.min(part1+part2, 80);
		}
		game.getItem(GameEntities.ITEM_OMEGAN).setItemLocation(newLocation);
	}
	
	private void omeganAttacks() {
		player.setStat("strength",(float) player.getStat("strength")-2);
		player.setStat("wisdom",(int) player.getStat("wisdom")-2);
		game.addMessage("Omegan attacks you!",false,true);
	}
	
	private void swampManNotPresent() {
		game.getItem(GameEntities.ITEM_SWAMPMAN).setItemLocation(76+rand.nextInt(2));
	}
		
	private void swampManTalks() {
		game.getItem(GameEntities.ITEM_SWAMPMAN).setItemFlag(-1);
		game.addPanelMessage("The swampman tells his tale",true);
		game.addPanelMessage("Median can disable the equipment",false);
		
		if (game.getItem(GameEntities.ITEM_PEBBLE).isAtLocation(GameEntities.ROOM_CARRYING)) {
			game.addPanelMessage("and asks you for the pebble you carry.",false);
		}
		game.setMessageGameState();
	}
	
	private void boatmanAppears() {
		game.getItem(25).setItemLocation(player.getRoom());
	}
	
	private void atWell() {
		game.addMessage("Pushed into the pit",false,true);
		game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
	}
	
	private void moveLogmen() {
		game.getItem(41).setItemLocation(21+((rand.nextInt(3)+1)*10)+(rand.nextInt(2)+1));
	}
	
	private void logmenPresent() {
		game.getItem(GameEntities.ITEM_LOGMEN).setItemFlag(game.getItem(GameEntities.ITEM_LOGMEN).getItemFlag()-1);
		
		//Upset the logmen
		if (areLogmenUpset()) {
			logmenUpset();
		}
	}
	
	private void logmenUpset() {
		
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
	}
	
	private void medianFollowing() {
		game.getItem(GameEntities.ITEM_MEDIAN).setItemLocation(player.getRoom());
	}
	
	private void medianHint() {
		String messageOne = "Median can disable the equipment";
		game.setMessageGameState();
		game.addPanelMessage(messageOne,true);
	}
	
	private void inVatRoom() {
		player.setStat("strength",(float) player.getStat("strength")-1);
		game.addMessage("The gas leaking from the vats burns your lungs!",false,true);
	}
	
	private void dropItems() {		
		int object = rand.nextInt(9)+1;
		if (game.getItem(object).isAtLocation(GameEntities.ROOM_CARRYING)) {
			game.getItem(object).setItemLocation(player.getRoom());
			game.addMessage(" You drop something.",false,false);
		}
	}
	
	private void atClashingStones() {
		game.addMessage(" You can go no further",false,false);
	}
	
	private void winGame() {
		String messageOne = "The world lives with new hope!";
		game.setMessageGameState();
		game.addPanelMessage(messageOne, false);
		game.addMessage("Your quest is over!",true,true);
		game.setEndGameState();
	}
	
	private void loseGame() {
		game.addMessage( "You have failed, the evil one succeeds.",true,true);
		game.setEndGameState();
	}
}

/* 18 June 2025 - Created File
 * 20 June 2025 - Added atOrchids, atThicket and adjust storm
 * 21 June 2025 - Continued with the post move events, up to Omegan, now up to logmen
 * 22 June 2025 - Finished the post move events. Made game and player changeable. Updated classes to get rid of result.
 * 16 July 2025 - Fixed error with omegan attacking
 * 26 July 2025 - Updated so that set message panel taken into account
 * 13 August 2025 - Fixed swampman movement
 * 17 August 2025 - Added JavaDocs
 * 3 September 2025 - Updated with new ActionResult changes
 * 13 October 2025 - Changed so Omegan does not go to Grandpa's Shack
 * 20 October 2025 - Fixed problem where Omegan is only in Grandpa's Shack
 * 30 November 2025 - Excluded storm from following player into shelter
 * 1 December 2025 - Tightened up the code
 * 3 December 2025 - Increased version number
 */
