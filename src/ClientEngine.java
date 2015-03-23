
public class ClientEngine extends Engine{

	
	private void performAction(Message message){
		System.out.println("client processing message " + message.message);
		
		switch(message.message){
		
		default: 
			System.out.println("What the hell even is this");
		
		}//end switch
	}
}
