/*
Title: Island of Secrets Map Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import controller.ShowMainViewButton;
import controller.MapController;
import data.Constants;
import interfaces.GameView;
import ui.GameController;
import ui.GameState;

/**
 * A JPanel that displays a grid-based map of the game world.
 * Each room is represented as a panel that can display an image and a tooltip.
 * Supports dynamic updates of room visuals based on the current game state.
 * Implements the {@link GameView} interface for integration with the game's view system.
 */
public class MapPanel extends JPanel implements GameView {
	
	private static final long serialVersionUID = -1097043236506747632L;
	
    /** The game controller providing game state and logic. */
	private GameController controller;
	
    /** The current game state used to determine room visuals. */
	private GameState state;
	
    /** Reference to the parent game panel for navigation actions. */
	private GamePanel panel;
	
    /** Maps room IDs to their corresponding JPanel components. */
	private final Map<Integer, JPanel> roomPanels = new HashMap<>();
	
    /** Maps room IDs to tooltip text for each room. */
	private final Map<Integer,String> roomToolTips = new HashMap<>();
	
    /** Caches images used for rooms and the player icon to improve performance. */
	private final ImageCache imageCache = new ImageCache();
	
    /** Flag indicating whether the map has been initialized. */
	private boolean isInitialised = false;
	
    /** Controller for mouse interaction with rooms. */
	private MapController mapController;
	
    /** Number of rows in the map grid. */
	public static final int MAP_ROWS = 11;
	
    /** Number of columns in the map grid. */
	public static final int MAP_COLS = 10;
	
    /** Size in pixels of each room panel. */
	public static final int ROOM_SIZE = 50;
	
    /** Room ID that displays the navigation button. */
	public static final int SPECIAL_ROOM_ID = 85;
	
    /** Total number of cells in the map grid. */
	public static final int GRID_SIZE =  MAP_ROWS * MAP_COLS;
	
    /** Top-left room ID used for border calculations. */
	public static final int BORDER_TOP_LEFT = 1;
	
    /** Top-right room ID used for border calculations. */
	public static final int BORDER_TOP_RIGHT = 10;
	
    /** Bottom-left room ID used for border calculations. */
	public static final int BORDER_BOTTOM_LEFT = 71;
	
    /** Bottom-right room ID used for border calculations. */
	public static final int BORDER_BOTTOM_RIGHT = 80;
	
    /** Left room ID used for border calculations. */
	public static final int BORDER_LEFT = 1;
	
    /** Right room ID used for border calculations. */
	public static final int BORDER_RIGHT = 0;
	
    /** Base path for image resources. */
	public static final String PATH = "/images/";
	
    /** File extension for image resources. */
	public static final String FILE_TYPE = ".png";
	
    /**
     * Constructs a MapPanel associated with a game controller and parent panel.
     *
     * @param game  the GameController providing state and game logic
     * @param panel the parent GamePanel for navigation actions
     */
	public MapPanel(GameController game, GamePanel panel) {

		this.controller = game;
		this.state = game.getState();
		this.panel = panel;
		this.mapController = new MapController();
		
		setLayout(new GridLayout(MAP_ROWS,MAP_COLS));
	}

    /**
     * Called when the map view is activated.
     * Initializes the map and tooltips on the first activation and refreshes the map.
     */
	@Override
	public void onViewActivated() {
		if (!isInitialised) {
			initialiseTooltips();
			initialiseMap();
			isInitialised=true;
		}
		refreshMap();
	}
	
	/**
     * Called when the map view is deactivated.
     * Clears cached images to free memory.
     */
	@Override
	public void onViewDeactivated() {
		imageCache.clear();
	}
	
    /**
     * Returns the Swing component representing this view.
     *
     * @return this MapPanel as a JComponent
     */
	@Override
	public JComponent getViewComponent() {
		return this;
	}
	
    /** Initializes the map grid by creating room panels and adding them to the layout. */
	private void initialiseMap() {
		for (int roomId = 1;roomId<GRID_SIZE;roomId++) {
			JPanel roomPanel = createRoomPanel(roomId);
			roomPanels.put(roomId, roomPanel);
			add(roomPanel);
		}
	}
	
    /**
     * Creates a JPanel representing a single room.
     * Adds mouse interaction and sets initial borders and tooltips.
     *
     * @param roomId the ID of the room to create
     * @return a JPanel representing the room
     */
	private JPanel createRoomPanel(int roomId) {
		
		JPanel panel = new JPanel(new BorderLayout()) {
			
			private static final long serialVersionUID = -8288954048757686323L;

			@Override
			public JToolTip createToolTip() {
				JToolTip tip = super.createToolTip();
				tip.setBackground(new Color(255,255,225)); //Light Yellow
				tip.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				return tip;
			}
		};
		
		panel.addMouseListener(mapController.createMouseAdapter(roomId));
		panel.setToolTipText(roomToolTips.get(roomId));
		panel.setBorder((Border) createRoomBorder(roomId));
		panel.setPreferredSize(new Dimension(ROOM_SIZE,ROOM_SIZE));
		
		return panel;
	}
	
    /**
     * Determines and creates the border for a room panel based on its position in the grid.
     *
     * @param roomId the ID of the room
     * @return a Border object for the room
     */
    private Border createRoomBorder(int roomId) {
      
    	Border border;
    	
		if (roomId<BORDER_TOP_RIGHT && roomId>BORDER_TOP_LEFT) {
			border = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK);
		} else if (roomId==BORDER_TOP_LEFT) {
			border = BorderFactory.createMatteBorder(2, 2, 0, 0, Color.BLACK);
		} else if (roomId==BORDER_TOP_RIGHT) {
			border = BorderFactory.createMatteBorder(2, 0, 0, 2, Color.BLACK);
		} else if (roomId==BORDER_BOTTOM_LEFT) {
			border = BorderFactory.createMatteBorder(0, 2, 2, 0, Color.BLACK);
		} else if (roomId==BORDER_BOTTOM_RIGHT) {
			border = BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK);
		} else if (roomId>BORDER_BOTTOM_LEFT && roomId<BORDER_BOTTOM_RIGHT) {
			border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);
		} else if (roomId%MAP_COLS==1 && roomId<=80) {
			border = BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK);
		} else if (roomId%MAP_COLS==0 && roomId<=80) {
			border = BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK);
		} else {
			border = BorderFactory.createEmptyBorder();
		}	        
        return border;
    }
    
    /** Initializes tooltip text for all rooms based on their names in the current state. */
	private void initialiseTooltips() {
		for (int roomId=1;roomId <= Constants.NUMBER_OF_ROOMS;roomId++) {
			roomToolTips.put(roomId,generateRoomTooltip(roomId));
		}
	}
	
    /**
     * Generates HTML-formatted tooltip text for a room.
     *
     * @param roomId the room ID
     * @return the tooltip text
     */
	private String generateRoomTooltip(int roomId) {
		return String.format("<html><b>%s</b></html>",state.getRoomName(roomId));
	}
    
    /**
     * Updates the controller and state, then refreshes the map UI.
     *
     * @param controller the new GameController
     */
    public void refreshUI(GameController controller) {
    	this.controller = controller;
    	this.state = controller.getState();
    	refreshMap();
    }
    
    /**
     * Refreshes the map visuals for all rooms on the Event Dispatch Thread.
     */
    public void refreshMap() {
    	
    	SwingUtilities.invokeLater(() -> {
    		this.state = controller.getState();
    		roomPanels.forEach(this::updateRoomPanel);
    		revalidate();
    		repaint();
    	});
    }
    
    /**
     * Updates the visuals and tooltip for a single room panel.
     *
     * @param roomId the ID of the room
     * @param panel  the JPanel representing the room
     */
    private void updateRoomPanel(int roomId,JPanel panel) {
    	panel.removeAll();
    	
    	if (roomId<= Constants.NUMBER_OF_ROOMS) {
    		if(state.getRoomVisited(roomId)) {
    			updateRoomVisuals(panel,roomId,state);
    			panel.setToolTipText(roomToolTips.get(roomId));
    		} else {
    			panel.setToolTipText(null);
    		}
    	}
    	
    	if (roomId == SPECIAL_ROOM_ID) {
    		addNavigationButton(panel);
    	}
    	panel.revalidate();
		panel.repaint();
    }
    
    /**
     * Updates the visual representation of a room, including its image and border.
     *
     * @param panel  the room panel
     * @param roomId the room ID
     * @param state  the current game state
     */
    private void updateRoomVisuals(JPanel panel,int roomId,GameState state) {
    	
    	String imageName = state.getCurrentRoom() == roomId 
    			? "adventurer"
    			: state.getRoomImageType(roomId);
    	
    	//Add Room Image
    	ImageIcon icon = imageCache.getImage(PATH + imageName + FILE_TYPE);
    	if (icon != null) {
    		panel.add(new JLabel(icon), BorderLayout.CENTER);
    	}
    	
    	//Update borders based on exits
    	boolean[] exits = state.getRoomExits(roomId);
    	panel.setBorder(BorderFactory.createMatteBorder(
    			exits[0] ? 0:2, //North
    			exits[3] ? 0:2, //West
    			exits[1] ? 0:2, //South
    			exits[2] ? 0:2, //East
    			Color.BLACK
    	));
    }
    
    /** Adds a "Back" navigation button to a special room panel. */
    private void addNavigationButton(JPanel panel) {
    	    	
    	JButton backButton = new JButton("Back");
    	backButton.addActionListener(new ShowMainViewButton(this.panel));
    	panel.add(backButton);
    }
	
    /**
     * Caches images to reduce redundant loading.
     * Loads images from resources and scales them for display.
     */
    private static class ImageCache {
    	
        /** Thread-safe cache mapping file paths to ImageIcon objects. */
    	private final ConcurrentHashMap<String,ImageIcon> cache = new ConcurrentHashMap<>();
    	
        /**
         * Returns a cached image or loads it if missing.
         *
         * @param path the resource path for the image
         * @return the ImageIcon corresponding to the path
         */
    	public ImageIcon getImage(String path) {
    		ImageIcon result = cache.get(path);

    		if (result == null) {
    			result = createImageIcon(path);
    			cache.put(path, result);
    		}
    		return result;
    	}
    	
        /**
         * Loads an image from resources and scales it.
         * Returns a placeholder if the image is missing.
         *
         * @param path the resource path
         * @return a scaled ImageIcon
         */
        private ImageIcon createImageIcon(String path) {

            ImageIcon result = null;
            try {
                InputStream stream = getClass().getResourceAsStream(path);
                if (stream != null) {
                    BufferedImage img = ImageIO.read(stream);
                    if (img != null) {
                        result = new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                    }
                    stream.close();
                }
            } catch (IOException e) {
                System.err.println("Couldn't load image: " + path);
                result = createMissingImageIcon();
            }
            return result;
        }
        
        /** Clears the image cache. */
        public void clear() {
            cache.clear();
        }
        
        /**
         * Creates a placeholder red square image to use when an image cannot be loaded.
         *
         * @return a missing ImageIcon
         */
        private static ImageIcon createMissingImageIcon() {
            BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.RED);
            g.fillRect(0, 0, 50, 50);
            g.dispose();
            return new ImageIcon(img);
        }
    }
}

/*
 * 2 February 2025 - Created File
 * 4 February 2025 - Added Borders to the map panel.
 * 5 February 2025 - Added code to only set label when room entered.
 * 9 February 2025 - Retrieve and add room type to the panel
 * 10 February 2025 - Added the images to the map. Added the walls
 * 22 February 2025 - Added image for player
 * 5 March 2025 - Increased to v4.0
 * 5 April 2025 - Updated code based on Deepseek recommendations
 * 6 April 2025 - Fixed issue where map not displaying
 * 7 April 2025 - Fixed problem where not all rooms being displayed.
 * 				- Button to return player to game now works.
 * 20 April 2025 - Update class based on recommendations
 * 				 - Added tool tips to display location name when hovering
 * 21 April 2025 - Moved MapController to a separate class in  Controller
 * 27 July 2025 - Updated game button to remove code not used.
 * 18 September 2025 - Removed game and panel from Map Controller
 * 26 September 2025 - 
 * 3 December 2025 - Increased version number
 */