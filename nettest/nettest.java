package nettest;

import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

import nettest.ClientFinder;

public class nettest implements ActionListener{

	boolean running;
	int numberOfClients;
	int maxClients = 4;
	
	int[]x;
	int[]y;
	
	ServerSocket ss;
	Socket[] clients;
	BufferedReader[] incomingMessages;
	PrintWriter[] sender;
	
	public nettest() {
		numberOfClients = 0;
		x = new int[4];
		y = new int[4];
		JFrame window = new JFrame("Server");
		
		window.setLayout(null);
		window.setSize(400,400);
		JButton closeServer = new JButton("ShutDown");
		
		closeServer.addActionListener(this);
		closeServer.setPreferredSize(new Dimension(200,100));
		closeServer.setBounds(0, 60, 100, 20);
		
		JPanel drawerThing = new JPanel();
		drawerThing.setSize(500,500);
		drawerThing.setBackground(Color.BLACK);
		drawerThing.setBounds(100,0,500,500);
		
		window.add(drawerThing);
		
		window.add(closeServer);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		window.setVisible(true);
		
		running = true;
		clients = new Socket[4];
		incomingMessages = new BufferedReader[4];
		sender = new PrintWriter[4];
		
		try {
			ss = new ServerSocket( 8282);

			Thread object = new Thread( new ClientFinder(this));
			object.start();
			
			
			
			while(running) {
				Graphics g = drawerThing.getGraphics();
				g.setColor(Color.black);
				g.fillRect(0, 0, 500, 500);
				for( int i = 0; i < numberOfClients; i ++) {
					g.setColor(Color.RED);
					g.fillRect(x[i], y[i], 50, 50);
				}
				
				try {
					Thread.sleep(1000/20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block 
					e.printStackTrace();
				}
				for(int i = 0; i < incomingMessages.length; i++) {
					
					if(incomingMessages[i] != null) {
						
						if(incomingMessages[i].ready()) {
							
							int info = incomingMessages[i].read();
							//System.out.println("info" + Integer.toBinaryString(info));
							
							if (info == 0b11111111111111111111111111111111) {
								System.out.println("Client" + i + " is gone");
								x[i] = -5;
								clients[i].close();
							}
							
							if ((info & (1 << 3)) != 0) {
								y[i] = y[i] - 2;
							}
							if ((info & (1 << 2)) != 0) {
								x[i] -= 2;
							}
							if ((info & (1 << 1)) != 0) {
								
								y[i] += 2;
							}
							if ((info & (1 << 0)) != 0) {
								
								x[i] += 2;
							}
						}
					} 
				}
			}
			
			System.out.println("Closing server");
			
			for(Socket i : clients) {
				if(i != null) {
					System.out.println("Killed Client");
					i.close();
				}
			}
			
			ss.close();
			window.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new nettest();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		running = false;
		System.out.println("running is false");
	}

}
