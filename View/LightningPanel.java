/*
Title: Island of Secrets Lightning Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 30 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import Model.GameEngine;

public class LightningPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int number = 0;
	private JLabel label;
	private GamePanel game;
	
    public LightningPanel(int initialNumber,GamePanel game,GameEngine engine) {
        this.number = initialNumber;
        this.game = game;

        // Set a BorderLayout to center the label
        setLayout(new BorderLayout());

        // Create and add the label to the center
        label = new JLabel("⚡⚡ Lightning Flashes ⚡⚡", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.CENTER);

        startLightningEffect();
    }
	
	
    private void startLightningEffect() {
    	
        Timer timer = new Timer(200, e -> {
        	
            // Change background color based on even/odd number
            if ((number & 1) == 0) {
                setBackground(Color.YELLOW);
            } else {
                setBackground(Color.BLACK);
            }

            // Increment the number
            number++;

            // Optional: Stop the timer after 10 iterations
            // Returns to original panel
            if (number > 10) {
                ((Timer) e.getSource()).stop();
                resetPanel(game);
            }
            
            JLabel label = new JLabel("Lightning Flashes!!");
            this.add(label);

            // Repaint the panel to reflect changes
            repaint();
        });

        timer.start();
    }
    
	private void resetPanel(GamePanel game) {
		game.removeAll();
		//game.add(this.engine);
		game.revalidate();
		game.repaint();
	}
}
/* 25 November 2024 - Created File
 * 23 December 2024 - Updated to version 2.
 * 18 January 2025 - Removed the unused includes and added a serializable section to get rid of warnings.
 * 31 January 2025 - Completed Testing and increased version
 * 5 March 2025 - Increased to v4.0
 * 21 March 2025 - Removed the notes
 * 26 March 2025 - Commented out code to allow to run
 * 30 March 2025 - Removed unusued code
*/