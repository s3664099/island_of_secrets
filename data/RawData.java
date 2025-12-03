/*
Title: Island of Secrets Raw Data
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package data;

public class RawData {
	
	
	private static final Integer[] LOCATION_TYPES = {1,1,1,2,13,4,5,6,7,8,
													9,9,10,10,11,12,5,3,14,12,
													9,1,41,41,4,4,16,15,14,16,
													17,17,18,19,1,4,34,16,20,21,
													22,23,22,22,1,24,25,26,16,27,
													16,22,22,28,29,30,31,32,33,22,
													35,36,18,39,30,30,30,33,33,22,
													35,35,18,37,37,37,38,40,9,9};
	
    private static final String[] LOCATION_IMAGE = {"forest","eviltree","pods","cliff","factory",
    										 "vat","battlement","sanctum","cave","bush",
    										 "stone","cloud","paddock","well","hand",
    										 "room","creek","bridge","dunes","here",
    										 "archway","hut","table","nest","castle",
    										 "bones","bookshelf","hill","stonehenge","pyramid",
    										 "island","column","desert","castledoor","flowers",
    										 "brokenchair","village","swamp","stonetree","stumphouse",
    										 "path"};
		    
	private static final String[] LOCATIONS = {
		"4the furthest depth of the forest1101",			// 1  F    1 (Forest)
		"4the depths of the mutant forest1000",				// 2  F    1 (Forest)
		"7a path out of the overground depths1000",			// 3  F    1 (Forest)
		"6a carniverous tree1000",							// 4  CT   2 (Carniverous Tree)
		"4a corral beneath the Crimson Canyon1110",			// 5  Pk  13 (Paddock/Corral)
		"7the top of a steep cliff1011",					// 6  CL   4 (Cliff)  
		"4the marsh factory1001",							// 7  Fc   5 (Factory)
		"4the sludge fermation vats1110",					// 8  Vt   6 (Vat)
		"7the uppermost battlements1001",					// 9  Bt   7 (Battlements)
		"4Omegan's sanctum1110",							//10  Sm   8 (Sanctum
		"4Snelm's lair1001",								//11  C    9 (Cave)
		"2a dark cave0000",									//12  C    9 (Cave)
		"1broken branches0100",								//13  B   10 (Bush)
		"1a thicket of biting bushes0000",					//14  B   10 (Bush)
		"1a huge glassy stone1110",							//15  St  11 (Stone)
		"7the edge of the Crimson Canyon0011",				//16  CL   4 (Cliff)
		"4the clone factory0101",							//17  Fc   5 (Factory)
		"4a corridor of clone storage casks1100",			//18  Pd   3 (Pod)
		"7edge of the well0000",							//19  W   14 (Well)
		"4the Room of Secret Visions1110",					//20  Cl  12 (Cloud)
		"4Snelm's inner chamber0111",						//21  C    9 (Cave)
		"3the southern edge of the forest0101",				//22  F    1 (Forest)
		"7a leafy path1000",								//23  P   43 (Path)
		"3a fork in the path0100",							//24  P   43 (Path)
		"7an apparently unclimable rocky path1100",			//25  CL   4 (Cliff)
		"7a ledge atop the Crimson Canyon0010",				//26  CL   4 (Cliff)
		"4a tall entrance chamber1101",						//27  R   16 (Room)
		"4a low passage with arms reaching from the wall1010",//28 H  15 (Hands)
		"7the approach to the Well of Despair0001",			//29  W   14 (Well)
		"4a dim corridor deep in the castle1010",			//30  R   16 (Room)
		"4the stagnant waters of the crawling creek1001",	//31  Cr  17 (Creek)
		"4a shallow pool off the creek1100",				//32  Cr  17 (Creek)
		"7a log pier, jutting out over the creek0000",		//33  Br  18 (Bridge)
		"4a stretch of featureless dunes1100",				//34  SD  19 (Sand Dunes)
		"1a group of tall trees1010",						//35  F    1 (Forest)
		"7a narrow ledge at the summit of the canyon0011",	//36  CL   4 (Cliff)
		"2a monsterous portal in the castle wall0011",		//37  Bt  34 (Castle Door) 
		"4a chamber inches deep with dust0001",				//38  R   16 (Room)
		"4here!!!!!1111",									//39  !   20 (Exclamation Mark)
		"2a carved archway0010",							//40  Ar  21 (Archway)
		"4a small hut in the log settlement0111",			//41  H   22 (Hut)
		"1a huge split-log table1001",						//42  Tb  23 (Table)
		"4the porch of the logman's cabin0110",				//43  H   22 (Hut)
		"4grandpa's shack1101",								//44  H   22 (Hut)
		"3a clearing in the trees by a rickety shack0010",	//45  F    1 (Forest)
		"4the nest of a huge dactyl0111",					//46  N   24 (Nest)
		"6the Castle of Dark Secrets by two huge stones0011",//47 Cs  25 (Castle)
		"4a room littered with bones0111",					//48  Bn  26 (Bones)
		"4the Cell of Whispered Secrets0111",				//49  R   16 (Room)
		"4the Library of Written Secrets0111",				//50  Bk  27 (Bookshelf)
		"4a refuse strewn storeroom1111",					//51  R   16 (Room)
		"4the Logmen's hall0000",							//52  H   22 (Hut)
		"5a log building1000",								//53  H   22 (Hut)
		"7a rutted hillside1100",							//54  Hl  28 (Hill)
		"7a windswept plain amongst stone megaliths0100",	//55  SH  29 (Stonehenge)
		"7the steps of an ancient pyramid1010",				//56  Py  30 (Pyramid)
		"7the Island of Secrets0111",						//57  Is  31 (Island)
		"1a broken marble column1001",						//58  Co  32 (Column)
		"7an expanse of cracked, baked earth1110",			//59  Ds  33 (Desert)
		"4a deserted abode hut1011",						//60  H   22 (Hut)
		"4a livid growth of mad orchids1011",				//61  Fl  35 (Flowers)
		"4a corner strewn with broken chairs0111",			//62  Ch  36 (Broken Chair)
		"7the bridge near to a log settlement0011",			//63  Br  18 (Bridge)
		"1a crumbling mass of petrified trees1011",			//64  Fs  39 (Stone Trees)
		"3the edge of the pyramid1101",						//65  Py  30 (Pyramid)
		"7the roof of the ancient pyramid0100",				//66  Py  30 (Pyramid)
		"3an impassable split in the pyramid1110",			//67  Py  30 (Pyramid)
		"7a barren blasted wasteland0001",					//68  Ds  33 (Desert)
		"4an expanse of bleak, burnt land1100",				//69  Ds  33 (Desert)
		"5a delapidated abode hut0110",						//70  H   22 (Hut)
		"4the heart of the lillies0101",					//71  Fl  35 (Flowers)
		"4the midst of the lillies1100",					//72  Fl  35 (Flowers)
		"3a river's edge by a log bridge0100",				//73  Br  18 (Bridge)
		"3a petrified village by a river crowded with lillies0100",//74 40 (Village)
		"4the remains of a village1100",					//75  Vi  40 (Village)
		"3the entrance to a petrified village1100",			//76  Vi  40 (Village)
		"4a swamp matted with fibrous roots1100",			//77  Sw  41 (Swamp)
		"2a village of hollow stumps defying the swamp0100",//78  SV  42 (Stump Village)
		"4a tunnel into one of the tree stumps1100",		//79  Cv   9 (Cave)
		"4a hollow chamber many meters in diameter1110"		//80  Cv   9 (Cave)
	};
		
	private static final String[] OBJECTS = {
			
		//Carriable Items
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
		
		//Food
		"a grain loaf",										//17
		"a juicy melon",									//18
		"some biscuits",									//19
		"a growth of mushrooms",							//20
		
		//Drink
		"a bottle of water",								//21
		"a flagon of wine",									//22
		"a flowing sap",									//23
		"a sparkling freshwater spring",					//24
		
		//Other Items
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
		
	private static final String[] VERBS = {
		"n","s","e","w","go","get","take","give","drop","leave",								//10
		"eat","drink","ride","open","pick","chop","chip","tap","break","fight",					//20
		"strike","attack","hit","kill","swim","shelter","help","scratch","catch","rub",			//30
		"polish","read","examine","fill","say","wait","rest","wave","info","load",				//40
		"save","quit","restart"																	//43
	};
	
	private static final String[] NOUNS = {
		"apple","egg","flower","jug","rag","parchment","torch","pebble","axe","rope",            //10
		"staff","chip","coal","flint","hammer","beast","loaf","melon","biscuits","mushrooms",    //20
		"bottle","flagon","sap","water","boat","chest","column","opening","trapdoor","villager", //30
		"liquid","swampman","sage","books","roots","storm","wraiths","cloak","omegan","snake",   //40
		"logmen","scavenger","median","north","south","east","west","up","down","in","out"       //51
	};
			
	private static final String ITEM_LOCATION = "MNgIL5;/U^kZpcL%LJ£5LJm-ALZ/SkIngRm73**MJFF          ";
	private static final String ITEM_FLAG = "90101191001109109000901000111000000100000010000000000";
	private static final String[] PREPOSITIONS = {
		"by","facing","at","in","outside","beneath","on"	
	};
	
	public static String getLocation(int number) {
		
		if (number<0 || number >= LOCATIONS.length) {
			throw new IllegalArgumentException("Raw Data - Invalid location number: "+number);
		}
		
		return LOCATIONS[number];
	}
	
	public static String getImage(int number) {
		
		if (number<0 || number >= LOCATION_TYPES.length) {
			throw new IllegalArgumentException("Raw Data - Invalid location type number: "+number);
		}
		
		return LOCATION_IMAGE[LOCATION_TYPES[number]-1];
	}
	
	public static String getObjects(int number) {
				
		if (number<0 || number >= OBJECTS.length+1) {
			throw new IllegalArgumentException("Raw Data - Invalid object number: "+number);
		}
		
		return OBJECTS[number-1];
	}

	public static String[] getPrepositions() {
		return PREPOSITIONS;
	}
		
	public static char getItemLocation(int number) {
		
		if (number<0 || number >= ITEM_LOCATION.length()) {
			throw new IllegalArgumentException("Raw Data - Invalid object location number: "+number);
		}
		
		return ITEM_LOCATION.charAt(number-1);
	}
	
	public static char getItemFlag(int number) {
		
		if (number<0 || number >= ITEM_FLAG.length()) {
			throw new IllegalArgumentException("Raw Data - Invalid object flag number: "+number);
		}		
		
		return ITEM_FLAG.charAt(number-1);
	}
	
	public static String[] getVerbs() {
		return VERBS;
	}
	
	public static String[] getNouns() {
		return NOUNS;
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
 * 7 February 2025 - Added the arrays for location images
 * 9 February 2025 - Added image names & function to retrieve the relevant name for location
 * 11 February 2025 - Fixed incorrect images on map
 * 5 March 2025 - Increased to v4.0
 * 10 March 2025 - Updated code as recommended by DeepSeek
 * 				 - Added reference to location of exception
 * 				 - Fix problem with error preventing objects
 * 26 May 2025 - Moved commands around for easy referencing
 * 6 July 2025 - Only one way into and out of Snelm's Lair
 * 3 December 2025 - Increased version number
*/