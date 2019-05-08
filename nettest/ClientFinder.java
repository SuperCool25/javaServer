package nettest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientFinder implements Runnable{

	nettest n;
	Socket[] clients;	
	
	public ClientFinder( nettest n) {
		this.n = n;
	}
	
	@Override
	public void run() {
		while (!n.ss.isClosed()) {
			try {
				Thread.sleep(1000/20);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (n.numberOfClients < n.maxClients) {
				try {
					n.clients[n.numberOfClients] = n.ss.accept();
					System.out.println("New Client" + n.numberOfClients);
					n.sender[n.numberOfClients] = new PrintWriter(n.clients[n.numberOfClients].getOutputStream(),true);
					n.incomingMessages[n.numberOfClients] = new BufferedReader(
							new InputStreamReader(n.clients[n.numberOfClients].getInputStream()));
					n.sender[n.numberOfClients].println(n.numberOfClients);
					n.sender[n.numberOfClients].flush();
					n.numberOfClients++;
				}
				catch(SocketException e) {
					System.out.println(e.getMessage());
				}

				
				}else {
					System.out.println("ALREADY 4 CLIENTS" + n.numberOfClients);
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Finder is down");
		
	}

}
