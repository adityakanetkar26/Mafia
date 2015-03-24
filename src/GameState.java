import java.util.HashMap;


public class GameState {

	HashMap<String, Player> players = new HashMap<String, Player>();
	Player self;
	
	public void addPlayer(Player player){
		players.put(player.id, player);
	}
	
	public void assignPlayer(Player player){
		
	}
	
	public void playerUpdateClient(String id, String update){
		
	}
	
	public void playerUpdateServer(String id, String update){
		
	}
	
	public void stateUpdateServer(String update){
		
	}
	
	public void stateUpdateClient(String update){
		
	}
	
	public String getPlayerState(Player player){
		return getPlayerState(player, player);
	}
	
	public String getPlayerState(Player player, Player observer){
		return "dsadfsadf";
	}
	
	public String getGameState(){
		return "gdfgdf";
	}
}
