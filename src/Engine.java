import java.util.concurrent.LinkedBlockingQueue;


public class Engine extends Thread{

	LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
	
	private void performAction(Message message){
		System.out.println(message.message);
				
	}
	
	public void run(){
		boolean stop = false;
		while(!stop){
			try {
				Message message = messages.take();
				if(message.message.equals("stop")){
					stop = true;
				}
				else{
					performAction(message);
				}
			} catch (Exception e) {
				System.out.println("engine fucked up on message");
				e.printStackTrace();
			}
		}
	}//end run
}
