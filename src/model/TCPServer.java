package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.omg.CORBA.UnknownUserException;

import model.exceptions.UserAlreadyExistsException;

/**
 * Dieser TCP-Server verwaltet den Nachrichtenverlauf und kennt alle anwesenden Teilnehmer.
 * @author Jones
 *
 */
public class TCPServer {
   /* TCP-Server, der Verbindungsanfragen entgegennimmt */

   /* Semaphore begrenzt die Anzahl parallel laufender Worker-Threads  */
   public Semaphore workerThreadsSem;

   public List<TCPWorkerThread> workerThreads;
   public Set<String> chatTeilnehmer;
   public StringBuffer chatVerlauf;
   
   /* Portnummer */
   public final int serverPort;
   
   /* Anzeige, ob der Server-Dienst weiterhin benoetigt wird */
   public boolean serviceRequested = true;
		 
   /* Konstruktor mit Parametern: Server-Port, Maximale Anzahl paralleler Worker-Threads*/
   public TCPServer(int serverPort, int maxThreads) {
      this.serverPort = serverPort;
      this.workerThreadsSem = new Semaphore(maxThreads);
      this.chatTeilnehmer = new HashSet<>();
      this.chatVerlauf = new StringBuffer();
      workerThreads = new ArrayList<>();
   }

   /**
    * Startet dem Server und kümmert sich darum, dass jedem neuen Client ein neuer WorkerThread zugewiesen wird.
    */
   public void startServer() {
      ServerSocket welcomeSocket; // TCP-Server-Socketklasse
      Socket connectionSocket; // TCP-Standard-Socketklasse

      int nextThreadNumber = 0;

      try {
         /* Server-Socket erzeugen */
         welcomeSocket = new ServerSocket(serverPort);

         while (serviceRequested) { 
				workerThreadsSem.acquire();  // Blockieren, wenn max. Anzahl Worker-Threads erreicht
				
            System.out.println("TCP Server is waiting for connection - listening TCP port " + serverPort);
            /*
             * Blockiert auf Verbindungsanfrage warten --> nach Verbindungsaufbau
             * Standard-Socket erzeugen und an connectionSocket zuweisen
             */
            connectionSocket = welcomeSocket.accept();

            
            /* Neuen Arbeits-Thread erzeugen und die Nummer, den Socket sowie das Serverobjekt uebergeben */
            TCPWorkerThread worker = new TCPWorkerThread(++nextThreadNumber, connectionSocket, this);
            workerThreads.add(worker);
            worker.start();
          }
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }

   /**
    * Der Server speichert den gesamten Chatverlauf.
    * Mithilfe dieser Methode kann man eine Nachricht hinzufügen.
    * @param chatName Der Benutzer von dem die Nachricht gekommen ist.
    * @param nachricht Die Nachricht, die der Benutzer gesendet hat.
    * @return Den Nachrichteneintrag
    */
   public String addTextnachricht(String chatName, String nachricht){
       Calendar cal = Calendar.getInstance();
       SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
       String eintrag = "\n"+chatName+" ("+sdf.format(cal.getTime())+"): "+nachricht;
	   chatVerlauf.append(eintrag);
	   return eintrag;
   }
   
   /**
    * Gibt Auskunft darüber, ob ein Chatname bereits vergeben wurde.
    * @param name Der zu überprüfende Name
    * @return
    */
   public boolean isUser(String name){
	   if(chatTeilnehmer.contains(name)){
		   return true;
	   }
	   return false;
   }

   /**
    * Der Server verwaltet alle User des Chatraumes.
    * Mithilfe diser Methode kann man neue Teilnehmer in den Raum einfügen.
    * @param userProtocol Ein Protokoll, um den User bekannt zu geben. Muss die Form "ChatName:hierNameEinfügen" besitzen.
    * @return den tatsächlichen Chatnamen
    * @throws UnknownUserException Wenn das Protokoll nicht richtig gelesen werden kann.
    * @throws IllegalArgumentException Wenn der Name ungültig ist. (Zum Beispiel ein leerer Name)
    * @throws UserAlreadyExistsException Wenn der Chatname bereits vergeben ist in dem Chatraum.
    * @throws IOException 
    */
   public String userRegistrieren(String userProtocol) throws UnknownUserException, IllegalArgumentException, UserAlreadyExistsException, IOException {
		String prefix = "ChatName:";
		if(!userProtocol.startsWith(prefix)){
			throw new UnknownUserException();
		}
		
		String chatname = userProtocol.substring(prefix.length(), userProtocol.length());
		if(chatname.equals("")){
			throw new IllegalArgumentException("Chatname darf nicht leer sein.");
		}
		
		if(isUser(chatname)){
			throw new UserAlreadyExistsException("Es gibt bereits einen Chatteilnehmer mit demselben Namen");
		}
		chatTeilnehmer.add(chatname);
		return chatname;
	}
   
   public void entferneUser(String chatname) throws IOException, UnknownUserException{
	   if(isUser(chatname)){
		   chatTeilnehmer.remove(chatname);
	   } else{
		   throw new UnknownUserException();
	   }
   }
   
   
   	public String getChatVerlauf(){
   		return chatVerlauf.toString();
   	}

	public void writeToClients(String nachricht) throws IOException {		
		for(TCPWorkerThread t : workerThreads){
			t.writeToClient(nachricht);
		}
	}
}


