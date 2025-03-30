/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 26 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Model.GameEngine;

public class MessagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private GamePanel game;
	private GameEngine engine;
	private String game_messages;
	
	private final int HEAD = 0;
	private final String FONT = "Arial";
	private final int FONT_SIZE = 24;
	
    public MessagePanel(GamePanel game,GameEngine engine,List<String> messages,String start_message) {
    	
        this.game = game;
        this.engine = engine;
                
        // Set a BorderLayout to center the label
        setLayout(new BorderLayout());
        this.game_messages = start_message+messages.get(HEAD);
        
        //Displays the first message
        label = createLabel("<html>"+this.game_messages+"</html>");
        add(label, BorderLayout.CENTER);
        
        //Updates the first message with the second and removes them
        if (messages.size() > 0) {
        	
        	this.game_messages = this.game_messages+"<br>";
        		
        	if (messages.size()>0) {
        		messages.remove(HEAD);
        	}
        }
        
        startSequence(messages);

    }
	
    private JLabel createLabel(String text) {
        label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font(FONT, Font.BOLD, FONT_SIZE)); 
        return label;
    }
    
    private void startSequence(List<String> messages) {
            	
    	// First delay: 2 seconds for initial message
        new Thread(() -> {
            try {
            	            	
            	int delay = 2;
            	
            	//Last Message
            	if (messages.size()==0) {
            		delay=5;
            	}
          	
            	TimeUnit.SECONDS.sleep(delay);
                
            	if (messages.size()==0) {
            		SwingUtilities.invokeLater(() ->  resetPanel(game));
            	} else {
            		setPanel(game,new MessagePanel(this.game,this.engine,messages,this.game_messages));
            	}

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
	private void setPanel(JPanel game,JPanel panel) {
		game.removeAll();
		game.add(panel);
		game.revalidate();
		game.repaint();
	}
    
	private void resetPanel(GamePanel game) {
		game.removeAll();
		//game.add(this.engine);
		game.revalidate();
		game.repaint();
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
 */