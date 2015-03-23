
public class ServerEngine extends Engine{

	static ServerEngine serverEngine;
	
	public static void startServer(){
		if(serverEngine == null){
			serverEngine = new ServerEngine();
			serverEngine.start();
		}		
	}
	
	private void performAction(Message message){
		System.out.println("server processing message " + message.message);
		
		String[] tokens = message.message.split("$");
		
		switch(tokens[0]){
		
		default: 
			System.out.println("What the hell even is this");	
		}//end switch
	}
}
