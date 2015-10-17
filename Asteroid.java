import java.awt.image.BufferedImage;

public class Asteroid extends Sprite
{
	private int size = 3; //Defines asteroid size (big=3, middle=2, small=1)
	public Asteroid(int x, int y, int w, int h, BufferedImage img, int speed, double angle) 
	{
		super(x, y, w, h, img, speed, Math.toRadians(angle));
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int s)
	{
		size = s;
	}
}
