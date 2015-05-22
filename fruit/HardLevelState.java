import greenfoot.*;

/**
 * Write a description of class HardLevelState here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HardLevelState extends LevelState 
{
    // instance variables - replace the example below with your own
  
    /**
     * Constructor for objects of class HardLevelState
     */
    public HardLevelState()
    {
        top_score = 200000000;
    }

   public  void Handle(PaintWorld paintworld)
   {
       System.out.println("hard");
       
       
       if(paintworld.sccore_strategy.getScore() >= top_score)   
      {      
         NewGameLevelInfo info = new NewGameLevelInfo("FinishAllLevel.png");
         paintworld.addObject(info, Utility.canvas_width / 2, Utility.canvas_height / 2);
         Greenfoot.stop(); 
       
         System.out.println("Congradulations, You are the best player!");
      }
   }
}
