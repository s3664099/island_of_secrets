/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 0.19
Date: 23 October 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class Game {
	
	private Data locations;
	private Data objects;
	private Data prepositions;
	private Data itemLocation;
	private Data itemVisibility;
	private Data verbs;
	private Data nouns;
	private int room = 23;
	private final int noRooms = 80;
	private final int noItems = 43;
	private float strength = 100;
	private int wisdom = 35;
	private int timeRemaining = 1000;
	private final int noVerbs = 42;
	private final int noNouns = 52;
	private int weight = 0;
	private final int carriableItems = 24;
	private int food = 2;
	private int drink = 2;
	private final int foodLine = 16;
	private final int drinkLine = 21;
	private String line = "----------------------------------------------------------------";
	private String message = "LET YOUR QUEST BEGIN";
	private boolean gamePlaying = true;
	Random rand = new Random();
	
	public Game(Data locations, Data objects, Data prepositions, Data code_L, Data code_F,
				Data verbs,Data nouns) {
		this.locations = locations;
		this.objects = objects;
		this.prepositions = prepositions;
		this.itemLocation = code_L;
		this.itemVisibility = code_F;
		this.verbs = verbs;
		this.nouns = nouns;
	}
	
	public void run() {
		
		//this.verbs.displayData();
		
		while (this.gamePlaying) {
			ClearScreen();
			String exits = Display(this.timeRemaining,this.strength,this.wisdom,this.room);
			String action = getAction("%-10sWHAT WILL YOU DO: ");
			this.timeRemaining --;
			this.strength -= (this.weight/this.noItems)+.1;
			processAction(action,exits);
		}
	}
	
	//Retrieves the location details and splits the code
	private String[] getRoom(Data rooms,int room) {
		
		String[] roomDetails = new String[3];
		int newRoom = room;
		
		if (room == 20) {
			newRoom = rand.nextInt(noRooms);
			newRoom ++;
		}
		
		String roomDescription = rooms.retrieveData(newRoom);
		roomDetails[0] = roomDescription.substring(0,1);		
		roomDetails[1] = roomDescription.substring(1,roomDescription.length()-4);
		roomDetails[2] = roomDescription.substring(roomDescription.length()-4,roomDescription.length());

		if (room == 39) {
			int begin = rand.nextInt(5);
			roomDetails[2] = "101110100".substring(begin,begin+4);
		} else if (room == 20) {
			roomDetails[2] = "1110";
		}
		
		return roomDetails;
	}

	//Checks if there are any visible items in the room
	private String getItems(Data items, int roomNumber) {
		
		int numItems = 0;
		String itemDetails = "";
		
		for (int x=1;x<this.noItems+1;x++) {
			
			if (this.itemLocation.retrieveIntData(x) == roomNumber &&
					this.itemVisibility.retrieveIntData(x)<1) {
				
				if (numItems>0) {
					itemDetails += ", ";
				}
				itemDetails += items.retrieveData(x);
				numItems ++;
			}		
		}
		
		if (numItems>0) {
			itemDetails = String.format("YOU SEE %s",itemDetails);
		}
		
		return itemDetails;
	}	
	
	//Clears the screen (though does not work on the console).
	private void ClearScreen() {
		
       try {
		Runtime.getRuntime().exec("clear");
       } catch (IOException e) {
		e.printStackTrace();
       }		
	}
	
	//Displays the contents of the room.
	private String Display(int timeRemaining,float strength,int wisdom,int roomNumber) {
		
		//Gets details of location and any items 
		String[] roomDetails = getRoom(this.locations,roomNumber);
		String roomDescription = String.format("YOU ARE %s %s", 
				prepositions.retrieveData(Integer.parseInt(roomDetails[0])),roomDetails[1]);
		String itemDetails = getItems(this.objects,roomNumber);
		
		System.out.printf("%-10sISLAND OF SECRETS%-20sTIME REMAINING: %d%n"," "," ",timeRemaining);
		System.out.printf("%-5s%s%n"," ",this.line);
		System.out.printf("%-10sSTRENGTH = %f%-23sWISDOM = %d%n"," ",strength," ",wisdom);
		System.out.printf("%-5s%s%n%-10s%s%n"," ",this.line," ",roomDescription);
		
		//If there are any items, displays item line
		if (itemDetails.length()>0) {
			System.out.printf("%-10s%s%n"," ",itemDetails);
		}
		
		System.out.printf("%-5s%s%n%-10s%s%n%n"," ",this.line," ",this.message);
		this.message = "";
		
		return roomDetails[2];
	}
	
	private String getAction(String query) {
		
		String action = "";

	    Scanner myObj = new Scanner(System.in);
	    System.out.printf(query," ");

	    action = myObj.nextLine();
	    
	    System.out.printf("%n%n", null);
		
		return action;
	}
	
	//Move player to new location when running for shelter
	private int getIntInput() {
		
		boolean correct = false;
		Scanner myObj = new Scanner(System.in);
		String option = "";
		int intOption = 0;
		
		while (!correct) {
			option = myObj.nextLine();
			
			if (option.equals("1")) {
				intOption = 44;
				correct = true;
			} else if (option.equals("2")) {
				intOption = 11;
				correct = true;				
			} else if (option.equals("3")) {
				intOption = 41;
				correct = true;					
			} else {
				System.out.println("Please enter 1,2 or 3");
			}
		}
		return intOption;
	}
	
	//Function to location the word in the word bank
	private int getWords(String action, int noWords, Data wordBank) {
		
		int wordFound = 0;
		int wordNo = 1;
		
		if (action.length()<3) {
			action = String.format("%s%s", action,"???");
		}
				
		while (wordNo<noWords+1) {
			
			if (action.startsWith(wordBank.retrieveData(wordNo))) {
				wordFound = wordNo;
				wordNo = 99;
			}
			wordNo ++;
		}
		
		if (wordFound == 0) {
			wordFound = noWords+1;
		}
		
		return wordFound;
		
	}
	
	//Codes the noun, as occurs in the game
	private String codeNoun(int nounFound) {
				
		String codedNoun = String.format("%d%d%d%d", nounFound,this.itemLocation.retrieveIntData(nounFound),
				this.itemVisibility.retrieveIntData(nounFound),this.room);
		codedNoun = String.valueOf(Integer.parseInt(codedNoun.trim()));
		
		if (codedNoun.length()>1) {
			codedNoun = codedNoun.substring(0,codedNoun.length());
		}
		
		System.out.println(codedNoun);
						
		return codedNoun;
		
	}	
	
	private String processAction(String action,String exits) {
		
		String[] actions = {"",""};
		action = action.toUpperCase();
		String[] noWords = action.split(" ");
		actions[0] = noWords[0];
		String response = "";
		
		if (noWords.length>1) {
			actions[1] = noWords[1];
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
		
		//810
		if (verbFound>0 && verbFound<6) {
			move(codedNoun,nounFound,verbFound,exits);

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
		}
		
		return "";
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
				
		if (codedNoun.equals("490051") && this.itemVisibility.retrieveIntData(29) == 0) {
			poisonWater();
		} else {
			if(this.room == this.itemLocation.retrieveIntData(39) && 
				(this.strength+this.wisdom<180 || this.room == 10)) {
				this.message = "YOU CAN'T LEAVE!";
			} else if (this.room == this.itemLocation.retrieveIntData(32) &&
					this.itemVisibility.retrieveIntData(32)<1 && direction ==3) {
				this.message = "HE WILL NOT LET YOU PASS";
			} else if (this.room == 47 && this.itemVisibility.retrieveIntData(44) == 0) {
				this.message = "THE ROCKS MOVE TO PREVENT YOU";
			} else if (this.room == 28 && this.itemVisibility.retrieveIntData(7) != 1) {
				this.message = "THE ARMS HOLD YOU FAST";
			} else if (this.room == 45 && this.itemVisibility.retrieveIntData(40) == 0 &&
					direction == 4) {
				this.message = "HISSSS!";
			} else if (this.room == 42 && this.itemVisibility.retrieveIntData(16) + 
					this.itemLocation.retrieveIntData(16) != -1 && direction ==3) {
				this.message = "TOO STEEP TO CLIMB";
			} else if (this.room == 51 && direction == 3) {
				this.message = "THE DOOR IS BARRED!";
			} else {
				if(exits.charAt(direction-1) == '0') {
					this.room = this.room + Integer.parseInt("-10+10+01-01".substring((direction-1)*3, ((direction-1)*3)+3));
					moved = true;
					this.message = "OK";
				}
				
				if (direction<1 || !moved) {
					this.message = "YOU CAN'T GO THAT WAY";
				}
								
				if (this.room == 33 && this.itemLocation.retrieveIntData(16) == 0) {
					this.itemLocation.updateIntData(16, rand.nextInt(4));
					this.itemVisibility.updateIntData(15,0);
					this.message = "THE BEAST RUNS AWAY";
				}
				
				if (this.room == this.itemLocation.retrieveIntData(25) || 
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
		
		if (((this.itemVisibility.retrieveIntData(nounNumber)>0 && 
			this.itemVisibility.retrieveIntData(nounNumber)<9) ||
			this.itemLocation.retrieveIntData(nounNumber) != this.room) &&
			nounNumber < this.carriableItems) {
			
			this.message = "WHAT "+this.objects.retrieveData(nounNumber)+"?";			
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
		} else if ((this.itemLocation.retrieveIntData(nounNumber) == this.room) &&
				(this.itemVisibility.retrieveIntData(nounNumber)<1 ||
				 this.itemVisibility.retrieveIntData(nounNumber)==0) && nounNumber<
				 this.carriableItems) {
			this.itemVisibility.updateIntData(nounNumber,0);
			takeSuccessful = true;
		} else if (nounNumber == 16 && this.itemLocation.retrieveIntData(10) != 0) {
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
			
			if (this.itemVisibility.retrieveIntData(nounNumber)>1) {
				this.itemVisibility.updateIntData(nounNumber, 0);
			}
			
			//Bird Section (Stealing egg)
			if (codedNoun.equals("246046") || this.itemLocation.retrieveIntData(11) != 0) {
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
		
		if ((nounNumber != 24 && this.itemLocation.retrieveIntData(nounNumber)>0) ||
			nounNumber >= 52) {
			this.message = "YOU DON'T HAVE THE "+actions[1];
		} else {
			String action = getAction("GIVE THE "+actions[1]+" TO WHOM ");
			action = action.toUpperCase();
			int objectNumber = getWords(action, this.noNouns,this.nouns);
			
			if (this.room != this.itemLocation.retrieveIntData(objectNumber)) {
				this.message = "THE "+action+" IS NOT HERE";
			} else if (codedNoun.equals("10045") && objectNumber ==40) {
				this.itemLocation.updateIntData(nounNumber, 81);
				this.itemVisibility.updateIntData(40,1);
				this.message = "THE SNAKE UNCURLS";
			} else if (codedNoun.equals("2413075") && objectNumber == 40 && this.drink>1) {
				this.itemVisibility.updateIntData(11,0);
				this.message = "HE OFFERS HIS STAFF";
				this.drink --;
			} else {
				this.message = "IT IS REFUSED";
				codedNoun = codedNoun.substring(0,3);
				
				if ((codedNoun.equals("300") || codedNoun.equals("120")) && objectNumber == 42) {
					this.wisdom += 10;
					this.itemLocation.updateIntData(nounNumber,81);
				}
				
				if (codedNoun.equals("40") && this.itemVisibility.retrieveIntData(4)<0 && objectNumber == 32) {
					this.itemVisibility.updateIntData(objectNumber,1);
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
					this.itemVisibility.updateIntData(8,-1);		
				}
				
				if (this.itemLocation.retrieveIntData(nounNumber) == 81 ||
					(nounNumber == 24 && this.itemLocation.retrieveIntData(11)>0 &&
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
			if (nounFound == 4 && this.itemLocation.retrieveIntData(nounFound) == 0) {
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
		
		if (this.itemLocation.retrieveIntData(nounFound) == 0 && nounFound < this.foodLine) {
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
			if (this.itemVisibility.retrieveIntData(4)+this.itemVisibility.retrieveIntData(3) != -1) {
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
			this.itemVisibility.updateIntData(nounFound,-1);
			this.message = "IT ALLOWS YOU TO RIDE";
		};
	}
	
	private void open(String codedNoun) {
		
		if (codedNoun.equals("2644044")) {
			this.message = "CHEST OPEN";
			this.itemVisibility.updateIntData(6,9);
			this.itemVisibility.updateIntData(5,9);
			this.itemVisibility.updateIntData(15,9);
		}
		
		if (codedNoun.equals("2951151")) {
			this.message = "THE TRAPDOOR CREAKS";
			this.itemVisibility.updateIntData(29,0);
			this.wisdom += 3;
		}
	}
	
	private void breakObject(String codedNoun, int verbChosen , int nounChosen) {
		
		this.strength -= 2;
		if (codedNoun.equals("3577077") && this.itemLocation.retrieveIntData(9) == 0) {
			this.itemVisibility.updateIntData(23,0);
			this.itemLocation.updateIntData(23,this.room);
		}
		
		if (this.noVerbs>15 && this.noVerbs<19 && (this.itemLocation.retrieveIntData(9) == 0
				|| this.itemLocation.retrieveIntData(15) == 0)) {
			this.message = "OK";
		}
		
		if (codedNoun.equals("1258158") || codedNoun.equals("2758158") && this.itemLocation.retrieveIntData(15) == 0) {
			this.itemVisibility.updateIntData(12,0);
			this.itemVisibility.updateIntData(27,0);
			this.message = "CRACK";
		}
		
		if (codedNoun.substring(0,4).equals("1100") && this.room == 10) {
			breakAttack(nounChosen);
		}
		
		if (verbChosen == 18 && (nounChosen>29 &&  nounChosen<34) || (nounChosen>38 &&  nounChosen<44) || nounChosen == 16) {
			if (this.itemLocation.retrieveIntData(9)==0) {
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
		
		if (this.itemLocation.retrieveIntData(nounChosen) == this.room) {
			this.itemVisibility.updateIntData(51,1);
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
			
			if (this.itemLocation.retrieveIntData(2) == this.room) {
				this.message += " THE EGG HATCHES INTO A BABY DACTYL "+
						"WHICH TAKES OMEGAN IN ITS CLAWS AND FLIES AWAY";
				this.itemLocation.updateIntData(39,81);
				this.itemLocation.updateIntData(2,81);
				this.itemVisibility.updateIntData(2,-1);
				this.strength += 40;
			}
		} else if (nounChosen-10>1 && nounChosen-10<5) {
			
			if (this.itemLocation.retrieveIntData(31) == this.room) {
				this.message = "THE COAL BURNS WITH A WARM RED FLAME";
				this.itemVisibility.updateIntData(13,-1);
				
				if (this.room == 10 && this.itemLocation.retrieveIntData(39) == this.room) {
					this.message += " WHICH DISOLVES OMEGAN'S CLOAK";
					this.strength += 20;
				}
			}	
		}
		this.wisdom += 10;
		this.itemLocation.updateIntData(nounChosen,81);
		this.itemVisibility.updateIntData(nounChosen,-1);
	}
	
	private void attack(String codedNoun, int nounChosen) {
		this.strength -= 2;
		this.wisdom -=2;
		
		if (this.room == this.itemLocation.retrieveIntData(nounChosen) ||
				this.itemLocation.retrieveIntData(nounChosen) == 0) {
			if (nounChosen == 39) {
				this.message = "HE LAUGHS DANGEROUSLY";
			} else if (nounChosen == 32) {
				this.message = "THE SWAMPMAN IS UNMOVED";
			} else if (nounChosen == 33) {
				this.message = "YOU CAN'T TOUCH HER!";
				this.itemLocation.updateIntData(3, 81);
			} else if (nounChosen == 41) {
				this.message = "THEY THINK THAT'S FUNNY!";
			} else if (nounChosen == 46) {
				flyRandom();
			} else if (codedNoun.substring(0,4).equals("1400") && 
					this.itemLocation.retrieveIntData(39) == this.room) {
				breakAttack(nounChosen);
			}
			
			this.strength -= 8;
			this.wisdom -=5;
		}
	}
	
	private void swim(String verb) {
		
		if (this.room != 51 || this.itemVisibility.retrieveIntData(28)>0) {
			this.message = "YOU CAN'T "+verb+" HERE";
			this.wisdom += 1;
		}
	}
	
	private void shelter() {
		
		if (this.itemVisibility.retrieveIntData(36)<0) {
			
			//Clear the screen
			
			System.out.println("YOU CAN SHELTER IN:");
			System.out.println("1) GRANDPA'S SHACK");
			System.out.println("2) CAVE OF SNELM");
			System.out.println("3) LOG CABIN");
			System.out.println("CHOOSE FROM 1-3");
			
			this.room = getIntInput();
			this.itemVisibility.updateIntData(22,this.room*-1);
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
			this.itemVisibility.updateIntData(3,0);
			this.message = "SHE NODS SLOWLY";
			this.wisdom += 5;
		}
	}
	
	private void rub(String codedNoun,int nounFound,int verbFound, String[] actions) {
		this.message = "A-DUB-DUB";
		
		if (codedNoun.substring(0,4).equals("2815")) {
			if(this.itemVisibility.retrieveIntData(nounFound) == 1) {
				this.itemVisibility.updateIntData(nounFound, 0);
				this.message = "REFLECTIONS STIR WITHIN";
			}
		} else if (this.itemLocation.retrieveIntData(5)==0) {
			this.itemVisibility.updateIntData(8,0);
			take(nounFound,codedNoun,verbFound,actions);
			this.message = "THE STONE UTTERS STONY WORDS";
		}
	}
} 
/*
































































































340 ON INT(A/10)+1 GOSUB 590,600,610,620,630,630,640
350 IF R=61 THEN LET X=X+FNR(2)+1
360 IF R=14 AND FNR(3)=1 THEN LET Y=Y-1:LET F$="YOU ARE BITTEN"
370 IF F(36)<1 AND -R<>F(22) THEN LET F(36)=F(36)+1:LET L(36)=R:LET Y=Y-1
380 IF R<>L(16) AND L(16)>0 THEN LET L(16)=1+FNR(4)
390 IF R<>L(39) THEN LET L(39)=10*(FNR(5)+1)+7*FNR(3)
400 IF R=L(39) AND R<>L(43) AND F(13)>-1 THEN LET Y=Y-2:LET X=X-2
410 IF R<78 THEN LET L(32)=76+FNR(2)
420 IF R=33 OR R=57 OR R=73 AND FNR(2)=1 THEN LET L(25)=R
430 IF R=L(32) AND FNR(2)=1 AND F(32)=0 THEN GOSUB 1310
440 IFR=19ANDY<70ANDF(43)=0ANDFNR(4)=1THENF$="PUSHED INTO THE PIT":F(W)=1
450 IF R<>L(41) THEN LET L(41)=21+(FNR(3)*10)+FNR(2)
460 IF R=L(41) THEN LET F(41)=F(41)-1:IF F(41)<-4 THEN GOSUB 1230
470 IF F(43)=0 THEN LET L(43)=R
480 IF L(43)<18 AND R<>9 AND R<>10 AND F(W-2)<1 THEN GOSUB 1330
490 IF R=18 THEN LET Y=Y-1
500 IF Y<50 THEN LET O=FNR(9):GOSUB 1530:IF L(O)=R THEN F$="YOU DROP SOMETHING"
510 IF L<900 AND R=23 AND F(36)>0 AND FNR(3)=3 THEN GOSUB 1360
520 IF R=47 AND F(8)>0 THEN LET F$=F$+" YOU CAN GO NO FURTHER"
530 IF F(8)+F(11)+F(13)=-3 THEN LET F(W)=1:GOSUB 2800
540 IF F(W)=0 AND L>0 AND Y>1 AND X>1 THEN GOTO 30
550 IF L<1 OR Y<1 THEN LET F$="YOU HAVE FAILED, THE EVIL ONE SUCCEEDS"
560 PRINT:PRINT F$:PRINT "YOUR FINAL SCORE=";INT(X+Y+(ABS(L/7*(L<640))))
570 PRINT:PRINT:PRINT "GAME OVER"
580 END



620 ON A-29 GOSUB 2500,2500,2300,2300,2330,2350,2400,2400,2470,2540:RETURN
630 ON A-39 GOSUB 2600,2600,2720,640
640 RETURN




1230 GOSUB2770:LET F$="":LET A$="#THE LOGMEN "+M$
1240 LET F(41)=0:LET Y=Y-4:LET X=X-4
1250 IF R<34 THEN LET A$=A$+"THROW YOU IN THE WATER":LET R=32
1260 IF R>33 THEN LET A$=A$+"TIE YOU UP IN A STOREROOM":LET R=51
1270 GOSUB2750:GOSUB2760
1280 FOR I=3 TO 4
1290 IF L(I)=0 THEN LET L(I)=42
1300 NEXT I:RETURN
1310 LET A$="*THE SWAMPMAN TELLS HIS TALE"
1320 GOSUB2740:LET F(32)=-1:RETURN
1330 LET F$="MEDIAN CAN DISABLE THE EQUIPMENT"
1340 IF L(8)=0 THEN LET F$=F$+" AND ASKS YOU FOR THE PEBBLE YOU CARRY"
1350 RETURN
1360 LET F(36)=-(FNR(4)+6):LET F$="A STORM BREAKS OVERHEAD!":RETURN




1600 IF L(I)<>0 AND I<C1 THEN LET I=I+1:GOTO 1600
1610 IF L(I)=0 THEN LET L(I)=R:LET F(I)=0:GOSUB1540:LET F$="YOU DROP SOMTHING"
1620 RETURN











2300 LET F$="EXAMINE THE BOOK FOR CLUES"
2310 IF LEFT$(B$,3)="600"THEN LET F$=L$
2320 RETURN
2330 IF B$="40041" THEN LET F(4)=-1:LET F$="FILLED"
2340 RETURN
2350 LET F$=X$:IF X$=H$ AND R=47 AND F(8)=0THEN LET F(44)=1:LET F$=J$
2360 IF X$<>P$ OR R<>L(42) OR L(3)<81 OR L(12)<18 THEN RETURN
2370 LET F$="HE EATS THE FLOWERS- AND CHANGES":LET F$="YOU DON'T HAVE "+X$:RETURN


2400 GOSUB2770:FOR I=1 TO ABS(F(36))+3
2410 LET L=L-1:IF Y<100 FOR -R=F(22) THEN LET Y=Y+1
2420 PRINT"TIME PASSES":GOSUB2760
2430 NEXT I
2440 IF L>100 OR F(36)<1 THEN LET X=X+2:LET F(36)=1
2450 IF A=37 OR A=36 THEN LET F$="OK"
2460 RETURN
2470 IF R=L(25)THEN LET F$="THE BOATMAN WAVES BACK"
2480 IF LEFT$(B$,3)="700"THEN LET F(7)=1:LET F$=N$:LET X=X+8
2490 RETURN

2540 GOSUB2770 :PRINT" INFO - ITEMS CARRIED":GOSUB2780
2550 PRINT G$:TAB(0);" FOOD=";F;TAB(23);"DRINK=";G:PRINT G$;:LET F$="OK"
2560 FOR I=1 TO C4
2570 READ Y$:IF L(I)=0 THEN PRINT Y$
2580 NEXT I
2590 PRINT G$;:GOSUB2730:RETURN
2600 LET C$="LOAD":IF A=41 THEN LET C$="SAVE"
2610 PRINT"PREPARE TO ";C$:GOSUB2730
2620 IF A=40 THEN OPEN 1,1,0,"ISDATA"
2630 IF A=41 THEN OPEN 1,1,1,"ISDATA"
2640 IF A=41 THEN F(50)=R:F(49)=Y:F(48)=X:F(47)=F:F(46)=B:F(45)=L
2650 FOR I=1 TO W
2660 IF A=40 THEN INPUT#1,L(I):INPUT#1,F(I)
2670 IF A=41 THEN PRINT#1,L(I):PRINT#1,F(I)
2680 NEXT I
2690 CLOSE 1
2700 IF A=40 THEN R=F(50):Y=F(49):X=F(48):F=F(47):G=F(46):L=F(45)
2710 LET F$="OK":RETURN
2720 LET F(W)=-1:LET F$="YOU RELINQUISH YOUR QUEST.":LET L=1:RETURN
2730 INPUT "PRESS RETURN";A$:RETURN

2750 GOSUB720:GOSUB2760:RETURN



2800 LET A$="*THE WORLD LIVES WITH NEW HOPE":GOSUB2750
2810 LET F$="YOUR QUEST IS OVER":RETURN
2820 PRINT"INITIALISING"

































8 September 2024 - Created File
9 September 2024 - Added method to retrieve input and started processing command
10 September 2024 - Added code to respond to incorrect commands
12 September 2024 - Started building the move methods.
14 September 2024 - Added the poison water method and continued the move method
					completed main move section.
17 September 2024 - Finished Movement and started on take objects
21 September 2024 - Finished give & drop methods
22 September 2024 - Completed Eat & Drink methods
23 September 2024 - Completed ride, open and started break
30 September 2024 - Continued working on the break function
4 October 2024 - Finished Break Method
5 October 2024 - Finished Attack Method
12 October 2024 - Finished Last part of attack method and added swim method.
				  Added shelter method & set up Help method
18 October 2024 - Added the help method
23 October 2024 - Added rub method
*/