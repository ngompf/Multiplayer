package game;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class GamePacket implements Serializable{
	
	public static final int BYTE_ARRAY_LENGTH = 512;
	
	public static final byte CONNECT = 0;
	public static final byte ASK_ACK = 1;
	public static final byte ACK = 2;
	public static final byte NO_ACK = 3;
	public static final byte DISCONNECT = 4;

	private transient SocketAddress socketAddress;
	private byte packetType;
	private int packetId;
	private String userName;
	private ArrayList<GameObject> gameObjects;
	
	private GamePacket(String userName, int packetId, byte packetType, SocketAddress socketAddress) {
		this.packetType = packetType;
		this.packetId = packetId;
		this.userName = userName;
		this.socketAddress = socketAddress;
	}
	
	private GamePacket(String userName, int packetId, byte packetType, SocketAddress socketAddress, ArrayList<GameObject> gameObjects) {
		this.packetType = packetType;
		this.packetId = packetId;
		this.userName = userName;
		this.socketAddress = socketAddress;
		this.gameObjects = gameObjects;
	}
	
	public SocketAddress getSocketAddress() {
		return socketAddress;
	}
	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}
	
	public byte getPacketType() {
		return packetType;
	}
	
	public int getPacketId() {
		return packetId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	public static GamePacket connectPacket(String userName, SocketAddress socketAddress) {
		return new GamePacket(userName, 0, CONNECT, socketAddress);
	}
	
	public static GamePacket disconnectPacket(String userName, SocketAddress socketAddress) {
		return new GamePacket(userName, -1, DISCONNECT, socketAddress);
	}
	
	public static GamePacket ackPacket(GamePacket gamePacket) {
		return new GamePacket(gamePacket.userName, gamePacket.packetId, ACK, gamePacket.socketAddress);
	}
	
	public static GamePacket nextPacket(GamePacket gamePacket, ArrayList<GameObject> gameObjects) {
		return new GamePacket(gamePacket.userName, gamePacket.packetId++, NO_ACK, gamePacket.socketAddress, gameObjects);
	}

	public static GamePacket broadcastPacket(ArrayList<GameObject> objects) {
		return new GamePacket("Server", -1, NO_ACK, null, objects);
	}

	public static DatagramPacket pack(GamePacket gp){
		byte[] byteArray = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);   
			out.writeObject(gp);
			out.flush();
			byteArray = bos.toByteArray();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new DatagramPacket(byteArray,byteArray.length,gp.socketAddress);
		
	}
	
	public static GamePacket unPack(DatagramPacket p){
		GamePacket gp = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(p.getData());
			ObjectInput in = null;
			in = new ObjectInputStream(bis);
			gp = (GamePacket) in.readObject();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gp.socketAddress = p.getSocketAddress();
		return gp;
	}

	
}
