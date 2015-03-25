import java.util.HashMap;
import java.util.Random;
import java.lang.Math;


public class GameState {

	HashMap<int, Player> players = new HashMap<int, Player>();
	int badPlayerCount;
	int goodPlayerCount;
	
	Player self;
	
	public void addPlayer(Player player){
		players.put(player.id, player);
	}
	
	public void assignIdentities(){
		int totalSize = players.size();
		badPlayerCount = int(Math.sqrt(double(totalSize)));
		goodPlayerCount = totalSize - badPlayerCount;

		Random randomGenerator = new Random();
		
		int remainingAssign = badPlayerCount;
		while(remainingAssign != 0){
			int randomInt = randomGenerator.nextInt(badPlayerCount);
			Player p = players.get(randomInt);
			boolean playerType = p.isGoodOrBad();

			if(playerType == true){
	
				assignPlayer(p);
				remainingAssign--;
			}
		}
		
	}
	public void assignPlayer(Player player){
		player.setBad();
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
