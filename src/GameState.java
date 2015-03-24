import java.util.HashMap;


public class GameState {

	HashMap<String, Player> players = new HashMap<String, Player>();
	Player self;
	
	public void addPlayer(Player player){
		players.put(player.id, player);
	}
	
	public void assignPlayer(Player player){
		
	}
	
	public void playerUpdate(String id, String update){
		
	}
	
	public String getPlayerState(Player p){
		return "";
	}
}
