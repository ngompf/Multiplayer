package game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class Client {
	public boolean connected;
	public static final int  DEFAULT_RECEIVE_TIMEOUT = 500;
	public static final int  MAX_ATTEMPTS = 10;
	
	public ClientThread clientThread;
	private DatagramSocket socket;
	
	private SocketAddress socketAddress;
	public String userName;
	private GameManager gameManager;
	
	public Client(SocketAddress socketAddress, String userName, GameManager gameManager) {
		this.socketAddress = socketAddress;
		this.userName = userName;
		this.gameManager = gameManager;
		connected = false;
		
	}
	public GamePacket connect() {
		try {
			socket = new DatagramSocket();
			clientThread = new ClientThread(socket,this, gameManager);
			GamePacket gamePacket = GamePacket.connectPacket(userName,socketAddress);
			sendPacket(gamePacket);
			clientThread.start();
			return gamePacket;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void disconnect() {
		if(connected) {
			connected = false;
			sendPacket(GamePacket.disconnectPacket(userName,socketAddress));
		}
	}
	
	public void sendPacket(GamePacket packet) {
		try {
			clientThread.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
 
class ClientThread extends Thread{
	private DatagramSocket socket;
	private Client client;
	private GameManager gameManager;
	
	private byte[] buf = new byte[GamePacket.BYTE_ARRAY_LENGTH];
	
	public ClientThread(DatagramSocket socket, Client client, GameManager gameManager) {
		this.socket = socket;
		this.client = client;
		this.gameManager = gameManager;
	}
	
	public void run() {
		while(client.connected) {
			try {
				gameManager.packetInbox(receive());
			} catch (IOException e) {
				
			}
		}
	}
	
	public GamePacket receive() throws IOException {
		byte[] buf = new byte[GamePacket.BYTE_ARRAY_LENGTH];
    	DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
    	return GamePacket.unPack(packet);
    }
	
	public void send(GamePacket packet) throws IOException {
		DatagramPacket sendGamePacket = GamePacket.pack(packet);
		switch(packet.getPacketType()) {
		case GamePacket.ASK_ACK:
		case GamePacket.CONNECT:
		case GamePacket.DISCONNECT:
			socket.close();
			socket = new DatagramSocket();
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
			socket.setSoTimeout(Client.DEFAULT_RECEIVE_TIMEOUT);
			int attempt = 0;
			client.connected = false; 
			socket.send(sendGamePacket);
			do {
				try {
					socket.receive(receivePacket);
					GamePacket receiveGamePacket = GamePacket.unPack(receivePacket);
					if(receiveGamePacket.getPacketId() == packet.getPacketId() && 
						receiveGamePacket.getPacketType() == GamePacket.ACK) {
						client.connected = true;
						break;
					}
					
				} catch (SocketTimeoutException e) {
					socket.send(sendGamePacket);
				}
				attempt++;
			}while(attempt < Client.MAX_ATTEMPTS);
			socket.setSoTimeout(0);
			break;
		default:
			socket.send(sendGamePacket);
		}
    }
	
}
