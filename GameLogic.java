import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameLogic 
{
	//Define variables
	private final int asSpeed = 5, spSpeed = 4, shotSpeed = 10, ASTRNUM = 4, MAXFIREBALLS = 100, SPACE = 200, TRANSFORM = 3; 
	private int pwidth, pheight, num, life = 3;
	private Random randomGenerator = new Random(); //To generate random location
	private Asteroid currentAstroid1, currentAstroid2 ;
	private boolean spaceShipHitted = false;
	private boolean gameOver = false, win = false;

	//Create game objects
	protected SpaceShip gameSpaceShip;
	private Asteroid[] asteroids;
	private FireBall[] fireballs;

	//Create buffered images
	private BufferedImage bufferdSpaceshipImg = null;
	private BufferedImage bufferedAstroidSmall = null;
	private BufferedImage bufferedAstroidMiddle = null;
	private BufferedImage bufferedAstroidBig = null;

	public GameLogic(int pwidth, int pheight, BufferedImage a, BufferedImage b, BufferedImage c, BufferedImage d)
	{
		this.pwidth = pwidth;
		this.pheight = pheight;
		bufferdSpaceshipImg = a;
		bufferedAstroidSmall = b;
		bufferedAstroidMiddle = c ;
		bufferedAstroidBig = d;
		num = (int) Math.pow(ASTRNUM, TRANSFORM); // Create array in a size of maximum possible asteroids after transform
		//Create game objects
		gameSpaceShip = new SpaceShip((pwidth-bufferdSpaceshipImg.getWidth(null))/2, (pheight-bufferdSpaceshipImg.getHeight(null))/2, pwidth, pheight, bufferdSpaceshipImg, spSpeed, 0);
		fireballs = new FireBall[MAXFIREBALLS];
		createAstroids();

	}
	//Function to update the game every move
	public void gameUpdate()
	{
		//Update location for each asteroid
		for(int i = 0; i< asteroids.length; i++)
		{
			if(asteroids[i] != null)
				asteroids[i].updateSprite();
		}
		//Update location for each fireball
		for(FireBall f: fireballs)
		{
			if(f != null)
				f.updateSprite();
		}
		checkCollision(); //Check if was collision
		checkWinning(); // Did you win the game?
	}

	public int getLifeNum()
	{
		return life;
	}

	//Function to create asteroids
	private void createAstroids()
	{
		Rectangle r;
		r = new Rectangle(gameSpaceShip.x - SPACE, gameSpaceShip.y - SPACE, gameSpaceShip.imageWidth + SPACE, gameSpaceShip.imageHeight + SPACE);
		asteroids = new Asteroid[num];
		for (int i= 0; i< ASTRNUM; i++) // Create constant number of asteroids
		{			
			do
			{
				//Random place on screen and random angle for move
				int x = randomGenerator.nextInt(pwidth);
				int y = randomGenerator.nextInt(pheight);
				asteroids[i] = new Asteroid(x, y, pwidth, pheight, bufferedAstroidBig, asSpeed, randomGenerator.nextInt(360));
			}
			while(asteroids[i].getBoundingBox().intersects(r)); //If too close to spaceship, regenerate

		}
	}
	
	//Drawing all objects of the game
	public void drawGame(Graphics2D g2d)
	{
		//Draw fireballs
		for(int i=0; i<fireballs.length; i++)
		{
			if(fireballs[i] != null)
				fireballs[i].drawSprite(g2d);
		}
		//Draw spaceship
		if(!spaceShipHitted)
			gameSpaceShip.drawSprite(g2d);
		
		//Draw asteroids
		for(int i = 0; i< asteroids.length; i++)
		{
			if(asteroids[i] != null)
				asteroids[i].drawSprite(g2d);
		}
	}

	private void checkCollision()
	{
		//Check for all asteroids
		for(int i = 0; i < asteroids.length; i++)
		{
			if (asteroids[i] != null)
			{
				//Check if asteroid hits the spaceship
				if (currentAstroid1 != asteroids[i] && currentAstroid2 != asteroids[i] && asteroids[i].getBoundingBox().intersects(gameSpaceShip.getBoundingBox()))
				{
					currentAstroid1 = asteroids[i];
					splitAstroid(asteroids[i], i);
					spaceShipHitted = true;
				}

				//Check if some fireball hit's some asteroid
				for(int j=0; j < fireballs.length; j++)
				{
					if(fireballs[j] != null) 
					{
						try
						{
							if(asteroids[i].getBoundingBox().intersects(fireballs[j].getBoundingBox()))
							{
								fireballs[j] = null;
								splitAstroid(asteroids[i], i);
							}
						}
						catch(Exception e){}
					}
				}
			}
		} 
	}

	//Function to split asteroid to 2 asteroids or destroy it 
	private void splitAstroid(Asteroid as, int j)
	{
		BufferedImage p = null;
		//Picture depends on size
		switch (as.getSize()) {
		case 3:
			p = bufferedAstroidMiddle;
			as.setSize(2);
			break;
		case 2:	
			p = bufferedAstroidSmall;
			as.setSize(1);
			break;
		default:
			as.setSize(0);
			p = null;
			break;
		}
		if (p!= null) // Transform
		{
			as.bImage = p;
			int an = randomGenerator.nextInt(360);
			as.angle = Math.toRadians(an);
			as.speed += 2;
			an = randomGenerator.nextInt(360);
			Asteroid newAs = new Asteroid(as.x, as.y, pwidth, pheight, p, as.speed, an);
			newAs.setSize(as.getSize());

			for(int i=0; i< asteroids.length; i++)
			{
				if(asteroids[i] == null)
				{
					asteroids[i] = newAs;
					currentAstroid2 = newAs;
					break;
				}
			}
		}
		else //destroy
		{
			as = null;
		}
		asteroids[j] = as;
	}

	//Shoot fireball 
	public void shoot()
	{
		//Create fireball and save in array
		for(int i = 0; i<fireballs.length; i++)
		{
			if (fireballs[i] == null)
			{
				fireballs[i] = new FireBall(0, 0, pwidth, pheight, null, shotSpeed, gameSpaceShip.angle);
				fireballs[i].x = gameSpaceShip.x + gameSpaceShip.imageWidth/2 - fireballs[i].imageWidth/2;
				fireballs[i].y = gameSpaceShip.y + gameSpaceShip.imageHeight/2 - fireballs[i].imageHeight/2;
				break;
			}
			else  //If fireball get out of borders, destroy it
			{
				if(fireballs[i].getDestroy())
					fireballs[i] = null;
			}
		}
	}

	//The are 3 trials, if not over, start spaceship from begin point 
	public void startAgain()
	{
		life--;
		if(life == 0)
		{
			gameOver = true;
			return;
		}

		spaceShipHitted = false;
		currentAstroid1 = null;
		currentAstroid2 = null;
		gameSpaceShip = new SpaceShip((pwidth-bufferdSpaceshipImg.getWidth(null))/2, (pheight-bufferdSpaceshipImg.getHeight(null))/2, pwidth, pheight, bufferdSpaceshipImg, spSpeed, 0);
	}

	//Function to check if any asteroids left - if not -you win!
	private void checkWinning()
	{
		win = true;
		for(int i=0; i< asteroids.length; i++)
		{
			if(asteroids[i] != null)
			{
				win = false;
			}
		}
	}

	//If panel was resized, suite the game for new panel's size
	public void resize(int pwidth, int pheight)
	{
		this.pwidth = pwidth;
		this.pheight = pheight;
		gameSpaceShip.pWidth = pwidth;
		gameSpaceShip.pHeight = pheight;
		for(int i=0; i<asteroids.length; i++)
		{
			if(asteroids[i] != null)
			{
				asteroids[i].pWidth = pwidth;
				asteroids[i].pHeight = pheight;
			}
		}
	}

	//-----------------Getters--------------------- 
	public boolean getSpaceShipHitted()
	{
		return spaceShipHitted;
	}

	public boolean getGameOver()
	{
		return gameOver;
	}

	public boolean getWin()
	{
		return win;
	}
}
