import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DisplayPanel extends JPanel{
	
	LinkedBlockingQueue<Message> messages;
	GameState state;
	JPanel connectPanel;
	JButton startButton;
	JLabel gameLabel;
	String displayState = "not in game";
	
	BufferedImage[] civPics;
	BufferedImage[] mafPics;
	BufferedImage[] unkPics;
	
	HashMap<Player, PlayerView> playerViews = new HashMap<Player, PlayerView>();
	
	Color fontColor = new Color(250,125,0);
	Color baseColor = new Color(20,20,20);
	Color secColor = new Color(40,40,40);
	
	public DisplayPanel(GameState s, LinkedBlockingQueue<Message> msgs){
		state = s;
		messages = msgs;
		loadImages();
		setBackground(baseColor);
		//setPreferredSize(new Dimension(1000,600));
		
		connectPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		connectPanel.setBackground(baseColor);
		//connectPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//connectPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		//connectPanel.setPreferredSize(this.getSize());
		//connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.Y_AXIS));
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(3,2));
		settingsPanel.setBackground(secColor);
		
		JCheckBox ballotBox = new JCheckBox("Open Ballot", true);
		JCheckBox privateBox = new JCheckBox("Private Chat", true);
		JLabel dayLabel = new JLabel("Day Seconds: ");
		JLabel nightLabel = new JLabel("Night Seconds: ");
		JTextField dayField = new JTextField("60");
		JTextField nightField = new JTextField("30");
		JComboBox serverList = new JComboBox(new String[]{"Choose a server...", "Host server", "Vlad's Resnet"});
		JButton connectButtonA = new JButton("Connect");
		JTextField serverField = new JTextField("...or enter IP adress here.");
		JTextField nameField = new JTextField("Player name");
		JButton connectButtonB = new JButton("Connect");
		startButton = new JButton("Start Game...");
		gameLabel = new JLabel("Waiting for game start...");
		connectButtonA.setForeground(fontColor);
		connectButtonB.setForeground(fontColor);
		startButton.setForeground(fontColor);
		serverField.setForeground(fontColor);
		nameField.setForeground(fontColor);
		serverList.setForeground(fontColor);
		gameLabel.setForeground(fontColor);
		ballotBox.setForeground(fontColor);
		privateBox.setForeground(fontColor);
		dayLabel.setForeground(fontColor);
		nightLabel.setForeground(fontColor);
		dayField.setForeground(fontColor);
		nightField.setForeground(fontColor);
		connectButtonA.setBackground(secColor);
		connectButtonB.setBackground(secColor);
		startButton.setBackground(secColor);
		serverField.setBackground(secColor);
		nameField.setBackground(secColor);
		serverList.setBackground(secColor);
		ballotBox.setBackground(secColor);
		privateBox.setBackground(secColor);
		dayField.setBackground(secColor);
		nightField.setBackground(secColor);
		dayLabel.setBackground(secColor);
		nightLabel.setBackground(secColor);
		connectButtonA.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		connectButtonB.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		startButton.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		serverField.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		nameField.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		serverList.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		ballotBox.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		privateBox.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		dayField.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		nightField.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		dayLabel.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		nightLabel.setFont(new Font("Cooper Black", Font.PLAIN, 24));
		gameLabel.setFont(new Font("Cooper Black", Font.PLAIN, 20));

		connectButtonA.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					if(serverList.getSelectedItem().equals("Host server")){
						messages.put(new Message("start server$"+nameField.getText()+
							"$open ballot#" + (ballotBox.isSelected()? "yes" : "no") + "#private chat#" + (privateBox.isSelected()? "yes" : "no")+
							"#day seconds#" + dayField.getText() + "#night seconds#" + nightField.getText(), null));
					}
					else if(serverList.getSelectedItem().equals("Vlad's Resnet")){
						messages.put(new Message("connect to server$128.61.105.220$40000$"+nameField.getText(), null));
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		});

		connectButtonB.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					messages.put(new Message("connect to server$" + serverField.getText() + "$40000$"+nameField.getText(), null));
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
		
		serverList.addItemListener(new ItemListener(){
			
			public void itemStateChanged(ItemEvent arg0) {
				if(serverList.getSelectedIndex()==1){
					c.gridx = 1;
					c.gridy = 3;
					c.gridwidth = 3;
					connectPanel.add(settingsPanel, c);
					connectPanel.revalidate();
				}
				else{
					connectPanel.remove(settingsPanel);
					connectPanel.revalidate();
				}
				
			}
			
		});
		
		this.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {
				
			}

			public void mouseEntered(MouseEvent arg0) {
				
			}

			public void mouseExited(MouseEvent arg0) {
				
			}

			public void mousePressed(MouseEvent arg0) {
				
			}

			public void mouseReleased(MouseEvent arg0) {
				for(Player player : playerViews.keySet()){
					PlayerView playerView = playerViews.get(player);
					if(player != state.self && arg0.getX() > playerView.x && arg0.getX() < playerView.rx&& arg0.getY() > playerView.y && arg0.getY() < playerView.ry){
						int voteTime = 0;
						if(state.gamePhase.equals("day")){
							voteTime = state.daySeconds - state.timeRemaining;
						}
						else if(state.gamePhase.equals("night")){
							voteTime = state.nightSeconds - state.timeRemaining;
						}
						
						messages.add(new Message("player update$" + state.self.id + "$vote#" + player.id + "#vote time#" + voteTime,null));
					}
				}
			}
			
		});
		
		settingsPanel.add(privateBox);
		settingsPanel.add(ballotBox);
		settingsPanel.add(dayLabel);
		settingsPanel.add(dayField);
		settingsPanel.add(nightLabel);
		settingsPanel.add(nightField);
		
		ImageIcon icon = new ImageIcon(mafPics[0]);
		JLabel connectTopLabel = new JLabel("Welcome to Mafia, son", icon, JLabel.CENTER);
		connectTopLabel.setFont(new Font("Cooper Black", Font.PLAIN, 30));
		connectTopLabel.setHorizontalTextPosition(JLabel.CENTER);
		connectTopLabel.setVerticalTextPosition(JLabel.BOTTOM);
		connectTopLabel.setBackground(new Color(50,50,50));
		connectTopLabel.setForeground(fontColor);
		//connectTopPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weighty = 0.9;
		connectPanel.add(connectTopLabel,c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0.1;
		c.gridwidth = 1;
		c.weightx = 0.1;
		connectPanel.add(nameField,c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weighty = 0.1;
		c.weightx = 0.1;
		connectPanel.add(serverList, c);
		
		c.gridx = 3;
		connectPanel.add(connectButtonA,c);
		
		c.gridx = 1;
		c.gridy = 4;
		connectPanel.add(serverField,c);
		
		c.gridx = 3;
		connectPanel.add(connectButtonB, c);

		c.gridx = 0;
		c.gridy = 5;
		c.weightx = 0.5;
		connectPanel.add(new JLabel(), c);
		
		c.gridx = 2;
		c.weightx = 0.1;
		connectPanel.add(new JLabel(), c);
		
		c.gridx = 4;
		c.weightx = 0.5;
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
		double factor = 0.33;

		for(Player player : state.players.values()){
			if(!playerViews.containsKey(player)){
				playerViews.put(player, new PlayerView());
			}
			String visibleRole;
			PlayerView playerView = playerViews.get(player);
			if(player == state.self || state.self.role.equals("bad") || !player.aliveDead){
				visibleRole = player.role;
			}
			else{
				visibleRole = "unassigned";
			}
			if(!visibleRole.equals(playerView.visibleRole)){
				playerView.visibleRole = visibleRole;
				Random rand = new Random();
				if(visibleRole.equals("good")){
					playerView.pic = civPics[rand.nextInt(civPics.length)];
				}
				else if(visibleRole.equals("bad")){
					playerView.pic = mafPics[rand.nextInt(mafPics.length)];
				}
				else if(visibleRole.equals("unassigned")){
					playerView.pic = unkPics[rand.nextInt(unkPics.length)];
				}
			}
			playerView.visibleVote = null;
			if(player.votingAgainst!=null && (player == state.self || state.openBallot) && (state.gamePhase.equals("day") || (state.gamePhase.equals("night") && state.self.role.equals("bad") && player.role.equals("bad")))){
				playerView.visibleVote = state.players.get(Integer.parseInt(player.votingAgainst));
			}
			
			BufferedImage img = playerView.pic;
			double pyy = Math.min(this.getHeight()*factor, img.getHeight()); 
			double pxx = Math.min(this.getWidth()*factor, img.getWidth()); 
			double py = Math.min(pyy, 1.0*(pxx)*img.getHeight()/(img.getWidth()));
			double px = Math.min(pxx, 1.0*(pyy)*img.getWidth()/(img.getHeight())); 
			
			double ry = (this.getHeight()-py-40)/2;
			double rx = (this.getWidth()-px-40)/2;
			
			double ary = (this.getHeight()-2*py-40)/2;
			double arx = (this.getWidth()-2*px-40)/2;
	
			double dx = Math.sqrt(arx*arx + ary*ary);
			
			if(player == state.self){
				playerView.x = (int) (rx) + 20;
				playerView.y = (int) (ry + ry) + 20;
				playerView.ax = (int) (arx + px + 20);
				playerView.ay = (int) (ary + py + 20+ 100);
			}
			else{
				playerView.x = (int) (rx + 20 + Math.cos(Math.PI*2*count/numPlayers - Math.PI/2)*rx);
				playerView.y = (int) (ry + 20 - Math.sin(Math.PI*2*count/numPlayers - Math.PI/2)*ry);
				playerView.ax = (int) (arx + px + 20 + Math.cos(Math.PI*2*count/numPlayers - Math.PI/2)*arx*.9);
				playerView.ay = (int) (ary + py + 20 - Math.sin(Math.PI*2*count/numPlayers - Math.PI/2)*ary*.9);
				count++;
			}
			playerView.rx = (int) (playerView.x + px);
			playerView.ry = (int) (playerView.y + py);
			
			g.drawImage(img,playerView.x,playerView.y, (int)px, (int)py, null);
			
		}
		for(Player player : state.players.values()){
			PlayerView playerView = playerViews.get(player);
			g.setColor(fontColor);
			if(playerView.visibleVote != null){
				PlayerView target = playerViews.get(playerView.visibleVote);
				g.drawLine(playerView.ax, playerView.ay, target.ax, target.ay);
				double dist = Math.sqrt((playerView.ax - target.ax)*(playerView.ax - target.ax) + (playerView.ay - target.ay)*(playerView.ay - target.ay));
				g.drawLine(target.ax, target.ay, (int)(target.ax + 10.0*(playerView.ax - target.ax)/dist-5.0*(playerView.ay - target.ay)/dist), (int)(target.ay + 10.0*(playerView.ay - target.ay)/dist+5.0*(playerView.ax - target.ax)/dist));
				g.drawLine(target.ax, target.ay, (int)(target.ax + 10.0*(playerView.ax - target.ax)/dist+5.0*(playerView.ay - target.ay)/dist), (int)(target.ay + 10.0*(playerView.ay - target.ay)/dist-5.0*(playerView.ax - target.ax)/dist));
			}
			g.setColor(fontColor);
			g.setFont(new Font("Cooper Black", Font.PLAIN, 20));
			g.drawString((player == state.self ? "YOU - " : "") + player.name + (playerView.visibleRole.equals("unassigned") ? "" : ", " + (player.role.equals("good") ? "Citizen" : "Mafioso")) + (player.aliveDead ? "" : " (Dead)"), playerView.x, playerView.y-5);
			
		}
		
		if(state.gamePhase.equals("day")){
			gameLabel.setText("The day has dawned... Choose whom to lynch " + state.timeRemaining);
		}
		else if(state.gamePhase.equals("night")){
			gameLabel.setText("Night has fallen... The mafia choose a target " + state.timeRemaining);
		}
		else if(state.gamePhase.equals("awaiting players")){
//			int good = 0;
//			int bad = 0;
//			for(Player player : state.players.values()){
//				if(player.role.equals("good") && player.aliveDead){
//					good++;
//				}
//				else if(player.role.equals("bad") && player.aliveDead){
//					bad++;
//				}
//			}
			gameLabel.setText((state.winner == null ? "" : "Game over, " + state.winner + " won. ") + "Waiting for game start...");
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
				this.add(gameLabel);
			}
			this.revalidate();
			
		}
		else if(!state.gamePhase.equals("awaiting players") && displayState.equals("awaiting players")){
			displayState = state.gamePhase;
			if(ServerEngine.serverEngine!=null){
				this.remove(startButton);
				this.add(gameLabel);
				this.revalidate();
			}
			
			
		}
		else if(state.gamePhase.equals("awaiting players") && !displayState.equals("awaiting players")){
			displayState = state.gamePhase;
			if(ServerEngine.serverEngine!=null){
				this.remove(gameLabel);
				this.add(startButton);
				this.revalidate();
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
	
	private class PlayerView{
		
		BufferedImage pic;
		String visibleRole;
		int x;
		int y;
		int rx;
		int ry;
		int ax;
		int ay;
		Player visibleVote;
		
	}

}
