/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Model.GameEngine;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -5095376582483866399L;

	public GameFrame(GameEngine engine) {
		
		super("Island of Secrets");
		initiliseUI(engine);
	}
	
	private void initiliseUI(GameEngine engine) {

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		configureWindow();
		
		GamePanel gamePanel = new GamePanel(engine);
		this.add(gamePanel);
				
		SwingUtilities.invokeLater(() -> {
			gamePanel.setCommandField();
			setVisible(true);
		});
	}
	
	public void configureWindow()  {
		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setResizable(false);
		
		//Center
		setLocationRelativeTo(null);
	}
}
/* 8 November 2024 - Created File
 * 23 December 2024 - Passed frame to panel to enable quit function
 * 					- Updated to version 2.
 * 31 January 2025 - Completed Testing and increased version
 * 2 February 2025 - Added generated serial ID
 * 3 March 2025 - Added call to focus on command line
 * 5 March 2025 - Increased to v4.0
 */