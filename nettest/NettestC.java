package nettest;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;

public class NettestC implements ActionListener, WindowListener, KeyListener {
	
	
	boolean running;
	boolean[] keys;
	int ClientID = -1;
	Socket ss;
	
	public NettestC() {
		keys = new boolean[256];
		running = true;
		JFrame window = new JFrame("Client");
		
		JButton moveBtn = new JButton("Move");
		moveBtn.addActionListener(this);
		moveBtn.addKeyListener(this);
		window.addWindowListener(this);
		window.setLayout(new GridLayout(1,2));
		window.setSize(200, 100);
		window.add(moveBtn);
		window.setVisible(true);
		
		try {
			ss = new Socket("192.168.0.6", 8282);
			//ss.getOutputStream().write("Hello\n".getBytes());
			//ss.getOutputStream().flush();
			BufferedReader r = new BufferedReader( new InputStreamReader(ss.getInputStream()));
			ClientID = Integer.parseInt(r.readLine());
			System.out.println("Got ID" + ClientID);
			
			while (running) {
				byte message = 0b00000000;
				
				if (keys[KeyEvent.VK_W]) {
					message |= 0b10000000;
				}
				if (keys[KeyEvent.VK_A]) {
					message |= 0b01000000;
				}
				if (keys[KeyEvent.VK_S]) {
					message |= 0b00100000;
				}
				if (keys[KeyEvent.VK_D]) {
					message |= 0b00010000;
				}
				
				if (message != 0b00000000) {
					ss.getOutputStream().write(message);
					ss.getOutputStream().flush();
				}
				try {
					Thread.sleep(1000/20);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		window.dispose();
	}
	
	public static void main(String[] args) {
		new NettestC();

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Window Closing");
		running = false;
		try {
			if (ss != null ) {
				ss.getOutputStream().write(0b11111111);
				ss.getOutputStream().flush();
				ss.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't close socket");
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("Window Closed");
		running = false;
		try {
			if (ss != null ) {
				
				ss.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Couldn't close socket");
		}
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		keys[e.getKeyCode()] = false;
	}
}
