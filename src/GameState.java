import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import sun.misc.Queue;

public class GameState {

	HashMap<Integer, Player> players = new HashMap<Integer, Player>();
	LinkedBlockingQueue<Message> messages;
	Queue<String> chatMessages = new Queue<String>();
	PrintWriter logWriter;
	
	int badPlayerCount;
	int goodPlayerCount;
	
	Player self;
	
	String gamePhase = "not in game";
	int timeRemaining = 0;
	String winner = null;
	
	boolean openBallot = true;
	boolean privateChat = true;
	boolean chatEnabled = true;
	boolean nightChatEnabled = true;
	int daySeconds = 60;
	int nightSeconds = 30;
	
	public void addPlayer(Player player){
		players.put(player.id, player);
	}
	
	public void assignIdentities(){
		int totalSize = players.size();
		badPlayerCount = Math.min(2,(int)Math.sqrt((double)totalSize));
		goodPlayerCount = totalSize - badPlayerCount;

		ArrayList<Player> unassignedPeople = new ArrayList<Player>();
		for(Player player : players.values()){
			player.role = "good";
			player.aliveDead = true;
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
			case "name":
				player.name = value;
				break;
			case "role":
				player.role = value;
				break;
			case "alive":	
				player.aliveDead = (value.equals("alive"));
				break;
			case "vote":
				if(value.equals("null")){
					player.votingAgainst = null;
				}
				else{
					if(player.aliveDead && (gamePhase.equals("day") || (gamePhase.equals("night") && player.role.equals("bad")))){
						player.votingAgainst = value;
					}
				}	
				break;
			case "vote time":
				player.voteTime = Integer.parseInt(value);
				break;
			default:
				System.out.println("weird update..");
			}
		}
	}
	
	public void processChat(String s, String r, String msg){
		Player sender = null;
		if(!s.equals("null")){
			sender = players.get(Integer.parseInt(s));
		}
		Player receiver = null;
		if(!r.equals("all")){
			receiver = players.get(Integer.parseInt(r));
		}

		if(sender == null){
			chatMessages.enqueue("A god of some sort" + (receiver == null ? " (to all):  " : " (to " + receiver.name + "):  ") + msg);
		}
		else{
			if((receiver == null || receiver == self || sender == self) && (gamePhase.equals("awaiting players") || sender.aliveDead) && self!=null)
			{
				if(!(gamePhase.equals("night") && self.isAliveOrDead() && !(self.role.equals("bad") && sender.role.equals("bad")))){
					chatMessages.enqueue(sender.name + (receiver == null ? " (to all):  " : " (to " + receiver.name + "):  ") + msg);
				}
			}
		}
	}

	
	public void stateUpdate(String update){
		String[] updates = update.split("#");
		for(int a = 0; a < updates.length; a = a + 2){
			String field = updates[a];
			String value = updates[a+1];

			switch(field){
			case "phase":
				gamePhase = value;
				break;
			case "winner":
				winner = value;
				break;

			default:
				System.out.println("weird update..");
			}
		}
	}

	public void gameTransition(String transition) throws InterruptedException{
		switch(transition){
		case "endnight":
			collectAndProcessVotes();
			if(!checkEnd()){
				messages.put(new Message("game transition$startday",null));
			}
			else{
				messages.put(new Message("game update$phase#awaiting players#winner#" + (badPlayerCount > 0 ? "mafia" : "citizens"),null));
			}
			break;
		case "endday":
			collectAndProcessVotes();
			if(!checkEnd()){
				messages.put(new Message("game transition$startnight",null));
			}
			else{
				messages.put(new Message("game update$phase#awaiting players#winner#" + (badPlayerCount > 0 ? "mafia" : "citizens"),null));
			}
			break;
		case "startnight":
			messages.put(new Message("game update$phase#night",null));
			startTimer("game transition$endnight", nightSeconds);
			announce("night phase: only mafia can chat and vote, and only mafia can see messages and votes.", false);
			break;
		case "startday":
			messages.put(new Message("game update$phase#day",null));
			startTimer("game transition$endday", daySeconds);
			announce("day phase: all living players can chat, see messages, and vote.", false);
			break;

		default:
			System.out.println("weird update..");
		}
	}
	
	private boolean checkEnd(){
		if(goodPlayerCount == 0 || badPlayerCount == 0)
			return true;
		else
			return false;
	}
	
	private void startTimer(String update, int seconds){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run() {
				try {
					messages.put(new Message(update,null));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				timer.cancel();
			}
			
		}, 1000*seconds);
		timeRemaining = seconds+1;
		timer.scheduleAtFixedRate(new TimerTask(){

			public void run() {
				timeRemaining--;
				try {
					messages.put(new Message("timer$"+timeRemaining,null,false));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			

		}, 0, 1000);
	}
	
	public String getPlayerState(Player player){
		return "role#" + player.role + "#alive#" + (player.aliveDead ? "alive" : "dead") + 
				"#name#" + player.name + "#vote#"+(player.votingAgainst==null ? "null" : player.votingAgainst) +
				"#vote time#" + player.voteTime;
	}
	
	public String getGameState(){
		return "phase#" + gamePhase;
	}
	
	public String getGameSettings(){
		return "open ballot#" + (openBallot ? "yes" : "no") + "#private chat#" + (privateChat? "yes" : "no") + 
				"#day seconds#" + daySeconds + "#night seconds#" + nightSeconds;
	}
	
	public void setGameSettings(String update){
		String[] updates = update.split("#");
		for(int a = 0; a < updates.length; a = a + 2){
			String field = updates[a];
			String value = updates[a+1];

			switch(field){
			case "open ballot":
				openBallot = value.equals("yes");
				break;
			case "private chat":
				privateChat = value.equals("yes");
				break;
			case "day seconds":
				daySeconds = Integer.parseInt(value);
				break;
			case "night seconds":
				nightSeconds = Integer.parseInt(value);
				break;
			default:
				System.out.println("weird setting.. " + field);
			}
		}		
	}
	
	public void collectAndProcessVotes(){
		Player maxPlayer = (Player) players.values().toArray()[0];
		int maxVotes = 0;
		HashMap<Player, Integer> votes = new HashMap<Player, Integer>();
		for(Player player : players.values()){
			votes.put(player, 0);
		}

		for(Player player : players.values()){
			String vote = player.getVote();
			if(vote != null){
				votes.put(players.get(Integer.parseInt(vote)), votes.get(players.get(Integer.parseInt(vote)))+1);
				if(votes.get(players.get(Integer.parseInt(vote)))>maxVotes){
					maxVotes = votes.get(players.get(Integer.parseInt(vote)));
					maxPlayer = players.get(Integer.parseInt(vote));
				}
			}
		}
		
		if(maxPlayer.role.equals("good")){
			goodPlayerCount--;
		}
		else{
			badPlayerCount--;
		}

		try {
			messages.put(new Message("player update$" + maxPlayer.id + "$alive#dead",null));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		
		for(Player player : players.values()){
			try {
				messages.put(new Message("player update$" + player.id + "$vote#null",null,false));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}//end vote
	
	
	public void record(String line){
		try{
			if(logWriter == null){
				logWriter = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
			}
			logWriter.println(line);
			logWriter.flush();
		}
		catch (IOException e) {
			System.out.println("logging fail");
			e.printStackTrace();
		}
	}
	
	public void announce(String msg, boolean record){
		try {
			messages.put(new Message("chat$null$all$" + msg, null, record));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void announceSettings(){
		announce("[current game settings: " + "private chat " + (privateChat? "enabled" : "disabled") + ", open ballot " + (openBallot? "enabled" : "disabled") + "]", false);
	}

}
