import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;


public class NetInterface extends Thread{
	
	ServerSocket socket;
	LinkedBlockingQueue<String> messages;
	HashMap<String, NetConnection> connections = new HashMap<String, NetConnection>();
	int counter = 0;
	
	public NetInterface(LinkedBlockingQueue<String> msgs, boolean isServer){
		messages = msgs;
		
		if(isServer){
			try {
				socket = new ServerSocket(40000);
			} catch (IOException e) {
				System.out.println("fucking server listener constructor");
				e.printStackTrace();
			}
			start();
		}
		
	}
	
	public String establishConnection(String ip, int port){
		counter++;
		NetConnection connection = new NetConnection(ip, port, messages, Integer.toString(counter));
		connections.put(Integer.toString(counter), connection);
		return Integer.toString(counter);
	}
	
	public void sendMessage(String destination, String message){
		connections.get(destination).sendMessage(message);
	}
	
	public void run(){
		while(true){
			try {
				counter++;
				Socket sock = socket.accept();
				NetConnection connection = new NetConnection(sock, messages, Integer.toString(counter));
				connections.put(Integer.toString(counter), connection);
			} catch (IOException e) {
				System.out.println("server listener loop fucked up");
				e.printStackTrace();
			}
			
		}
	}

}
