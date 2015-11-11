package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;

import model.TCPClient;
import view.ChatClientUI;
import view.ChatraumBeitrittsFormUI;


public class ChatClientController
{
    ChatClientUI ui;
    ChatraumBeitrittsFormUI beitrittsUi;
    TCPClient client;
    
    /**
     * Initialisiert den Controller
     */
    public ChatClientController(){
        
        ui = new ChatClientUI();
        beitrittsUi = new ChatraumBeitrittsFormUI();
        
        addActionListenerUI();
        addActionListenerRaumBetretenUI();
        ui.showUI();
    }
    
    private void addActionListenerRaumBetretenUI() {
    	
    	beitrittsUi.getAbbrechen().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				beitrittsUi.getFrame().setVisible(false);
			}
		});
    	
    	beitrittsUi.getVerbinden().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String hostname = beitrittsUi.getHostname().getText();
				String chatName = beitrittsUi.getChatName().getText();

				// TODO wann erzeugt man den Client genau? Es ist zu spät hier am Abend ^^
				client = new TCPClient(); 
				try{
					client.anmelden(hostname,chatName);
					beitrittsUi.getFrame().setVisible(false);
				} catch(Exception ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(beitrittsUi.getFrame(), "Ihre Anfrage konnte nicht bearbeitet werden. Bitte überprüfen Sie ihre Eingaben.");
				}
				
			}
		});
	}

	/**
     * Registriert die Listener an den UI-Elementen
     */
    private void addActionListenerUI() {
		
    	/*
    	 * Der Listener für den "Senden"-Button
    	 */
    	ui.getBtnNachrichtSenden().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				nachrichtSenden();
			}
		});
    	
    	/*
    	 * Realisiert, dass man mit Enter die Nachrichten versenden kann und die JTextArea reseted wird
    	 */
    	ui.getChatInputTextArea().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {

			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					ui.getBtnNachrichtSenden().doClick();
				}
			}
		});
		
    	/*
    	 * Der Listener für das "Beenden" jMenuItem
    	 */
    	ui.getMntmBeenden().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				programmSicherBeenden();
			}
		});
    	
    	ui.getMntmRaumBetreten().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chatRaumBetreten();
			}
		});
	}

    /*
     * TODO Input wird aus dem Textfeld gezogen und müsste hier jetzt über den Client dem Server geschickt werden.
     * Die aktuelle Implementierung ist nur zu Testzwecken für die GUI gewesen
     */
    private void nachrichtSenden(){
    	String input = ui.getChatInputTextArea().getText();
		if(!input.equals("")){
			String output = ui.getChatOutputTextArea().getText();
			ui.getChatOutputTextArea().setText(output + "\n" + input); 
			ui.getChatInputTextArea().setText("");
		}
    }
    
    /*
     * TODO Hier müsste man sich vernünftig beim Server abmelden und das java Programm anschließend schließen
     */
    private void programmSicherBeenden(){
    	
    }
    
    public void chatRaumBetreten(){
    	beitrittsUi.showUI();
    }
    
	public static void main(String[] args)
    {
        ChatClientController controller = new ChatClientController();
    }
}
