/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.3
Date: 17 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import javax.swing.JPanel;

import Data.Constants;
import Data.Item;
import View.GamePanel;
import View.LightningPanel;
import View.MapPanel;
import View.MessagePanel;
import java.util.Random;

public class GameEngine {
	
	private Game game;
	private Player player;
	private String[] commands = {"","",""};
	private String codedCommand;
	private int nounNum;
	private Test test = new Test();
	private boolean restart = false;
	
	public GameEngine(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.test.setTest(this.game, this.player);
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
	
	public Game getGame() {
		return this.game;
	}
	
	public String getExits() {
		
		String exitDisplay = "";
		
		if (player.getPanelFlag()==0) {
			exitDisplay = game.getExits(player.getRoom());
		}		
		
		return exitDisplay;
	}
	
	public String getSpecialExits() {

		String exitDisplay = "";
		
		if (player.getPanelFlag()==0) {
			exitDisplay = game.getSpecialExits(player.getRoom());
		}		
		
		return exitDisplay;		
		
	}
	
	public String getMessage() {
		
		//Swimming in poisoned Waters?
		if (player.getPanelFlag()==4) {
			
			if (player.getStrength()<15) {
				game.addMessage("|You are very weak!");
			}
			
			game.addMessage("|||Which Way?");
		}
		
		String message = game.getMessage();
		game.clearMessage();
		
		return message;
	}
	
	//Passes three previous commands to Panel.
	public String[] getCommands() {
		return this.commands;
	}
	
	public void processCommand(String command,GamePanel game) {
		
		//Saves the commands into the previous command list
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
		
		//Checks if player 'Swimming in Poisoned Waters'
		if (player.getPanelFlag()!=4) {
			
			CommandProcess processCommands = new CommandProcess(command,this.game);
			int verbNumber = processCommands.getVerbNumber();
			int nounNumber = processCommands.getNounNumber();
		
			//Either verb or noun doesn't exist
			if (verbNumber>Constants.noVerbs || nounNumber == Constants.noNouns) {
				this.game.setMessage("You can't "+command);
			}

			//Neither exists
			if (verbNumber>Constants.noVerbs && nounNumber == 52) {
				this.game.setMessage("What!!");
			}
		
			//No second word move to end
			if (nounNumber == -1) {
				nounNumber = Constants.noNouns;
			}
		
			this.player.update();
			Item item = this.game.getItem(nounNumber);
			String codedCommand = processCommands.codeCommand(this.player.getRoom(),nounNumber,item);
			processCommands.executeCommand(this.game, player, nounNumber);
			
			this.codedCommand = codedCommand;
			this.nounNum = nounNumber;
			
			//Has a game been loaded?
			if (processCommands.checkLoadedGame()) {
				this.game = processCommands.getGame();
				this.player = processCommands.getPlayer();
			}
			
			test.displayValue(this.game, this.player);
			
			determinePanel(game);
			
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
				this.game.endGame();
			}
			test.displayValue(this.game, this.player);
			resetPanel(game);
		}
	}
	
	public int getResponseType() {
		return game.getResponse();
	}
	
	public void processGive(String object,GamePanel game) {

		//Checks if the response is 'to xxxx'
		String[] instructions = object.split(" ");
		if (instructions[0].equals("to") && instructions.length==2) {
			object = instructions[1];
		}
		
		//Is the reponse correct for a give command?
		if (object.split(" ").length==1) {
			CommandProcess processCommands = new CommandProcess();
			processCommands.executeGive(this.game,this.player,this.nounNum,object,this.codedCommand);
		} else {
			this.game.addMessage("I'm sorry, I don't understand.");
		}
		
		this.game.setResponse(0);
		determinePanel(game);
	}
	
	public void processShelter(String object,GamePanel game) {
		
		//Checks if response is 1,2,or 3
		if (object.equals("1") || object.equals("2") || object.equals("3")) {
			CommandProcess processCommands = new CommandProcess();
			
			//Determines the room the player goes to
			int room = 44;
			if (object.equals("2")) {
				room = 11;
			} else if (object.equals("3"))  {
				room = 41;
			}
			
			processCommands.executeShelter(this.game, this.player,room);
		} else {
			this.game.addMessage("Please enter either 1,2 or 3");
		}
		
		this.game.setResponse(0);
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
	
	//What panel is to be displayed after the command is executed.
	private void determinePanel(GamePanel game) {
		
		if (player.getPanelFlag()==2) {
			setPanel(game, new LightningPanel(0,game,this));
			player.setPanelFlag(0);
		} else if (player.getPanelFlag()==3) {
			setPanel(game,new MessagePanel(game,this,this.game.getMsgOne(),
					 this.game.getMsgTwo(),this.game.getLoop()));
			player.setPanelFlag(0);
		} else {
			resetPanel(game);
		}	
	}
	
	//Displays the map panel
	public void setMapPanel(GamePanel game) {
		setPanel(game,new MapPanel(game,this));
	}
	
	//Displays the game panel
	public void setGamePanel(GamePanel game) {
		resetPanel(game);
	}
	
	public void restart(Game game, Player player) {
		this.game = game;
		this.player = player;
		
		for (int x=0;x<this.commands.length;x++) {
			this.commands[x] = "";
		}
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
22 December 2024 - Added check to determine response type
23 December 2024 - Added shelter process
				 - Updated to version 2.
26 December 2024 - Removed the strength display, and added some comments
29 December 2024 - Added calls to the test object
				 - Added call to the special exits in the room
3 January 2025 - Moved the panel determination to separate function
11 January 2025 - Added the end game flag to when the player runs out of strength while swimming.
15 January 2025 - Added code so shelter goes to the correct place.
31 January 2025 - Completed Testing and increased version
1 February 2025 - Removed unused variables
5 February 2025 - Added getter for the game
17 February 2025 - Added restart function
*/