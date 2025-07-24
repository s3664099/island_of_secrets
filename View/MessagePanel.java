/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.4
Date: 24 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Interfaces.GameView;
import Model.GameEngine;
import UISupport.GameController;
import UISupport.GameState;

public class MessagePanel extends JPanel implements GameView {

	private static final long serialVersionUID = 7193673927279090863L;
	private GameController controller;
	private GameState state;
	private GamePanel panel;
	
	private JLabel label;
	private List<String> gameMessages;

	private final int HEAD = 0;
	private final String FONT = "Arial";
	private final int FONT_SIZE = 24;
	private static final int DISPLAY_DURATION_SECONDS = 2;
	
    public MessagePanel(GameController game, GamePanel panel) {
    	
    	this.controller = game;
    	this.state = game.getState();
    	this.panel = panel;
    	
    	setLayout(new BorderLayout());
    	label = createLabel("");
    	add(label,BorderLayout.CENTER);
    }
	
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(FONT, Font.BOLD, FONT_SIZE)); 
        return label;
    }
    
    public void refreshUI(GameController controller) {
    	this.controller = controller;
    	this.state = controller.getState();
    	this.gameMessages = new ArrayList<>(state.getPanelMessage());
    	startMessageSequence();
    }
    
    private void startMessageSequence() {
    	new Thread(() -> {
    		for (String message:gameMessages) {
    			SwingUtilities.invokeLater(() -> {
    				label.setText("<html>"+message+"</html>");
    			});
    			try {
    				TimeUnit.SECONDS.sleep(DISPLAY_DURATION_SECONDS);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		//SwingUtilities.invokeLater(() -> controller.switchToView("main"));
    	} ).start();
    }

	@Override
	public JComponent getViewComponent() {
		return this;
	}
}
/* 30 November 2024 - Created File
 * 1 December 2024 - Change name of class to specific name
 * 2 December 2024 - Added longer delay for last display
 * 4 December 2024 - Added code to have different messages appear if repeating
 * 23 December 2024 - Updated to version 2.
 * 18 January 2025 - Removed the unused includes and added a serializable section to get rid of warnings.
 * 31 January 2025 - Completed Testing and increased version
 * 5 March 2025 - Increased to v4.0
 * 21 March 2025 - Updated for MessageBuilder class
 * 22 March 2025 - Fixed issue with MessagePanel - works now.
 * 26 March 2025 - Commented out code to allow code to run
 * 24 July 2025 - Started updating message panel to display special messages
 */