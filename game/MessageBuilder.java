/*
Title: Island of Secrets Message Builder Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import data.Constants;

import java.util.List;

/**
 * Utility class for constructing and formatting messages into fixed-length lines.
 * <p>
 * Messages longer than the configured maximum length are automatically split
 * across multiple entries at the last space before the limit. The builder can
 * also concatenate longer messages while preserving readability.
 * </p>
 *
 * <p>Instances are serializable for persistence or network transfer.</p>
 */
public class MessageBuilder implements Serializable {
	
	private static final long serialVersionUID = -6554901859226547810L;
	private List<String> messages;
	private int maxMessageLength;
	private static final Logger logger = Logger.getLogger(MessageBuilder.class.getName());
	
    /**
     * Creates a new {@code MessageBuilder} with the default maximum message length.
     */
	public MessageBuilder() {
		this.messages = new ArrayList<>();
		this.maxMessageLength = Constants.MESSAGE_LENGTH; //Default max length\
	}
	
    /**
     * Creates a new {@code MessageBuilder} initialized with a single message.
     *
     * @param message the initial message to add
     */
	public MessageBuilder(String message) {
		this.messages = new ArrayList<>();
		this.messages.add(message);
		this.maxMessageLength = Constants.MESSAGE_LENGTH; //Default max length
	}
	
    /**
     * Creates a new {@code MessageBuilder} with a custom maximum message length.
     *
     * @param maxMessageLength the maximum number of characters per message
     */
	public MessageBuilder(int maxMessageLength) {
		this.messages = new ArrayList<>();
		this.maxMessageLength = maxMessageLength;
	}
	
    /**
     * Adds a message to the builder. If the message exceeds the maximum length,
     * it is split into multiple lines at the last space before the limit.
     *
     * @param message the message to add
     * @param clear   if true, clears all existing messages before adding
     */
	public void addMessage(String message, boolean clear) {
		
		//Ignores empty or null messages
		if (message != null && !message.isEmpty()) {
			
			//Clears the message if instructed
			if (clear) {
	            logger.info("Clearing messages before adding new one.");
				clearMessages();
			}
			
			//Split the message is if exceeds the max length
			while(message.length() > maxMessageLength) {
				int lastSpace = message.lastIndexOf(" ",maxMessageLength);

				if (lastSpace == -1) {
					lastSpace = maxMessageLength;
				}
				messages.add(message.substring(0,lastSpace).trim());
				message = message.substring(lastSpace).trim();
			}
			
			if (!message.isEmpty()) {
				messages.add(message);
			}
		}
	}
	
    /**
     * Adds a long message that should be split intelligently into multiple lines.
     * Unlike {@link #addMessage(String, boolean)}, this method concatenates parts
     * unless a period is found, in which case a new line is started.
     *
     * @param message the message to add
     * @param clear   if true, clears all existing messages before adding
     */
    public void addLongMessage(String message, boolean clear) {

    	if (message != null && !message.isEmpty()) {
    		
			//Clears the message if instructed
			if (clear) {
              logger.info("Clearing messages before adding new one.");
              clearMessages();
			}
    		
			if (message.length()<=maxMessageLength) {
				messages.add(message);
			} else {
	            while (!message.isEmpty()) {
	                int splitPoint = message.lastIndexOf(' ', maxMessageLength);
	                if (splitPoint == -1) {
	                    splitPoint = message.length();
	                }
	                messages.add(message.substring(0, splitPoint).trim());
	                message = message.substring(splitPoint).trim();
	            }
			}
    	}
    }
	
    /**
     * Returns the list of formatted messages.
     *
     * @return an unmodifiable list of messages
     */
    public List<String> getMessages() {
    	return Collections.unmodifiableList(messages); 
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
 * 20 August 2025 - Updated class
 * 29 November 2025 - Fixed issue where entire inventory not displaying
 * 3 December 2025 - Increased version number
 */
