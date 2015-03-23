
public class ClientEngine extends Engine{

	
	private void performAction(Message message){
		System.out.println("client processing message " + message.message);
		
		String[] tokens = message.message.split("$");
		
		switch(tokens[0]){
		
		case "start server":
			ServerEngine.startServer();
			break;
		case "connect to server":
			break;
		case "player update":
			break;
		default: 
			System.out.println("What the hell even is this");
		
		}//end switch
	}
}
