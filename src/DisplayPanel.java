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
	
	BufferedImage[] civPics;
	BufferedImage[] mafPics;
	BufferedImage unknown;
	HashMap<Player, BufferedImage> playerPics = new HashMap<Player, BufferedImage>();
	
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
		connectButtonA.setForeground(new Color(250,125,0));
		connectButtonB.setForeground(new Color(250,125,0));
		serverField.setForeground(new Color(250,125,0));
		serverList.setForeground(new Color(250,125,0));
		connectButtonA.setBackground(new Color(0,40,80));
		connectButtonB.setBackground(new Color(0,40,80));
		serverField.setBackground(new Color(0,40,80));
		serverList.setBackground(new Color(0,40,80));
		connectButtonA.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		connectButtonB.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		serverField.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		serverList.setFont(new Font("Cooper Black", Font.PLAIN, 14));

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
		
		this.setLayout(new GridLayout(0,1));
		this.add(connectPanel);
		
	}//end constructor
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int numPlayers = state.players.values().size();
		int count = 1;
		int x;
		int y;
		int radius = Math.min(this.getWidth(), this.getHeight())*1/3;
		for(Player player : state.players.values()){
			if(!playerPics.containsKey(player)){
				
			}
			if(player == state.self){
				x = this.getWidth()/2;
				y = this.getHeight()/3 + radius;
			}
			else{
				x = (int) (this.getWidth()/2 + Math.cos(Math.PI*2*count/numPlayers - Math.PI/2)*radius);
				y = (int) (this.getHeight()/3 + Math.sin(Math.PI*2*count/numPlayers - Math.PI/2)*radius);
				count++;
			}
			BufferedImage img = playerPics.get(player);
			g.drawImage(img,x,y, Math.min(radius*x/y, img.getWidth()), Math.min(radius, img.getHeight()), null);
			
		}
	}//end repaint
	
	private void loadImages(){
		mafPics = new BufferedImage[2];
		civPics = new BufferedImage[3];
		try {
			//unknown = ImageIO.read(new File("/img/unknown.png"));
			for(int a = 0; a < mafPics.length; a++){
				mafPics[a] = ImageIO.read(new File("img\\m" + a + ".png"));
			}
			for(int b = 0; b < civPics.length; b++){
				civPics[b] = ImageIO.read(new File("img\\c" + b + ".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
