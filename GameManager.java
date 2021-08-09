package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

public class GameManager {
	public GameWindow gameWindow;
	public Client client;
	public Map<String, GameObject> gameObjectMap;
	public GameObject player;
	public KeyBoardHandler keyBoardHandler;
	private GamePacket gamepacket;
	
	public Timer refreshTimer;
	
	public GameManager() {
		keyBoardHandler = new KeyBoardHandler(this);
		gameWindow = new GameWindow(this);
		gameObjectMap = new HashMap<String, GameObject>();
		refreshTimer = new Timer(30, new ActionListener() {
		       @Override
		       public void actionPerformed(final ActionEvent e) {
		    	   ArrayList<GameObject> gameObjectsToSend = new ArrayList<GameObject>();
		    	   for (GameObject o : gameObjectMap.values()) {
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
				gameObjectMap = new HashMap<String, GameObject>();
				gameObjectMap.put(player.owner, player);
				for (GameObject os : gamepacket.getGameObjects()) 
					if(!os.owner.equals(player.owner)) 
						gameObjectMap.put(os.owner,os);
				
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
			gameObjectMap = null;
			player = null;
			client = null;
			refreshTimer.stop();
		}
	}
	
	public void createPlayer(String userName) {
		player = new GameObject(userName,0,10,50,50,5,1);
		gameObjectMap.put(userName,player);
	}
}
