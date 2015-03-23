
public class ClientEngine extends Engine{

	NetConnection serverConnection;
	Player self;
	
	protected void performAction(Message message){
		System.out.println("client processing message " + message.message);
		
		String[] tokens = message.message.split("\\$");
		//System.out.println(tokens[0]);
		switch(tokens[0]){
		
		case "start server":
			ServerEngine.startServer();
			break;
		case "connect to server":
			serverConnection = new NetConnection(tokens[1], Integer.parseInt(tokens[2]), messages);
			serverConnection.sendMessage("player join");
			break;
		case "player update":
			break;
		default: 
			System.out.println("What the hell even is this");
		
		}//end switch
	}
}
