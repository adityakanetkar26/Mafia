import java.util.ArrayList;

public class Player {
	
	int id;
	String role; //good, bad, Unassigned
	boolean aliveDead; //Alive Player: True, Dead Player: False
	String votingAgainst;
	String name;
	ArrayList<Player> visiblePlayers;
		
	public Player(int identifier, String Name){
		id = identifier;
		name = Name;
		aliveDead = true;
		role = "unassigned";
	}

	public String getVote(){
		return votingAgainst;
	}
	
	public boolean isAliveOrDead(){
		return aliveDead;
	}

	public void updateRole(boolean newState){
		aliveDead = newState;
	}
	
	public String getRole(){
		return role;
	}
		
	public void setRole(String r){
		role = r;
	}
	
	
}
