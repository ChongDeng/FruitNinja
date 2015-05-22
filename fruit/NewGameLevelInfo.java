import greenfoot.*;

/**
 * Write a description of class NewGameLevelInfo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NewGameLevelInfo extends Actor
{
    private int timer = 50;
    GreenfootImage begin = null;
    
    public NewGameLevelInfo()
    {
        begin = new GreenfootImage("EnterNextLevel.png");
    }
    
    public NewGameLevelInfo(String img)
    { 
         begin = new GreenfootImage(img);
    }
     
    /**
     * Act - do whatever the NewGameLevelInfo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
            timer--;         
            if(timer <= 0)
            { 
                getWorld().removeObject(this);
            } 
             
        // Add your action code here.
    }
    
    
    
    protected void addedToWorld(World world){        
        begin.scale(world.getWidth() /2 , world.getHeight() / 2);
        setImage(begin);
    }
    
  
}