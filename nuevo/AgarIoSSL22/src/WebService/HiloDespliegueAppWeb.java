package WebService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ServerModel.ServerModel;

public class HiloDespliegueAppWeb extends Thread {
	
	WebServer server;
	
	public HiloDespliegueAppWeb(WebServer server) {
		
		this.server = server;
	}
	
	public void run() {
		
		while(server.webService) {
			System.out.println(":::Web Server Started:::");
			ServerSocket serverSocket = server.getServerSocketWebService();
			try {
				Socket cliente = serverSocket.accept();
				ServerWebClientAccept hilo = new ServerWebClientAccept(cliente, server);
				hilo.start();	
				
			} catch (IOException e) {
				System.out.println("Exception in HiloDespliegueAppWeb");
			}
			
		}
		
	}
	

}
