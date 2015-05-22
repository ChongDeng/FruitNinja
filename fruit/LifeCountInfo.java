import greenfoot.*;

/**
 * Write a description of class LifeCountInfo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LifeCountInfo extends Actor
{
    GreenfootImage begin = null;
    
     public LifeCountInfo()
    {
        begin = new GreenfootImage("three_star.png");
    }
    
    public LifeCountInfo(String img)
    { 
         begin = new GreenfootImage(img);
    }
    
    protected void addedToWorld(World world){        
        //begin.scale(world.getWidth() /2 , world.getHeight() / 2);
        setImage(begin);
    }
}
