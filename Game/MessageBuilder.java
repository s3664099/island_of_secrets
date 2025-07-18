/*
Title: Island of Secrets Message Builder Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.3
Date: 14 July 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;

import Data.Constants;

public class MessageBuilder implements Serializable {
	
	private static final long serialVersionUID = -6554901859226547810L;
	private List<String> messages;
	private int maxMessageLength;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	//Standard Construction
	public MessageBuilder() {
		this.messages = new ArrayList<>();
		this.maxMessageLength = Constants.MESSAGE_LENGTH; //Default max length\
	}
	
	//Initialises with a message
	public MessageBuilder(String message) {
		this.messages = new ArrayList<>();
		this.messages.add(message);
		this.maxMessageLength = Constants.MESSAGE_LENGTH; //Default max length
	}
	
	//Initialises with a custom max length
	public MessageBuilder(int maxMessageLength) {
		this.messages = new ArrayList<>();
		this.maxMessageLength = maxMessageLength;
	}
	
	/**
     * Adds a message to the builder. If the message exceeds the maximum length,
     * it is split into multiple lines at the last space before the limit.
     */
	public void addMessage(String message, boolean clear) {
		
		//Ignores empty or null messages
		if (message != null && !message.isEmpty()) {
			
			//Clears the message if instructed
			if (clear) {
				logger.info("Clear Message");
				clearMessages();
			}
			
			//Split the message is if exceeds the max length
			while(message.length() > maxMessageLength) {
				int lastSpace = message.lastIndexOf(" ",maxMessageLength);

				if (lastSpace == -1) {
					
					//No space found, for split at max length
					lastSpace = maxMessageLength;
				}
				messages.add(message.substring(0,lastSpace).trim());
				message = message.substring(lastSpace).trim();
			}
			
			//Adds the remaining part of the message
			if (!message.isEmpty()) {
				messages.add(message);
			}
		}
	}
	
	/**
     * Adds a message that should be concatenated with the last message if possible.
     * If the last message ends with a period, the new message starts on a new line.
     * Otherwise, it is appended with a comma.
     */
    public void addLongMessage(String message, boolean clear) {

    	if (message != null && !message.isEmpty()) {
    		
			//Clears the message if instructed
			if (clear) {
				clearMessages();
			}
    		
			if (message.length()<=maxMessageLength) {
				messages.add(message);
			} else {
								
				while(message.length()>0) {

					int messageEnd = maxMessageLength;
					if (messageEnd>message.length()) {
						messages.add(message);
						message = "";
					} else {
					
						boolean foundBlank = false;
						while (!foundBlank) {
							if (message.charAt(messageEnd)==' ') {
								messages.add(message.substring(0, messageEnd));
								message = (message.substring(messageEnd));
								foundBlank = true;
							} else {
								messageEnd --;
							}
						}
					}
				}
			}
    	}    	
    }
	
    /**
     * Returns the list of formatted messages.
     */
    public List<String> getMessages() {
        return messages; // Return a copy to prevent external modification
    }

    /**
     * Clears all messages from the builder.
     */
    public void clearMessages() {
        messages.clear();
    }
}

/* 19 March 2025 - Created Class
 * 20 March 2025 - Completed class based on recommendations.
 * 				   Add clear functionality inside message
 * 7 July 2025 - Added serializable
 * 14 July 2025 - Change way long message works for long strings
 */
