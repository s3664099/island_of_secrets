/*
Title: Island of Secrets Book Button
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 7 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import Model.GameController;
import View.GamePanel;

public class BookButton implements ActionListener {

	private GamePanel panel;
	private GameController controller;
	
	public BookButton(GamePanel panel,GameController controller) {		
		this.panel = panel;
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				URI uri = new URI("https://archive.org/details/island-of-secrets_202303");
				desktop.browse(uri);
			} catch (IOException excp) {
				excp.printStackTrace();
			} catch (URISyntaxException excp) {
				excp.printStackTrace();
			}
		}
		
		panel.refreshMainView(controller);
	}

}

/* 2 March 2025 - Created Class
 * 5 March 2025 - Increased to v4.0
 * 26 March 2025 - Commented out code to enable to run
 * 7 April 2025 - Button to open webpage now works.
 */
