package game;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class KeyBoardHandler{
	public InputMap inputMap;
	public ActionMap actionMap;
	
	public KeyStroke pauseKey;
	public KeyStroke upKey;
	public KeyStroke downKey;
	public KeyStroke leftKey;
	public KeyStroke rightKey;
	
	public KeyStroke pauseKeyRelease;
	public KeyStroke upKeyRelease;
	public KeyStroke downKeyRelease;
	public KeyStroke leftKeyRelease;
	public KeyStroke rightKeyRelease;
	
	public KeyBoardHandler(GameManager gameManager) {
		inputMap = new InputMap();
		actionMap = new ActionMap();
		
		pauseKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
		
		upKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP,0);
		downKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0);
		leftKey = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0);
		rightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0);
		
		upKeyRelease = KeyStroke.getKeyStroke(KeyEvent.VK_UP,0,true);
		downKeyRelease = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0,true);
		leftKeyRelease = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,true);
		rightKeyRelease = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,true);
		
		
		addKeyBinding(pauseKey,gameManager);
		
		addKeyBinding(upKey,gameManager);
		addKeyBinding(downKey,gameManager);
		addKeyBinding(leftKey,gameManager);
		addKeyBinding(rightKey,gameManager);
		
		addKeyBinding(upKeyRelease,gameManager);
		addKeyBinding(downKeyRelease,gameManager);
		addKeyBinding(leftKeyRelease,gameManager);
		addKeyBinding(rightKeyRelease,gameManager);
	}
	
	public void addKeyBinding(KeyStroke key, GameManager gameManager) {
		inputMap.put(key, key.toString());
		actionMap.put(key.toString(), new KeyBoardAction(key,gameManager));
	}
}

class KeyBoardAction extends AbstractAction{
	KeyStroke key;
	GameManager gameManager;
	
	public KeyBoardAction(KeyStroke key, GameManager gameManager) {
		this.key = key;
		this.gameManager = gameManager;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GameWindow gameWindow = gameManager.gameWindow;
		GameObject player = gameManager.player;
		KeyBoardHandler keyBoardHandler = gameManager.keyBoardHandler;
		if(gameWindow.currentPanel() == gameWindow.gamePanel) {
			if(key == keyBoardHandler.pauseKey) {
				gameWindow.changePanel(gameWindow.pauseMenuPanel);
			}
			
			else if(key == keyBoardHandler.upKey) {
				player.trimIn();
			}
			else if(key == keyBoardHandler.upKeyRelease) {
				player.stopTrim();
			}
			
			else if(key == keyBoardHandler.downKey) {
				player.trimOut();
			}
			if(key == keyBoardHandler.downKeyRelease) {
				player.stopTrim();
			}
			
			else if(key == keyBoardHandler.leftKey) {
				player.rotateLeft();
			}
			else if(key == keyBoardHandler.leftKeyRelease) {
				player.stopRotation();
			}
			
			else if(key == keyBoardHandler.rightKey) {
				player.rotateRight();
			}
			else if(key == keyBoardHandler.rightKeyRelease) {
				player.stopRotation();
			}
		}
	}
}
