/*
Title: Island of Secrets Message Builder Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 19 March 2025
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
	public void addMessage(String message) {
		
		//Ignores empty or null messages
		if (message != null && !message.isEmpty()) {
			
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
	
	public void concatMessage(String message) {
		this.message.add(message);
	}
	
	public List<String> getMessage() {
		return this.message;
	}
}

/* 19 March 2025 - Created Class
 * 
 */
