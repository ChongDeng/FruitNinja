import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;  
import javax.swing.Timer; 

/**
 * Write a description of class PaintingWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PaintWorld extends KinectWorld
{
    private GreenfootImage cachedUserImage;
    private static final int THUMBNAIL_WIDTH = 80;
    private static final int THUMBNAIL_HEIGHT = 60;
    
    private static final int SPAWN_EVERY = 4;
    private int spawnCounter = SPAWN_EVERY;
   
    private long leftHandUp;
    private Label leftHandWarning;    

    private static int generation_cnt = 0;
    
    private int canvas_width = getWidth();    
    private int base_Xposition = 20;
    private int interval = (canvas_width - base_Xposition * 2) / 8;    
    private int fruit_position_array[ ] = {canvas_width / 2 - 280,canvas_width / 2 - 250,  canvas_width / 2 - 220, canvas_width / 2 - 190, canvas_width / 2 - 160, canvas_width / 2 - 130, canvas_width / 2 - 100, canvas_width / 2 - 70, canvas_width / 2 - 35, canvas_width / 2 , canvas_width / 2 + 35, canvas_width / 2 + 70, canvas_width / 2 + 100, canvas_width / 2 + 130, canvas_width / 2 + 160,canvas_width / 2 + 190,canvas_width / 2 + 220, canvas_width / 2 + 250,  canvas_width / 2 + 280};
    //private ArrayList fruit_position_array = new ArrayList();
   
    private int prev_position = 0;
    private int cur_position = 0;
    
    private  ActionListener listener;
    private boolean has_stopped = false;
    private boolean has_started_timer = false;
    
    GreenfootImage warn_info = new GreenfootImage(getBackground().getWidth() / 2,  getBackground().getHeight() / 2);
    GreenfootImage warn_img =  new GreenfootImage("time_out.png");
    GreenfootImage image = getBackground();
    FruitSubject p = new FruitProxy();    
    
    public Score sccore_strategy = new Score();
    
    private boolean background_music_played = false;
    private KeyBoardSubject keyboardsubject  = new KeyBoardSubject();
    
    public LevelState levelstate = new EasyLevelState();
    public ScoreView score_view; 
    
    public LifeCountInfo three_star = new LifeCountInfo( );
    public LifeCountInfo two_star = new LifeCountInfo("two_star.png");
    public LifeCountInfo one_star = new LifeCountInfo("one_star.png");
    
    public NewGameLevelInfo combo_info = new NewGameLevelInfo("Combo.png");  
     
    public PaintWorld()
    {    
        super(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, 1.0, false); 
        
        int width = getWidth();
        int height = getHeight();
        Utility.setCanvasWidth(width);
        Utility.setCanvasHeight(height);
        addObject(new Instructions(), width/2, height/2);
        set_canvas_label();    
                
        score_view = new ScoreView();
        addObject(score_view, 80, 40);       
        
        
       
        addObject(three_star, Utility.canvas_width  - 85, 40);
      
       // ScoreAndImage.RefreshScore(this, sccore_strategy);
        //Greenfoot.playSound("background.wav");
    }
    
    public void act()
    {
       
        super.act();              
        generate_fruits();   
        //begin_cut();
        set_play_time_once(600000);
        PlayBackgoundSoundOnce();       
        
        if (!isConnected())
            return;
        
        //getBackground().setColor(java.awt.Color.WHITE);
        //getBackground().fill();
        setBackground("bacground.png"); 
        GreenfootImage userImage = getCombinedUserImage();
        userImage.scale(getWidth(), getHeight());
        getBackground().drawImage(userImage, 0, 0);     
        
        cachedUserImage = getCombinedUserImage();    
    }
  
 
  private void generate_fruits()
  {
         
           if(generation_cnt == 0)
           {
               int position_index = Greenfoot.getRandomNumber(fruit_position_array.length);
               cur_position = fruit_position_array[position_index];       
               //int position_index = Greenfoot.getRandomNumber(fruit_position_array.size());
               //cur_position = (int)fruit_position_array.get(position_index);                               
               if(cur_position  == prev_position)
                 cur_position =  (cur_position + 4) % (fruit_position_array.length - 1);
                 
                 
               //addObject(new Watermelon(), cur, 15);    
               //addObject(new Watermelon("half_wm.png"), cur, 15);     
               //addObject(new Banana("half_banana.png"), cur, 15);  
               //addObject(new Orange("half_orange.png"), cur, 15);  
               //addObject(new Pear("half_pear.png"), cur, 15);  
               //addObject(new Watermelon("half_wm.png"), cur, 15); 
               
               // Fruit fruit_kind_array[ ] = { new Bomb("bomb.png", sccore_strategy)};
                
               /*
                Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                           };
               */                             
              /*
              Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                            new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                            
                                            new Bomb("bomb.png", sccore_strategy, keyboardsubject)};
              */
             
             if(levelstate.getClass() == EasyLevelState.class)
             {
                 Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                            new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                            
                                            new Bomb("bomb.png", sccore_strategy, keyboardsubject)};
                                            
               int fruit_kind = Greenfoot.getRandomNumber(fruit_kind_array.length);
               addObject(fruit_kind_array[fruit_kind], cur_position, 15);    
               //addObject(fruit_kind_array[fruit_kind], 20, 15); 
               prev_position = cur_position;
             }
             if(levelstate.getClass() == MediumLevelState.class)
             {
                 Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                           
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                            
                                            new Bomb("bomb.png", sccore_strategy, keyboardsubject)};
                                            
               int fruit_kind = Greenfoot.getRandomNumber(fruit_kind_array.length);
               addObject(fruit_kind_array[fruit_kind], cur_position, 15);    
               //addObject(fruit_kind_array[fruit_kind], 20, 15); 
               prev_position = cur_position;
             }
              if(levelstate.getClass() == HardLevelState.class)
             {
                 Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),                                          
                                            new Bomb("bomb.png", sccore_strategy, keyboardsubject), new Bomb("bomb.png", sccore_strategy, keyboardsubject)};
                                            
               int fruit_kind = Greenfoot.getRandomNumber(fruit_kind_array.length);
               addObject(fruit_kind_array[fruit_kind], cur_position, 15);    
               //addObject(fruit_kind_array[fruit_kind], 20, 15); 
               prev_position = cur_position;
             }
             
             
             
               /*
               Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                            new Bomb("bomb.png", sccore_strategy, keyboardsubject)};
               */                             
                                           /*
                                           Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy),
                                           };
                                           */
                                            
              
           }
           generation_cnt = (generation_cnt + 1) % 24;
  }
  
  private void begin_cut( )
  {          
           p.cut_fruit( );          
  }
  
  private void set_canvas_label()
  {
        //int width = getWidth();
        //int height = getHeight();
        
        Label instr = new Label("Work by CMPE202 Team 18:", 20);
        addObject(instr,  getBackground().getWidth() - instr.getImage().getWidth(), getBackground().getHeight() - (instr.getImage().getHeight() / 2) - 70);           
        Label instr2 = new Label("Yue Shen, Xiaoxiao Li", 20);
        addObject(instr2,  getBackground().getWidth() - instr2.getImage().getWidth() / 2 - 10, getBackground().getHeight() - (instr2.getImage().getHeight() / 2) - 50);     
        Label instr3 = new Label("LingQian Xie,  Kai Yao", 20);
        addObject(instr3,  getBackground().getWidth() - instr3.getImage().getWidth() / 2 - 10, getBackground().getHeight() - (instr3.getImage().getHeight() / 2) - 30);     
        Label instr4 = new Label("Chong Deng", 20);
        addObject(instr4,  getBackground().getWidth() - instr4.getImage().getWidth() / 2 - 10, getBackground().getHeight() - (instr4.getImage().getHeight() / 2) - 10);     
  }
  
  public GreenfootImage getCachedCombinedUserImage()
    {
        return cachedUserImage;
    }
    
  public void set_play_time_once(int time_val)
  {
      if(!has_started_timer)
      {
           listener = new TimePrinter();
           Timer t = new Timer(time_val, listener);
           t.start();
           has_started_timer = true;
      }
     
  }
  

   class TimePrinter implements ActionListener
   {  
     
      public void actionPerformed(ActionEvent event)
      {  if(!has_stopped)
         {
             Greenfoot.stop();
             Utility.StopBackgroundMusic();
             has_stopped = true;
             String score_str = String.valueOf(sccore_strategy.getScore());
             String info = "Game Over!" + "\n Your Score: " + score_str;
             Label instr = new Label(info, 80);             
             getBackground().setColor(Color.RED);
             addObject(instr,  getBackground().getWidth() / 2,  getBackground().getHeight() / 2);   
             
             /*
             GreenfootImage background = getBackground();
             background.setColor(Color.RED);
             Font font=new Font("",Font.BOLD,50);
             background.setFont(font);
             background.drawString("Time is out!", getBackground().getWidth() / 2 - 150,  getBackground().getHeight() / 2 );
             */
             
             //addObject(instr,  getBackground().getWidth() - instr.getImage().getWidth(), getBackground().getHeight() - (instr.getImage().getHeight() / 2) - 120);     
             //GreenfootImage warn_info = new GreenfootImage("time_out.png");
            
             
             //image.drawImage(warn_img,  getBackground().getWidth() / 2,  getBackground().getHeight() / 2);
         }
     }
   }
   
  public void PlayBackgoundSoundOnce()
  {
      Utility.StartBackgroundMusic();
      /*
      if(!background_music_played)
      {
         Greenfoot.playSound("background.wav");
         background_music_played = true;
      }
      */
      
      
      /*
      if(!background_music_played)
      {
         Greenfoot.playSound("background.wav");
         background_music_played = true;
      }
      */
  }
  
  public void UpdateKeyBoardSubjectState()
  {
       if(Greenfoot.isKeyDown("down"))            
           keyboardsubject.SubjectState ="down";
       else if(Greenfoot.isKeyDown("up"))            
           keyboardsubject.SubjectState ="up";
       else if(Greenfoot.isKeyDown("left"))            
           keyboardsubject.SubjectState ="left";
       else if(Greenfoot.isKeyDown("right"))            
           keyboardsubject.SubjectState ="right";    
  }
}
