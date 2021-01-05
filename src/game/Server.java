package game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



public class Server {
	public static void main(String[] args) throws Exception
    {
		new ClientHandler("55555").start();
    }
}
//TEsting Branch
class ClientHandler extends Thread {
    private DatagramSocket socket;
    private int port;
    private byte[] buf = new byte[GamePacket.BYTE_ARRAY_LENGTH];
    Map<String, SocketAddress> userMap = new HashMap<String, SocketAddress>();
    

    public ClientHandler(String port) {
        try {
        	this.port = Integer.parseInt(port);
			socket = new DatagramSocket(this.port);  
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void run() {
    	while(true) {
			GamePacket gamePacket = receive();
			
			switch(gamePacket.getPacketType()) {
				case GamePacket.CONNECT:
					userMap.put(gamePacket.getUserName(), gamePacket.getSocketAddress());
					System.out.println(gamePacket.getUserName() + " Connected "+ userMap.size());
					
					send(GamePacket.ackPacket(gamePacket));
					break;
					
				case GamePacket.DISCONNECT:
					
					System.out.println(gamePacket.getUserName() + " Disconnected " + userMap.size());
					
					userMap.remove(gamePacket.getUserName());
					
					send(GamePacket.ackPacket(gamePacket));
					broadcast(gamePacket);
					break;
					
				case GamePacket.ASK_ACK:
					send(GamePacket.ackPacket(gamePacket));
					break;
					
				case GamePacket.NO_ACK:
				default:
					broadcast(gamePacket);
					break;
			}
		}
        //socket.close();
    }
    
    public GamePacket receive() {
    	DatagramPacket packet = new DatagramPacket(buf, buf.length);
    	try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		} 
    	return GamePacket.unPack(packet);
    }
    
    void broadcast(GamePacket packet) {
    	
        for (Entry<String, SocketAddress> user : userMap.entrySet()) {
        	if(!user.getKey().equals(packet.getUserName())) {
	        	try {
	        		packet.setSocketAddress(user.getValue());
					socket.send(GamePacket.pack(packet));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
    }
    
    void send(GamePacket packet) {
        try {
			socket.send(GamePacket.pack(packet));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

