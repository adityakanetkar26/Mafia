import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

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
	
	public DisplayPanel(GameState s, LinkedBlockingQueue<Message> msgs){
		state = s;
		messages = msgs;
		setBackground(new Color(0,20,40));
		setPreferredSize(new Dimension(1000,800));
		
		connectPanel = new JPanel();
		connectPanel.setLayout(new BoxLayout(connectPanel, BoxLayout.Y_AXIS));
		connectPanel.add(Box.createVerticalGlue());
		JComboBox serverList = new JComboBox(new String[]{"Choose a server...", "Host server", "Vlad's Resnet"});
		connectPanel.add(serverList);
		JTextField serverField = new JTextField("...or enter IP adress here.");
		connectPanel.add(Box.createVerticalGlue());
		connectPanel.add(serverField);
		JButton connectButton = new JButton("Connect");
		connectPanel.add(Box.createVerticalGlue());
		connectPanel.add(connectButton);
		connectPanel.add(Box.createVerticalGlue());
		connectButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				try {
					if(serverList.getSelectedItem().equals("Host server")){
						messages.put(new Message("start server", null));
					}
					else if(serverList.getSelectedItem().equals("Choose a server...")){
						messages.put(new Message("connect to server$" + serverField.getText() + "$" + 40000, null));
					}
					else if(serverList.getSelectedItem().equals("Vlad's Resnet")){
						messages.put(new Message("connect to server$128.61.105.220$40000", null));
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		});		
		
		this.add(connectPanel);
		
	}
	
	

}
