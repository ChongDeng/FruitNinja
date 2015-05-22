import greenfoot.*;

/**
 * Write a description of class Utility here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Utility  
{
    // instance variables - replace the example below with your own
    public static int canvas_width;
    public static int canvas_height;
    
    public static long prevTime = 0;
    public static int combonumber = 0;
    
    public static GreenfootSound music = new GreenfootSound("background.wav");
    private static boolean hasPlayedMusic = false; 
    public static int bomb_exploded_number = 0;
    public static int speed = 5; 

    /**
     * Constructor for objects of class Utility
     */
    public Utility()
    {
    }

    public static void setCanvasWidth(int val)
    {
       canvas_width = val;
    }
    
    public static int getCanvasWidth( )
    {
       return canvas_width;
    }
    
     public static void setCanvasHeight(int val)
    {
       canvas_height = val;
    }
    
    public static int getCanvasHeight( )
    {
       return canvas_height;
    }
    
  
    
    public static void StartBackgroundMusic()
    {
         if(!hasPlayedMusic)
         {
             music.playLoop();
             hasPlayedMusic = true;
         }
    }
    
    public static void StopBackgroundMusic()
    {
         music.stop();
    }

    
}
