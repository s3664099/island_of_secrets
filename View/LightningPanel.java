/*
Title: Island of Secrets Lightning Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.0
Date: 25 November 2024
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
import javax.swing.Timer;

import Model.GameEngine;

public class LightningPanel extends JPanel {

	private int number = 0;
	private JLabel label;
	
    public LightningPanel(int initialNumber) {
        this.number = initialNumber;

        // Set a BorderLayout to center the label
        setLayout(new BorderLayout());

        // Create and add the label to the center
        label = new JLabel("Lightning Flashes", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24)); // Optional: Customize the font
        label.setForeground(Color.WHITE); // Optional: Text color
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
            if (number > 10) {
                ((Timer) e.getSource()).stop();
            }
            
            JLabel label = new JLabel("Lightning Flashes!!");
            this.add(label);

            // Repaint the panel to reflect changes
            repaint();
        });

        timer.start();
    }
}
/* 25 November 2024 - Created File
 * 
*/