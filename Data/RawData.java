/*
Title: Island of Secrets Raw Data
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.1
Date: 2 February 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class RawData {
	
	private static String[] locationTypes = {"F","F","F","CT","P","CL","Fc","V","Bt","Sm",
											 "C","C","F","B","St","Cl","Fc","P","W","Cl",
											 "C","F","P","P","CL","CL","R","H","W","R",
											 "Cr","Cr","Br","SD","F","CL","Bt","R","!","Ar",
											 "H","Tb","H","H","F","N","Cs","Bn","R","Bk",
											 "R","H","H","Hl","SH","Py","Is","Co","Ds","H",
											 "Fl","Ch","Br","Fs","Py","Py","Py","Ds","Ds","F",
											 "Fl","Fl","Br","V","V","V","S","SV","C","C"};
	
	private static String[] locations = {
		"4the furthest depth of the forest1001",			// 1  F
		"4the depths of the mutant forest1000",				// 2  F
		"7a path out of the overground depths1000",			// 3  F
		"6a carniverous tree1000",							// 4  CT
		"4a corral beneath the Crimson Canyon1110",			// 5  P (Paddock/Corral)
		"7the top of a steep cliff1011",					// 6  CL
		"4the marsh factory1001",							// 7  Fc (Factory)
		"4the sludge fermation vats1110",					// 8  V
		"7the uppermost battlements1001",					// 9  Bt (Battlements)
		"4Omegan's sanctum1110",							//10  Sm (Sanctum
		"4Snelm's lair0001",								//11  C (Cave)
		"2a dark cave0000",									//12  C (Cave)
		"1broken branches0100",								//13  B
		"1a thicket of biting bushes0000",					//14  B
		"1a huge glassy stone1110",							//15  St
		"7the edge of the Crimson Canyon0011",				//16  CL
		"4the clone factory0101",							//17  Fc
		"4a corridor of clone storage casks1100",			//18  P (Pod)
		"7edge of the well0000",							//19  W
		"4the Room of Secret Visions1110",					//20  Cl (Cloud)
		"4Snelm's inner chamber0111",						//21  C
		"3the southern edge of the forest0101",				//22  F
		"7a leafy path1000",								//23  P
		"3a fork in the path0100",							//24  P
		"7an apparently unclimable rocky path1100",			//25  CL
		"7a ledge atop the Crimson Canyon0010",				//26  CL	 
		"4a tall entrance chamber1101",						//27  R (Room)
		"4a low passage with arms reaching from the wall1010",//28 H (Hands)
		"7the approach to the Well of Despair0001",			//29  W
		"4a dim corridor deep in the castle1010",			//30  R
		"4the stagnant waters of the crawling creek1001",	//31  Cr (Creek)
		"4a shallow pool off the creek1100",				//32  Cr (Creek)
		"7a log pier, jutting out over the creek0000",		//33  Br (Bridge)
		"4a stretch of featureless dunes1100",				//34  SD (Sand Dunes)
		"1a group of tall trees1010",						//35  F
		"7a narrow ledge at the summit of the canyon0011",	//36  CL
		"2a monsterous portal in the castle wall0011",		//37  Bt
		"4a chamber inches deep with dust0001",				//38  R
		"4here!!!!!1111",									//39  !
		"2a carved archway0010",							//40  Ar (Archway)
		"4a small hut in the log settlement0111",			//41  H
		"1a huge split-log table1001",						//42  Tb (Table)
		"4the porch of the logman's cabin0110",				//43  H
		"4grandpa's shack1101",								//44  H
		"3a clearing in the trees by a rickety shack0010",	//45  F
		"4the nest of a huge dactyl0111",					//46  N
		"6the Castle of Dark Secrets by two huge stones0011",//47 Cs (Castle)
		"4a room littered with bones0111",					//48  Bn (Bones)
		"4the Cell of Whispered Secrets0111",				//49  R
		"4the Library of Written Secrets0111",				//50  Bk (Bookshelf)
		"4a refuse strewn storeroom1111",					//51  R
		"4the Logmen's hall0000",							//52  H
		"5a log building1000",								//53  H
		"7a rutted hillside1100",							//54  Hl
		"7a windswept plain amongst stone megaliths0100",	//55  SH (Stonehenge)
		"7the steps of an ancient pyramid1010",				//56  Py
		"7the Island of Secrets0111",						//57  Is
		"1a broken marble column1001",						//58  Co
		"7an expanse of cracked, baked earth1110",			//59  Ds
		"4a deserted abode hut1011",						//60  H
		"4a livid growth of mad orchids1011",				//61  Fl
		"4a corner strewn with broken chairs0111",			//62  Ch
		"7the bridge near to a log settlement0011",			//63  Br
		"1a crumbling mass of petrified trees1011",			//64  Fs
		"3the edge of the pyramid1101",						//65  Py
		"7the roof of the ancient pyramid0100",				//66  Py
		"3an impassable split in the pyramid1110",			//67  Py
		"7a barren blasted wasteland0001",					//68  Ds
		"4an expanse of bleak, burnt land1100",				//69  Ds
		"5a delapidated abode hut0110",						//70  H
		"4the heart of the lillies0101",					//71  Fl
		"4the midst of the lillies1100",					//72  Fl
		"3a river's edge by a log bridge0100",				//73  Br
		"3a petrified village by a river crowded with lillies0100",//74 Vi
		"4the remains of a village1100",					//75  Vi
		"3the entrance to a petrified village1100",			//76  Vi
		"4a swamp matted with fibrous roots1100",			//77  Sw
		"2a village of hollow stumps defying the swamp0100",//78  SV
		"4a tunnel into one of the tree stumps1100",		//79  Cv
		"4a hollow chamber many meters in diameter1110"		//80  Cv
	};
		
	private static String[] objects = {
		"a shiny apple",									// 1
		"a fossilised egg",									// 2
		"a lily flower",									// 3
		"an earthenware jug",								// 4
		"a dirty old rag",									// 5
		"a ragged parchment",								// 6
		"a flickering torch",								// 7
		"a blistering pebble",								// 8
		"a woodman's axe",									// 9
		"a coil of rope",									//10
		"a rugged staff",									//11
		"a chip of marble",									//12
		"a polished coal",									//13
		"a piece of flint",									//14
		"a geologist's hammer",								//15
		"a wild canyon beast",								//16
		"a grain loaf",										//17
		"a juicy melon",									//18
		"some biscuits",									//19
		"a growth of mushrooms",							//20
		"a bottle of water",								//21
		"a flagon of wine",									//22
		"a flowing sap",									//23
		"a sparkling freshwater spring",					//24
		"the Boatman",										//25
		"a strapped oak chest",								//26
		"a fracture in the column",							//27
		"a mouth-like opening",								//28
		"an open trapdoor",									//29
		"a parched, dessicated villager",					//30
		"a still of bubbling green liquid",					//31
		"a tough skinned swampman",							//32
		"the Sage of the Lillies",							//33
		"wall after wall of evil books",					//34
		"a number of softer roots",							//35
		"fierce living storm that follows you",				//36
		"malevolent wraiths who push you towards the well",	//37
		"his dreaded cloak of entropy",						//38
		"Omegan the evil one",								//39
		"an immense snake wound around the hut",			//40
		"a group of aggressive logmen",						//41
		"the ancient scavenger","Median"					//42,43
	};
		
	private static final String[] verbs = {
		"n","s","e","w","go","get","take","give","drop","leave","eat","drink","ride",
		"open","pick","chop","chip","tap","break","fight","strike","attack","hit",
		"kill","swim","shelter","help","scratch","catch","rub","polish","read",
		"examine","fill","say","wait","rest","wave","info","load","save","quit","games"
	};
	
	private static final String[] nouns = {
		"apple","egg","flower","jug","rag","parchment","torch","pebble","axe","rope",
		"staff","chip","coal","flint","hammer","beast","loaf","melon","biscuits",
		"mushrooms","bottle","flagon","sap","water","boat","chest","column","opening",
		"trapdoor","villager","liquid","swampman","sage","books","roots","storm","wraiths",
		"cloak","omegan","snake","logmen","scavenger","median","north","south","east","west",
		"up","down","in","out"
	};
			
	private static String itemLocation = "MNgIL5;/U^kZpcL%LJ£5LJm-ALZ/SkIngRm73**MJFF          ";
	private static String itemFlag = "90101191001109109000901000111000000100000010000000000";
	
	private static String[] prepositions = {
		"by","facing","at","in","outside","beneath","on"	
	};
	
	public static String getLocation(int number) {
		return locations[number];
	}
	
	public static String getObjects(int number) {
		return objects[number-1];
	}

	public static String[] getPrepositions() {
		return prepositions;
	}
		
	public static char getItemLocation(int number) {
		return itemLocation.charAt(number-1);
	}
	
	public static char getItemFlag(int number) {
		return itemFlag.charAt(number-1);
	}
	
	public static String[] getVerbs() {
		return verbs;
	}
	
	public static String[] getNouns() {
		return nouns;
	}
}
/* 9 September 2024 - Created File
 * 29 October 2024 - Moved to version 1
 * 30 October 2024 - Moved to data packages
 * 				   - made class static
 * 31 October 2024 - Added description array and method to retrieve it
 * 				   - Added description for items and changed way to retrieve item and locations
 * 1 November 2024 - Added the code to handle the items
 * 2 November 2024 - Added arrays to hold verbs & nouns
 * 2 November 2024 - Started fixing up the data so it isn't all caps
 * 19 December 2024 - Added the games command
 * 20 December 2024 - Moved the games command
 * 23 December 2024 - Updated to version 2.
 * 20 January 2025 - Change liqupr to liquid
 * 31 January 2025 - Completed Testing and increased version
 * 2 February 2025 - Added comments to outline Location and Item numbers.
 * 				   - Removed one of the exits to the abode hut.
*/