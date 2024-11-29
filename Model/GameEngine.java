/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.10
Date: 25 November 2024
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
		return player.getTime();
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
				
		if (player.getPanelFlag()==1) {
			setPanel(game, new GivePanel(this,nounNumber));
			player.setPanelFlag(0);
		} else if (player.getPanelFlag()==2) {
			setPanel(game, new LightningPanel(0,game,this));
			player.setPanelFlag(0);			
		} else {
			resetPanel(game);
		}
	}
	
	public void processGive(String object,GamePanel game,int nounNumber) {
		CommandProcess processCommands = new CommandProcess();
		processCommands.executeGive(nounNumber,object);
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
		
		String[] actions = {"",""};
		action = action.toUpperCase();
		String[] noWords = action.split(" ");
		actions[0] = noWords[0];
		String response = "";
		
		if (noWords.length>1) {
			actions[1] = action.substring(actions[0].length()+1);
			System.out.println(actions[1]);
		} else {
			actions[1] = "???";
		}
				
		int verbFound = getWords(actions[0],this.noVerbs,this.verbs);
		int nounFound = getWords(actions[1],this.noNouns,this.nouns);
		
		if (actions[1].equals("???")) {
			response = "MOST ACTIONS NEED TWO WORDS";
		} else if ((verbFound>this.noVerbs) && (nounFound>this.noNouns)) {
			response = "WHAT!";
		} else if ((verbFound>this.noVerbs) || (nounFound>this.noNouns)) {
			response = String.format("%s%s %s", "YOU CAN'T ",actions[0],actions[1]);
		}
		
		String codedNoun = codeNoun(nounFound);
		
		System.out.println(verbFound);
		


		//1080
		} else if (verbFound == 6 || verbFound == 7 || verbFound == 15 || verbFound == 29) {
			take(nounFound,codedNoun,verbFound,actions);

		//1390
		} else if (verbFound == 8) {
			give(nounFound,codedNoun,actions);
		
		//1530 (1540)
		} else if (verbFound == 9 || verbFound == 10) {
			drop(nounFound,verbFound);
		
		//1630
		} else if (verbFound == 11) {
			eat(nounFound,actions);
		
		//1670
		} else if (verbFound == 12) {
			drink(nounFound,actions);
		
		//1710
		} else if (verbFound == 13) {
			ride(codedNoun,nounFound);
		
		//1730
		} else if (verbFound == 14) {
			open(codedNoun);

		//1760
		} else if (verbFound>15 && verbFound<20) {
			breakObject(codedNoun,verbFound,nounFound);
			
		//1820
		} else if (verbFound>19 && verbFound<24) {
			attack(codedNoun, nounFound);
		
		//1910
		} else if (verbFound == 24) {
			kill(nounFound);
		
		//2100
		} else if (verbFound == 25) {
			swim(actions[0]);
		
		//2210
		} else if (verbFound == 26) {
			shelter();
		
		//2270
		} else if (verbFound == 27 || verbFound == 28) {
			help(codedNoun,verbFound);
			
		//2500
		} else if (verbFound == 30 || verbFound == 31) {
			rub(codedNoun,nounFound,verbFound,actions);
		//2300
		} else if (verbFound == 32 || verbFound == 33) {
			examine(codedNoun);
		//2330
		} else if (verbFound == 34) {
			fill(codedNoun);
		//2350
		} else if (verbFound == 35) {
			say(actions[1]);
		//2400
		} else if (verbFound == 36 || verbFound == 37) {
			rest(verbFound);
		//2470
		} else if (verbFound == 38) {
			wave(codedNoun);
		//2450
		} else if (verbFound == 39) {
			info();
		//2600
		} else if (verbFound == 40) {
			//Load
		//2600
		} else if (verbFound == 41) {
			//Save
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
	
	private void move(String codedNoun, int nounChosen, int verbChosen,String exits) {
		
		int direction = 0;
		boolean moved=false;
		
		if (nounChosen == 52) {
			direction = verbChosen;
		} else if (nounChosen > this.noItems && nounChosen<this.noNouns) {
			direction = nounChosen-this.noItems;
		} else if (codedNoun.equals("500012") || codedNoun.equals("500053") ||
				codedNoun.equals("500045")) {
			direction = 4;
		} else if (codedNoun.equals("500070") || codedNoun.equals("500037") ||
				codedNoun.equals("510011") || codedNoun.equals("510041")) {
			direction = 1;
		} else if (codedNoun.equals("510043") || codedNoun.equals("490066") ||
				codedNoun.equals("490051")) {
			direction = 1;
		} else if (codedNoun.equals("510060") || codedNoun.equals("480056")) {
			direction = 2;
		} else if (codedNoun.equals("510044") || codedNoun.equals("510052")) {
			direction = 3;
		}
				
		if (codedNoun.equals("490051") && this.itemFlag.getIntData(29) == 0) {
			poisonWater();
		} else {
			if(this.room == this.itemLocation.getIntData(39) && 
				(this.strength+this.wisdom<180 || this.room == 10)) {
				this.message = "YOU CAN'T LEAVE!";
			} else if (this.room == this.itemLocation.getIntData(32) &&
					this.itemFlag.getIntData(32)<1 && direction ==3) {
				this.message = "HE WILL NOT LET YOU PASS";
			} else if (this.room == 47 && this.itemFlag.getIntData(44) == 0) {
				this.message = "THE ROCKS MOVE TO PREVENT YOU";
			} else if (this.room == 28 && this.itemFlag.getIntData(7) != 1) {
				this.message = "THE ARMS HOLD YOU FAST";
			} else if (this.room == 45 && this.itemFlag.getIntData(40) == 0 &&
					direction == 4) {
				this.message = "HISSSS!";
			} else if (this.room == 42 && this.itemFlag.getIntData(16) + 
					this.itemLocation.getIntData(16) != -1 && direction ==3) {
				this.message = "TOO STEEP TO CLIMB";
			} else if (this.room == 51 && direction == 3) {
				this.message = "THE DOOR IS BARRED!";
			} else {
								
				if (this.room == 33 && this.itemLocation.getIntData(16) == 0) {
					this.itemLocation.updateIntData(16, rand.nextInt(4));
					this.itemFlag.updateIntData(15,0);
					this.message = "THE BEAST RUNS AWAY";
				}
				
				if (this.room == this.itemLocation.getIntData(25) || 
					nounChosen == 25) {
					this.message = "";
					String noun = "#YOU BOARD THE CRAFT ";
					
					if (this.room<60) {
						noun = String.format("%s%s",noun,"FALLING UNDER THE SPELL OF THE BOATMAN ");
						
					}
					
					noun = String.format("%s%s",noun,"AND ARE TAKEN TO THE ISLAND OF SECRETS");
					
					//Line 720
					System.out.println(noun.substring(1,noun.length()));
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if (this.room<60) {
						System.out.println("TO SERVE OMEGAN FOREVER!");
					} else {
						System.out.println("THE BOAT SKIMS THE DARK SILENT WATERS");
						this.room=57;
					}
				}
			}
		}
	}
	
	private void take(int nounNumber, String codedNoun, int verbNumber, String[] actions) {
		
		boolean takeSuccessful = false;
		
		if (((this.itemFlag.getIntData(nounNumber)>0 && 
			this.itemFlag.getIntData(nounNumber)<9) ||
			this.itemLocation.getIntData(nounNumber) != this.room) &&
			nounNumber < this.carriableItems) {
			
			this.message = "WHAT "+this.objects.getStringData(nounNumber)+"?";			
		} else if (codedNoun.equals("3450050")) {
			this.strength = this.strength-8;
			this.wisdom = this.wisdom-5;
			this.message = "THEY ARE CURSED";
		} else if (codedNoun.equals("3810010")) {
			
			//Give it better effects
			this.message = "///Lightning Flashes";
			this.itemLocation.updateIntData(nounNumber, this.room);
			this.strength = this.strength-8;
			this.wisdom = this.wisdom-2;
			
		} else if ((verbNumber == 15 && nounNumber !=20 && nounNumber != 1) || 
				nounNumber > this.carriableItems) {
			this.message = "YOU CAN'T "+actions[0]+" "+actions[1];
		} else if ((this.itemLocation.getIntData(nounNumber) == this.room) &&
				(this.itemFlag.getIntData(nounNumber)<1 ||
				 this.itemFlag.getIntData(nounNumber)==0) && nounNumber<
				 this.carriableItems) {
			this.itemFlag.updateIntData(nounNumber,0);
			takeSuccessful = true;
		} else if (nounNumber == 16 && this.itemLocation.getIntData(10) != 0) {
			this.itemLocation.updateIntData(nounNumber, this.room);
			this.message = "IT ESCAPED";
			verbNumber = 0;
		}
		
		if (takeSuccessful) {
			if (nounNumber > this.foodLine && nounNumber <this.foodLine) {
				this.food += 2;
				verbNumber = -1;
			} else if (nounNumber >= this.drinkLine && nounNumber <=this.carriableItems) {
				this.drink += 2;
				verbNumber = -1;
			}
			
			if (nounNumber>this.foodLine && nounNumber<this.carriableItems) {
				this.itemLocation.updateIntData(nounNumber,-81);
			}
			
			this.message = "TAKEN";
			this.wisdom += 4;
			this.weight += 1;
			
			if (this.itemFlag.getIntData(nounNumber)>1) {
				this.itemFlag.updateIntData(nounNumber, 0);
			}
			
			//Bird Section (Stealing egg)
			if (codedNoun.equals("246046") || this.itemLocation.getIntData(11) != 0) {
				this.message = "YOU ANGER THE BIRD";
				
				if(rand.nextInt(3)>2) {
					flyRandom();
				};
			}
		}
	}
	
	private void flyRandom() {
		this.message = "#YOU ANGER THE BIRD WHICH FLIES YOU TO A REMOTE PLACE";
		this.room = 63+rand.nextInt(6);
	}
	
	private void give(int nounNumber,String codedNoun,String[] actions) {
		
		String message = "";
		
		if ((nounNumber != 24 && this.itemLocation.getIntData(nounNumber)>0) ||
			nounNumber >= 52) {
			this.message = "YOU DON'T HAVE THE "+actions[1];
		} else {
			String action = getAction("GIVE THE "+actions[1]+" TO WHOM ");
			action = action.toUpperCase();
			int objectNumber = getWords(action, this.noNouns,this.nouns);
			
			if (this.room != this.itemLocation.getIntData(objectNumber)) {
				this.message = "THE "+action+" IS NOT HERE";
			} else if (codedNoun.equals("10045") && objectNumber ==40) {
				this.itemLocation.updateIntData(nounNumber, 81);
				this.itemFlag.updateIntData(40,1);
				this.message = "THE SNAKE UNCURLS";
			} else if (codedNoun.equals("2413075") && objectNumber == 40 && this.drink>1) {
				this.itemFlag.updateIntData(11,0);
				this.message = "HE OFFERS HIS STAFF";
				this.drink --;
			} else {
				this.message = "IT IS REFUSED";
				codedNoun = codedNoun.substring(0,3);
				
				if ((codedNoun.equals("300") || codedNoun.equals("120")) && objectNumber == 42) {
					this.wisdom += 10;
					this.itemLocation.updateIntData(nounNumber,81);
				}
				
				if (codedNoun.equals("40") && this.itemFlag.getIntData(4)<0 && objectNumber == 32) {
					this.itemFlag.updateIntData(objectNumber,1);
					this.itemLocation.updateIntData(nounNumber,81);
				}
				
				if (codedNoun.substring(0,2).equalsIgnoreCase("80") && objectNumber == 43) {
					this.itemLocation.updateIntData(objectNumber,81);
					
					message = "HE TAKES IT ";
					
					if (this.room != 8) {
						message += "RUNS DOWN THE CORRIDOR, ";
					}
					
					System.out.println(message);
					System.out.println("AND CASTS IT INTO THE CHEMICAL VATS, PURIFYING THEM WITH");
					System.out.println("A CLEAR BLUE LIGHT REACHING FAR INTO THE LAKES AND RIVERS BEYOND");
					this.itemFlag.updateIntData(8,-1);		
				}
				
				if (this.itemLocation.getIntData(nounNumber) == 81 ||
					(nounNumber == 24 && this.itemLocation.getIntData(11)>0 &&
					 this.drink>0)) {
					this.message = "IT IS ACCEPTED";
				}
				
				if (objectNumber == 41) {
					this.itemLocation.updateIntData(nounNumber,51);
					this.message = "IT IS TAKEN";
				}
			}
		}
	}
	
	private void drop(int nounFound,int verbFound) {
		
		if (verbFound == 9) {
			if (nounFound == 4 && this.itemLocation.getIntData(nounFound) == 0) {
				this.itemLocation.updateIntData(nounFound, 81);
				this.wisdom --;
				this.message = "IT BREAKS!";
			} else {
				dropObject(nounFound);
			}
		} else {
			dropObject(nounFound);
		}
	}
	
	private void dropObject(int nounFound) {
		
		if (this.itemLocation.getIntData(nounFound) == 0 && nounFound < this.foodLine) {
			this.itemLocation.updateIntData(nounFound,this.room);
			this.message = "DONE";
			this.weight --;
		}
	}
	
	private void eat(int nounFound, String[] actions) {
		
		if ((nounFound < this.foodLine || nounFound>this.carriableItems) && !actions[1].equals("???")) {
			this.message = "YOU CAN'T "+actions[0]+" "+actions[1];
			this.wisdom --;
		} else {
			this.message = "YOU HAVE NO FOOD";
			
			if (this.food>0) {
				this.food --;
				this.strength += 10;
				this.message = "OK";
			}
			
			if (nounFound == 3) {
				this.strength -= 5; 
				this.wisdom -= 2;
				this.message = "THEY MAKE YOU VERY ILL";
			}
		}
		
	}
	
	private void drink(int nounFound,String[] action) {
		
		if (nounFound == 31) {
			if (this.itemFlag.getIntData(4)+this.itemFlag.getIntData(3) != -1) {
				this.message = "YOU DON'T HAVE "+action[0];
			} else {
				System.out.println("YOU TASTE A DROP AND .. ");
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				this.message = "OUCH";
				this.wisdom -=4;
				this.strength -= 7;
			}
		} else {
			if (((nounFound < this.drinkLine || nounFound>this.carriableItems) && !action[1].equals("???"))) {
				this.message = "YOU CAN'T "+action[0]+" "+action[1];
				this.wisdom --;
			} else {
				this.message = "YOU HAVE NO DRINK";
				
				if (this.drink>0) {
					this.drink --;
					this.strength += 7;
					this.message = "OK";
				}
			}
		}
	}
	
	private void ride(String codedNoun, int nounFound) {
				
		if (codedNoun.substring(0,4).equals("1600")) {
			this.itemFlag.updateIntData(nounFound,-1);
			this.message = "IT ALLOWS YOU TO RIDE";
		};
	}
	
	private void open(String codedNoun) {
		
		if (codedNoun.equals("2644044")) {
			this.message = "CHEST OPEN";
			this.itemFlag.updateIntData(6,9);
			this.itemFlag.updateIntData(5,9);
			this.itemFlag.updateIntData(15,9);
		}
		
		if (codedNoun.equals("2951151")) {
			this.message = "THE TRAPDOOR CREAKS";
			this.itemFlag.updateIntData(29,0);
			this.wisdom += 3;
		}
	}
	
	private void breakObject(String codedNoun, int verbChosen , int nounChosen) {
		
		this.strength -= 2;
		if (codedNoun.equals("3577077") && this.itemLocation.getIntData(9) == 0) {
			this.itemFlag.updateIntData(23,0);
			this.itemLocation.updateIntData(23,this.room);
		}
		
		if (this.noVerbs>15 && this.noVerbs<19 && (this.itemLocation.getIntData(9) == 0
				|| this.itemLocation.getIntData(15) == 0)) {
			this.message = "OK";
		}
		
		if (codedNoun.equals("1258158") || codedNoun.equals("2758158") && this.itemLocation.getIntData(15) == 0) {
			this.itemFlag.updateIntData(12,0);
			this.itemFlag.updateIntData(27,0);
			this.message = "CRACK";
		}
		
		if (codedNoun.substring(0,4).equals("1100") && this.room == 10) {
			breakAttack(nounChosen);
		}
		
		if (verbChosen == 18 && (nounChosen>29 &&  nounChosen<34) || (nounChosen>38 &&  nounChosen<44) || nounChosen == 16) {
			if (this.itemLocation.getIntData(9)==0) {
				kill(nounChosen);
			}
		}
	}
	
	//Break Method
	private void kill(int nounChosen) {
		//1910
		this.strength -= 12;
		this.wisdom -= 10;
		this.message = "THAT WOULD BE UNWISE";
		
		if (this.itemLocation.getIntData(nounChosen) == this.room) {
			this.itemFlag.updateIntData(51,1);
			this.message = "THUNDER SPLITS THE SKY!";
			this.message += " IT IS THE TRIUMPHANT VOICE OF OMEGAN";
			
			//Clears Screen & Displays 2740
			System.out.println(this.message);
			
			this.message = "WELL DONE ALPHAN! THE MEANS BECOMES THE END ..";
			this.message += " I CLAIM YOU AS MY OWN! HA HA HAH!";
			
			//DISPLAY MESSAGE 720
			System.out.println(this.message);
			
			//PAUSE
			this.strength = 0;
			this.wisdom = 0;
			this.timeRemaining = 0;
		}
	}
	
	//Shared routine in the break & attack methods - line 1980
	private void breakAttack(int nounChosen) {

		if (nounChosen-10==1) {
			
			this.message = "IT SHATTERS RELEASING A DAZZLING RAINBOW OF COLOURS!";
			
			if (this.itemLocation.getIntData(2) == this.room) {
				this.message += " THE EGG HATCHES INTO A BABY DACTYL "+
						"WHICH TAKES OMEGAN IN ITS CLAWS AND FLIES AWAY";
				this.itemLocation.updateIntData(39,81);
				this.itemLocation.updateIntData(2,81);
				this.itemFlag.updateIntData(2,-1);
				this.strength += 40;
			}
		} else if (nounChosen-10>1 && nounChosen-10<5) {
			
			if (this.itemLocation.getIntData(31) == this.room) {
				this.message = "THE COAL BURNS WITH A WARM RED FLAME";
				this.itemFlag.updateIntData(13,-1);
				
				if (this.room == 10 && this.itemLocation.getIntData(39) == this.room) {
					this.message += " WHICH DISOLVES OMEGAN'S CLOAK";
					this.strength += 20;
				}
			}	
		}
		this.wisdom += 10;
		this.itemLocation.updateIntData(nounChosen,81);
		this.itemFlag.updateIntData(nounChosen,-1);
	}
	
	private void attack(String codedNoun, int nounChosen) {
		this.strength -= 2;
		this.wisdom -=2;
		
		if (this.room == this.itemLocation.getIntData(nounChosen) ||
				this.itemLocation.getIntData(nounChosen) == 0) {
			if (nounChosen == 39) {
				this.message = "HE LAUGHS DANGEROUSLY";
			} else if (nounChosen == 32) {
				this.message = "THE SWAMPMAN IS UNMOVED";
			} else if (nounChosen == 33) {
				this.message = "YOUnoun CAN'T TOUCH HER!";
				this.itemLocation.updateIntData(3, 81);
			} else if (nounChosen == 41) {
				this.message = "THEY THINK THAT'S FUNNY!";
			} else if (nounChosen == 46) {
				flyRandom();
			} else if (codedNoun.substring(0,4).equals("1400") && 
					this.itemLocation.getIntData(39) == this.room) {
				breakAttack(nounChosen);
			}
			
			this.strength -= 8;
			this.wisdom -=5;
		}
	}
	
	private void swim(String verb) {
		
		if (this.room != 51 || this.itemFlag.getIntData(28)>0) {
			this.message = "YOU CAN'T "+verb+" HERE";
			this.wisdom += 1;
		}
	}
	
	private void shelter() {
		
		if (this.itemFlag.getIntData(36)<0) {
			
			//Clear the screen
			
			System.out.println("YOU CAN SHELTER IN:");
			System.out.println("1) GRANDPA'S SHACK");
			System.out.println("2) CAVE OF SNELM");
			System.out.println("3) LOG CABIN");
			System.out.println("CHOOSE FROM 1-3");
			
			this.room = getIntInput();
			this.itemFlag.updateIntData(22,this.room*-1);
			System.out.println("YOU BLINDLY RUN THROUGH THE STORM");
			this.message = "YOU REACH SHELTER";
			
			//Pause
		}
	}
	
	private void help(String codedNoun, int verbChosen) {
		
		if (codedNoun.equals("3075075") ||codedNoun.equals("3075075")) {
			this.message = "HOW WILL YOU DO THAT";
		}
		
		if (codedNoun.equals("3371071") && verbChosen == 28) {
			this.itemFlag.updateIntData(3,0);
			this.message = "SHE NODS SLOWLY";
			this.wisdom += 5;
		}
	}
	
	private void rub(String codedNoun,int nounFound,int verbFound, String[] actions) {
		this.message = "A-DUB-DUB";
		
		if (codedNoun.substring(0,4).equals("2815")) {
			if(this.itemFlag.getIntData(nounFound) == 1) {
				this.itemFlag.updateIntData(nounFound, 0);
				this.message = "REFLECTIONS STIR WITHIN";
			}
		} else if (this.itemLocation.getIntData(5)==0) {
			this.itemFlag.updateIntData(8,0);
			take(nounFound,codedNoun,verbFound,actions);
			this.message = "THE STONE UTTERS STONY WORDS";
		}
	}
	
	private void examine(String codedNoun) {
		this.message = "EXAMINE THE BOOK FOR CLUES";
		if (codedNoun.substring(0,2).equals("600")) {
			this.message = "REMEMBER ALADIN IT WORKED FOR HIM";
		}
	}
	
	private void fill(String codedNoun) {
		if (codedNoun.equals("40041")) {
			this.itemFlag.updateIntData(4,-1);
			this.message = "FILLED";
		}
	}
	
	private void say(String noun) {
		this.message = noun;
		
		if (noun.equals("STONEY WORDS") && this.room == 47 && this.itemFlag.getIntData(8) == 0) {
			this.itemFlag.updateIntData(44,1);
			this.message = "THE STONES ARE FIXED";
		} 
		
		if (noun.equals("REMEMBER OLD TIMES") && this.itemLocation.getIntData(3)>80 &&
			this.room == this.itemLocation.getIntData(42) && this.itemLocation.getIntData(12)>17) {
			this.message = "HE EATS THE FLOWERS- AND CHANGES";
			this.itemFlag.updateIntData(42,1);
			this.itemFlag.updateIntData(43,0);
		}	
	}
	
	private void rest(int verbFound) {
		
		//Clear Screen
		
		for(int i=0;i<Math.abs(this.itemFlag.getIntData(36))+3;i++) {
			this.timeRemaining--;
			if (this.strength<100 || this.itemFlag.getIntData(22)==-this.room) {
				this.strength ++;
			}
			System.out.println("TIME PASSES");
			//Pause
		}
		if (this.strength>100 || this.itemFlag.getIntData(36)<1) {
			this.wisdom+=2;
			this.itemFlag.updateIntData(36,1);
		}
		
		if (verbFound == 37 || verbFound == 36) {
			this.message = "OK";
		}
	}
	
	private void wave(String codedNoun) {
		if (this.room == this.itemLocation.getIntData(25)) {
			this.message = "THE BOATMAN WAVES BACK";
		}
		
		if (codedNoun.substring(0,3).equals("700")) {
			this.itemFlag.updateIntData(7,1);
			this.message = "THE TORCH BRIGHTENS";
			this.wisdom += 8;
		}
	}
	
	private void info() {
		System.out.println("INFO - ITEMS CARRIED");
		System.out.println("----------------------------------------");
		System.out.printf("FOOD=%d                        DRINK=%d%n",this.food,this.drink);
		System.out.println("----------------------------------------");

		int noItems = 0;
		for (int x=0;x<this.carriableItems;x++) {
			if (this.itemLocation.getIntData(x+1)==0) {
				System.out.println(this.objects.getStringData(x+1));
				noItems++;
			}
		}
		
		if (noItems>0) {
			System.out.println("----------------------------------------");
		}
		
		this.message = "OK";
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
*/