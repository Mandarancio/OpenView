package core;

/***
 * Basic message class
 * 
 * @author martino
 * 
 */
public class Message {
	/***
	 * Message Type enum
	 * 
	 * @author martino
	 * 
	 */
	public enum MessageType {
		WARNING, ERROR, INFO
	};

	/***
	 * Message to pass
	 */
	private String message_;
	/***
	 * Message Tyope
	 */
	private MessageType type_;

	/***
	 * Initialize the object
	 * 
	 * @param msg
	 *            message to pass
	 * @param type
	 *            type of message
	 */
	public Message(String msg, MessageType type) {
		message_ = (msg);
		type_ = type;
	}

	/***
	 * Get the text of the message
	 * 
	 * @return the text of the message
	 */
	public String getMessage() {
		return message_;
	}

	/***
	 * Get the type of message
	 * 
	 * @return the type of message
	 */
	public MessageType getType() {
		return type_;
	}
}
