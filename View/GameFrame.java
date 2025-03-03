/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 3.2
Date: 3 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import javax.swing.JFrame;

import Model.GameEngine;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -5095376582483866399L;

	public GameFrame(GameEngine game) {
		
		super("Island of Secrets");

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		GamePanel gamePanel = new GamePanel(game,this);
		this.add(gamePanel);
		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setVisible(true);
		gamePanel.setCommandField();
	}
}
/* 8 November 2024 - Created File
 * 23 December 2024 - Passed frame to panel to enable quit function
 * 					- Updated to version 2.
 * 31 January 2025 - Completed Testing and increased version
 * 2 February 2025 - Added generated serial ID
 * 3 March 2025 - Added call to focus on command line
 */