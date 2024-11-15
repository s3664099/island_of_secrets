/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.1
Date: 14 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

//880 IF B$="490051" AND F(29)=0 THEN GOSUB 2110:RETURN - Poisonous Waters Minigame
//Have code to change u & d to go up & go down. Also enter and exit to go in and out

package Model;

import Data.Constants;

public class Commands {
	
	private int verb;
	private int noun;
	private String code;
	
	public Commands(int verb,int noun, String code) {
		this.verb = verb;
		this.noun = noun;
		this.code = code;
	}
	
	public void move(Game game,Player player) {
		
		int direction = 0;
		boolean haveMoved=false;
		
		//Verb only
		if (this.noun == 52) {
			direction = this.verb;
		} else if (this.noun>Constants.noItems && this.noun<Constants.noNouns) {
			direction = this.noun-Constants.noItems;
		}
		
		if (code.equals("500012") || code.equals("500053") || code.equals("500045")) {
			direction = 4;
		} else if (code.equals("500070")||code.equals("500037")||code.equals("510011")||
				   code.equals("510041") ||code.equals("510043")||code.equals("490066")||
				   code.equals("490051")) {
			direction = 1;
		} else if (code.equals("510060")||code.equals("480056")) {
			direction = 2;
		} else if (code.equals("510044")||code.equals("510052")) {
			direction = 3;
		}
		
		
		
		/*
		
		
			
			

			

			
			
			890 IF R=L(39) AND (X+Y<180 OR R=10) THEN LET F$=W$+"LEAVE!":RETURN
			900 IF R=L(32) AND F(32)<1 AND D=3 THEN LET F$="HE WILL NOT LET YOU PASS":RETURN
			910 IF R=47 AND F(44)=0 THEN LET F$="THE ROCKS MOVE TO PREVENT YOU":RETURN
			920 IF R=28 AND F(7)<>1 THEN LET F$="THE ARMS HOLD YOU FAST":RETURN
			930 IF R=45 AND F(40)=0 AND D=4 THEN LET F$="HISSSS!":RETURN
			940 IF R=25 AND F(16)+L(16)<>-1 AND D=3 THEN LET F$="TOO STEEP TO CLIMB":RETURN
			950 IF R=51 AND D=3 THEN LET F$="THE DOOR IS BARRED!":RETURN */
		
		if (direction>4) {
			direction = 0;
		}
		
		if (direction>0) {
			if (game.checkExit(player.getRoom(),direction-1)) {
				int newRoom = player.getRoom() + Integer.parseInt(
						"-10+10+01-01".substring((direction-1)*3, ((direction-1)*3)+3));
				player.setRoom(newRoom);
				game.setMessage("Ok");
				haveMoved = true;
			}
		}
		
		if (direction<1 || !haveMoved) {
			game.setMessage("You can't go that way");
		}




		
			/*990 IF R=33 AND L(16)=0 THEN L(16)=FNR(4):F(16)=0:F$="THE BEAST RUNS AWAY"
			1000 IF R<>L(25) OR O<>25 THEN RETURN
			1010 LET F$="":LET A$="#YOU BOARD THE CRAFT "
			1020 IF X<60 THEN LET A$=A$+S$
			1030 LET A$=A$+T$
			1040 GOSUB2740:GOSUB2760:GOSUB2760
			1050 IF X<60 THEN LET A$="#TO SERVE OMEGAN FOREVER!":LET F(W)=1
			1060 IF X>59 THEN LET A$="#THE BOAT SKIMS THE DARK SILENT WATERS":LET R=57
			1070 GOSUB2750:GOSUB2760:GOSUB2760:RETURN
		 */
		
	}
}

/* 13 November 2024 - Created File. Added code to move player
 * 14 November 2042 - Added code to handle special movement commands
 * 
 */