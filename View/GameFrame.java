/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 8 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import javax.swing.JFrame;

import Interfaces.GameUI;
import Model.GameEngine;
import UISupport.GameController;

public class GameFrame extends JFrame implements GameUI {

	private static final long serialVersionUID = -5095376582483866399L;
	private final GamePanel gamePanel;

	public GameFrame(GameEngine engine) {
		
		super("Island of Secrets");
		GameController controller = new GameController(engine,this);
		this.gamePanel = new GamePanel(controller);
		initiliseUI();
	}
	
	private void initiliseUI() {

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(gamePanel);
		configureWindow();
		setVisible(true);
	}
	
	public void configureWindow()  {
		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setResizable(false);
		
		//Center
		setLocationRelativeTo(null);
	}

	@Override
	public void refreshUI(GameController game) {
		gamePanel.refreshMainView(game);		
	}

	@Override
	public void showMapView(GameController game) {
		// TODO Auto-generated method stub
		
	}
	
	public void closeUI() {
		setVisible(false);
		dispose();
	}
}
/* 8 November 2024 - Created File
 * 23 December 2024 - Passed frame to panel to enable quit function
 * 					- Updated to version 2.
 * 31 January 2025 - Completed Testing and increased version
 * 2 February 2025 - Added generated serial ID
 * 3 March 2025 - Added call to focus on command line
 * 5 March 2025 - Increased to v4.0
 * 29 March 2025 - Hid set command field
 * 4 April 2025 - Updated frame to build map panel
 * 8 April 2025 - Removed unusued code
 */