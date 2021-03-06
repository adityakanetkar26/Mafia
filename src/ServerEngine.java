import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerEngine extends Engine{

	static ServerEngine serverEngine;
	
	ServerListener serverListener = new ServerListener(messages);
	HashMap<Player, NetConnection> playerConnections = new HashMap<Player, NetConnection>();
	int playerCounter = 0;
	
	public static void startServer(){
		if(serverEngine == null){
			serverEngine = new ServerEngine();
			serverEngine.state.messages = serverEngine.messages;
			serverEngine.state.gamePhase = "awaiting players";
			serverEngine.start();
		}		
	}
	
	protected void performAction(Message message){
		System.out.println("server processing message " + message.message);
		if(message.toLog){
			state.record(message.message);
		}
		
		String[] tokens = message.message.split("\\$");
		
		switch(tokens[0]){
		
		case "player join":
			playerJoin(message, tokens[1]);
			break;
			
		case "player update":
			state.playerUpdate(tokens[1], tokens[2]);
			propagatePlayerUpdate(tokens[1], tokens[2]);
			break;
			
		case "game update":
			state.stateUpdate(tokens[1]);
			propagateGameUpdate(tokens[1]);
			break;
		
		case "game transition":
			try {
				state.gameTransition(tokens[1]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
			
		case "start game":
			state.assignIdentities();
			for(Player player : state.players.values()){
				state.record(player.id + " assigned to " + player.role + "  (forced log entry)");
				propagatePlayerUpdate(Integer.toString(player.id), state.getPlayerState(player));
			}
			state.announceSettings();
			messages.add(new Message("game transition$startnight", null));
			break;
		case "chat":	
			for(NetConnection net : playerConnections.values()){
				net.sendMessage("chat$" + tokens[1] + "$" + tokens[2] + "$" + tokens[3]);
			}
			break;
		case "timer":
			for(NetConnection net : playerConnections.values()){
				net.sendMessage("timer$" + tokens[1]);
			}
			break;
		case "game settings":
			state.setGameSettings(tokens[1]);
			propagateGameSettings();
			break;
		case "spawn fake player":
			spawnFakePlayer(tokens[1]);
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
	
	private void propagateGameSettings(){
		for(NetConnection net : playerConnections.values()){
			net.sendMessage("game settings$" + state.getGameSettings());
		}
	}
	
	private void playerJoin(Message message, String name){
		if(!playerConnections.containsValue(message.source)){
			playerCounter++;
			Player newPlayer = new Player(playerCounter, name);
			state.addPlayer(newPlayer);
			playerConnections.put(newPlayer, message.source);
			message.source.sendMessage("player join$"+ newPlayer.id + "$" + name);
			
			for(Player player : state.players.values()){
				propagatePlayerUpdate(Integer.toString(player.id), state.getPlayerState(player));
			}
			propagateGameUpdate(state.getGameState());
			propagateGameSettings();
		}
	}
	
	private void spawnFakePlayer(String name){
		playerCounter++;
		Player newPlayer = new Player(playerCounter, name);
		state.addPlayer(newPlayer);
		for(Player player : state.players.values()){
			propagatePlayerUpdate(Integer.toString(player.id), state.getPlayerState(player));
		}
	}
}
