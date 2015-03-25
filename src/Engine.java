import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class Engine extends Thread{

	LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	GameState state = new GameState();
	Display display;
	boolean displayDebugs = false;
	
	protected void performAction(Message message){
		System.out.println(message.message);
				
	}
	
	public void showDisplay(){
		display = new Display(messages, state);
	}
	
	public void run(){
		boolean stop = false;
		while(!stop){
			try {
				Message message = messages.take();
				if(display != null && displayDebugs){
					display.appendMessage("DEBUG: " + message);
				}
				if(message.message.equals("stop")){
					stop = true;
				}
				else{
					performAction(message);
					if(display != null){
						display.updateDisplay();
					}
				}
			} catch (Exception e) {
				System.out.println("engine fucked up on message");
				e.printStackTrace();
			}
		}
	}//end run
}
