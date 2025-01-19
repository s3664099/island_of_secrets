/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 2.1
Date: 18 January 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Font;
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
	
    public MessagePanel(GamePanel game,GameEngine engine,String messageOne,String messageTwo,int noMessages) {
    	
        this.game = game;
        this.engine = engine;
        noMessages --;
        
        // Set a BorderLayout to center the label
        setLayout(new BorderLayout());
        
        label = createLabel("<html>"+messageOne+"</html>");
        add(label, BorderLayout.CENTER);

        //Splits the second message to create multiple messages
        String[] messages = messageTwo.split("\\|");
        
        if (messages.length == 1) {
        	messageOne = messageOne+"<br>"+messageTwo;
        } else {
        	messageOne = messageOne+"<br>"+messages[0];
        	messageTwo = messageTwo.substring(messages[0].length()+1);
        }
        
        startSequence(noMessages,messageOne,messageTwo);

    }
	
    private JLabel createLabel(String text) {
        label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24)); // Optional: Customize the font
        return label;
    }
    
    private void startSequence(int noMessages,String messageOne,String messageTwo) {
            	
    	// First delay: 2 seconds for initial message
        new Thread(() -> {
            try {
            	
            	int delay = 2;
            	
            	if (noMessages==0) {
            		delay=5;
            	}
            	
            	TimeUnit.SECONDS.sleep(delay);
                
            	if (noMessages == 0) {
            		SwingUtilities.invokeLater(() ->  resetPanel(game));
            	} else {
            		setPanel(game,new MessagePanel(this.game,this.engine,messageOne,messageTwo,noMessages));
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
		game.add(this.engine);
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
 */