package game;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class GameObject implements Serializable{
	public static final double ANGLE_PERSISON = 10000.0;
	
	public static final Polygon boat = new Polygon(new int[] {0, 1, 2, 2, 1, -1, -2, -2, -1}, new int[] {8, 5, 1, -2, -5, -5, -2, 1, 5}, 9);
	public static final Polygon rudder = new Polygon(new int[] {0,0}, new int[] {0,-2}, 2);
	public static final Polygon sail = new Polygon(new int[] {0,0}, new int[] {0,-7}, 2);
	
	
	public int scale;
	public double xPos;
	public double yPos;
	public int boatRot;
	public int rudderRot;
	public int sailRot;
	public transient int xSpeed;
	public transient int ySpeed;
	public transient int speed;
	public transient int activeSpeed;
	public transient int activeBoatRotRate;
	public transient int activeSailRotRate;
	public transient int rotRate;
	public String owner;
	public int id;
	
	public GameObject(String userName, int id, int scale, int xPos, int yPos, int speed, int rotRate) {
		owner = userName;
		this.scale = scale;
		this.id = id;
		this.xPos = xPos;
		this.yPos = yPos;
		this.speed = speed;
		this.rotRate = rotRate;
		
		boatRot = 0;
		xSpeed = 0;
		xSpeed = 0;
		activeBoatRotRate = 0;
		activeSailRotRate = 0;
		activeSpeed = 0;
		
	}
	
	public void update() {
		xPos += activeSpeed*cos(boatRot+90);
		yPos += activeSpeed*sin(boatRot+90);
		boatRot += activeBoatRotRate;
		sailRot += activeSailRotRate;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        
        g2.drawPolygon(translatePolygon(rotatePolygon(scalePolygon(boat,scale), boatRot, 0, 0), xPos, yPos));
        g2.drawPolygon(translatePolygon(rotatePolygon(rotatePolygon(scalePolygon(rudder,scale), rudderRot, 0, 0), boatRot, 0, -5*scale), xPos, yPos));
        g2.drawPolygon(translatePolygon(rotatePolygon(rotatePolygon(scalePolygon(sail,scale), sailRot, 0, 0), boatRot, 0, 3*scale), xPos, yPos));
        
        Font font = new Font("Dialog", Font.BOLD, 15);
		FontMetrics metrics = g.getFontMetrics(font);
        g2.setFont(font);
        g2.drawString(owner,((int)xPos-(metrics.stringWidth(owner) / 2)),(int)yPos+scale*10);
	}	
	
	public void rotateRight() {
		activeBoatRotRate = rotRate;
		rudderRot = -20;
	}
	
	public void rotateLeft() {
		activeBoatRotRate = -rotRate;
		rudderRot = 20;
	}
	
	public void stopRotation() {
		activeBoatRotRate = 0;
		rudderRot = 0;
	}
	
	public void trimIn() {
		activeSailRotRate = rotRate;
		activeSpeed = speed;
	}
	
	public void trimOut() {
		activeSailRotRate = -rotRate;
		activeSpeed = 0;
	}
	
	public void stopTrim() {
		activeSailRotRate = 0;
	}
	
	public String getId() {
		return owner + id;
	}
	
	private static Polygon translatePolygon (Polygon p, double xPos, double yPos) {
		Polygon pOut = new Polygon();
		double x,y;
		
		for (int i = 0; i < p.npoints; i++) {
			x = p.xpoints[i] + xPos;
			y = p.ypoints[i] + yPos;
			pOut.addPoint((int)x, (int)y);
        }
		
		return pOut;
	}
	
	private static Polygon rotatePolygon (Polygon p, double r,  int xOffset, int yOffset) {
		Polygon pOut = new Polygon();
		int x,y;
		double sin = sin(r);
		double cos = cos(r);
		
		for (int i = 0; i < p.npoints; i++) {
			x = (int)((p.xpoints[i]+xOffset) * cos - (p.ypoints[i]+yOffset)* sin);
			y = (int)((p.xpoints[i]+xOffset) * sin + (p.ypoints[i]+yOffset) * cos);
			pOut.addPoint(x, y);
        }
		
		return pOut;
	}
	
	private static Polygon scalePolygon (Polygon p, int scale) {
		Polygon pOut = new Polygon();
		int x,y;
		
		for (int i = 0; i < p.npoints; i++) {
			x = p.xpoints[i]*scale;
			y = p.ypoints[i]*scale;
			pOut.addPoint(x, y);
        }
		
		return pOut;
	}
	
	private static double sin(double degrees) {
		return Math.round(Math.sin(Math.toRadians(degrees))*ANGLE_PERSISON)/ANGLE_PERSISON;
	}
	
	private static double cos(double degrees) {
		return Math.round(Math.cos(Math.toRadians(degrees))*ANGLE_PERSISON)/ANGLE_PERSISON;
	}
	
}
