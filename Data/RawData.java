/*
Title: Island of Secrets Raw Data
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.5
Date: 3 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package Data;

public class RawData {
	
	private static String[] locations = {
		"4the furthest depth of the forest1001",
		"4the depths of the mutant forest1000",
		"7a path out of the overground depths1000",
		"6a carniverous tree1000",
		"4a corral beneath the crimson canyon1110",
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
		"7the edge of the crimson canyon0011",
		"4the clone factory0101",
		"4a corridor of clone storage casks1100",
		"7edge of the well0000",
		"4the room of secret visions1110",
		"4Snelm's inner chamber0111",
		"3the southern edge of the forest1010",
		"7a leafy path1000",
		"3a fork in the path0100",
		"7an apparently unclimable rocky path1100",
		"7a ledge atop the crimson canyon0010",
		"4a tall entrance chamber1101",
		"4a low passage with arms reaching from the wall1010",
		"7the approach to the well of despair0001",
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
		"3A CLEARING IN THE TREES BY A RICKETY SHACK0010",
		"4THE NEST OF A HUGE DACTYL0111",
		"6THE CASTLE OF DARK SECRETS BY TWO HUGE STONES0011",
		"4A ROOM LITTERED WITH BONES0111",
		"4THE CELL OF WHISPERED SECRETS0111",
		"4THE LIBRARY OF WRITTEN SECRETS0111",
		"4A REFUSE STREWN STOREROOM1111",
		"4THE LOGMAN'S HALL0000",
		"5A LOG BUILDING1000",
		"7A RUTTED HILLSIDE1100",
		"7A WINDSWEPT PLAIN AMONGST STONE MEGALITHS0100",
		"7THE STEPS OF AN ANCIENT PYRAMID1010",
		"7THE ISLAND OF SECRETS0111",
		"1A BROKEN MARBLE COLUMN1001",
		"7AN EXPANSE OF CRACKED, BAKED EARTH1100",
		"4A DESERTED ABODE HUT1010",
		"4A LIVID GROWTH OF MAD ORCHIDS1011",
		"4A CORNER STREWN WITH BROKEN CHAIRS0111",
		"7THE BRIDGE NEAR TO A LOG SETTLEMENT0011",
		"1A CRUMBLING MASS OF PETRIFIED TREES1011",
		"3THE EDGE OF THE PYRAMID1101",
		"7THE ROOF OF THE ANCIENT PYRAMID0100",
		"3AN IMPASSABLE SPLIT IN THE PYRAMID1110",
		"7A BARREB BLASTED WASTELAND0001",
		"4AN EXPANSE OF BLEAK, BURNT LAND1100",
		"5A DELAPIDATED ABODE HUT0110",
		"4THE HEART OF THE LILLIES0101",
		"4THE MIDST OF THE LILLIES1100",
		"3A RIVER'S EDGE BY A LOG BRIDGE0100",
		"3A PETRIFIED VILLAGE BY A RIVER CROWDED WITH LILLIES0100",
		"4THE REMAINS OF A VILLAGE1100",
		"3THE ENTRANCE  TO A PETRIFIED VILLAGE1100",
		"4A SWAMP MATTED WITH FIBROUS ROOTS1100",
		"2A VILLAGE OF HOLLOW STUMPS DEFYING THE SWAMP0100",
		"4A TUNNEL INTO ONE OF THE TREE STUMPS1100",
		"4A HOLLOW CHAMBER MANY METERS IN DIAMETER1110"		
	};
		
	private static String[] objects = {
		"A shiny apple",
		"A fossilised egg",
		"A lily flower",
		"An earthenware jug",
		"A dirty old rag",
		"A ragged parchment",
		"A flickering torch",
		"A blistering pebble",
		"A woodman's axe",
		"A coil of rope",
		"A RUGGED STAFF",
		"A CHIP OF MARBLE",
		"A POLISH COAL",
		"A PIECE OF FLINT",
		"A GEOLOGIST'S HAMMER",
		"A WILD CANYON BEAST",
		"A GRAIN LOAF",
		"A JUICY MELON",
		"SOME BISCUITS",
		"A GROWTH OF MUSHROOMS",
		"A BOTTLE OF WATER",
		"A FLAGON OF WINE",
		"A FLOWING SAP",
		"A SPARKLING FRESHWATER SPRING",
		"THE BOATMAN",
		"A STRAPPED OAK CHEST",
		"A FRACTURE IN THE COLUMN",
		"A MOUTH-LIKE OPENING",
		"AN OPEN TRAPDOOR",
		"A PARCHED, DESSICATED VILLAGER",
		"A STILL OF BUBBLING GREEN LIQUID",
		"A TOUGH SKINNED SWAMPMAN",
		"THE SAGE OF THE LILLIES",
		"WALL AFTER WALL OF EVIL BOOKS",
		"A NUMBER OF SOFTER ROOTS",
		"FIERCE LIVING STORM THAT FOLLOWS YOU",
		"MALEVOLENT WRAITHS WHO PUSH YOU TOWARDS THE WELL",
		"HIS DREADED CLOAK OF ENTROPY",
		"OMEGAN THE EVIL ONE",
		"AN IMMENSE SNAKE WOUND AROUND THE HUT",
		"A GROUP OF AGGRESSIVE LOGMEN",
		"THE ANCIENT SCAVENGER","MEDIAN"
	};
		
	private static final String[] verbs = {
		"n","s","e","w","go","get","take","give","drop","leave","eat","drink","ride",
		"open","pick","chop","chip","tap","break","fight","strike","attack","hit",
		"kill","swim","shelter","help","scratch","catch","rub","polish","read",
		"examine","fill","say","wait","rest","wave","info","load","save","quit"
	};
	
	private static final String[] nouns = {
		"apple","egg","flower","jug","rag","parchment","torch","pebble","axe","rope",
		"staff","chip","coal","flint","hammer","beast","loaf","melon","biscuits",
		"mushrooms","bottle","flagon","sap","water","boatman","chest","column","opening",
		"trapdoor","villager","liquor","swampman","sage","books","roots","storm","wraiths",
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
*/