import greenfoot.*;
import java.awt.Color;
/**
 * Write a description of class Counter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ScoreView  extends Actor
{
    private int totalCount = 0;
    public ScoreView()
    {
       setImage(new GreenfootImage("Your Score: " + totalCount, 30, Color.RED, Color.WHITE));
    }

    /**
     * Increase the total amount displayed on the counter, by a given amount.
     */
    public void bumpCount(int amount)
    {
        totalCount += amount;
        setImage(new GreenfootImage("Your Score: " + totalCount, 30, Color.RED, Color.WHITE));
    }
    
    public void RefreshScoreView(int score)
    {        
        setImage(new GreenfootImage("Your Score: " + score, 30, Color.RED, Color.WHITE));
    }
    
   
    
}
