package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.Box.Filler;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class ChatClientUI extends Observable
{
    JFrame frame; 
    JButton anmelden;
    JButton abmelden;
    JButton beenden;
    JTextArea chatArea;
    
    
    /**
     * Initialisiert die UI.
     */
    public ChatClientUI(){
        int width = 600;
        int height = 400;
        frame = new JFrame("MyChat24");
        JPanel levelNullPanel = new JPanel(new BorderLayout()); 
        JSplitPane splitPane = new JSplitPane();
        JPanel levelEinsPanelLeft = new JPanel(new GridLayout(0, 2));
        JSplitPane levelEinsPanelRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JScrollPane levelZweiPanelNorth = new JScrollPane();
        JScrollPane levelZweiPanelSouth = new JScrollPane();
        JMenuBar menuBar = new JMenuBar();
        JMenu menuProgramm = new JMenu("MyChat");
        JMenuItem beendenMenuItem = new JMenuItem("Beenden");
        chatArea = new JTextArea();
        
        // ************************* Level 0 - Das unterste Panel *************************
        frame.setJMenuBar(menuBar);
        menuBar.add(menuProgramm);
        menuProgramm.add(beendenMenuItem);
        frame.add(levelNullPanel);
        levelNullPanel.add(splitPane);

        // ************************* Level 1 - Das SplitPane  *************************
        splitPane.setLeftComponent(levelEinsPanelLeft);
        splitPane.setRightComponent(levelEinsPanelRight);
        
        // ************************* Level 1.Left - Das linke Panel  *************************
        int widthLeftPanel = (int) (width * 0.25); // 1/4 der Breite
        levelEinsPanelLeft.setPreferredSize(new Dimension(widthLeftPanel,height));
        
        GroupLayout leftPanelLayout = new GroupLayout(levelEinsPanelLeft);
        levelEinsPanelLeft.setLayout(leftPanelLayout);
        
        leftPanelLayout.setHorizontalGroup(
                leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(widthLeftPanel, widthLeftPanel, widthLeftPanel)
        );
        
        // ************************* Level 1.Right - Das rechte Panel  *************************
        int widthRightPanel = (int) (width*0.75); // 3/4 der Breite
        levelEinsPanelRight.setPreferredSize(new Dimension(widthRightPanel,height));
        levelEinsPanelRight.setTopComponent(levelZweiPanelNorth);
        levelEinsPanelRight.setBottomComponent(levelZweiPanelSouth);
        
        // ************************* Level 2.Rechts.North - Der Chatverlauf  *************************
        int heightRightPanelNorth = (int) (height*0.75); // 3/4 der Höhe
        
        Dimension north = new Dimension(widthRightPanel, heightRightPanelNorth);
        Filler fillerNorth = new Filler(north,north,north);
        levelZweiPanelNorth.setPreferredSize(new Dimension(widthRightPanel,heightRightPanelNorth));
        levelZweiPanelNorth.setViewportView(fillerNorth);
//        GroupLayout northPanelLayout = new GroupLayout(levelZweiPanelNorth);
//        levelZweiPanelNorth.setLayout(northPanelLayout);
//        
//        northPanelLayout.setHorizontalGroup(
//                northPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGap(widthRightPanel, widthRightPanel, widthRightPanel)
//        );
        
        // ************************* Level 2.Rechts.South - Das Eingabefeld  *************************
        int heightRightPanelSouth = (int) (height*0.25); // 1/4 der Höhe
        Dimension south = new Dimension(widthRightPanel, heightRightPanelSouth);
        Filler fillerSouth = new Filler(south,south,south);
        levelZweiPanelSouth.setPreferredSize(new Dimension(widthRightPanel,heightRightPanelSouth));
        levelZweiPanelSouth.setViewportView(fillerSouth);
        
        
//        GroupLayout southPanelLayout = new GroupLayout(levelZweiPanelSouth);
//        levelZweiPanelSouth.setLayout(southPanelLayout);
//        
//        southPanelLayout.setHorizontalGroup(
//                southPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGap(widthRightPanel, widthRightPanel, widthRightPanel)
//        );
        // ************************* Frame-Settings *************************
        // Client muss über beenden Button geschlossen werden
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        
        frame.pack();
    }
    
    public void addActionListener(){
        
    }
    
    
    /**
     *  Zeigt die UI an.
     *  
     */
    public void showUI(){
        frame.setVisible(true);
    }
}
