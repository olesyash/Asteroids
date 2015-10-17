import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprite {
	protected static final int SIZE = 10; // default sprite's dimensions
    protected BufferedImage bImage; //Image
    protected int speed; 
    protected double angle;
    protected int imageWidth, imageHeight; // image dimensions
    protected int x, y; 
    protected int pWidth, pHeight;  // panel's dimensions
    protected AffineTransformOp op; //For rotation
    
    public Sprite(int x, int y, int w, int h, BufferedImage img, int speed, double angle) 
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = speed;
        pWidth = w;
        pHeight = h;
        bImage = img;
        if(bImage != null)
        {
            imageWidth = bImage.getWidth(null);
            imageHeight = bImage.getHeight(null);
        }
        else
            setImageDimensions();     
    }
   
    
    public void setImageDimensions()
    {
    	imageWidth = SIZE;
    	imageHeight = SIZE;
    }
    
    public Rectangle getBoundingBox()
    {
        return new Rectangle(getLocX(), getLocY(), imageWidth, imageHeight);
    }

    public int getLocX() {
        return x;
    }
    
    public int getLocY() {
        return y;
    }
    
    public int getImageWidth() { 
        return imageWidth;
    }
    
    public int getImageHeight()
    {
        return imageHeight;
    }
    public void updateSprite()
    {
       moveForward(); 
       //Check if object crossed panel boarders and replace to other side 
        if(angle > 0 && x - imageWidth >= pWidth)
        	x = 0;
        else if(y-imageHeight >= pHeight)
        	y = 0;
        else if( x + imageWidth <= 0)
        	x = pWidth;
        else if(y + imageHeight <= 0)
        	y = pHeight;
    }
    
    //Move forward on defined angle 
	public void moveForward()
	{
	    x += speed * Math.sin(angle);
	    y -= speed * Math.cos(angle);
	}
    
    public void drawSprite(Graphics2D g)
    {
    	g.drawImage(bImage, op, x, y);
    }
    
    
}

