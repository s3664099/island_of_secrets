package View;

import javax.swing.JFrame;

import Model.GameEngine;

public class GameFrame extends JFrame {

	public GameFrame(GameEngine game) {
		
		super("Island of Secrets");

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		GamePanel gamePanel = new GamePanel(game,this);
		this.add(gamePanel);
		
		//sets the boundaries of the frame.
		setBounds(100,100, 800,600);
		setVisible(true);
	}
}
