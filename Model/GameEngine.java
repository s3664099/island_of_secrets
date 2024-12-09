/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.15
Date: 8 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import javax.swing.JPanel;

import Data.Constants;
import Data.Item;
import View.GameFrame;
import View.GamePanel;
import View.GivePanel;
import View.LightningPanel;
import View.MessagePanel;

public class GameEngine {
	
	private Game game;
	private Player player;
	private String[] commands = {"","",""};
	private GameFrame frame;
	
	public GameEngine(Game game,Player player) {
		this.game = game;
		this.player = player;
	}
		
	public String getTime() {
		return player.getTimeDetails();
	}
	
	public String getStatus() {
		return player.getStatus();
	}
	
	public String getRoom() {
		
		player.updateDisplayRoom();
		int room = player.getDisplayRoom();
		return String.format("You are %s",game.getRoomName(room));
	}
	
	public String getItems() {
		return game.getItems(player.getDisplayRoom());
	}
	
	public String getExits() {
		return game.getExits(player.getRoom());
	}
	
	public String getMessage() {
		
		String message = game.getMessage();
		game.clearMessage();
		
		return message;
	}
	
	public String getCommand(int number) {
		return game.getCommand(number);
	}
	
	public String[] getCommands() {
		return this.commands;
	}
	
	public void processCommand(String command,GamePanel game) {
		
		if (this.commands[0].equals("")) {
			this.commands[0] = command;
		} else if (this.commands[1].equals("")) {
			this.commands[1] = command;
		} else if (this.commands[2].equals("")) {
			this.commands[2] = command;
		} else {
			this.commands[0] = this.commands[1];
			this.commands[1] = this.commands[2];
			this.commands[2] = command;
		}
		
		CommandProcess processCommands = new CommandProcess(command,this.game);
		int verbNumber = processCommands.getVerbNumber();
		int nounNumber = processCommands.getNounNumber();
		
		//Either verb or noun doesn't exist
		if (verbNumber>Constants.noVerbs || nounNumber == 52) {
			this.game.setMessage("You can't "+command);
		}

		//Neither exists
		if (verbNumber>Constants.noVerbs && nounNumber == 52) {
			this.game.setMessage("What!!");
		}
		
		//No second word move to end
		if (nounNumber == -1) {
			nounNumber = 52;
		}
		
		this.player.update();
		Item item = this.game.getItem(nounNumber);
		String codedCommand = processCommands.codeCommand(this.player.getRoom(),nounNumber,item);
		processCommands.executeCommand(this.game, player, nounNumber);
		
		if (processCommands.checkLoadedGame()) {
			this.game = processCommands.getGame();
			this.player = processCommands.getPlayer();
		}
		
		if (player.getPanelFlag()==1) {
			setPanel(game, new GivePanel(this,nounNumber,codedCommand));
			player.setPanelFlag(0);
		} else if (player.getPanelFlag()==2) {
			setPanel(game, new LightningPanel(0,game,this));
			player.setPanelFlag(0);
		} else if (player.getPanelFlag()==3) {
			setPanel(game,new MessagePanel(game,this,this.game.getMsgOne(),
										   this.game.getMsgTwo(),this.game.getLoop()));
			player.setPanelFlag(0);
		
		//Swimming in poisonous waters - 2110
		} else if (player.getPanelFlag()==4) {

			player.setPanelFlag(0);			

		//Shelter - 2220
		} else if (player.getPanelFlag()==5) {
			
			player.setPanelFlag(0);	
			
		} else {
			resetPanel(game);
		}
	}
	
	public void processGive(String object,GamePanel game,int nounNumber,String codedNoun) {
		CommandProcess processCommands = new CommandProcess();
		processCommands.executeGive(this.game,this.player,nounNumber,object,codedNoun);
		resetPanel(game);
	}
	
	private void setPanel(JPanel game,JPanel panel) {
		game.removeAll();
		game.add(panel);
		game.revalidate();
		game.repaint();
	}
	
	private void resetPanel(GamePanel game) {

		game.removeAll();
		game.add(this);
		game.revalidate();
		game.repaint();
	}
	
	/*

	

	



	

	

	

	

	

	

	
	private void processAction(String action,String exits) {
		

		

		



		


		} else if (verbFound == 42) {

			//Quit Game
			this.gamePlaying = false;
			this.message = "YOU RELINQUISH YOUR QUEST.";
			this.itemFlag.updateIntData(this.itemFlag.getDataLength(),-1);
			
			//Press Return
		}
		
		//At Mad Orchards
		if (this.room == 61) {
			this.wisdom += rand.nextInt(2)+1;
		}
		
		//At Thicket of Biting Bushes
		if (this.room == 14 && rand.nextInt(3)==1) {
			this.strength --;
			this.message = "YOU ARE BITTEN";
		}
		
		//Living Storm Movement
		if (this.itemFlag.getIntData(36)<1 && this.room != -this.itemFlag.getIntData(22)) {
			int newValue = this.itemFlag.getIntData(36);
			this.itemFlag.updateIntData(36, newValue++);
			this.itemLocation.updateIntData(36,this.room);
			this.strength --;
		}
		
		//Wild Canyon Beast Movement
		if (this.room != this.itemLocation.getIntData(16) && this.itemLocation.getIntData(16)>0) {
			this.itemLocation.updateIntData(16,rand.nextInt(4)+1);
		}
		
		//Omegan Movement
		if (this.room != this.itemLocation.getIntData(39)) {
			int part1 = 10 * rand.nextInt(5)+2;
			int part2 = 7 *rand.nextInt(3)+1;
			int newLocation = Math.min(part1+part2, 80);			
		}
		
		//Omegan Present
		if (this.room == this.itemLocation.getIntData(39) &&
			this.room != this.itemLocation.getIntData(43) &&
			this.itemFlag.getIntData(13)>-1) {
			this.strength -=2;
			this.wisdom -=2;
		}
		
		//Swampman's Movement
		if (this.room<78) {
			this.itemLocation.updateIntData(32,76+rand.nextInt(2));
		}
		
		//Boatman location update
		if (this.room == 33 || this.room==57 || this.room==73 || rand.nextInt(2)==1) {
			this.itemLocation.updateIntData(25,this.room);
		}
		
		//Swampman Present?
		if (this.room == this.itemLocation.getIntData(32) && rand.nextInt(2)==1 &&
			this.itemFlag.getIntData(32) == 0) {
			
			System.out.println("THE SWAMPMAN TELLS HIS TALE");
			//Clears Screens - Displays above - pauses
			this.itemFlag.updateIntData(31,-1);
			this.message = "MEDIAN CAN DISABLE THE EQUIPMENT";
			
			if (this.itemLocation.getIntData(8) == 0) {
				this.message += " AND ASKS FOR THE PEBBLE YOU CARRY";
			}
		}
		
		//Next to the well
		if (this.room == 19 && this.strength<70 && this.itemFlag.getIntData(43)==0 &&
			rand.nextInt(4)==1)  {
			this.message = "PUSHED INTO THE PIT";
			this.itemFlag.updateIntData(this.noNouns,1);
		}
		
		//Not in same location as logmen - Moves them
		if (this.room != this.itemLocation.getIntData(41)) {
			this.itemLocation.updateIntData(41,21+(rand.nextInt(3)*10)+rand.nextInt(2));
		
		//Same room as the logmen
		} else {
			
			int flagValue = this.itemFlag.getIntData(41);
			flagValue -=1;
			this.itemFlag.updateIntData(41,flagValue);
			
			if (flagValue<-4) {
				
				//Clear Screen
				this.message = "";
				System.out.println("THE LOGMEN DECIDE TO HAVE A LITTLE FUN AND ");
				this.itemFlag.updateIntData(41,0);
				this.strength -= 4;
				this.wisdom -=4;
				
				if (this.room<34) {
					System.out.println("THROW YOU IN THE WATER");
					this.room = 32;
				} else {
					System.out.println("TIE YOU UP IN A STOREROOM");
					this.room = 51;
				}
				
				//Display message & Pause
				for (int i=3;i<5;i++) {
					if (this.itemLocation.getIntData(i) == 0) {
						this.itemLocation.updateIntData(i,42);
					}
				}
			}
		}
		
		//Move Median to player's location
		if (this.itemFlag.getIntData(43)==0) {
			this.itemLocation.updateIntData(43,this.room);
		}
		
		if ((this.itemLocation.getIntData(43)<18) && this.room !=9 && this.room !=10 &&
			 this.itemFlag.getIntData(this.itemLocation.getDataLength()-2)<1) {
			this.message = "MEDIAN CAN DISABLE THE EQUIPMENT";
		}
		
		if (this.room == 18) {
			this.strength --;
		}
		
		//Too Week to carry items
		if (this.strength<50) {
			int object = rand.nextInt(9);
			
			String addMessage = "";
			
			if (object == 4 && this.itemLocation.getIntData(object)==0) {
				this.itemLocation.updateIntData(object,81);
				addMessage = " AND IT BREAKS!";
			}
			
			if (this.itemLocation.getIntData(object)==0 && object< this.foodLine) {
				this.itemLocation.updateIntData(object, this.room);
				this.weight --;
				this.message = "YOU DROP SOMETHING"+addMessage;
			}
		}
		
		//Updates the living storms
		if (this.timeRemaining<900 && this.room==23 && this.itemFlag.getIntData(36)>0 &&
			rand.nextInt(3)==3) {	
			this.itemFlag.updateIntData(36,rand.nextInt(4)+6*-1);
			this.message = "A STORM BREAKS OVERHEAD!";
		}
		
		//Standing Stones
		if (this.room == 47 && this.itemFlag.getIntData(8)>0) {
			this.message += " YOU CAN GO NO FURTHER";
		}
		
		if (this.itemFlag.getIntData(8)+this.itemFlag.getIntData(11)+this.itemFlag.getIntData(13)==-3) {
			this.itemFlag.updateIntData(this.itemFlag.getDataLength(),1);
			System.out.println("THE WORLD LIVES WITH NEW HOPE");
			this.message = "YOUR QUEST IS OVER";
			this.gamePlaying = false;
			//Display - Pause
		}
		
		if (this.timeRemaining<1 || this.strength<1 || this.wisdom<1) {
			this.message = "YOU HAVE FAILED, THE EVIL ONE SUCCEEDS";
			this.gamePlaying = false;
		}
	}
	
	private void poisonWater() {
		
		this.wisdom --;
		Random rand = new Random();
		this.room = rand.nextInt(4)+1;
		ClearScreen();
		System.out.printf("%nSWIMMING IN THE POISONOUS WATERS%n YOUR STRENGHT = %d%n",this.strength);
		this.message = "YOU SURFACE";
		int moveCount=0;
		String codedNoun = "";
		
		while ((this.room/2>moveCount) && this.strength>2) {

			for (int x=1;x<this.room;x++) {
				if (this.strength<15) {
					System.out.println("YOU ARE VERY WEAK");
				}	
				String action = getAction("%-10sWHICH WAY: ");
				codedNoun = String.format("%s%s", codedNoun,action.charAt(0));
			}
		
			codedNoun = codedNoun.toUpperCase();

			for (int x=0;x<this.room-1;x++) {
				this.strength -= ((this.weight/this.noItems)+.1)-3;
			
				if (codedNoun.substring(x,x+1).equals("N")) {
					moveCount++;
				}
			}
		}
		
		if (this.strength<2) {
			this.message = "YOU LOST AND DROWNED";
		}
		
		this.room = 30+rand.nextInt(3)+1;
	}
	

	
	private void flyRandom() {
		this.message = "#YOU ANGER THE BIRD WHICH FLIES YOU TO A REMOTE PLACE";
		this.room = 63+rand.nextInt(6);
	}
	

	

	*/
} 
/*
8 September 2024 - Created File
9 September 2024 - Added method to retrieve input and started processing command
10 September 2024 - Added code to respond to incorrect commands
12 September 2024 - Started building the move methods.
14 September 2024 - Added the poison water method and continued the move method
					completed main move section.
17 September 2024 - Finished Movement and started on take objects
21 September 2024 - Finished give & drop methods2750 GOSUB720:GOSUB2760:RETURN
22 September 2024 - Completed Eat & Drink methods
23 September 2024 - Completed ride, open and started break
30 September 2024 - Continued working on the break function
4 October 2024 - Finished Break Method
5 October 2024 - Finished Attack Method
12 October 2024 - Finished Last part of attack method and added swim method.
				  Added shelter method & set up Help method
18 October 2024 - Added the help method
23 October 2024 - Added rub method, examine & fill method
24 October 2024 - Added the say & rest method
26 October 2024 - Added wave method and refactored data name and methods. Added info method
27 October 2024 - Started writing the updates for every move
28 October 2024 - Added Swampman Section
29 October 2024 - Added end game functions & completed basic conversion
	  			- Upgraded to version 1.0
1 November 2024 - Cleared previous methods and stored game & player class
4 November 2024 - Added methods to retrieve strings from game & player
5 November 2024 - Added get exit method
				- Added get message method
				- Added get previous command method
				- Added process command method
7 November 2024 - Added array to hold three previous commands
8 November 2024 - Change frame to panel and added refresh options.
9 November 2024 - Began working on processing the command
11 November 2024 - Got the command processing working and now working on coding the command
12 November 2024 - Added extra parameter to create coded command being noun number
13 November 2024 - Added code to execute the command
25 November 2024 - Added new panel for handling the give command.
				   Moved panel generating functions into new methods
29 November 2024 - Passed coded command to GivePanel
30 November 2024 - Added message panel for response to giving Median a shiny pebble
1 December 2024 - Added panel to display messages.
7 December 2024 - Added stub for poisonous waters subgame
8 December 2024 - Added code to retrieve loaded game details.
*/