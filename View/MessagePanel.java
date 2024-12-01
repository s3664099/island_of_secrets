/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 1 December 2024
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Model.GameEngine;

public class MessagePanel extends JPanel {

	private JLabel label;
	private GamePanel game;
	private GameEngine engine;
	private boolean room;
	
    public MessagePanel(GamePanel game,GameEngine engine,String messageOne,String messageTwo,int noMessages) {
    	
        this.game = game;
        this.engine = engine;
        noMessages --;
        
        // Set a BorderLayout to center the label
        setLayout(new BorderLayout());
        
        label = createLabel("<html>"+messageOne+"</html>");
        add(label, BorderLayout.CENTER);
        
        messageOne = messageOne+"<br>"+messageTwo;
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

            	TimeUnit.SECONDS.sleep(2);
                
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
 */