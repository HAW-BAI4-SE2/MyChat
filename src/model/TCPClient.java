package model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient extends Thread{

    /* Portnummer */
    private final int serverPort;

    /* Hostname */
    private final String hostname;
    
    /* Chatname */
    private final String chatName;

    private Socket clientSocket; // TCP-Standard-Socketklasse

    private DataOutputStream outToServer; // Ausgabestream zum Server
    private BufferedReader inFromServer; // Eingabestream vom Server
    private InputStreamReader inputStream;
    private StringBuffer stringBuffer;
    BufferedInputStream bis;
    
    
    private boolean serviceRequested = true; // Client beenden?

    public TCPClient(String hostname, int serverPort, String chatName) {
        this.serverPort = serverPort;
        this.hostname = hostname;
        this.chatName = chatName;
    }
    
    public TCPClient(String hostname, String chatName) throws IOException{
        this.serverPort = 56789;
        this.hostname = hostname;
        this.chatName = chatName;
        this.stringBuffer = new StringBuffer(); 
    }

    public void run(){
        /* Client starten. Ende, wenn quit eingegeben wurde */
        String modifiedSentence; // vom Server modifizierter String

        try {
	        /* Socket erzeugen --> Verbindungsaufbau mit dem Server */
	        clientSocket = new Socket(hostname, serverPort);
	
	        /* Socket-Basisstreams durch spezielle Streams filtern */
	        outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        inFromServer = new BufferedReader(new InputStreamReader(
	        		clientSocket.getInputStream()));
	        
	        bis = new BufferedInputStream(clientSocket.getInputStream());
	        inputStream = new InputStreamReader(bis, "US-ASCII");
	        

	        writeToServer("Hallo, ich möchte gerne den Chatraum betreten");
	        
            while (serviceRequested) {
            	
                /* Modifizierten String vom Server empfangen */
                modifiedSentence = readFromServer();

                System.out.println(modifiedSentence);
                /* Test, ob Client beendet werden soll */
                if (modifiedSentence.startsWith("CLIENT_QUIT:"+chatName)) { //TODO StartWith könnte auch True liefern, wenn String leer ist
                    serviceRequested = false;
                } 
            }
			clientSocket.close();
	        
        } catch (IOException e) {
//            throw new IOException("Connection aborted by server!");
        }
        System.out.println("TCP Client stopped!");
    }

    private void writeToServer(String request) throws IOException {
        /* Sende eine Zeile (mit CRLF) zum Server */
        outToServer.writeBytes(request + '\r' + '\n');
        System.out.println("TCP Client has sent the message: " + request);
    }

    private String readFromServer() throws IOException {
        /* Lies die Antwort (reply) vom Server */
//        String reply = inFromServer.readLine();
//        System.out.println("TCP Client got from Server: " + reply);
        int c;
        while ( (c = inputStream.read()) != -1){ // -1 soll das Ende signalisieren
          stringBuffer.append( (char) c);
        }
        return stringBuffer.toString();

    }
    
	public void anmelden() throws IllegalAccessException{
		// TODO Auto-generated method stub
		System.out.println("Der CLient tut noch nichts");
		
		if(chatName.equals("Merkel")){
			throw new IllegalAccessException("Die klassische Merkel Exception");
		}
	}
	
	/**
	 * Ermöglicht es sich als Client beim Chat abzumelden.
	 * @throws IOException
	 */
	public void abmelden() throws IOException{
		
		
		try {
			writeToServer("SERVER_QUIT:"+chatName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IOException("Der Server konnte die Anfrage nicht bearbeiten.");
		}
	}

}
