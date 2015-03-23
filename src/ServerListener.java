import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;


public class ServerListener extends Thread{
	
	ServerSocket socket;
	LinkedBlockingQueue<Message> messages;
	ArrayList<NetConnection> connections = new ArrayList<NetConnection>();
	
	public ServerListener(LinkedBlockingQueue<Message> msgs){
		messages = msgs;
		try {
			socket = new ServerSocket(40000);
		} catch (IOException e) {
			System.out.println("fucking server listener constructor");
			e.printStackTrace();
		}
		start();
	}
	
	public void run(){
		while(true){
			try {
				Socket sock = socket.accept();
				NetConnection connection = new NetConnection(sock, messages);
				connections.add(connection);
			} catch (IOException e) {
				System.out.println("server listener loop fucked up");
				e.printStackTrace();
			}
			
		}
	}

}
