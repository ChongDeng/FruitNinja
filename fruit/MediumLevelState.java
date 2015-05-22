/**
 * Write a description of class MediumLevelState here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MediumLevelState extends LevelState 
{
    // instance variables - replace the example below with your own
  

    /**
     * Constructor for objects of class MediumLevelState
     */
    public MediumLevelState()
    {
        top_score = 45;
    }

    public  void Handle(PaintWorld paintworld)
   {
       System.out.println("medium");
      if(paintworld.sccore_strategy.getScore() >= top_score)   
      {
         NewGameLevelInfo info = new NewGameLevelInfo();
         paintworld.addObject(info, Utility.canvas_width / 2, Utility.canvas_height / 2);
         
         paintworld.levelstate = new HardLevelState();
         System.out.println("Now Switch to hard level!");
         Utility.speed =  Utility.speed  + 6;
      }
       
   }
}
