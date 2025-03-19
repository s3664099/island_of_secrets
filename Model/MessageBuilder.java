/*
Title: Island of Secrets Message Builder Class
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 19 March 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Model;

import java.util.List;

import Data.Constants;

public class MessageBuilder {
	
	private List<String> message;
	private int maxMessageLength;
	
	public MessageBuilder() {
		
	}
	
	public MessageBuilder(String message) {
		this.message.add(message);
	}
	
	public void addLongMessage(String message) {
		
		//Is this the first message?
		if (!displayMessage) {
			
			//Clears and adds
			displayMessage = true;
			this.message.clear();
			this.message.add(message);

		} else {
			
			//Gets the last one and adds new message to the end
			int listSize = this.message.size();
			String lastLine = this.message.get(listSize);
			
			//Checks how the last line ends
			if (lastLine.endsWith(".")) {
				lastLine = String.format("%s %s", lastLine, message);
			} else {
				lastLine = String.format("%s, %s", lastLine, message);
			}
			
			lastLine += message;
			lastLine = lastLine.trim();
			
			//Checks if message to long
			if (lastLine.length()>Constants.MESSAGE_LENGTH) {
				
				//Splits the message at the last space, and adds last two message to the end
				int lastSpace = lastLine.lastIndexOf(" ");
				lastLine = lastLine.substring(lastSpace, lastLine.length());
				String oldLastLine = this.message.get(listSize).substring(0,lastSpace);
				this.message.remove(lastLine);
				this.message.add(oldLastLine);
				this.message.add(lastLine);

			} else {

				//Concat's the message and replaces it with the last one.
				message  = this.message.get(listSize)+message;
				this.message.remove(listSize);
				this.message.add(message);
			}
		}
	}
	
	public void addMessage(String message) {
		this.message.add(message);
	}
	
	public List<String> getMessage() {
		return this.message;
	}
}

/* 19 March 2025 - Created Class
 * 
 */
