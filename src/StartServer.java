import model.TCPServer;

public class StartServer {

	public static void main(String[] args) {
	      TCPServer myServer = new TCPServer(56789, 10);
	      myServer.startServer();
		
	}
}
