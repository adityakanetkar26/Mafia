import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameState {

	HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	int badPlayerCount;
	int goodPlayerCount;
	
	Player self;
	
	String gamePhase = "not in game";
	
	public void addPlayer(Player player){
		players.put(player.id, player);
	}
	
	public void assignIdentities(){
		gamePhase = "night";
		int totalSize = players.size();
		badPlayerCount = (int)Math.sqrt((double)totalSize);
		goodPlayerCount = totalSize - badPlayerCount;

		ArrayList<Player> unassignedPeople = new ArrayList<Player>();
		for(Player player : players.values()){
			player.role = "good";
			unassignedPeople.add(player);
		}
		
		Random randomGenerator = new Random();
		
		int remainingAssign = badPlayerCount;
		while(remainingAssign != 0){
			int randomInt = randomGenerator.nextInt(unassignedPeople.size());
			Player p = unassignedPeople.remove(randomInt);
			p.role = "bad";
			remainingAssign--;
		}
		
	}
	public void assignPlayer(Player player){
		//player.setBad();
	}
	
	public void playerUpdate(String i, String update){
		int id = Integer.parseInt(i);
		if(!players.containsKey(id)){
			players.put(id, new Player(id, i));
		}
		Player player = players.get(id);
		String[] updates = update.split("#");
		for(int a = 0; a < updates.length; a = a + 2){
			String field = updates[a];
			String value = updates[a+1];
			
			switch(field){
			case "role":
				player.role = value;
				break;
			case "alive":
				player.aliveDead = (value.equals("alive"));
				break;
			case "vote":
				
				break;
			default:
				System.out.println("weird update..");
			}
		}
	}
	
	public void stateUpdateServer(String update){
		gamePhase = update;
	}
	
	public void stateUpdateClient(String update){
		gamePhase = update;
	}
	
	public String getPlayerState(Player player){
		return "role#" + player.role + "#alive#" + (player.aliveDead ? "alive" : "dead");
	}
	
	public String getGameState(){
//		if(badPlayerCount == 0)
//			return "Good Guys Win";
//		else if(goodPlayerCount == 0)
//			return "Bad Guys Win";
//
//		return "Game Still On! Good Luck!";
		return gamePhase;
	}

}
