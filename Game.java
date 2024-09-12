/*
Title: Island of Secrets Game
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 0.8
Date: 8 September 2024
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
	private int noRooms = 80;
	private int noItems = 43;
	private float strength = 100;
	private int wisdom = 35;
	private int timeRemaining = 1000;
	private int noVerbs = 42;
	private int noNouns = 52;
	private int weight = 0;
	private String line = "----------------------------------------------------------------";
	private String message = "LET YOUR QUEST BEGIN";
	private boolean gamePlaying = true;
	
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
		
		while (this.gamePlaying) {
			ClearScreen();
			String exits = Display(this.timeRemaining,this.strength,this.wisdom,this.room);
			String action = getAction();
			this.timeRemaining --;
			this.strength -= (this.weight/this.noItems)+.1;
			processAction(action);
		}
	}
	
	//Retrieves the location details and splits the code
	private String[] getRoom(Data rooms,int room) {
		
		String[] roomDetails = new String[3];
		int newRoom = room;
		Random rand = new Random();
		
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
			System.out.printf("%-10s%s"," ",itemDetails);
		}
		
		System.out.printf("%-5s%s%n%-10s%s%n%n"," ",this.line," ",this.message);
		
		return roomDetails[2];
	}
	
	private String getAction() {
		
		String action = "";

	    Scanner myObj = new Scanner(System.in);
	    System.out.printf("%-10sWHAT WILL YOU DO: "," ");

	    action = myObj.nextLine();
	    
	    System.out.printf("%n%n", null);
		
		return action;
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
	
	private String processAction(String action) {
		
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
		
		return "";
	}
	
	private int move(String codedNoun, int nounChosen, int verbChosen) {
		
		int direction = 0;
		int c=0;
		
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
			//GOSUB 2110
		} else {
			
		}
		
		return 0;
	}
}
/*
2110 LET X=X-1:LET R=FNR(5):GOSUB2770:PRINT "SWIMMING IN THE POISONOUS WATERS"
2120 LET J=0:LET B$="":LET F$="YOU SURFACE":PRINT "YOUR STRENGTH = ";INT(Y)
2130 FOR I=1 TO R
2140 IF Y<15 THEN PRINT"YOU ARE VERY WEAK"
2150 PRINT"WHICH WAY";:INPUT X$:LET X$=LEFT$(X$,1):LET B$=B$+X$:NEXT I
2160 FOR I=1TO R
2170 LET Y=FNS(Z)-3:IF MID$(B$,I,1)="N" THEN LET J=J+1
2180 NEXT I:IF R/2>J AND Y>1 THEN GOTO 2110
2190 IF Y<2 THEN LET F$="YOU LOST AND DROWNED"
2200 LET R=30+FNR(3):RETURN





890 IF R=L(39) AND (X+Y<180 OR R=10) THEN LET F$=W$+"LEAVE!":RETURN
900 IF R=L(32) AND F(32)<1 AND D=3 THEN LET F$="HE WILL NOT LET YOU PASS":RETURN
910 IF R=47 AND F(44)=0 THEN LET F$="THE ROCKS MOVE TO PREVENT YOU":RETURN
920 IF R=28 AND F(7)<>1 THEN LET F$="THE ARMS HOLD YOU FAST":RETURN
930 IF R=45 AND F(40)=0 AND D=4 THEN LET F$="HISSSS!":RETURN
940 IF R=25 AND F(16)+L(16)<>-1 AND D=3 THEN LET F$="TOO STEEP TO CLIMB":RETURN
950 IF R=51 AND D=3 THEN LET F$="THE DOOR IS BARRED!":RETURN
960 IFD>0THENIFMID$(D$,D,1)="0"THENR=R+VAL(MID$("-10+10+01-01",D*3+2,3)):C=1
970 LET F$="0K"
980 IF D<1 OR C=0 THEN LET F$=W$+"GO THAT WAY"
990 IF R=33 AND L(16)=0 THEN L(16)=FNR(4):F(16)=0:F$="THE BEAST RUNS AWAY"
1000 IF R<>L(25) OR O<>25 THEN RETURN
1010 LET F$="":LET A$="#YOU BOARD THE CRAFT "
1020 IF X<60 THEN LET A$=A$+S$
1030 LET A$=A$=+T$
1040 GOSUB2740:GOSUB2760:GOSUB2760
1050 IF X<60 THEN LET A$="#TO SERVE OMEGAN FOREVER!":LET F(W)=1
1060 IF X>59 THEN LET A$="#THE BOAT SKIMS THE DARK SILENT WATERS":LET R=57
1070 GOSUB2750:GOSUB2760:GOSUB2760:RETURN











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
590 ON A GOSUB 810,810,810,810,810,1080,1080,1390,1530:RETURN
600 ON A-9 GOSUB 1540,1630,1670,1710,1730,1080,1760,1760,1760,1760:RETURN
610 ON A-19 GOSUB 1820,1820,1820,1820,1910,2100,2210,2270,2270,1080:RETURN
620 ON A-29 GOSUB 2500,2500,2300,2300,2330,2350,2400,2400,2470,2540:RETURN
630 ON A-39 GOSUB 2600,2600,2720,640
640 RETURN




1080 IF (F(0)>0 AND F(0)<9 OR L(0)<>R)AND O<=C3 THEN F$="WHAT "+X$+"?":RETURN
1090 IF B$="3450050"THEN LET Y=Y-8:LET X=X-5:LET F$="THEY ARE CURSED":RETURN
1100 IF B$="3810010" THEN GOSUB1370
1110 IF(A=15ANDO<>20ANDO<>1)OR(A=29ANDO<>16)ORO>C3THENF$=W$+C$+" "+X$:RETURN
1120 IF L(O)=R AND (F(O)<1 OR F(O)=9)AND O<C3 THEN LET L(O)=0:LET A=-1
1130 IF O=16 AND L(10)<>0 THEN LET L(O)=R:LET F$="IT ESCAPED":LET A=0
1140 IF O>C1 AND O<C2 THEN LET F=F+2:LET A=-1
1150 IF O>=C2 AND O<=C3 THEN LET G=G+2:LET A=-1
1160 IF O>C1 AND O<C3 THEN LET L(O)=-81
1170 IF A=-1 THEN LET F$="TAKEN":LET X=X+4:LET E=E+1:IF F(O)>1 THEN LET F(O)=0
1180 IF B$<>"246046"OR L(11)=0 THEN RETURN
1190 LET F$=U$:LET L(O)=R:IF FNR(3)<3 THEN RETURN
1200 LET A$="#"+U$+R$
1210 LET R=63+FNR(6):LET L(16)=1:LET F$=""
1220 GOSUB2740:GOSUB2760
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
1370 FOR K=1 TO 30:GOSUB2770 :PRINT"///LIGHTNING FLASHES!":NEXT K
1380 LET L(39)=R:LET Y=Y-8:LET X=X-2:RETURN
1390 IF (O<>24 AND L(O)>0) OR O=52 THEN LET F$="YOU DON'T HAVE THE "+X$:RETURN
1400 PRINT"GIVE THE ";X$;" TO WHOM";:INPUT X$
1410 LET Q=0:GOSUB760:LET N=0:LET O=Q
1420 IF R<>L(N) LEN LET F$="THE "+X$+" IS NOT HERE":RETURN
1430 IF B$="10045" AND N=40 THEN L(O)=81:F(40)=1:F$="THE SNAKE UNCURLS"
1440 IFB$="2413075"ANDN=30ANDG>1THENF(11)=0:F$="HE OFFERS HIS STAFF":G=G-1
1450 LET B$=LEFT$(B$,3):LET F$="IT IS REFUSED"
1460 IF B$="300" AND N=42 THEN LET X=X+10:LET L(O)=81
1470 IF B$="120" AND N=42 THEN LET X=X+10:LET L(O)=81
1480 IF B$="40" AND F(4)<0 AND N=32 THEN LET F(N)=1:LET L(O)=81
1490 IF LEFT(B$,2)="80"AND N=43 THEN LET L(O)=81:GOSUB1560
1500 IF L(O)=81 OR (O=24 AND L(11)>0 AND G>0)THEN LET F$="IT IS ACCEPTED"
1510 IF N=41 THEN LET L(O)=51:LET F$="IT IS TAKEN"
1520 RETURN
1530 IF O=4 AND L(O)=0THEN LET L(O)=81:LET X=X-1:LET F$="IT BREAKS!":RETURN
1540 IF L(O)=0 AND O<=C1 THEN LET L(O)=R:LET F$="DONE":LET E=E-1
1550 RETURN
1560 LET A$="*HE TAKES IT ":IF R<>8 THEN LET A$+A$+"RUNS DOWN THE CORRIDOR,"
1570 GOSUB2740:A$="*AND CASTS IT INTO THE CHEMICAL VATS, PURIFYING THEM WITH"
1580 A$=A$+" A CLEAR BLUE LIGHT REACHING FAR INTO THE LAKES AND RIVERS BEYOND"
1590 LET F(8)=-1:GOSUB2750:GOSUB2760:GOSUB2760:RETURN
1600 IF L(I)<>0 AND I<C1 THEN LET I=I+1:GOTO 1600
1610 IF L(I)=0 THEN LET L(I)=R:LET F(I)=0:GOSUB1540:LET F$="YOU DROP SOMTHING"
1620 RETURN
1630 IF(O<C1 OR O>C3) AND X$="???" THEN LET F$=W$+C$+" "+X$:LET X=X-1:RETURN
1640 LET F$="YOU HAVE NO FOOD":IF F>0 THEN LET F=F-1:LET Y=Y+10:LET F$="OK"
1650 IF D=3 THEN LET X=X-5:LET Y=Y-2:LET F$="THEY MAKE YOU VERY ILL"
1660 RETURN
1670 IF O=31 THEN GOSUB 2380:RETURN
1680 IF I$<>"???" AND (O<21 OR O>C3) THEN LET F$=W$+C$+" "+X$:LET X=X-1:RETURN
1690 LET F$="YOU HAVE NO DRINK":IF G>0 THEN LET G=G-1:LET Y=Y+7:LET F$="OK"
1700 RETURN
1710 IF LEFT$(B$,4)="1600" THEN LET F(O)=-1:LET F$="IT ALLOWS YOU TO RIDE"
1720 RETURN
1730 IF B$="2644044" THEN LET F$="CHEST OPEN":LET F(6)=9:LET F(5)=9:LET F(15)=9
1740 IF B$="2951151" THEN LET F$="THE TRAPDOOR CREAKS":LET F(29)=0:LET X=X+3
1750 RETURN
1760 LET Y=Y-2:IF B$="3577077"AND L(9)=0THEN LET F(23)=0:LET L(23)=R
1770 IF V>15 AND V<19 AND (L(9)=0 OR L(15)=0) THEN LET F$="OK"
1780 IF B$="1258158"OR B$="2758158"AND L(15)=0 THEN F(12)=0:F(27)=0:F$="CRACK"
1790 IF LEFT$(B$,4)="1100" AND R=10 THEN GOSUB 1980
1800 IF A=18 AND (O>29 AND O<34) OR (O>38 AND O<44) OR O=16 THEN GOSUB 1900
1810 RETURN
1820 LET Y=Y-2:LET X=X-2:IF R<>L(O) AND L(O)<>0THE RETURN
1830 IF O=39 THEN LET F$="HE LAUGHS DANGEROUSLY"
1840 IF O=32 THEN LET F$="THE SWAMPMAN IS UNMOVED"
1850 IF O=33 THEN LET F$=W$+"TOUCH HER!":LET L(3)=81
1860 IF O=41 THEN LET F$="THEY THINK THAT'S FUNNY!"
1870 IF O=46 THEN GOSUB1200
1880 IF LEFT$(B$,4)="1400"AND R=L(39)THEN GOSUB1980
1890 LET Y=Y-8:LET X=X-5:RETURN
1900 IF L(9)>0 THEN RETURN
1910 LET Y=Y-12:LET X=X-10:LET F$="THAT WOULD BE UNWISE!"
1920 IF R<>L(0) THEN RETURN
1930 LET F(W)=1:LET A$="#THUNDER SPLITS THE SKY!":LET F$=""
1940 LET A$=A$+"IT IS THE TRIUMPHANT VOICE OF OMEGAN.":GOSUB2740
1950 LET A$="#WELL DONE ALPHAN! THE MEANS BECOMES THE END.."
1960 LET A$=A$+"I CLAIM YOU AS MY OWN! HA HA HAH!":GOSUB2750
1970 GOSUB2760:LET X=0:LET L=0:LET Y=0:RETURN
1980 GOSUB2770:ON O-1 GOSUB 2010,2060,2060,2060
1990 LET X=X+10:LET L(O)=81:LET F(O)=-1:GOSUB270:GOSUB2760:GOSUB2760
2000 RETURN
2010 LET A$="#IT SHATTERS RELEASING A DAZZLING RAINBOW OF COLOURS!"
2020 IF L(2)<>R THEN RETURN
2030 LET A$=A$+"THE EGG HATCHES INTO A BABY DACTYL "+O$
2040 LET L(39)=81:LET L(2)=81:LET F(2)=-1:LET Y=Y+40
2050 RETURN
2060 IF L(31)<>R THEN RETURN
2070 LET A$="*THE COAL BURNS WIGTH A WARM RED FLAME":LET F(13)=-1
2080 IF R=10 AND R=L(39)THEN A$=A$+" WHICH DISOLVES OMEGAN'S CLOAK":Y=Y+20
2090 RETURN
2100 IF R<>51 OR F(29)>0THEN LET F$=W$+C$+" HERE":X=X+1
	- Then swimming in poisoned waters


2210 IF F(36)>-1 THEN RETURN
2220 GOSUB2770 :PRINT"YOU CAN SHELTER IN:";PRINT"1) GRANDPA'S SHACK"
2230 PRINT"2) CAVE OF SNELM";PRINT"3) LOG CABIN":PRINT"CHOOSE FROM 1-3":INPUTA$
2240 IF A$>"0" AND A$<"4"THEN LET R=ASC(MID$("A >",VAL(A$),1))-21:LET F(22)=-R
2250 PRINT"YOU BLINDLY RUN THROUGH THE STORM":LET F$="YOU REACH SHELTER"
2260 GOSUB2760:RETURN
2270 IF B$="3075075"OR B$="3371071"THEN LET F$="HOW WILL YOU DO THAT"
2280 IF B$="3371071" AND A=28 THEN F(3)=0:F$="HOW WILL YOU DO THAT"
2290 RETURN
2300 LET F$="EXAMINE THE BOOK FOR CLUES"
2310 IF LEFT$(B$,3)="600"THEN LET F$=L$
2320 RETURN
2330 IF B$="40041" THEN LET F(4)=-1:LET F$="FILLED"
2340 RETURN
2350 LET F$=X$:IF X$=H$ AND R=47 AND F(8)=0THEN LET F(44)=1:LET F$=J$
2360 IF X$<>P$ OR R<>L(42) OR L(3)<81 OR L(12)<18 THEN RETURN
2370 LET F$="HE EATS THE FLOWERS- AND CHANGES":LET F$="YOU DON'T HAVE "+X$:RETURN
2380 IF F(4)+L(4)<>-1 THEN LET F$="YOU DON'T HAVE "+X$:RETURN
2390 GOSUB2770:PRINT"YOU TASTE A DROP AND..":GOSUB2760:F$="*OUCH":Y=Y-4:X=X-7
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
2500 LET F$="A-DUB-DUB":IF LEFT$(B$,4)<>"2815"THEN RETURN
2510 IF F(O)=1 THEN LET F(O)=0:LET F$=K$:RETURN
2520 IF L(5)=0 THEN LET F(8)=0:GOSUB1080:LET F$="THE STONE UTTERS"+H$
2530 RETURN
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
2740 GOSUB2770
2750 GOSUB720:GOSUB2760:RETURN
2760 FOR D=1 TO 900:NEXT D:RETURN


2800 LET A$="*THE WORLD LIVES WITH NEW HOPE":GOSUB2750
2810 LET F$="YOUR QUEST IS OVER":RETURN
2820 PRINT"INITIALISING"

































8 September 2024 - Created File
9 September 2024 - Added method to retrieve input and started processing command
10 September 2024 - Added code to respond to incorrect commands
*/