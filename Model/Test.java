/*
Title: Island of Secrets Initialise Test Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.0
Date: 29 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.Set;
import java.util.TreeSet;

public class Test {

	public void setTest(Game game, Player player) {
		player.setRoom(27);
		//player.setTime(900);
		//player.setWisdom(80);
		//game.getItem(3).setLocation(0);
		//game.getItem(12).setLocation(0);
		//game.getItem(3).setFlag(0);
		//game.getItem(12).setFlag(0);
		//game.getItem(13).setLocation(player.getRoom());
		//game.getItem(39).setLocation(player.getRoom());
		//game.getItem(12).setLocation(0);
		//player.setWisdom(80);
	}
	
	public void displayValue(Game game, Player player) {

		//System.out.println(player.getWisdom());
		//System.out.println(game.getItem(3).getItem());
		//System.out.println(game.getItem(16).getItem());
		//System.out.println(game.getItem(7).getLocation());
		//System.out.println(game.getItem(7).getFlag());
		//System.out.println(game.getItem(11).getFlag());
		//System.out.println(game.getItem(13).getFlag());
		//System.out.println(game.getRoomName(33));
		//System.out.println(game.getItem(43).getFlag());
		//System.out.println(game.getItem(16).getLocation());
		//player.setStrength(40);
		System.out.println(player.getRoom());
		
	}
	
	public void displayLocations(Game game) {
        // Create a set and add the specified numbers
        Set<Integer> numbers = new TreeSet<>();
        numbers.add(32);
        numbers.add(33);
        numbers.add(42);
        numbers.add(43);
        numbers.add(52);
        numbers.add(53);

        // Iterate through the set and print each number
        for (int number : numbers) {
            System.out.println(game.getRoomName(number));
        }
    }
	
}

/* 29 December 2024 - Created file.
 * 
 */
