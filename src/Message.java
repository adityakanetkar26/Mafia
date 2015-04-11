
public class Message {
	
	String message;
	NetConnection source;
	boolean toLog = true;
	
	public Message(String m, NetConnection s){
		message = m;
		source = s;
	}
	
	public Message(String m, NetConnection s, boolean b){
		message = m;
		source = s;
		toLog = b;
	}

}
