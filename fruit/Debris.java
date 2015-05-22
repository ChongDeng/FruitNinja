import greenfoot.*;

/**
 * Write a description of class Debris here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Debris extends SmoothMover
{
    private static final int FORCE = 20;
    public Debris()
    {
        int direction = Greenfoot.getRandomNumber(360);
        int speed = Greenfoot.getRandomNumber(FORCE);
        increaseSpeed(new Vector(direction, speed));
        
        //random image size
        GreenfootImage img = getImage();
        int width = Greenfoot.getRandomNumber(300);
        int height = Greenfoot.getRandomNumber(300);
        img.scale(width+1, height+1);
        
        setRotation(Greenfoot.getRandomNumber(360));
    }
    
    /**
     * Act - do whatever the Debris wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        increaseSpeed(GRAVITY);
        move(5);
        if(isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }    
}
