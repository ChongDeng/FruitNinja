/**
 * Write a description of class FruitProxy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FruitProxy  implements FruitSubject
{
  private Fruit f = null; 
  
   public void cut_fruit()
   {
       if(f == null)
       {
          Fruit f = new Fruit("", new Score());
       }       
       f.cut_fruit();
   }
}
