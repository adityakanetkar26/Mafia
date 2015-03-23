import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;


public class NetConnection extends Thread{

	Socket socket;
	PrintWriter writer;
	BufferedReader reader;
	LinkedBlockingQueue<Message> messages;
	
	public NetConnection(Socket sock, LinkedBlockingQueue<Message> msgs){
		messages = msgs;
		socket = sock;
		try{
			writer = new PrintWriter(socket.getOutputStream(),true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e){
			System.out.println("fucking socket constructor");
			e.printStackTrace();
		}
		start();
	}
	
	public NetConnection(String ip, int port, LinkedBlockingQueue<Message> msgs){
		messages = msgs;
		try{
			socket = new Socket(ip, port);
			writer = new PrintWriter(socket.getOutputStream(),true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(Exception e){
			System.out.println("fucking socket constructor");
			e.printStackTrace();
		}
		start();
	}
	
	public void sendMessage(String m){
		writer.println(m);
	}
	
	public void run(){
		String input;
		try{
			while((input = reader.readLine()) != null){
				messages.put(new Message(input, this));
			}
		}
		catch(Exception e){
			System.out.println("socket reader cant read");
			e.printStackTrace();
		}
	}

}
