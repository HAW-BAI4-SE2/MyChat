package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//----------------------------------------------------------------------------

class TCPWorkerThread extends Thread {
/*
 * Arbeitsthread, der eine existierende Socket-Verbindung zur Bearbeitung
 * erhaelt
 */
private int name;
private String chatName;
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
   this.chatName = "User"+name;
}

public void run() {
   String userSentence;

   System.out.println("TCP Worker Thread " + chatName +
         " is running until SERVER_QUIT:"+chatName +" is received!");

   try {
      /* Socket-Basisstreams durch spezielle Streams filtern */
      inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToClient = new DataOutputStream(socket.getOutputStream());

      while (workerServiceRequested) {
         /* String vom Client empfangen und in Grossbuchstaben umwandeln */
         userSentence = readFromClient();

         /* Modifizierten String an Client senden */
         writeToClient(userSentence);

         /* Test, ob Arbeitsthread beendet werden soll */
         if (userSentence.startsWith("SERVER_QUIT:"+chatName)) {
        	 
        	 // hier checken, ob chatName gültiger User ist
        	 
            workerServiceRequested = false;
         }
      }

      /* Socket-Streams schliessen --> Verbindungsabbau */
      socket.close();
   } catch (IOException e) {
      System.err.println("Connection aborted by client!");
   } finally {
      System.out.println("TCP Worker Thread " + chatName + " stopped!");
      /* Platz fuer neuen Thread freigeben */
			server.workerThreadsSem.release();         
   }
}

private String readFromClient() throws IOException {
   /* Lies die naechste Anfrage-Zeile (request) vom Client */
   String request = inFromClient.readLine();
   System.out.println("TCP Worker Thread " + chatName + " detected job: " + request);

   return request;
}

private void writeToClient(String reply) throws IOException {
   /* Sende den String als Antwortzeile (mit CRLF) zum Client */
   outToClient.writeBytes(reply + '\r' + '\n');
   System.out.println("TCP Worker Thread " + chatName +
         " has written the message: " + reply);
}
}