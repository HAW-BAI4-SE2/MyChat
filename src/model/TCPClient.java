package model;

public class TCPClient {

	public void anmelden(String hostname, String chatName) throws IllegalAccessException{
		// TODO Auto-generated method stub
		System.out.println("Der CLient tut noch nichts");
		
		if(hostname.equals("Merkel")){
			throw new IllegalAccessException("Die klassische Merkel Exception");
		}
	}

}
