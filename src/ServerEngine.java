import java.util.HashMap;


public class ServerEngine extends Engine{

	static ServerEngine serverEngine;
	
	ServerListener serverListener = new ServerListener(messages);
	HashMap<Player, NetConnection> playerConnections = new HashMap<Player, NetConnection>();
	int playerCounter = 0;
	
	public static void startServer(){
		if(serverEngine == null){
			serverEngine = new ServerEngine();
			serverEngine.state.gamePhase = "awaiting players";
			serverEngine.start();
		}		
	}
	
	protected void performAction(Message message){
		System.out.println("server processing message " + message.message);
		
		String[] tokens = message.message.split("\\$");
		
		switch(tokens[0]){
		
		case "player join":
			playerJoin(message);
			break;
			
		case "player update":
			state.playerUpdate(tokens[1], tokens[2]);
			propagatePlayerUpdate(tokens[1], tokens[2]);
			break;
			
		case "game update":
			state.stateUpdateServer(tokens[1]);
			propagateGameUpdate(tokens[1]);
			break;
			
		default: 
			System.out.println("What the hell even is this");	
		}//end switch
	}
	
	private void propagatePlayerUpdate(String id, String update){
		for(NetConnection net : playerConnections.values()){
			net.sendMessage("player update$" + id + "$" + update);
		}
	}
	
	private void propagateGameUpdate(String update){
		for(NetConnection net : playerConnections.values()){
			net.sendMessage("game update$" + update);
		}
	}
	
	private void playerJoin(Message message){
		if(!playerConnections.containsValue(message.source)){
			playerCounter++;
			Player newPlayer = new Player(playerCounter, Integer.toString(playerCounter));
			state.addPlayer(newPlayer);
			playerConnections.put(newPlayer, message.source);
			message.source.sendMessage("player join$"+ newPlayer.id);
			//state.assignPlayer(newPlayer);
			
			if(state.players.size() > 4){
				state.assignIdentities();
			}
			
			for(Player player : state.players.values()){
				propagatePlayerUpdate(Integer.toString(player.id), state.getPlayerState(player));
			}
			propagateGameUpdate(state.getGameState());
			
		}
	}
}
