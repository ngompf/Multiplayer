package game;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class GameObject implements Serializable{
	public int xSize;
	public int ySize;
	public int xPos;
	public int yPos;
	public transient int xSpeed;
	public transient int ySpeed;
	public transient int speed;
	public String owner;
	public String userName;
	
	public GameObject(String userName, int xSize, int ySize,int xPos, int yPos, int speed) {
		owner = userName;
		this.userName = userName;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xPos = xPos;
		this.yPos = yPos;
		xSpeed = 0;
		xSpeed = 0;
		this.speed = speed;
		
	}
	
	public void update() {
		xPos += xSpeed;
		yPos += ySpeed;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Font font = new Font("Dialog", Font.BOLD, 15);
		FontMetrics metrics = g.getFontMetrics(font);
        g2.fill(new Ellipse2D.Double(xPos-(ySize/2), yPos-(ySize/2), xSize, ySize));
        g2.setFont(font);
        g2.drawString(userName,(xPos-(metrics.stringWidth(userName) / 2)),yPos+ySize);
	}	
}
