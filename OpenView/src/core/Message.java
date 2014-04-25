package core;

public class Message {
	public enum MessageType{
		WARNING,ERROR,INFO
	};
	private String message_;
	private MessageType type_;
	
	public Message(String msg,MessageType type){
		message_=(msg);
		type_=type;
	}
	public String getMessage() {
		return message_;
	}
	public MessageType getType() {
		return type_;
	}
}
