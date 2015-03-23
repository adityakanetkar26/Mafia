
public class ServerEngine extends Engine{

	static ServerEngine serverEngine;
	
	public static void startServer(){
		serverEngine = new ServerEngine();
		serverEngine.start();
	}
	
	private void performAction(Message message){
		System.out.println("server processing message " + message.message);
		
		switch(message.message){
		
		default: 
			System.out.println("What the hell even is this");	
		}//end switch
	}
}
