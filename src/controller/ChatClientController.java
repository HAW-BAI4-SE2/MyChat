package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import model.ClientObserver;
import model.TCPClient;
import view.ChatClientUI;
import view.ChatraumBeitrittsFormUI;


public class ChatClientController implements ClientObserver
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
				try {
					nachrichtSenden();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					ui.getChatInputTextArea().setText("");
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					ui.getBtnNachrichtSenden().doClick();
				}
			}
		});
		
    	/*
    	 * Der Listener für das "Abmelden" jMenuItem
    	 */
    	ui.getMntmAbmelden().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ausChatRaumAbmelden();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
    	
    	ui.getMntmRaumBetreten().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
		    	beitrittsUi.showUI();
			}
		});
	}

    /**
     * Registriert die Listener für die "RaumBetreten-Komponente"
     */
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

				try{
					raumBetreten(hostname,chatName);
				} catch(Exception ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(beitrittsUi.getFrame(), "Ihre Anfrage konnte nicht bearbeitet werden. Bitte überprüfen Sie ihre Eingaben.");
				}
				
			}
		});
	}
    
    /*
     * Diese Methode initialisiert alles was mit dem TCP-Client zu tun hat, der dann 
     * dafür sorgt, dass die Verbindung mit dem Chatraum hergestellt wird.
     */
	private void raumBetreten(String hostname, String chatName) throws IOException, IllegalAccessException {
		client = new TCPClient(hostname,chatName); 
		client.addObserver(this);
		client.anmelden(); // 
		client.start();
		beitrittsUi.getFrame().setVisible(false);		
	}
	
    /*
	 * Mithilfe dieser Methode kann man dem Server über den Client Nachrichten senden.
     */
    private void nachrichtSenden() throws IOException{
    	String input = ui.getChatInputTextArea().getText();
		if(!input.isEmpty()){
			
			client.writeToServer(input);
		}
    }
    
    /*
     * Sorgt dafür, dass der Client vernünftig beim Server abgemeldet wird.
     */
    private void ausChatRaumAbmelden() throws IOException{
    	client.abmelden();
    }

	@Override
	/*
	 * Sobald sich etwas beim Client ändert, zum Beispiel eine neu eingetroffene Nachricht,
	 * wird der ChatClientController hier informiert und sorgt dann dafür, dass die OutputTextArea
	 * aktualisiert wird.
	 */
	public void update() {
		System.out.println("ClientController wurde über Nachricht informiert.");
		String nachrichtenVerlauf = client.getNachrichtenverlauf();
		ui.getChatOutputTextArea().setText(nachrichtenVerlauf);
	}
    
}
