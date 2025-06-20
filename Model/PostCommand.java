/*
Title: Island of Secrets Post Command Functions
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 18 June 2025
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
	
	public PostCommand(ActionResult result) {
		game = result.getGame();
		player = result.getPlayer();
	}
	
	public ActionResult postUpdates() {
		
		return new ActionResult(game,player);
	}
	
	public void updates(Game game, Player player) {
		
		ActionResult result = new ActionResult(game,player);
		int stormRand = rand.nextInt(3);
		
		if (isAtOrchids()) {
			result = atOrchids(result);
		}
		
		if(isAtThicket()) {
			result = atThicket(result);
		}
		
		if(needAdjustStorm()) {
			result = adjustStorm(result);
		}
			
		//Adjusting flag of living storm
		if () {
			
		}

		

		//Does the living storm appear
		if ((int) player.getStat("timeRemaining")<900 && player.getRoom()==23 && 
			game.getItem(36).getItemFlag()>0 && stormRand ==2) {
			game.getItem(36).setItemFlag(-(rand.nextInt(4)+6));
			game.addMessage(" A storm breaks overhead!",false,true);
		}
		
		//Location of the wild canyon beast
		if (!game.getItem(16).isAtLocation(player.getRoom()) && 
			game.getItem(16).getItemLocation()>0) {
			game.getItem(16).setItemLocation(rand.nextInt(4)+1);
		}
		
		//Location of Omegan
		if (!game.getItem(39).isAtLocation(player.getRoom())) {
			int part1 = 10 * (rand.nextInt(5)+1);
			int part2 = 7 * (rand.nextInt(3)+1);
			int newLocation = Math.min(part1+part2, 80);
			game.getItem(39).setItemLocation(newLocation);
		
		//Is Omegan with player?
		} else if (game.getItem(39).isAtLocation(player.getRoom()) &&
				   !game.getItem(43).isAtLocation(player.getRoom()) &&
				   game.getItem(13).getItemFlag()>-1) {
			player.setStat("strength",(float) player.getStat("strength")-2);
			player.setStat("wisdom",(int) player.getStat("wisdom")-2);
		}
			
		//Swampman's Position if not with player
		if (!game.getItem(32).isAtLocation(player.getRoom()) && game.isRunningState()) {
			game.getItem(32).setItemLocation(76+rand.nextInt(2));
		}
		
		//Swampman with the player?
		if (game.getItem(32).isAtLocation(player.getRoom()) && rand.nextInt(2)==1 &&
			game.getItem(32).getItemFlag()==0) {
			
			game.getItem(32).setItemFlag(-1);
			
			game.addPanelMessage("The swampman tells his tale",true);
			game.addPanelMessage("Median can disable the equipment",false);
			
			if (game.getItem(8).isAtLocation(0)) {
				game.addPanelMessage("and asks you for the pebble you carry.",false);
			}
			game.setMessageGameState();
		}
		
		//Does the boatman appear?
		if ((player.getRoom()==33 || player.getRoom()==57 || player.getRoom()==73) &&
			rand.nextInt(2)==1) {
			game.getItem(25).setItemLocation(player.getRoom());
		}
		
		//Check if pushed into well
		if (player.getRoom()==19 && ((float) player.getStat("strength"))<70 && 
			game.getItem(43).getItemFlag()==0 && rand.nextInt(4)==1) {
			game.addMessage("Pushed into the pit",false,true);
			game.getItem(Constants.NUMBER_OF_NOUNS).setItemFlag(1);
		}
		
		//Movement of the logmen if player not present
		if (!game.getItem(41).isAtLocation(player.getRoom())) {
			game.getItem(41).setItemLocation(21+((rand.nextInt(3)+1)*10)+(rand.nextInt(2)+1));
		} else {
			game.getItem(41).setItemFlag(game.getItem(41).getItemFlag()-1);
			
			//Upset the logmen
			if (game.getItem(41).getItemFlag()<-4) {
				
				String message = "The Logmen decide to have a little fun and";
				String messageTwo = "tie you up in a storeroom";
				game.getItem(41).setItemFlag(0);
				player.setStat("strength",(float) player.getStat("strength")-4);
				player.setStat("wisdom",(int) player.getStat("wisdom")-4);
				
				//Player located determines where end up
				if (player.getRoom()<34) {
					messageTwo = "throw you in the water";
					player.setRoom(32);
				} else {
					player.setRoom(51);
				}
				
				game.setMessageGameState();
				game.addPanelMessage(message, true);
				game.addPanelMessage(messageTwo, false);
				
				//Do you lose items
				for (int i=3;i<5;i++) {
					if (game.getItem(i).getItemLocation()==0) {
						game.getItem(i).setItemLocation(42);
					}
				}
			}
		}
		
		//Move Median to player location is condition correct
		if (game.getItem(43).getItemFlag()==0) {
			game.getItem(43).setItemLocation(player.getRoom());
		}
		
		//Replays notice re: Median
		if (game.getItem(43).getItemLocation()<18 && player.getRoom() != 9 && 
			player.getRoom() != 10 && game.getItem(49).getItemFlag()<1) {
			
			String messageOne = "Median can disable the equipment";
			game.setMessageGameState();
			game.addPanelMessage(messageOne,true);
		}
		
		//Player in the clone vat room
		if (player.getRoom()==18) {
			player.setStat("strength",(float) player.getStat("strength")-1);
			game.addMessage("The gas leaking from the vats burns your lungs!",false,true);
		}
				
		//Too weak to carry something
		float str = (float) player.getStat("strength");
		int weight = (int) player.getStat("weight");
		if ((str-weight)<50) {
			int object = rand.nextInt(9)+1;
			
			if (game.getItem(object).isAtLocation(0)) {
				game.getItem(object).setItemLocation(player.getRoom());
				game.addMessage(" You drop something.",false,false);
			}
		}
				
		//Near the clashing stones
		if (player.getRoom()==47 && game.getItem(8).getItemFlag()>0) {
			game.addMessage(" You can go no further",false,false);
		}
		
		//Involving staff, pebble & coal - seems like a win condition
		if (game.getItem(8).getItemFlag()+game.getItem(11).getItemFlag()+game.getItem(13).getItemFlag()==-3) {
			
			//The flags of the above must total -3
			String messageOne = "The world lives with new hope!";
			game.setMessageGameState();
			game.addPanelMessage(messageOne, false);
			game.addMessage("Your quest is over!",true,true);
			game.setEndGameState();
		}
		
		//Fail Quest conditions
		if ((int) player.getStat("timeRemaining")<0 || ((float) player.getStat("strength"))<0 || game.getItem(Constants.NUMBER_OF_NOUNS).getItemFlag()==1) {
			game.addMessage( "You have failed, the evil one succeeds.",true,true);
			game.setEndGameState();
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

}

/* 18 June 2025 - Created File
 * 20 June 2025 - Added atOrchids, atThicket and adjust storm
 */
