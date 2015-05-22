import greenfoot.*;

/**
 * Write a description of class Stain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Stain extends Actor
{
    public Stain()
    {
        GreenfootImage img = getImage();
        int width = img.getWidth()-200;
        int hight = img.getHeight()-200;
        if(width > 0 && hight > 0)
            img.scale(width, hight);
        setImage(img);
    }
    /**
     * Act - do whatever the Stain wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
