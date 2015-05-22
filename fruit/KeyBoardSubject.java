import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class KeyBoardSubject here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KeyBoardSubject  
{
       public String SubjectState;
       
        /**
         * Constructor for objects of class KeyBoardSubject
         */
        public KeyBoardSubject()
        {
        }
    
        private List<KeyBoardObserver> observers = new ArrayList<KeyBoardObserver>();

        public void Attach(KeyBoardObserver observer)
        {
            observers.add(observer);
        }
      
        public void Detach(KeyBoardObserver observer)
        {
            observers.remove(observer);
        }
       
        public void Notify()
        {
            for(KeyBoardObserver o : observers)
            {
                o.Update();
            }
        }
        
         public String getSubjectState()
        {
            return SubjectState;           
        }
        
        public void setSubjectState(String val)
        { 
        	SubjectState = val;
        }



}
