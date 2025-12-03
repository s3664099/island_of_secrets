/*
Title: Island of Secrets Game Frame
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import javax.swing.JFrame;

import game.GameEngine;
import interfaces.GameUI;
import ui.GameController;

/**
 * The {@code GameFrame} class provides the main application window for the
 * <em>Island of Secrets</em> game. It extends {@link javax.swing.JFrame} and
 * implements the {@link GameUI} interface, serving as the top-level container
 * for all game views.
 *
 * <p>The frame hosts a single {@link GamePanel} instance, which displays and
 * updates the various game screens. It also coordinates with the
 * {@link GameController} to refresh the UI based on current game state.
 */
public class GameFrame extends JFrame implements GameUI {

	private static final long serialVersionUID = -5095376582483866399L;
	
    /** The main panel that renders the game interface. */
	private final GamePanel gamePanel;
	
    /** X-coordinate for the initial frame position. */
	private final int X_BOUND = 100;
	
    /** Y-coordinate for the initial frame position. */
	private final int Y_BOUND = 100;
	
    /** Width of the frame in pixels. */
	private final int BOUND_WIDTH = 800;
	
    /** Height of the frame in pixels. */
	private final int BOUND_HEIGHT = 600;
	
    /**
     * Constructs a new {@code GameFrame} with the specified game engine.
     * <p>
     * A {@link GameController} is created and linked to this frame and a
     * {@link GamePanel}. The UI is initialized and made visible.
     *
     * @param engine the {@link GameEngine} used to drive the game logic;
     *               must not be {@code null}
     */
	public GameFrame(GameEngine engine) {
		super("Island of Secrets");
		GameController controller = new GameController(engine,this);
		this.gamePanel = new GamePanel(controller);
		initialiseUI();
	}
	
    /**
     * Initializes the frame’s user interface settings, including default
     * close operation, content pane, and window configuration.
     */
	private void initialiseUI() {

		//kills the window when the 'x' is clicked at the top
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(gamePanel);
		configureWindow();
		setVisible(true);
	}
	
    /**
     * Configures window properties such as size, position, and resizability.
     * Uses the constant bounds defined in this class and centers the window
     * on the screen.
     */
	public void configureWindow()  {
		
		//sets the boundaries of the frame.
		setBounds(X_BOUND,Y_BOUND,BOUND_WIDTH,BOUND_HEIGHT);
		setResizable(false);
		
		//Center
		setLocationRelativeTo(null);
	}

    /**
     * Refreshes the UI to reflect the current state of the game.
     * <p>
     * Delegates to the appropriate {@link GamePanel} method depending on
     * whether the controller is in message view, lightning view, or the
     * default main view.
     *
     * @param game the current {@link GameController} providing game state;
     *             must not be {@code null}
     */
	@Override
	public void refreshUI(GameController game) {
		
		if (game.isMessageState()) {
			gamePanel.refreshMessageView(game);
		} else {
			gamePanel.refreshMainView(game);
		}
	}

    /**
     * Displays the map view of the game.
     * <p>
     * Currently not implemented.
     *
     * @param game the {@link GameController} used for map data
     */
	@Override
	public void showMapView(GameController game) {}
	
    /**
     * Closes and disposes of the game window, releasing all associated
     * resources.
     */
	public void closeUI() {
		setVisible(false);
		dispose();
	}
}
/* 8 November 2024 - Created File
 * 23 December 2024 - Passed frame to panel to enable quit function
 * 					- Updated to version 2.
 * 31 January 2025 - Completed Testing and increased version
 * 2 February 2025 - Added generated serial ID
 * 3 March 2025 - Added call to focus on command line
 * 5 March 2025 - Increased to v4.0
 * 29 March 2025 - Hid set command field
 * 4 April 2025 - Updated frame to build map panel
 * 8 April 2025 - Removed unusued code
 * 26 July 2025 - Added check to display messages
 * 15 August 2025 - Added check for lightning panel
 * 22 September 2025 - Updated and added JavaDocs
 * 23 November 2025 - Removed Lightning Panel
 * 3 December 2025 - Increased version number
 */