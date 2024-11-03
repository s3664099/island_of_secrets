/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 2 November 2024
Source: https://archive.org/details/island-of-secrets_202303
*/
package View;

import javax.swing.JFrame;


public class GameFrame extends JFrame {

	public GameFrame() {
		
		super("Island of Secrets");

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		
		setVisible(true);
	}
	
}

/* 2 November 2024 - Created File
 *
 */