
public class Player {
	
	int id;
	boolean goodBad; //Good Player: True, Bad Player: False
	boolean aliveDead; //Alive Player: True, Dead Player: False
	String name;
	ArrayList<Player> visiblePlayers;
		
	public Player(int identifier, String Name){
		id = identifier;
		name = Name;
		goodBad = true;		//Assigning a player as good by default
	}

	public String Vote(){
		//Create GUI to ask the player to vote against another player. 
		//Get that vote and send to the server
		String vote; 
		return vote;
	}
	
	public boolean isAliveOrDead(){
		return aliveDead;
	}

	public void updateRole(boolean newState){
		aliveDead = newState;
	}
	
	public boolean isGoodOrBad(){
		return goodBad;
	}
		
	public void setBad(){
		goodBad = false;
	}
}
