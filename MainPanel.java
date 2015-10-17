import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable //, ActionListener
{
	//Define variables
	private static final int space = 10, num = 15;
	private int PWIDTH = 602, PHEIGHT = 698;
	private GameLogic gl;
	private boolean running; 
	private int count;

	//Buffered image definition 
	private BufferedImage mainBufferedImage = null;
	private BufferedImage bufferedLifeImage = null;
	private BufferedImage bufferdSpaceshipImg = null;
	private BufferedImage bufferedAstroidImg = null;
	private BufferedImage bufferedAstroidImg2 = null;
	private BufferedImage bufferedAstroidImg3 = null;
	private BufferedImage[] explosion;


	public MainPanel()
	{
		//Create buffered images from sources 
		bufferdSpaceshipImg = createBufferedImage("ship.png");
		bufferedAstroidImg = createBufferedImage("big.jpg");
		bufferedAstroidImg2 = createBufferedImage("middle.jpg");
		bufferedAstroidImg3 = createBufferedImage("small.jpg");
		bufferedLifeImage = createBufferedImage("life.png");
		
		//Create game logic
		gl = new GameLogic(PWIDTH, PHEIGHT, bufferdSpaceshipImg, bufferedAstroidImg3, bufferedAstroidImg2, bufferedAstroidImg);
		running = true; 
		count = 0;
		setFocusable(true);
		requestFocusInWindow();
		this.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event){}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent event) 
			{	
				// Pressed arrow up
				if (event.getKeyCode() == KeyEvent.VK_UP)
					gl.gameSpaceShip.updateSprite();
				// Pressed arrow left
				else if (event.getKeyCode() == KeyEvent.VK_LEFT)
					gl.gameSpaceShip.rotate(-1);
				// Pressed arrow right
				else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
					gl.gameSpaceShip.rotate(1);
				else if(event.getKeyCode() == KeyEvent.VK_SPACE)
					gl.shoot();	
				repaint();

			}
		});

	}
	public void run() 
	{
		while (running)
		{
			gl.gameUpdate();       // Update the game's logic
			gameRender();        // double buffering
			repaint();          // paint on screen
			count = (count+1) % num; 
			try 
			{
				Thread.sleep(40);
			}
			catch(InterruptedException e){}
		}

	}

	public void gameRender()
	{
		Graphics2D dbg;
		mainBufferedImage = new BufferedImage(PWIDTH, PHEIGHT, BufferedImage.OPAQUE);
		dbg = mainBufferedImage.createGraphics();

		//Set black background
		dbg.setColor(Color.BLACK);
		dbg.fillRect(0, 0, PWIDTH, PWIDTH);
		
		//Draw the game objects
		gl.drawGame(dbg);

		//Explosion effect if spaceship was hitted
		if(gl.getSpaceShipHitted())
		{
			explosion = loadStripFile("explosion.png", num);
			dbg.drawImage(explosion[count], gl.gameSpaceShip.x, gl.gameSpaceShip.y, this);
			if(count == num-1)
				gl.startAgain();
		}
		//Draw life static pictures
		for (int i = 0; i < gl.getLifeNum(); i++)
			dbg.drawImage(bufferedLifeImage, space*(i+1) + i*bufferedLifeImage.getWidth(null), space, null);

		//Game over!
		if(gl.getGameOver())
		 {
			repaint();
			JOptionPane.showMessageDialog(null, "Game Over!", "", JOptionPane.INFORMATION_MESSAGE);
			running = false;
		}
		//You win!
		if(gl.getWin())
		{
			repaint();
			JOptionPane.showMessageDialog(null, "You win!", "", JOptionPane.INFORMATION_MESSAGE);
			running = false;
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//Panel was resized, configure the game to fit new panel's size
		if(PWIDTH != getWidth() || PHEIGHT != getHeight()) 
		{
			PWIDTH = getWidth();
			PHEIGHT = getHeight();
			gl.resize(PWIDTH, PHEIGHT);		
		}	
		g.drawImage(mainBufferedImage, 0, 0, this); //Draw buffred image
	}

	// Only start the animation once the JPanel has been added to the JFrame
	public void addNotify()
	{ 
		super.addNotify();   // creates the peer
		startGame();    // start the thread
	}

	public void startGame()
	{
		(new Thread(this)).start();
	}

	//Function to create buffered image
	private BufferedImage createBufferedImage(String s)
	{
		BufferedImage bf = null;
		try 
		{
			bf = ImageIO.read(getClass().getResource(s));

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return bf;
	}

	//Function to create buffered array for strip image
	public BufferedImage[] loadStripFile(String fName, int n)
	{
		BufferedImage img = createBufferedImage(fName);
		if(img == null)
			return null;
		int w = (int)(img.getWidth() / n);
		int h = img.getHeight();
		int t = img.getColorModel().getTransparency();
		BufferedImage[] res = new BufferedImage[n];
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration();
		Graphics2D g2d;
		for(int i = 0; i < n; i++)
		{
			res[i] = gc.createCompatibleImage(w, h, t);
			g2d = res[i].createGraphics();
			g2d.drawImage(img, 0, 0, w, h, i * w, 0, (i*w) + w, h, null);
		}
		return res;
	}

}