/**
 * Write a description of class SimpleScoreStrategy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimpleScoreStrategy implements ScoreStrategy {
    // instance variables - replace the example below with your own

    public int settScore(Class FruitType)  throws Exception {
        int score = 0;
        if (FruitType == Pear.class){
            score += 2;
            System.out.println("pear");
        }else if (FruitType == Watermelon.class){
            score += 2;    
            System.out.println("Watermelyon");
        }else if (FruitType == Orange.class){
             score += 2;
             System.out.println("Orange");
        }
        else if (FruitType == Banana.class){
             score += 2;
              System.out.println("Banana");
        }
        return score;
    }
}
