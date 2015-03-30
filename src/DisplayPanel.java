import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DisplayPanel extends JPanel{
	
	LinkedBlockingQueue<Message> messages;
	GameState state;
	JPanel connectPanel;
	JButton startButton;
	JLabel waitLabel;
	String displayState = "not in game";
	
	BufferedImage[] civPics;
	BufferedImage[] mafPics;
	BufferedImage[] unkPics;
	HashMap<Player, BufferedImage> playerPics = new HashMap<Player, BufferedImage>();
	HashMap<Player, String> playerViews = new HashMap<Player, String>();
	
	public DisplayPanel(GameState s, LinkedBlockingQueue<Message> msgs){
		state = s;
		messages = msgs;
		loadImages();
		setBackground(new Color(0,20,40));
		//setPreferredSize(new Dimension(1000,600));
		
		connectPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		connectPanel.setBackground(new Color(0,20,40));
		//connectPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//connectPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//connectPanel.setPreferredSize(this.getSize());
		//connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.Y_AXIS));
		
		JComboBox serverList = new JComboBox(new String[]{"Choose a server...", "Host server", "Vlad's Resnet"});
		JButton connectButtonA = new JButton("Connect");
		JTextField serverField = new JTextField("...or enter IP adress here.");
		JButton connectButtonB = new JButton("Connect");
		startButton = new JButton("Start Game...");
		waitLabel = new JLabel("Waiting for game start...");
		connectButtonA.setForeground(new Color(250,125,0));
		connectButtonB.setForeground(new Color(250,125,0));
		startButton.setForeground(new Color(250,125,0));
		serverField.setForeground(new Color(250,125,0));
		serverList.setForeground(new Color(250,125,0));
		waitLabel.setForeground(new Color(250,125,0));
		connectButtonA.setBackground(new Color(0,40,80));
		connectButtonB.setBackground(new Color(0,40,80));
		startButton.setBackground(new Color(0,40,80));
		serverField.setBackground(new Color(0,40,80));
		serverList.setBackground(new Color(0,40,80));
		connectButtonA.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		connectButtonB.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		startButton.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		serverField.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		serverList.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		waitLabel.setFont(new Font("Cooper Black", Font.PLAIN, 14));

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
		
		startButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					if(ServerEngine.serverEngine != null){
						ServerEngine.serverEngine.messages.put(new Message("start game", null));
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		ImageIcon icon = new ImageIcon(mafPics[0]);
		JLabel connectTopLabel = new JLabel("Welcome to Mafia, son", icon, JLabel.CENTER);
		connectTopLabel.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		connectTopLabel.setHorizontalTextPosition(JLabel.CENTER);
		connectTopLabel.setVerticalTextPosition(JLabel.BOTTOM);
		connectTopLabel.setBackground(new Color(50,50,50));
		connectTopLabel.setForeground(new Color(250,125,0));
		//connectTopPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weighty = 0.9;
		connectPanel.add(connectTopLabel,c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weighty = 0.1;
		c.weightx = 0.1;
		connectPanel.add(serverList, c);
		
		c.gridx = 3;
		connectPanel.add(connectButtonA,c);
		
		c.gridx = 1;
		c.gridy = 2;
		connectPanel.add(serverField,c);
		
		c.gridx = 3;
		connectPanel.add(connectButtonB, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0.3;
		connectPanel.add(new JLabel(), c);
		
		c.gridx = 2;
		c.weightx = 0.1;
		connectPanel.add(new JLabel(), c);
		
		c.gridx = 4;
		c.weightx = 0.3;
		connectPanel.add(new JLabel(), c);
		
		this.setLayout(new GridBagLayout());
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.fill = GridBagConstraints.BOTH;
		this.add(connectPanel,c);
		
	}//end constructor
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int numPlayers = state.players.values().size();
		int count = 1;
		int x;
		int y;
		int radius = Math.min(this.getWidth(), this.getHeight())*1/3;
		for(Player player : state.players.values()){
			if(!playerViews.containsKey(player) || !(playerViews.get(player).equals(player.role))){
				playerViews.put(player, player.role);
				Random rand = new Random();
				if(player.role.equals("good")){
					playerPics.put(player, civPics[rand.nextInt(civPics.length)]);
				}
				else if(player.role.equals("bad")){
					playerPics.put(player, mafPics[rand.nextInt(mafPics.length)]);
				}
				else if(player.role.equals("unassigned")){
					playerPics.put(player, unkPics[rand.nextInt(unkPics.length)]);
				}
			}
			if(player == state.self){
				x = this.getWidth()/2;
				y = this.getHeight()/3 + radius;
			}
			else{
				x = (int) (this.getWidth()/2 + Math.cos(Math.PI*2*count/numPlayers - Math.PI/2)*radius);
				y = (int) (this.getHeight()/3 - Math.sin(Math.PI*2*count/numPlayers - Math.PI/2)*radius);
				count++;
			}
			BufferedImage img = playerPics.get(player);
			g.drawImage(img,x,y, Math.min(radius*img.getWidth()/img.getHeight(), img.getWidth()), Math.min(radius, img.getHeight()), null);
			
		}
		
	}//end repaint
	
	public void updateDisplay(){
		if(!state.gamePhase.equals("not in game") && displayState.equals("not in game")){
			this.remove(connectPanel);
			displayState = "awaiting players";
			if(ServerEngine.serverEngine!=null){
				this.add(startButton);
			}
			else{
				this.add(waitLabel);
			}
			this.revalidate();
			
		}
		else if(!state.gamePhase.equals("awaiting players") && displayState.equals("awaiting players")){
			if(ServerEngine.serverEngine!=null){
				this.remove(startButton);
			}
			else{
				this.remove(waitLabel);
			}
		}
	}
	
	private void loadImages(){
		mafPics = new BufferedImage[4];
		civPics = new BufferedImage[7];
		unkPics = new BufferedImage[1];
		try {
			//unknown = ImageIO.read(new File("/img/unknown.png"));
			
			for(int a = 0; a < mafPics.length; a++){
				mafPics[a] = ImageIO.read(new File("img\\m" + a + ".png"));
			}
			for(int b = 0; b < civPics.length; b++){
				civPics[b] = ImageIO.read(new File("img\\c" + b + ".png"));
			}
			for(int c = 0; c < unkPics.length; c++){
				unkPics[c] = ImageIO.read(new File("img\\u" + c + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
