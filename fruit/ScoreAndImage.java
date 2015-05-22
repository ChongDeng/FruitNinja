import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class FinalScoreAndImage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScoreAndImage  
{
  
   public static void setFinalView(World w, Score sccore_strategy){
       Greenfoot.stop();            
       Utility.StopBackgroundMusic();
       String score_str = String.valueOf(sccore_strategy.getScore());
       String info = "Game Over!" + "\n Your Score: " + score_str;
       Label instr = new Label(info, 80);             
       //getBackground().setColor(Color.RED);
       w.addObject(instr,  w.getBackground().getWidth() / 2,   w.getBackground().getHeight() / 2);   
   }
   
   public static void RefreshScore(World w, Score sccore_strategy){
     
       /*
       String score_str = String.valueOf(sccore_strategy.getScore());
       String info = "Your Score: " + score_str;
       Label instr = new Label(info, 30);             
       //getBackground().setColor(Color.RED);
       w.addObject(instr, 40, 40);  
       */
      
        String score_str = String.valueOf(sccore_strategy.getScore());
       String info = "Your Score: " + score_str;
       Label instr = new Label(info, 30);             
       //getBackground().setColor(Color.RED);
       w.addObject(instr, 40, 40);  
       System.out.println("yes");
      //  setImage(new GreenfootImage("" + info, 20, Color.WHITE, Color.BLACK));
   }
   
  
}
