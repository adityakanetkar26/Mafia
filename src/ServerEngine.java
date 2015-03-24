import java.util.HashMap;


public class ServerEngine extends Engine{

	static ServerEngine serverEngine;
	
	ServerListener serverListener = new ServerListener(messages);
	HashMap<Player, NetConnection> playerConnections = new HashMap<Player, NetConnection>();
	int playerCounter = 0;
	
	public static void startServer(){
		if(serverEngine == null){
			serverEngine = new ServerEngine();
			serverEngine.start();
		}		
	}
	
	protected void performAction(Message message){
		System.out.println("server processing message " + message.message);
		
		String[] tokens = message.message.split("\\$");
		
		switch(tokens[0]){
		
		case "player join":
			if(!playerConnections.containsValue(message.source)){
				playerCounter++;
				Player newPlayer = new Player(Integer.toString(playerCounter));
				state.addPlayer(newPlayer);
				playerConnections.put(newPlayer, message.source);
				message.source.sendMessage("player join$"+ newPlayer.id);
				state.assignPlayer(newPlayer);
				
				for(Player player : state.players.values()){
					propagatePlayerUpdate(player);
				}
				
			}
			break;
			
		case "player update":
			state.playerUpdate(tokens[1], tokens[2]);
			propagatePlayerUpdate(state.players.get(tokens[1]));
			break;
			
		case "game update":
			
			break;
			
		default: 
			System.out.println("What the hell even is this");	
		}//end switch
	}
	
	private void propagatePlayerUpdate(Player player){
		
	}
	
	private void propagateGameUpdate(){
		
	}
}
