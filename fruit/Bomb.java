import greenfoot.*;

/**
 * Write a description of class Bomb here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bomb extends Fruit implements KeyBoardObserver
{
    private int vertical_step = 20;
    private int horizon_step = 20;
 
    private String observerState;
    private KeyBoardSubject subject;

        
     public Bomb(String img_name, Score sccore_strategy, KeyBoardSubject subject)
    {
        super(img_name, sccore_strategy);
        this.subject = subject;
    }   
   
    public void act()
    {
       super.act();
       
       //System.out.println("has_exploded: " + has_exploded);
       if(has_exploded)   
            return; 
            
       if(Greenfoot.isKeyDown("down"))
        {          
            if(!has_removed_from_world)
              setLocation(getX(), getY() + vertical_step);
        }
        
        if(Greenfoot.isKeyDown("up"))
        {          
              if(!has_removed_from_world)
                setLocation(getX(), getY() - vertical_step);
        }        
        
        if(Greenfoot.isKeyDown("left"))
        {          
              if(!has_removed_from_world)
                 setLocation(getX() - horizon_step, getY());
        }
         if(Greenfoot.isKeyDown("right"))
        {          
               if(!has_removed_from_world)
                 setLocation(getX() + horizon_step, getY());
        }
    }
    
     public void Update( )
     {       
        observerState = subject.SubjectState;
        
        if(observerState.equals("down"))
        {          
            if(!has_removed_from_world)
              setLocation(getX(), getY() + vertical_step);
        }
        
        if(observerState.equals("up"))
        {          
              if(!has_removed_from_world)
                setLocation(getX(), getY() - vertical_step);
        }        
        
        if(observerState.equals("left"))
        {          
              if(!has_removed_from_world)
                 setLocation(getX() - horizon_step, getY());
        }
        
        if(observerState.equals("right"))
        {          
               if(!has_removed_from_world)
                 setLocation(getX() + horizon_step, getY());
        }
     }
}
