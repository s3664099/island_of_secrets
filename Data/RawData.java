/*
Title: Island of Secrets Raw Data
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.1
Date: 20 January 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Data;

public class RawData {
	
	private static String[] locations = {
		"4the furthest depth of the forest1001",
		"4the depths of the mutant forest1000",
		"7a path out of the overground depths1000",
		"6a carniverous tree1000",
		"4a corral beneath the Crimson Canyon1110",
		"7the top of a steep cliff1011",
		"4the marsh factory1001",
		"4the sludge fermation vats1110",
		"7the uppermost battlements1001",
		"4Omegan's sanctum1110",
		"4Snelm's lair0001",
		"2a dark cave0000",
		"1broken branches0100",
		"1a thicket of biting bushes0000",
		"1a huge glassy stone1110",
		"7the edge of the Crimson Canyon0011",
		"4the clone factory0101",
		"4a corridor of clone storage casks1100",
		"7edge of the well0000",
		"4the Room of Secret Visions1110",
		"4Snelm's inner chamber0111",
		"3the southern edge of the forest0101",
		"7a leafy path1000",
		"3a fork in the path0100",
		"7an apparently unclimable rocky path1100",
		"7a ledge atop the Crimson Canyon0010",
		"4a tall entrance chamber1101",
		"4a low passage with arms reaching from the wall1010",
		"7the approach to the Well of Despair0001",
		"4a dim corridor deep in the castle1010",
		"4the stagnant waters of the crawling creek1001",
		"4a shallow pool off the creek1100",
		"7a log pier, jutting out over the creek0000",
		"4a stretch of featureless dunes1100",
		"1a group of tall trees1010",
		"7a narrow ledge at the summit of the canyon0011",
		"2a monsterous portal in the castle wall0011",
		"4a chamber inches deep with dust0001",
		"4here!!!!!1111",
		"2a carved archway0010",
		"4a small hut in the log settlement0111",
		"1a huge split-log table1001",
		"4the porch of the logman's cabin0110",
		"4grandpa's shack1101",
		"3a clearing in the trees by a rickety shack0010",
		"4the nest of a huge dactyl0111",
		"6the Castle of Dark Secrets by two huge stones0011",
		"4a room littered with bones0111",
		"4the Cell of Whispered Secrets0111",
		"4the Library of Written Secrets0111",
		"4a refuse strewn storeroom1111",
		"4the Logmen's hall0000",
		"5a log building1000",
		"7a rutted hillside1100",
		"7a windswept plain amongst stone megaliths0100",
		"7the steps of an ancient pyramid1010",
		"7the Island of Secrets0111",
		"1a broken marble column1001",
		"7an expanse of cracked, baked earth1100",
		"4a deserted abode hut1010",
		"4a livid growth of mad orchids1011",
		"4a corner strewn with broken chairs0111",
		"7the bridge near to a log settlement0011",
		"1a crumbling mass of petrified trees1011",
		"3the edge of the pyramid1101",
		"7the roof of the ancient pyramid0100",
		"3an impassable split in the pyramid1110",
		"7a barren blasted wasteland0001",
		"4an expanse of bleak, burnt land1100",
		"5a delapidated abode hut0110",
		"4the heart of the lillies0101",
		"4the midst of the lillies1100",
		"3a river's edge by a log bridge0100",
		"3a petrified village by a river crowded with lillies0100",
		"4the remains of a village1100",
		"3the entrance to a petrified village1100",
		"4a swamp matted with fibrous roots1100",
		"2a village of hollow stumps defying the swamp0100",
		"4a tunnel into one of the tree stumps1100",
		"4a hollow chamber many meters in diameter1110"		
	};
		
	private static String[] objects = {
		"a shiny apple",
		"a fossilised egg",
		"a lily flower",
		"an earthenware jug",
		"a dirty old rag",
		"a ragged parchment",
		"a flickering torch",
		"a blistering pebble",
		"a woodman's axe",
		"a coil of rope",
		"a rugged staff",
		"a chip of marble",
		"a polished coal",
		"a piece of flint",
		"a geologist's hammer",
		"a wild canyon beast",
		"a grain loaf",
		"a juicy melon",
		"some biscuits",
		"a growth of mushrooms",
		"a bottle of water",
		"a flagon of wine",
		"a flowing sap",
		"a sparkling freshwater spring",
		"the Boatman",
		"a strapped oak chest",
		"a fracture in the column",
		"a mouth-like opening",
		"an open trapdoor",
		"a parched, dessicated villager",
		"a still of bubbling green liquid",
		"a tough skinned swampman",
		"the Sage of the Lillies",
		"wall after wall of evil books",
		"a number of softer roots",
		"fierce living storm that follows you",
		"malevolent wraiths who push you towards the well",
		"his dreaded cloak of entropy",
		"Omegan the evil one",
		"an immense snake wound around the hut",
		"a group of aggressive logmen",
		"the ancient scavenger","Median"
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
*/