import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class DisplayPanel extends JPanel{
	
	LinkedBlockingQueue<Message> messages;
	GameState state;
	JPanel connectPanel;
	
	BufferedImage[] civPics;
	BufferedImage[] mafPics;
	BufferedImage unknown;
	HashMap<Player, BufferedImage> playerPics = new HashMap<Player, BufferedImage>();
	
	public DisplayPanel(GameState s, LinkedBlockingQueue<Message> msgs){
		state = s;
		messages = msgs;
		loadImages();
		setBackground(new Color(0,20,40));
		setPreferredSize(new Dimension(1000,800));
		
		connectPanel = new JPanel();
		connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.Y_AXIS));
		
		JPanel connectPanelA = new JPanel();
		connectPanelA.setLayout(new BoxLayout(connectPanelA, BoxLayout.X_AXIS));
		JComboBox serverList = new JComboBox(new String[]{"Choose a server...", "Host server", "Vlad's Resnet"});
		connectPanelA.add(Box.createHorizontalGlue());
		connectPanelA.add(serverList);
		JButton connectButtonA = new JButton("Connect");
		connectPanelA.add(connectButtonA);
		connectPanelA.add(Box.createHorizontalGlue());
		
		JPanel connectPanelB = new JPanel();
		connectPanelB.setLayout(new BoxLayout(connectPanelB, BoxLayout.X_AXIS));
		JTextField serverField = new JTextField("...or enter IP adress here.");
		connectPanelB.add(Box.createHorizontalGlue());
		connectPanelB.add(serverField);
		JButton connectButtonB = new JButton("Connect");
		connectPanelB.add(connectButtonB);
		connectPanelB.add(Box.createHorizontalGlue());
		
		connectPanel.add(Box.createVerticalGlue());
		connectPanel.add(connectPanelA);
		connectPanel.add(connectPanelB);
		//connectPanel.add(Box.createVerticalGlue());
		connectButtonA.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					if(serverList.getSelectedItem().equals("Host server")){
						messages.put(new Message("start server", null));
					}
					else if(serverList.getSelectedItem().equals("Vlad's Resnet")){
						messages.put(new Message("connect to server$128.61.105.220$40000", null));
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		});

		connectButtonB.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					messages.put(new Message("connect to server$" + serverField.getText() + "$" + 40000, null));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		});

		this.add(connectPanel);
		
	}//end constructor
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int numPlayers = state.players.values.size();
		int count = 1;
		int x;
		int y;
		int radius = Math.min(this.getWidth(), this.getHeight())*1/2;
		for(Player player : state.players.values()){
			if(!playerPics.containsKey(player)){
				
			}
			if(player == state.self){
				x = this.getWidth()/2;
				y = this.getHeight()/2 + radius;
			}
			else{
				x = (int) (this.getWidth()/2 + Math.cos(Math.PI*2*count/numPlayers - Math.PI/2)*radius);
				y = (int) (this.getHeight()/2 + Math.sin(Math.PI*2*count/numPlayers - Math.PI/2)*radius);
				count++;
			}
			g.drawImage(playerPics.get(player),x,y,null);
			
		}
	}//end repaint
	
	private void loadImages(){
		mafPics = new BufferedImage[5];
		civPics = new BufferedImage[5];
		try {
			unknown = ImageIO.read(new File("../img/unknown.png"));
			for(int a = 0; a < mafPics.length; a++){
				mafPics[a] = ImageIO.read(new File("/img/m" + a + ".png"));
			}
			for(int b = 0; b < civPics.length; b++){
				civPics[b] = ImageIO.read(new File("/img/c" + b + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
