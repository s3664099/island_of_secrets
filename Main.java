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
		Data code_L = new Data("MNgIL5;/U^kZpcL%LJ£5LJm-ALZ/SkIngRm73**MJFF          ",32);
		Data code_F = new Data("90101191001109109000901000111000000100000010000000000",48);
		code_L.modifyIntData();
		Data verbs = new Data("N??S??E??W??GO?GETTAKGIVDROLEAEATDRIRIDOPEPICC"
				+ "HOCHITAPBREFIGSTRATTHITKILSWISHEHELSCRCATRUBPOLREAEXAFILSA"
				+ "YWAIRESWAVINFXLOXSAQUI",3,false);
		Data nouns = new Data("APPEGGFLOJUGRAGPARTORPEBAXEROPSTACHICOAFLIHAMC"
				+ "ANLOAMELBISMUSBOTWINSAPWATBOACHECOLSTOTRAVILLIQSWASAGBOORO"
				+ "OASAWRACLOOMESNALOGSCAMEDNORSOUEASWESUP?DOWIN?OUT???",3,false);
	}
	
}

/*
7 Sept 2024 - Created File
8 Sept 2024 - Finished Initialisation
*/