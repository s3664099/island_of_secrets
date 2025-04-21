/*
Title: Island of Secrets Book Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 21 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import Model.GameController;
import View.GamePanel;

public class BookButton implements ActionListener {

	private final GamePanel panel;
	private final GameController controller;
	private static final String BOOK_URL = "https://archive.org/details/island-of-secrets_202303";
	
	public BookButton(GamePanel panel,GameController controller) {		
		this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");;
		this.controller = Objects.requireNonNull(controller, "GameController cannot be null");;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		openBookLink();
		refreshGameView();
	}
	
	private void openBookLink() {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(BOOK_URL));
			} catch (IOException|URISyntaxException e) {
				handleLinkError(e);
			}
		}
	}
	
	private void refreshGameView() {
		panel.refreshMainView(controller);
	}
	
	private void handleLinkError(Exception e) {
		System.err.println("Failed to open book linke :"+e.getMessage());
	}
}

/* 2 March 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 * 7 April 2025 - Button to open webpage now works.
 * 21 April 2025 - Updated based on DeepSeek recommendations
 */
