/*
Title: Island of Secrets Map Panel
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.2
Date: 6 April 2025
Source: https://archive.org/details/island-of-secrets_202303

- Rooms not displaying after certain spot
- Just pass game engine through to Map - don't really need to deal with the state there

*/

package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Data.Constants;
import Interfaces.GameView;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import Model.GameController;
import Model.GameState;

public class MapPanel extends JPanel implements GameView {
	
	private static final long serialVersionUID = -1097043236506747632L;
	private GameController controller;
	private GameState state;
	private GamePanel panel;
	private final Map<Integer, JPanel> roomPanels = new HashMap<>();
	private final ImageCache imageCache = new ImageCache();
	private boolean isInitialised = false;
	
	public MapPanel(GameController game, GamePanel panel) {

		this.controller = game;
		this.state = game.getState();
		this.panel = panel;
		
		setLayout(new GridLayout(11,10));
	}

	@Override
	public void onViewActivated() {
		if (!isInitialised) {
			initialiseMap();
			isInitialised=true;
		}
	}
	
	@Override
	public void onViewDeactivated() {
	}
	
	@Override
	public JComponent getViewComponent() {
		return this;
	}
		
	private void initialiseMap() {
		for (int roomId = 1;roomId<110;roomId++) {
			JPanel roomPanel = createRoomPanel(roomId);
			roomPanels.put(roomId, roomPanel);
			add(roomPanel);
		}
	}
	
	private JPanel createRoomPanel(int roomId) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder((Border) createRoomBorder(roomId));
		panel.setPreferredSize(new Dimension(50,50));
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
    
    public void refreshUI(GameController controller) {
    	this.controller = controller;
    	this.state = controller.getState();
    	refreshMap();
    }
    
    public void refreshMap() {
    	this.state = controller.getState();
    	
    	for (Map.Entry<Integer, JPanel> entry: roomPanels.entrySet()) {
    		
    		int roomId = entry.getKey();
    		JPanel panel = entry.getValue();
    		    		
    		panel.removeAll();
    		
    		if (roomId<=Constants.NUMBER_OF_NOUNS) {  		
    			if (state.getRoomVisited(roomId)) {
    				updateRoomVisuals(panel,roomId,state);
    			}
    		
    			if (roomId==85) {
    				addNavigationButton(panel);
    			}
    		}
    		
    		panel.revalidate();
    		panel.repaint();
    	}
    	
    	revalidate();
    	repaint();
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
    	JButton backButton = new JButton("Back to Game");
    	//backButton.addActionListener(e -> controller.showMainView());
    	panel.add(backButton);
    }
	
    //Image cache helper
    private static class ImageCache {
    	private final Map<String,ImageIcon> cache = new HashMap<>();
    	
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
            }
            return result;
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
 */