package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class ChatClientUI {

	private JFrame frame;
	
	private JMenuItem mntmAbmelden;
	private JMenuItem mntmRaumBetreten;
	private JButton btnNachrichtSenden;
	
	private JTextArea chatInputTextArea;
	private JTextArea chatOutputTextArea;
	private JTextArea teilnehmer;


	/**
	 * Create the application.
	 */
	public ChatClientUI() {
		initialize();
	}

	/**
	 * Initialisiert die UI. 
	 */
	private void initialize() {
        int width = 600;
        int height = 400;
        int heightDivider = (int) (height*0.75); // 3/4 der Höhe
        
		frame = new JFrame();
		frame.setSize(width,height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mntmName = new JMenu("MyChat24");
		menuBar.add(mntmName);
		
		mntmRaumBetreten = new JMenuItem("Chatraum beitreten");
		mntmName.add(mntmRaumBetreten);
		
		mntmAbmelden = new JMenuItem("Abmelden");
		mntmName.add(mntmAbmelden);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
		
		JPanel chatArea = new JPanel();
		splitPane.setRightComponent(chatArea);
		chatArea.setLayout(new BorderLayout(0, 0));
		
		JSplitPane chatAreaSplitPane = new JSplitPane();
		chatAreaSplitPane.setContinuousLayout(true);
		chatAreaSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		chatArea.add(chatAreaSplitPane, BorderLayout.CENTER);
		
		JPanel chatPanelSouth = new JPanel();
		chatAreaSplitPane.setRightComponent(chatPanelSouth);
		chatPanelSouth.setLayout(new BorderLayout(0, 0));
		
		JScrollPane chatInputScrollPane = new JScrollPane();
		chatPanelSouth.add(chatInputScrollPane, BorderLayout.CENTER);
		
		chatInputTextArea = new JTextArea();
		chatInputTextArea.setLineWrap(true);
		chatInputScrollPane.setViewportView(chatInputTextArea);
		
		JPanel chatPanelNorth = new JPanel();
		chatAreaSplitPane.setLeftComponent(chatPanelNorth);
		chatPanelNorth.setLayout(new BorderLayout(0, 0));
		
		JScrollPane chatOutputScrollPane = new JScrollPane();
		chatPanelNorth.add(chatOutputScrollPane, BorderLayout.CENTER);
		
		chatOutputTextArea = new JTextArea();
		chatOutputTextArea.setDisabledTextColor(Color.BLUE);
		chatOutputTextArea.setFont(new Font("Arial", Font.BOLD, 13));
		chatOutputTextArea.setEditable(false);
		chatOutputTextArea.setEnabled(false);
		chatOutputTextArea.setLineWrap(true);
		chatOutputScrollPane.setViewportView(chatOutputTextArea);
		chatAreaSplitPane.setDividerLocation(heightDivider); // DIVIDER ChatArea
		
		JPanel overviewPanel = new JPanel();
		splitPane.setLeftComponent(overviewPanel);
		overviewPanel.setLayout(new BorderLayout(0, 0));
		
		JSplitPane overviewSplitPane = new JSplitPane();
		overviewSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		overviewPanel.add(overviewSplitPane, BorderLayout.CENTER);
		
		JPanel overviewPanelNorth = new JPanel();
		overviewSplitPane.setLeftComponent(overviewPanelNorth);
		overviewPanelNorth.setLayout(new BorderLayout(0, 0));
		
		JSplitPane overviewPanelNorthSplitPane = new JSplitPane();
		overviewPanelNorthSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		overviewPanelNorth.add(overviewPanelNorthSplitPane, BorderLayout.NORTH);
		
		JPanel panelTeilnehmerHeader = new JPanel();
		overviewPanelNorthSplitPane.setLeftComponent(panelTeilnehmerHeader);
		panelTeilnehmerHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblChatTeilnehmer = new JLabel("Chat Teilnehmer");
		panelTeilnehmerHeader.add(lblChatTeilnehmer, BorderLayout.CENTER);
		
		JPanel panelTeilnehmerListe = new JPanel();
		panelTeilnehmerListe.setPreferredSize(new Dimension(10, 80));
		overviewPanelNorthSplitPane.setRightComponent(panelTeilnehmerListe);
		panelTeilnehmerListe.setLayout(new BorderLayout(0, 0));
		
		teilnehmer = new JTextArea();
		teilnehmer.setEditable(false);
		teilnehmer.setEnabled(false);
		panelTeilnehmerListe.add(teilnehmer, BorderLayout.CENTER);
		overviewPanelNorthSplitPane.setDividerLocation(20); // DIVIDER Zwischen TeilnehmerHeader und der TeilnehmerListe
		
		JPanel overviewPanelSouth = new JPanel();
		overviewSplitPane.setRightComponent(overviewPanelSouth);
		overviewPanelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnNachrichtSenden = new JButton("Senden");
		overviewPanelSouth.add(btnNachrichtSenden);
		overviewSplitPane.setDividerLocation(heightDivider-10); // DIVIDER des Overview-Splitpanes
		splitPane.setDividerLocation(100);
	}
	
    /**
     *  Zeigt die UI an.
     *  
     */
    public void showUI(){
        frame.setVisible(true);
    }

    /**
     * Liefert das "Abmelden" JMenuItem.
     * @return das "Abmelden" JMenuItem.
     */
	public JMenuItem getMntmAbmelden() {
		return mntmAbmelden;
	}

	/**
	 * Liefert das "Raum betreten" JMenuItem
	 * @return das "Raum betreten" JMenuItem
	 */
	public JMenuItem getMntmRaumBetreten() {
		return mntmRaumBetreten;
	}

	/**
	 * Liefert das "Nachricht senden" JMenuItem
	 * @return das "Nachricht senden" JMenuItem
	 */
	public JButton getBtnNachrichtSenden() {
		return btnNachrichtSenden;
	}
    
	/**
	 * Liefert die Input JTextArea
	 * @return die Input JTextArea
	 */
	public JTextArea getChatInputTextArea() {
		return chatInputTextArea;
	}

	/**
	 * Liefert die Output JTextArea
	 * @return die Output JTextArea
	 */
	public JTextArea getChatOutputTextArea() {
		return chatOutputTextArea;
	}

	/**
	 *  Liefert die Teilnehmer JTextArea // TODO Hier wäre eigentlich eine JList cooler
	 * @return die Teilnehmer JTextArea
	 */
	public JTextArea getTeilnehmer() {
		return teilnehmer;
	}

    
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ChatClient_UI window = new ChatClient_UI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
}