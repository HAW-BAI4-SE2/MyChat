package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.omg.CORBA.UnknownUserException;

import model.exceptions.UserAlreadyExistsException;

	//----------------------------------------------------------------------------
	
class TCPWorkerThread extends Thread {
	/*
	 * Arbeitsthread, der eine existierende Socket-Verbindung zur Bearbeitung
	 * erhaelt
	 */
	private int name;
	private Socket socket;
	private TCPServer server;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	boolean workerServiceRequested = true; // Arbeitsthread beenden?
	
	public TCPWorkerThread(int num, Socket sock, TCPServer server) {
	   /* Konstruktor */
	   this.name = num;
	   this.socket = sock;
	   this.server = server;
	}
	
	/*
	 * Implementations-Detail
	 * 
	 * Diese Methode arbeitet nach folgendem Protokoll:
	 * 
	 * 1. User-Registrierung
	 * 		- handelt es sich um einen gültigen Namen? (Bereits vergeben?)
	 * 2. Auf Anfragen des Clients reagieren.
	 * 
	 */
	public void run() {
	   String userSentence;
	
	   System.out.println("TCP Worker Thread " + this.getName() +
	         " is running until SERVER_QUIT:"+this.getName() +" is received!");
	
	   try {
	      /* Socket-Basisstreams durch spezielle Streams filtern */
	      inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	      outToClient = new DataOutputStream(socket.getOutputStream());
	
	      // ************* User-Registrierung ***********
	  	  userSentence = readFromClient();
	      String chatName = server.userRegistrieren(userSentence);
	      this.setName(chatName);
	      
	      writeToClient("ChatName:OK");
	      
	      String nachricht = server.addTextnachricht("Server", "Willkommen an Bord! " + this.getName() + " hat den Chatraum betreten.");
	      server.writeToClients(nachricht);
	      
	      nachricht = "CHAT_MEMBERS:UPDATED";
	      server.writeToClients(nachricht);
	      server.writeToClients(server.chatTeilnehmer.toString());
	      
	      // ************** Auf Anfragen des Clients reagieren **************
	      while (workerServiceRequested) {

	         userSentence = readFromClient();
	         
	         /* Test, ob Arbeitsthread beendet werden soll */
	         if (userSentence.startsWith("SERVER_QUIT:"+this.getName())) {
	        	 server.entferneUser(this.getName());
	        	 writeToClient("CLIENT_QUIT:OK");
	        	 
  		   		 nachricht = server.addTextnachricht("Server", this.getName() + " hat den Chatraum verlassen.");
	  		   	 System.out.println("von wegen leer:"+nachricht);
  		   		 server.writeToClients(nachricht);
			   
	        	 workerServiceRequested = false;
//	        	 break;
	         }
	         
	         /* die Nachricht des Clients verarbeiten */
	         nachricht = server.addTextnachricht(this.getName(),userSentence);	         
	         server.writeToClients(nachricht);
	      }
	
	      /* Socket-Streams schliessen --> Verbindungsabbau */
	      socket.close();
	   } catch (IOException e) {
		  System.err.println("Verbindung konnte nicht hergestellt werden.");
	      e.printStackTrace();
	   } catch (UnknownUserException e) {
			try {
				writeToClient("ChatName:Missing");
			} catch (IOException e1) {
				System.err.println("Verbindung konnte nicht hergestellt werden.");
				e1.printStackTrace(); // Connection abborted
			}
		} catch (IllegalArgumentException e) {
			try {
				writeToClient("ChatName:NotAllowed");
			} catch (IOException e1) {
				System.err.println("Verbindung konnte nicht hergestellt werden.");
				e1.printStackTrace(); // Connection abborted
			}
		} catch (UserAlreadyExistsException e) {
			try {
				writeToClient("ChatName:UserAlreadyExists");
			} catch (IOException e1) {
				System.err.println("Verbindung konnte nicht hergestellt werden.");
				e1.printStackTrace(); // Connection abborted
			}
		}  finally {
			System.out.println("TCP Worker Thread " + this.getName() + " stopped!");
	      /* Platz fuer neuen Thread freigeben */
			server.workerThreadsSem.release();
			server.workerThreads.remove(this);
		}
	}
	
	private String readFromClient() throws IOException {
	   /* Lies die naechste Anfrage-Zeile (request) vom Client */
	   String request = inFromClient.readLine();
	   System.out.println("TCP Worker Thread " + this.getName() + " detected job: " + request);
	
	   return request;
	}
	
	/**
	 * Antworten an den Client dieses WorkerThreads senden.
	 * @param reply die zu versendende Nachricht
	 * @throws IOException
	 */
	public void writeToClient(String reply) throws IOException {
	   /* Sende den String als Antwortzeile (mit CRLF) zum Client */
	   outToClient.writeBytes(reply + '\r' + '\n');
	   System.out.println("TCP Worker Thread " + this.getName() +
	         " has written the message: " + reply);
	}
}