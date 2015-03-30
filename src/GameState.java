import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import sun.misc.Queue;

public class GameState {

	HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	Queue<String> chatMessages = new Queue<String>();
	
	int badPlayerCount;
	int goodPlayerCount;
	
	Player self;
	
	String gamePhase = "not in game";
	
	public void addPlayer(Player player){
		players.put(player.id, player);
	}
	
	public void assignIdentities(){
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
				player.votingAgainst = value;
				break;
			case "chat":
				chatMessages.enqueue(value);
				break;
			default:
				System.out.println("weird update..");
			}
		}
	}
	
	public void stateUpdateServer(String update){
		gamePhase = update;
		if(gamePhase.equals("night")){
			//start a timer
			//allow bad chat
		}
		else if(gamePhase.equals("day")){
			//check end condition
			//start a timer
			//allow voting
			//allow chat
		}
		
	}
	
	public void stateUpdateClient(String update){
		gamePhase = update;
	}
	
	public String getPlayerState(Player player){
		return "role#" + player.role + "#alive#" + (player.aliveDead ? "alive" : "dead");
	}
	
	public String getGameState(){
		return gamePhase;
	}
	
	public void collectAndProcessVotes(){
		Integer[] votes = {0};
		int i = 0;
		for(Player player : players.values()){
			String vote = player.getVote();
			votes[i] = Integer.parseInt(vote);
			i++;
		}
		
		Map<Integer, Integer> voteFreq = new HashMap<Integer, Integer>();
		for(i = 0; i < votes.length; i++){
			voteFreq.put(votes[i], voteFreq.get(votes[i]) + 1);			
		}
		
		int max = 0;
		for(Map.Entry<Integer, Integer> voteF : voteFreq.entrySet() ){
			if(voteF.getValue() >= max){
				max = voteF.getKey();
			}
		}
		
		Player deadPlayer = players.get(max);
		deadPlayer.updateRole(false);
	}
	

}
