/*
Title: Island of Secrets Game Pabel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

import Model.GameController;

public class GamePanel extends JPanel {
	
    private static final long serialVersionUID = -1175236419449166126L;
	private final CardLayout cardLayout = new CardLayout();
    private final JPanel viewContainer = new JPanel(cardLayout);
    private final MainGamePanel mainView;
    private final MapPanel mapView;
    private final GameController controller;
	
	private static final String MAIN_VIEW = "MAIN";
	private static final String MAP_VIEW = "MAP";
	
	public GamePanel(GameController controller) {
		setLayout(new BorderLayout());
		this.controller = controller;
				
		this.mainView = new MainGamePanel(controller);
		this.mapView = new MapPanel(controller);
		
		//Initialise all views
		viewContainer.add(mainView,MAIN_VIEW);
		viewContainer.add(mapView,MAP_VIEW);
		
		add(viewContainer,BorderLayout.CENTER);
		showMainView();
	}
	
	public void refreshMainView(GameController controller) {
		mainView.refreshUI(controller);
	}
	
	public void showMainView() {
		cardLayout.show(viewContainer,MAIN_VIEW);
		refreshMainView(controller);
	}
	
	public void showMapView() {
		cardLayout.show(viewContainer, MAP_VIEW);
	}
	
}

/* 5 April 2025 - Created File
 * 
*/