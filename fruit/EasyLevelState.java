/**
 * Write a description of class EasyLevelState here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EasyLevelState extends LevelState 
{
    // instance variables - replace the example below with your own
       
    /**
     * Constructor for objects of class EasyLevelState
     */
    public EasyLevelState()
    { 
        top_score = 20;
    }

   public  void Handle(PaintWorld paintworld)
   {
       System.out.println("easy");
      if(paintworld.sccore_strategy.getScore() >= top_score)   
      {
         
         NewGameLevelInfo info = new NewGameLevelInfo();
         paintworld.addObject(info, Utility.canvas_width / 2, Utility.canvas_height / 2);
         
         paintworld.levelstate = new MediumLevelState();
         System.out.println("Now Switch to medium level!");
         Utility.speed =  Utility.speed  + 6;
      }
   }
}
