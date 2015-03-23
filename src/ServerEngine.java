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
				message.source.sendMessage("player update$"+ newPlayer.id + "$you");
				for(Player player : players.values()){
					playerConnections.get(player).sendMessage("player update$"+ newPlayer.id);
					message.source.sendMessage("player update$"+ player.id);
				}
				players.put(Integer.toString(playerCounter), newPlayer);
				playerConnections.put(newPlayer, message.source);
				
			}
			break;
		default: 
			System.out.println("What the hell even is this");	
		}//end switch
	}
}
