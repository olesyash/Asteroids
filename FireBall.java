import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class FireBall extends Sprite
{
	private final int SIZE = 10;
	private boolean destroy; //If fireball crossed panel borders, should be destroyed
	public FireBall(int x, int y, int w, int h, BufferedImage img, int speed, double angle) 
	{
		super(x, y, w, h, img, speed, angle);
		destroy = false;
	}

	//Draw fireball - orange circle
	public void drawSprite(Graphics2D g)
	{
		g.setColor(Color.ORANGE);
		g.fillOval(x, y, SIZE, SIZE);
	}
	
	public boolean getDestroy()
	{
		return destroy;
	}
	
	@Override
	public void updateSprite()
	{
		moveForward();
		//If fireball crossed panel borders, should be destroyed
		if(x<0 || y <0)
			destroy = true;	
		if(x - imageWidth >= pWidth ||y-imageHeight >= pHeight)
			destroy = true;
	}
	
}
