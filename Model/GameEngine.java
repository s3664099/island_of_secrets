/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.16
Date: 16 December 2024
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
import java.util.Random;

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
		
		String description = "";
		
		if (player.getPanelFlag()==4) {
			description = "You are swimming in poisoned waters";
		} else {
			player.updateDisplayRoom();
			int room = player.getDisplayRoom();		
			description = String.format("You are %s",game.getRoomName(room));
		}
		
		return description;
	}
	
	public String getItems() {
		
		String itemDisplay = "";
		
		if (player.getPanelFlag()==0) {
			itemDisplay = game.getItems(player.getDisplayRoom());
		}
		
		return itemDisplay;
	}
	
	public String getExits() {
		
		String exitDisplay = "";
		
		if (player.getPanelFlag()==0) {
			exitDisplay = game.getExits(player.getRoom());
		}		
		
		return exitDisplay;
	}
	
	public String getMessage() {
		
		if (player.getPanelFlag()==4) {
			game.addMessage(String.format("|Your Strength = %s", player.getStrength()));
			
			if (player.getStrength()<15) {
				game.addMessage("|You are very weak!");
			}
			
			game.addMessage("|||Which Way?");
		}
		
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
		
		if (player.getPanelFlag()!=4) {
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
		
			//Shelter - 2220
			} else if (player.getPanelFlag()==5) {
			
				player.setPanelFlag(0);	
			
			} else {
				resetPanel(game);
			}						
		} else {
			
			this.game.setMessage("Ok");
			
			if (command.substring(0,1).equals("n")) {
				player.adjustPosition();
			} else if (!command.substring(0,1).equals("s") &&
					   !command.substring(0,1).equals("e") &&
					   !command.substring(0,1).equals("w")) {
				this.game.setMessage("I do not understand");
			}
			
			float strengthAdj = (float) ((player.getWeight()/Constants.noNouns+0.1)-3);
			player.adjustStrength(strengthAdj);
			
			if (player.checkPosition()) {
				player.setPanelFlag(0);
				this.game.setMessage("You surface");
				Random rand = new Random();
				player.setRoom(rand.nextInt(3)+30);
				player.resetPosition();
			} else if (player.getStrength()<1) {
				this.game.setMessage("You get lost and drown");
				player.setPanelFlag(0);
			}
			
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
	
	public boolean checkEndGame() {
		return game.checkEndGame();
	}
	
	public int getFinalScore() {
		
		boolean timeBonus = player.getTime()<640;
		double timeScore = player.getTime()/7.0;
		double applyTimeBonus = timeBonus ? -timeScore:0;
		return (int) ((int) player.getStrength()+player.getWisdom()+applyTimeBonus);
	}
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
16 December 2024 - Added code to handle swimming in poisoned waters
*/