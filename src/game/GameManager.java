package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketAddress;
import java.util.ArrayList;

import javax.swing.Timer;

public class GameManager {
	public GameWindow gameWindow;
	public Client client;
	public ArrayList<GameObject> gameObjects;
	public GameObject player;
	public KeyBoardHandler keyBoardHandler;
	private GamePacket gamepacket;
	
	public Timer refreshTimer;
	
	public GameManager() {
		keyBoardHandler = new KeyBoardHandler(this);
		gameWindow = new GameWindow(this);
		gameObjects = new ArrayList<GameObject>();
		refreshTimer = new Timer(30, new ActionListener() {
		       @Override
		       public void actionPerformed(final ActionEvent e) {
		    	   ArrayList<GameObject> gameObjectsToSend = new ArrayList<GameObject>();
		    	   for (GameObject o : gameObjects) {
		    		   if(o.owner == client.userName)
		    			   gameObjectsToSend.add(o);
		    	   }
		    	   gameWindow.frame.repaint();
		    	   client.sendPacket(GamePacket.nextPacket(gamepacket, gameObjectsToSend));
		       }
		    });
	}
	public void packetInbox(GamePacket gamepacket) {
		switch(gamepacket.getPacketType()) {
			case GamePacket.NO_ACK:
				for (GameObject os : gamepacket.getGameObjects()) {
					for (GameObject oc : gameObjects) {
						if(os.userName.equals(oc.userName)) {
							gameObjects.remove(oc);
							break;
						}
					}
					gameObjects.add(os);
				}
				break;
			case GamePacket.DISCONNECT:
				for (GameObject oc : gameObjects) {
					if(oc.userName.equals(gamepacket.getUserName())) {
						gameObjects.remove(oc);
						break;
					}
				}
			default:
				break;
		}
	}
	public void connectClient(SocketAddress socketAddress, String userName) {
		client = new Client(socketAddress, userName, this);
		createPlayer(userName);
		gamepacket = client.connect();
		if(client.connected)
			refreshTimer.start();
		
	}
	public void disconnectClient() {
		if(client != null) {
			client.disconnect();
			gameObjects = null;
			player = null;
			client = null;
			refreshTimer.stop();
		}
	}
	public void createPlayer(String userName) {
		player = new GameObject(userName,51,51,50,50,5);
		gameObjects.add(player);
	}
}
