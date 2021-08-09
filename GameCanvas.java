package game;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GameCanvas extends JPanel{
	private GameManager gameManager;
	public GameCanvas(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
    public void paintComponent(Graphics g) {
    	ArrayList<GameObject> a = new ArrayList<GameObject>(gameManager.gameObjectMap.values());
        for (GameObject o : a) {
        	o.update();
        	o.draw(g);
   	 	}
    }
}