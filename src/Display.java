import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Display extends JFrame{
	
	LinkedBlockingQueue<Message> messages;
	GameState state;
	JTextArea textArea;
	DisplayPanel displayPanel;
	
	public Display(LinkedBlockingQueue<Message> msgs, GameState s){
		super("CS 7270");
		messages = msgs;
		state = s;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(1000,800));
		
		
		textArea = new JTextArea();
		JScrollPane textAreaScroll = new JScrollPane(textArea);
		textArea.setMaximumSize(new Dimension(Integer.MAX_VALUE,400));
		textArea.setPreferredSize(new Dimension(800,400));
		textAreaScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		textAreaScroll.setPreferredSize(new Dimension(1000,150));
		JTextField input = new JTextField();
		input.setMaximumSize(new Dimension(Integer.MAX_VALUE, input.getMinimumSize().height));
		displayPanel = new DisplayPanel(state, messages);
		textArea.setBackground(new Color(0,40,80));
		input.setBackground(new Color(0,40,80));
		textArea.setForeground(new Color(250,125,0));
		input.setForeground(new Color(250,125,0));
		input.setCaretColor(new Color(200,200,200));
		textArea.setEditable(false);
		
		input.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(!input.getText().equals("")){
					try {
						messages.put(new Message(input.getText(), null));
					} catch (InterruptedException e) {
						System.out.println("display fucked up message put");
						e.printStackTrace();
					}
					input.setText("");
				}
				
			}
			
		});
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(displayPanel);
		this.add(textAreaScroll);
		this.add(input);	
		
//		this.add(textArea, BorderLayout.SOUTH);
//		this.add(input, BorderLayout.SOUTH);	
//		this.add(displayPanel, BorderLayout.CENTER);
		
		this.pack();
		this.setVisible(true);
		
		input.requestFocusInWindow();
	}
	
	public void updateDisplay(){
		while(!state.chatMessages.isEmpty()){
			try {
				textArea.append(state.chatMessages.dequeue());
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
