/*
Title: Island of Secrets Main
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 0.1
Date: 7 September 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

public class Main {

	public void startGame() {

		//Initialises the raw data
		RawData data = new RawData();

		//Begins initialising the game
		Data locations = new Data(data.getLocations());
		Data objects = new Data(data.getObjects());
		Data prepositions = new Data(data.getPrepositions());
		Data itemLocation = new Data("MNgIL5;/U^kZpcL%LJ£5LJm-ALZ/SkIngRm73**MJFF          ",32);
		Data itemVisibility = new Data("90101191001109109000901000111000000100000010000000000",48);
		itemLocation.modifyIntData();
		Data verbs = new Data("N??S??E??W??GO?GETTAKGIVDROLEAEATDRIRIDOPEPICC"
				+ "HOCHITAPBREFIGSTRATTHITKILSWISHEHELSCRCATRUBPOLREAEXAFILSA"
				+ "YWAIRESWAVINFXLOXSAQUI",3,false);
		Data nouns = new Data("APPEGGFLOJUGRAGPARTORPEBAXEROPSTACHICOAFLIHAMC"
				+ "ANLOAMELBISMUSBOTWINSAPWATBOACHECOLSTOTRAVILLIQSWASAGBOORO"
				+ "OASAWRACLOOMESNALOGSCAMEDNORSOUEASWESUP?DOWIN?OUT???",3,false);
		
		System.out.println("Locations\n------------------------");
		for (int x=0;x<locations.getDataLength();x++) {
			System.out.printf("%d) %s %n",x+1, locations.getStringData(x+1));
		}

		System.out.println("\nItems\n------------------------");
		for (int x=0;x<objects.getDataLength();x++) {
			System.out.printf("%d) %s %n",x+1, objects.getStringData(x+1));
		}
		
		Game game = new Game(locations,objects,prepositions,itemLocation,itemVisibility,verbs,nouns);
		game.run();
	}
	
}

/*
7 Sept 2024 - Created File
8 Sept 2024 - Finished Initialisation
*/