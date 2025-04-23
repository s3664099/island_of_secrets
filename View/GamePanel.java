/*
Title: Island of Secrets Game Pabel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 15 April 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import UISupport.GameController;

/**
 * Main container panel that manages game views using CardLayout.
 * Handles transitions between the main game view and map view.
 */
public class GamePanel extends JPanel {
	
    private static final long serialVersionUID = -1175236419449166126L;
    
    //View Constants
	private static final String MAIN_VIEW = "MAIN";
	private static final String MAP_VIEW = "MAP";

	//UI Components
	private final CardLayout cardLayout = new CardLayout();
    private final JPanel viewContainer = new JPanel(cardLayout);
    private MainGamePanel mainView;
    private MapPanel mapView;
	
	public GamePanel(GameController controller) {
		Objects.requireNonNull(controller, "GameController cannot be null");
		initialiseUI(controller);
	}
	
	private void initialiseUI(GameController controller) {
		
		setLayout(new BorderLayout());
		
		//Create views
		this.mainView = new MainGamePanel(controller,this);
		this.mapView = new MapPanel(controller,this);
		
		//Configure view container
		viewContainer.add(mainView,MAIN_VIEW);
		viewContainer.add(mapView,MAP_VIEW);
		add(viewContainer,BorderLayout.CENTER);

		//Initial view
		showMainView();
	}
	
    /**
     * Refreshes the main game view
     * @param controller The game controller providing current state
     */
	public void refreshMainView(GameController controller) {
		 SwingUtilities.invokeLater(() -> mainView.refreshUI(controller));
	}
	
    /**
     * Refreshes the map view
     * @param controller The game controller providing current state
     */
	public void refreshMapView(GameController controller) {
		SwingUtilities.invokeLater(() -> mapView.refreshUI(controller));
	}
	
    /**
     * Shows the main game view and triggers activation
     */
	public void showMainView() {
		cardLayout.show(viewContainer,MAIN_VIEW);
		mainView.onViewActivated();
	}
	
    /**
     * Shows the map view and triggers activation
     */
	public void showMapView() {
		cardLayout.show(viewContainer, MAP_VIEW);
		mapView.onViewActivated();
	}
	
    /**
     * Gets the current active view name
     * @return Either MAIN_VIEW or MAP_VIEW constant
     */
	public String getCurrentView() {
		
	    String currentView = MAIN_VIEW;
	    boolean found = false;
	    
	    for (Component comp : viewContainer.getComponents()) {
	        if (!found && comp.isVisible()) {
	            currentView = (comp == mainView) ? MAIN_VIEW : MAP_VIEW;
	            found = true;
	        }
	    }
	    return currentView;
	}
}

/* 5 April 2025 - Created File
 * 6 April 2025 - Fixed double display for main view. Fixed issue with map not displaying
 * 15 April 2025 - Update class based on DeepSeek Recommendations
*/