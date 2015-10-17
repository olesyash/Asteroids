import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class SpaceShip extends Sprite
{
	private static final int ANGLE = 30; //Rotation one step angle
	private AffineTransform tx ;
	private int rotateAngle;
	public SpaceShip(int x, int y, int w, int h, BufferedImage img, int speed, double angle) 
	{
		super(x, y, w, h, img, speed, angle);
		rotateAngle = 0; 
		tx = AffineTransform.getTranslateInstance(x,y);
	}
	
	//Function to rotate spaceship using direction (left/right)
	public void rotate(int direction)
	{
		rotateAngle += direction*ANGLE;
		rotateAngle = rotateAngle % 360;
		double locationX = imageWidth/2; 
		double locationY = imageHeight/2;
		this.angle = Math.toRadians(rotateAngle);
		tx.rotate(angle);
		tx = AffineTransform.getRotateInstance(angle, locationX, locationY);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
	}
}
