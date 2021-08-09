package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

public class Server {
	
	public DatagramSocket socket;
	public WriterThread writerThread;
	public ReaderThread readerThread;
	public Map<String, SocketAddress> userMap = new HashMap<String, SocketAddress>();
	public Map<String, GameObject> gameObjectMap = new HashMap<String, GameObject>();
	
	public Server(int port) {
		try {
			socket = new DatagramSocket(port);
			writerThread = new WriterThread(this);
			readerThread = new ReaderThread(this);
		} catch (SocketException e1) {
			System.out.println("Port Unavailable");
			e1.printStackTrace();
		} 
		
		Timer refreshTimer = new Timer(30, new ActionListener() {
		       @Override
		       public void actionPerformed(final ActionEvent e) {
		    	   broadcast(GamePacket.broadcastPacket(new ArrayList<GameObject>(gameObjectMap.values())));
		       }
		    });
		refreshTimer.start();
		readerThread.start();
		
	}
	
	public void processPacket(GamePacket gamePacket) {
		switch(gamePacket.getPacketType()) {
			case GamePacket.CONNECT:
				System.out.println(gamePacket.getUserName() + " Connected ");
				addClient(gamePacket);
				break;
				
			case GamePacket.DISCONNECT:
				System.out.println(gamePacket.getUserName() + " Disconnected ");
				removeClient(gamePacket);
				break;
				
			case GamePacket.ASK_ACK:
				writerThread.send(GamePacket.ackPacket(gamePacket));
				break;
				
			case GamePacket.NO_ACK:
				processObjects(gamePacket);
				break;
				
			default:
				break;
		}
	}
	
	private void addClient(GamePacket gamePacket) {
		userMap.put(gamePacket.getUserName(), gamePacket.getSocketAddress());
		writerThread.send(GamePacket.ackPacket(gamePacket));
	}
	
	private void removeClient(GamePacket gamePacket) {
		userMap.remove(gamePacket.getUserName());
		writerThread.send(GamePacket.ackPacket(gamePacket));
		
		for (Map.Entry<String, GameObject> e : gameObjectMap.entrySet()) {
			if(e.getValue().owner.equals(gamePacket.getUserName())) {
				gameObjectMap.remove(e.getKey(),e.getValue());
				break; //unable to handle more than one object per user.
			}
		}
	}
	
	private void processObjects(GamePacket gamePacket) {
		for (GameObject go : gamePacket.getGameObjects()) {
			if(go.owner.equals(gamePacket.getUserName())) {
				gameObjectMap.put(go.getId(), go);
				break;
			}
   	 	}
	}
	
	private void broadcast(GamePacket gamePacket) {
		for (SocketAddress address : userMap.values()) {
			try {
				gamePacket.setSocketAddress(address);
				socket.send(GamePacket.pack(gamePacket));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception
    {
		new Server(55555);
    }
}


class WriterThread extends Thread {
	Server server;
	
	public WriterThread(Server server) {
		this.server = server;
	}
	
	void send(GamePacket packet) {
        try {
        	server.socket.send(GamePacket.pack(packet));
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

class ReaderThread extends Thread {
	Server server;
	
	public ReaderThread(Server server) {
		this.server = server;
	}
	
	public void run() {
		while(true) {
			server.processPacket(receive());
		}
	}
	
	public GamePacket receive() {
		byte[] buf = new byte[GamePacket.BYTE_ARRAY_LENGTH];
    	DatagramPacket packet = new DatagramPacket(buf, buf.length);
    	try {
    		server.socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	return GamePacket.unPack(packet);
    }
}


