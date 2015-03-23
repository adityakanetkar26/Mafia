import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Display extends JFrame{
	
	LinkedBlockingQueue<String> messages;
	JTextArea display;
	
	public Display(LinkedBlockingQueue<String> msgs){
		super("CS 7270");
		messages = msgs;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800,800));
		
		display = new JTextArea();
		JTextField input = new JTextField();
		display.setBackground(new Color(0,20,40));
		input.setBackground(new Color(0,20,40));
		display.setForeground(new Color(250,125,0));
		input.setForeground(new Color(250,125,0));
		input.setCaretColor(new Color(200,200,200));
		display.setEditable(false);
		
		input.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if(!input.getText().equals("")){
					try {
						messages.put(input.getText());
					} catch (InterruptedException e) {
						System.out.println("display fucked up message put");
						e.printStackTrace();
					}
					input.setText("");
				}
				
			}
			
		});
		
		this.add(input, BorderLayout.SOUTH);
		this.add(display, BorderLayout.CENTER);
		
		this.pack();
		this.setVisible(true);
		
		input.requestFocusInWindow();
	}

}