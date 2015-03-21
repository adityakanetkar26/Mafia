
public class Main {

//	static boolean isServer = false;
	
	
	public static void main(String[] args) {
		
//		if(args.length > 0 && args[0].equals("server")){
//			isServer = true;
//		}
		
		Engine engine = new Engine();
		engine.start();
		Display display = new Display(engine.messages);

	}

}
