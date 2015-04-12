
public class ClientEngine extends Engine{

	NetConnection serverConnection;
	
	protected void performAction(Message message){
		System.out.println("client processing message " + message.message);
		
		String[] tokens = message.message.split("\\$");
		//System.out.println(tokens[0]);
		switch(tokens[0]){
		
		case "start server":
			ServerEngine.startServer();
			ServerEngine.serverEngine.state.setGameSettings(tokens[2]);
			try {
				messages.put(new Message("connect to server$127.0.0.1$40000$" + tokens[1],null));
				messages.put(new Message("game settings$" + tokens[2], null));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
			
		case "connect to server":
			serverConnection = new NetConnection(tokens[1], Integer.parseInt(tokens[2]), messages);
			serverConnection.sendMessage("player join$"+tokens[3]);
			break;
		
		case "player join":
			Player player = new Player(Integer.parseInt(tokens[1]), tokens[2]);
			state.self = player;
			state.addPlayer(player);
			break;
			
		case "player update":
			if(message.source != serverConnection){
				serverConnection.sendMessage("player update$" + tokens[1] + "$" + tokens[2]);
			}
			else{
				state.playerUpdate(tokens[1], tokens[2]);
			}
			break;
			
		case "game update":
			state.stateUpdate(tokens[1]);
			break;
		
		case "chat":	
			if(message.source != serverConnection){
				serverConnection.sendMessage("chat$" + tokens[1] + "$" + tokens[2] + "$" + tokens[3]);
			}
			else{
				state.processChat(tokens[1], tokens[2], tokens[3]);
			}
			break;
		case "timer":
			state.timeRemaining=Integer.parseInt(tokens[1]);
			break;
		case "game settings":
			if(message.source != serverConnection){
				serverConnection.sendMessage("game settings$" + tokens[1]);
			}
			else{
				state.setGameSettings(tokens[1]);
			}
			break;
		case "spawn fake player":
			serverConnection.sendMessage("spawn fake player$" + tokens[1]);
			break;
		default: 
			System.out.println("What the hell even is this");
		
		}//end switch
	}
}
