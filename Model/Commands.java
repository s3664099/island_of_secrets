/*
Title: Island of Secrets Command Execution Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 13 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

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
		int c=0; //????
		
		//Verb only
		if (this.noun == 52) {
			direction = this.verb;
		} else if (this.noun>Constants.noItems && this.noun<Constants.noNouns) {
			direction = this.noun-Constants.noItems;
		}
		
		
		
		/*
			
			830 IF B$="500012" OR B$="500053" OR B$="500045" THEN LET D=4
			840 IF B$="500070" OR B$="500037" OR B$="510011" OR B$="510041" THEN LET D=1
			850 IF B$="510043" OR B$="490066" OR B$="490051" THEN LET D=1
			860 IF B$="510060" OR B$="480056" THEN LET D=2
			870 IF B$="510044" OR B$="510052" THEN LET D=3
			880 IF B$="490051" AND F(29)=0 THEN GOSUB 2110:RETURN
			890 IF R=L(39) AND (X+Y<180 OR R=10) THEN LET F$=W$+"LEAVE!":RETURN
			900 IF R=L(32) AND F(32)<1 AND D=3 THEN LET F$="HE WILL NOT LET YOU PASS":RETURN
			910 IF R=47 AND F(44)=0 THEN LET F$="THE ROCKS MOVE TO PREVENT YOU":RETURN
			920 IF R=28 AND F(7)<>1 THEN LET F$="THE ARMS HOLD YOU FAST":RETURN
			930 IF R=45 AND F(40)=0 AND D=4 THEN LET F$="HISSSS!":RETURN
			940 IF R=25 AND F(16)+L(16)<>-1 AND D=3 THEN LET F$="TOO STEEP TO CLIMB":RETURN
			950 IF R=51 AND D=3 THEN LET F$="THE DOOR IS BARRED!":RETURN */
		
		if (direction>0) {}

		/*if(exits.charAt(direction-1) == '0') {
			this.room = this.room + Integer.parseInt("-10+10+01-01".substring((direction-1)*3, ((direction-1)*3)+3));
			moved = true;
			this.message = "OK";
		}
		
		if (direction<1 || !moved) {
			this.message = "YOU CAN'T GO THAT WAY";
		}*/
		//960 IFD>0THENIFMID$(D$,D,1)="0"THENR=R+VAL(MID$("-10+10+01-01",D*3+2,3)):C=1
		//970 LET F$="0K"
		//980 IF D<1 OR C=0 THEN LET F$=W$+"GO THAT WAY"
		
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

/* 13 November 2024 - Created File
 * 
 */