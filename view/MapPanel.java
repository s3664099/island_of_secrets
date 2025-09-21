/*
Title: Island of Secrets Map Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.7
Date: 18 September 2025
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
import controller.GameController;
import controller.MapController;
import data.Constants;
import interfaces.GameView;
import ui.GameState;

public class MapPanel extends JPanel implements GameView {
	
	private static final long serialVersionUID = -1097043236506747632L;
	private GameController controller;
	private GameState state;
	private GamePanel panel;
	private final Map<Integer, JPanel> roomPanels = new HashMap<>();
	private final Map<Integer,String> roomToolTips = new HashMap<>();
	private final ImageCache imageCache = new ImageCache();
	private boolean isInitialised = false;
	private MapController mapController;
	
	public static final int MAP_ROWS = 11;
	public static final int MAP_COLS = 10;
	public static final int ROOM_SIZE = 50;
	public static final int SPECIAL_ROOM_ID = 85;
	public static final int GRID_SIZE =  MAP_ROWS * MAP_COLS;
	
	public MapPanel(GameController game, GamePanel panel) {

		this.controller = game;
		this.state = game.getState();
		this.panel = panel;
		this.mapController = new MapController();
		
		setLayout(new GridLayout(MAP_ROWS,MAP_COLS));
	}

	@Override
	public void onViewActivated() {
		if (!isInitialised) {
			initialiseTooltips();
			initialiseMap();
			isInitialised=true;
		}
		refreshMap();
	}
	
	@Override
	public void onViewDeactivated() {
		imageCache.clear();
	}
	
	@Override
	public JComponent getViewComponent() {
		return this;
	}
		
	private void initialiseMap() {
		for (int roomId = 1;roomId<GRID_SIZE;roomId++) {
			JPanel roomPanel = createRoomPanel(roomId);
			roomPanels.put(roomId, roomPanel);
			add(roomPanel);
		}
	}
		
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
	
    private Border createRoomBorder(int roomId) {
      
    	Border border;
    	
		if (roomId<10 && roomId>1) {
			border = BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK);
		} else if (roomId==1) {
			border = BorderFactory.createMatteBorder(2, 2, 0, 0, Color.BLACK);
		} else if (roomId==10) {
			border = BorderFactory.createMatteBorder(2, 0, 0, 2, Color.BLACK);
		} else if (roomId==71) {
			border = BorderFactory.createMatteBorder(0, 2, 2, 0, Color.BLACK);
		} else if (roomId==80) {
			border = BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK);
		} else if (roomId>71 && roomId<80) {
			border = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK);
		} else if (roomId==11 || roomId==21 || roomId==31 || roomId==41 || roomId==51 || roomId==61) {
			border = BorderFactory.createMatteBorder(0, 2, 0, 0, Color.BLACK);
		} else if (roomId==20 || roomId==30 || roomId==40 || roomId==50 || roomId==60 || roomId==70) {
			border = BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK);
		} else {
			border = BorderFactory.createEmptyBorder();
		}
	        
        return border;
    }
    
	private void initialiseTooltips() {
		for (int roomId=1;roomId <= Constants.NUMBER_OF_ROOMS;roomId++) {
			roomToolTips.put(roomId,generateRoomTooltip(roomId));
		}
	}
	
	private String generateRoomTooltip(int roomId) {
		return String.format("<html><b>%s</b></html>",state.getRoomName(roomId));
	}
    
    public void refreshUI(GameController controller) {
    	this.controller = controller;
    	this.state = controller.getState();
    	refreshMap();
    }
    
    public void refreshMap() {
    	
    	SwingUtilities.invokeLater(() -> {
    		this.state = controller.getState();
    		roomPanels.forEach(this::updateRoomPanel);
    		revalidate();
    		repaint();
    	});
    }
    
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
    
    private void updateRoomVisuals(JPanel panel,int roomId,GameState state) {
    	
    	String imageName = state.getCurrentRoom() == roomId 
    			? "adventurer"
    			: state.getRoomImageType(roomId);
    	
    	//Add Room Image
    	ImageIcon icon = imageCache.getImage("/Images/"+ imageName + ".png");
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
    
    private void addNavigationButton(JPanel panel) {
    	    	
    	JButton backButton = new JButton("Back");
    	backButton.addActionListener(new ShowMainViewButton(this.panel));
    	panel.add(backButton);
    }
	
    //Image cache helper
    private static class ImageCache {
    	private final ConcurrentHashMap<String,ImageIcon> cache = new ConcurrentHashMap<>();
    	
    	public ImageIcon getImage(String path) {
    		ImageIcon result = cache.get(path);
    		
    		if (result == null) {
    			result = createImageIcon(path);
    			cache.put(path, result);
    		}
    		
    		return result;
    	}
    	
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
        
        public void clear() {
            cache.clear();
        }
        
        private static ImageIcon createMissingImageIcon() {
            // Create a placeholder icon
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
 */