import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Display extends JFrame{
	
	LinkedBlockingQueue<Message> messages;
	GameState state;
	JTextArea textArea;
	DisplayPanel displayPanel;
	JScrollPane textAreaScroll;
	JComboBox chatList;
	HashMap<Integer, Player> chatListMap = new HashMap<Integer, Player>();
	
	public Display(LinkedBlockingQueue<Message> msgs, GameState s){
		super("CS 7270");
		messages = msgs;
		state = s;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(1200,800));
		
		chatList = new JComboBox(new String[]{"Send to: everyone"});
		textArea = new JTextArea();
		textAreaScroll = new JScrollPane(textArea);
		textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE,400));
		textArea.setPreferredSize(new Dimension(800,400));
		textAreaScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		textAreaScroll.setPreferredSize(new Dimension(1000,150));
		JTextField input = new JTextField();
		input.setMaximumSize(new Dimension(Integer.MAX_VALUE, input.getMinimumSize().height));
		displayPanel = new DisplayPanel(state, messages);
		textArea.setBackground(new Color(0,40,80));
		input.setBackground(new Color(0,40,80));
		chatList.setBackground(new Color(0,40,80));
		textArea.setForeground(new Color(250,125,0));
		input.setForeground(new Color(250,125,0));
		chatList.setForeground(new Color(250,125,0));
		input.setCaretColor(new Color(200,200,200));
		input.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		textArea.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		chatList.setFont(new Font("Cooper Black", Font.PLAIN, 14));
		textArea.setEditable(false);
		
		input.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(!input.getText().equals("")){
					if(input.getText().startsWith("cmd/")){
						try {
							messages.put(new Message(input.getText().substring(4),null));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else{
						if(state.self != null){
							try {
								String receiver = "";
								if(chatList.getSelectedItem().equals("Send to: everyone")){
									receiver = "all";
								}
								else{
									receiver = Integer.toString(chatListMap.get(chatList.getSelectedIndex()).id);
								}
								messages.put(new Message("chat$" + state.self.id + "$" + receiver + "$" +input.getText(), null));
							} catch (InterruptedException e) {
								System.out.println("display fucked up message put");
								e.printStackTrace();
							}
						}
					}
				}
				input.setText("");	
			}

		});
		
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.X_AXIS));
		input.setMaximumSize(new Dimension(input.getMaximumSize().width, chatList.getMaximumSize().height));
		chatPanel.add(input);
		chatPanel.add(chatList);
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(displayPanel);
		this.add(textAreaScroll);
		this.add(chatPanel);	
		
//		this.add(textArea, BorderLayout.SOUTH);
//		this.add(input, BorderLayout.SOUTH);	
//		this.add(displayPanel, BorderLayout.CENTER);
		
		this.pack();
		this.setVisible(true);
		
		input.requestFocusInWindow();
	}
	
	public void updateDisplay(){

		if(state.privateChat){
			for(Player player : state.players.values()){
				if(!chatListMap.containsValue(player)){
					chatList.addItem(player.name);
					chatListMap.put(chatList.getItemCount()-1, player);
				}
			}
		}
		else{
			for(int i : chatListMap.keySet()){
				chatList.removeItem(chatListMap.get(i).name);
			}
			chatListMap.clear();
		}
		
		while(!state.chatMessages.isEmpty()){
			try {
				textArea.append(state.chatMessages.dequeue() + "\n");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		displayPanel.updateDisplay();
		repaint();
//		textArea.setText("");
//		for(Player player : state.players.values()){
//			textArea.append(player.id + "\n");
//		}
	}
	
//	public void repaint(){
//		super.repaint();
//		
//	}
	
	public void appendMessage(String s){
		textArea.append(s);
	}

}
