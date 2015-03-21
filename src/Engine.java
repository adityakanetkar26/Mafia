import java.util.concurrent.LinkedBlockingQueue;


public class Engine extends Thread{

	LinkedBlockingQueue<String> messages = new LinkedBlockingQueue<String>();
	
	private void performAction(String message){
		System.out.println(message);
	}
	
	public void run(){
		boolean stop = false;
		while(!stop){
			try {
				String message = messages.take();
				if(message.equals("stop")){
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
