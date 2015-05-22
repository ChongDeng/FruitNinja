/**
 * Write a description of class Score here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Score{
    // instance variables - replace the example below with your own
    private int score;
    private ScoreStrategy strategy;
    /**
     * Constructor for objects of class Score
     */
    public Score(){
        score = 0;
        strategy = new SimpleScoreStrategy();
    }

    public void setStrategy(ScoreStrategy s){
        strategy = s;
    }

    public void setScore(Class FruitType){
        try{
            score += strategy.settScore(FruitType);
            System.out.println(score);
        }catch (Exception e){
            System.out.println(e.toString());
        }
       
    }
 
    public int getScore(){
        return score;
    }
}
