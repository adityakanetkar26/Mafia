import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class DisplayPanel extends JPanel{
	
	GameState state;
	
	public DisplayPanel(GameState s){
		state = s;
		setBackground(new Color(0,20,40));
		setPreferredSize(new Dimension(1000,800));
	}

}
