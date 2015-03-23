
public class Message {
	
	String message;
	NetConnection source;
	
	public Message(String m, NetConnection s){
		message = m;
		source = s;
	}

}
