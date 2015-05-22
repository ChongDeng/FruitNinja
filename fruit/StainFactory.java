/**
 * Write a description of class StainFactory here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StainFactory  
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class StainFactory
     */
    public StainFactory()
    {
    }

    public static Stain CreateStain(Class FruitType)
    {
        Stain stain = null;
        if (FruitType == Pear.class){
            stain = new YellowStain();
        }else if (FruitType == Watermelon.class){
            stain = new RedStain();
        }else if (FruitType == Orange.class){
            stain = new OrangeStain();
        }
        else if (FruitType == Banana.class){
              stain = new YellowStain();
        }
       
        return stain;        
    }
}
