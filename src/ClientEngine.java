
public class ClientEngine extends Engine{

	NetConnection serverConnection;
	
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
		
		case "player join":
			Player player = new Player(Integer.parseInt(tokens[1]), tokens[1]);
			state.self = player;
			state.addPlayer(player);
			break;
			
		case "player update":
			state.playerUpdateClient(tokens[1], tokens[2]);
			if(message.source != serverConnection){
				serverConnection.sendMessage("player update$" + state.self.id + "$" + state.getPlayerState(state.self));
			}
			break;
			
		case "game update":
			state.stateUpdateClient(tokens[1]);
			break;
			
		default: 
			System.out.println("What the hell even is this");
		
		}//end switch
	}
}
