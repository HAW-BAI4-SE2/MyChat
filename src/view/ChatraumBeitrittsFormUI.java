package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class ChatraumBeitrittsFormUI {

	JFrame frame;
	JTextField hostname;
	JTextField chatName; // der Name, den der User gerne im Chat verwenden möchte
	JButton abbrechen;
	JButton verbinden;
	
	public ChatraumBeitrittsFormUI(){
		initialize();
	}

	private void initialize() {
        int width = 200;
        int height = 200;
        
		frame = new JFrame();
		frame.setSize(width,height);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel.add(splitPane);
		
		JPanel panel_1 = new JPanel();
		splitPane.setLeftComponent(panel_1);
		
		JLabel lblRaumBetreten = new JLabel("Chatraum betreten");
		panel_1.add(lblRaumBetreten);
		
		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setLayout(new GridLayout(3, 2, 0, 5));
		
		JLabel lblHostname = new JLabel("Hostname:");
		panel_2.add(lblHostname);
		
		hostname = new JTextField();
		panel_2.add(hostname);
		hostname.setColumns(10);
		
		JLabel lblChatName = new JLabel("Chat-Name:");
		panel_2.add(lblChatName);
		
		chatName = new JTextField();
		panel_2.add(chatName);
		chatName.setColumns(10);
		
		abbrechen = new JButton("Abbrechen");
		panel_2.add(abbrechen);
		
		verbinden = new JButton("Verbinden");
		panel_2.add(verbinden);
		
	}
	
	
	public JFrame getFrame() {
		return frame;
	}

	public void showUI(){
		frame.setVisible(true);
	}

	public JTextField getHostname() {
		return hostname;
	}

	public JTextField getChatName() {
		return chatName;
	}

	public JButton getAbbrechen() {
		return abbrechen;
	}

	public JButton getVerbinden() {
		return verbinden;
	}
	
	
}
