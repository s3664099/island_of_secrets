/*
Title: Island of Secrets MessagePanel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 1.1
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

public class MedianPanel extends JPanel {

	private JLabel label;
	private GamePanel game;
	private GameEngine engine;
	private boolean room;
	
    public MedianPanel(GamePanel game,GameEngine engine,boolean room) {
    	
        this.game = game;
        this.engine = engine;
        this.room = room;
        
        // Set a BorderLayout to center the label
        setLayout(new BorderLayout());
        
        label = createLabel(room ? "<html>He takes it ...</html>" : 
        							"<html>He takes it, runs down the corridor, ...</html>");
        add(label, BorderLayout.CENTER);
        
        startSequence();

    }
	
    private JLabel createLabel(String text) {
        label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24)); // Optional: Customize the font
        return label;
    }
    
    private void startSequence() {
        // First delay: 2 seconds for initial message
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);

                SwingUtilities.invokeLater(() -> {
                    label.setText(room
                            ? "<html>He takes it and casts it into the chemical vats, purifying them with "
                            		+ "a clear blue light reaching far into the lakes and rivers beyond.</html>"
                            : "<html>He takes it, runs down the corridor and casts it into the chemical vats, "
                            		+ "purifying them with a clear blue light reaching far into the lakes "
                            		+ "and rivers beyond.</html>");
                    
                    new Thread(() -> {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            SwingUtilities.invokeLater(() -> resetPanel(game));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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