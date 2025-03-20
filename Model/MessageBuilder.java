/*
Title: Island of Secrets Message Builder Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.1
Date: 20 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.ArrayList;
import java.util.List;

import Data.Constants;

public class MessageBuilder {
	
	private List<String> messages;
	private int maxMessageLength;
	
	//Standard Construction
	public MessageBuilder() {
		this.messages = new ArrayList<>();
		this.maxMessageLength = Constants.MESSAGE_LENGTH; //Default max length
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
    		
    		if (messages.isEmpty()) {
    		
    			//First message, add it directly
    			messages.add(message);
    		} else {
    			
    			//Get last message
    			String lastMessage = messages.get(messages.size()-1);
    			
    			//Determine how to concatenate
    			if (lastMessage.endsWith(".")) {
    				
    				//Start new sentance
    				lastMessage += " " + message; 
    			} else {
    				
    				//Append the current sentance
    				lastMessage += ", " + message;
    			}
    			
    			//Check if the concatenated message exceeds the maximum length
    			if (lastMessage.length() <= maxMessageLength) {
    				
    				//Update the last message
    				messages.set(messages.size()-1, lastMessage);
    			} else {
    				
    				//Split the concatenated message
    				messages.remove(messages.size()-1);
    				addMessage(lastMessage,false);
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
 */
