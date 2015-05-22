import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer; 
import java.util.List;

public class Fruit extends SmoothMover implements FruitSubject
{   
    ActionListener listener;
    private boolean break_already = false;
    protected boolean has_removed_from_world = false;
    boolean BreakOnceFlg = false;
    String brake_img = null;
    int flg = 0;
    double x,y,a=0,b=0,ang,angN,mag,angx,angy,magG;
    int numAng=0;
    
    
    private static int generation_cnt = 0;
    private int prev_position = 0;
    private int cur_position = 0;
    private int canvas_width = Utility.getCanvasWidth( );
    private int base_Xposition = 20;
    private int interval = (canvas_width - base_Xposition * 2) / 8;    
    private int fruit_position_array[ ] = {canvas_width / 2 - 280,canvas_width / 2 - 250,  canvas_width / 2 - 230, canvas_width / 2 , canvas_width / 2 + 230, canvas_width / 2 + 250,  canvas_width / 2 + 280};
    World game_world = getWorld();
        
    private boolean combostatus = false;
    
    protected Score sccore_strategy = null;
    boolean has_set_score = false;
   
    private int rotation_angle = 0;
    
    private int timer = 20;
    private static final int NUM_FRAGMENTS = 20;
    protected boolean has_exploded = false;
    
    private int  boomb_num_threshold = 2;
   
    public Fruit(String brake_img, Score sccore_strategy)
    {
        this.brake_img = brake_img;
        this.sccore_strategy = sccore_strategy;
        has_exploded = false;
    }
    
    public void cut_fruit()
     {
         if(generation_cnt == 0)
           {
               int position_index = Greenfoot.getRandomNumber(fruit_position_array.length);
               cur_position = fruit_position_array[position_index];       
               //int position_index = Greenfoot.getRandomNumber(fruit_position_array.size());
               //cur_position = (int)fruit_position_array.get(position_index);                               
               if(cur_position  == prev_position)
                 cur_position =  (cur_position + 3) % (fruit_position_array.length - 1);
             
               Score sccore_strategy = new Score();
               Fruit fruit_kind_array[ ] = {new Banana("half_banana.png", sccore_strategy), new Pear("half_pear.png", sccore_strategy), 
                                            new Orange("half_orange.png", sccore_strategy), new Watermelon("half_wm.png",sccore_strategy)};
               int fruit_kind = Greenfoot.getRandomNumber(fruit_kind_array.length);
               game_world.addObject(fruit_kind_array[fruit_kind], cur_position, 15);    
               //addObject(fruit_kind_array[fruit_kind], 20, 15); 
               prev_position = cur_position;
           }
           generation_cnt = (generation_cnt + 1) % 24;
     }
     
    protected void addedToWorld(World world){
         //sccore_strategy.setScore(getClass());
         /*
         if(getClass() == Bomb.class)
         {
             Greenfoot.playSound("bomb-explode.wav");
             FinalScoreAndImage.setFinalView(getWorld(), sccore_strategy);
         }
         */
        //test();
          
                
         x=getX();
         y=getY();
         if(outOfBounds(x, y)){
             getWorld().removeObject(this);
             has_removed_from_world = true;           
             return;
            }
         for(int f=0;f<360;f=f+5){
             if(!ok((int)(x+5*Math.cos((f/180.0)*Math.PI)),(int)(y+5*Math.sin((f/180.0)*Math.PI)))){
                 numAng++;
                    angx=angx+Math.cos((f/180.0)*Math.PI);
                    angy=angy+Math.sin((f/180.0)*Math.PI);
                }
            }
        if(numAng!=0)
        {
            getWorld().removeObject(this);
            has_removed_from_world = true;
        }
    }
    
    private boolean outOfBounds(double x, double y)
    {
        return x < 10 || x > getWorld().getWidth() - 10
            || y < 10 || y > getWorld().getHeight() - 10;
    }
    
    public void act() 
    { 
        /*
        timer--;         
            if(timer <= 0)
            {
                explode();
                return;
            } 
         */   
        if(has_exploded)   
            return; 
            
        match_gesture();
        
        if(has_exploded)   
            return; 
            
        rotate_fruit();
      
        x = getX();
        y = getY() + Utility.speed;
        
        if(outOfBounds(x, y)){
            getWorld().removeObject(this);
            has_removed_from_world = true;
            return;
        }              
        setLocation((int)x,(int)y);         
    }    
    
    public boolean ok(int X,int Y){
        PaintWorld world = (PaintWorld)getWorld();
        
        GreenfootImage usersImage = world.getCachedCombinedUserImage();
        if (usersImage != null)
        {
            int ix = (int)X;
            int iy = (int)Y;
            if (ix >= 0 && iy >= 0 &&
                ix < usersImage.getWidth() && 
                iy < usersImage.getHeight() && 
                usersImage.getColorAt(ix, iy).getAlpha() > 0)
            {
                return false;
            }
        }
        

        return true;
    }
    
   
   public void PlayBreakSoundOnce()
   {
      if(!BreakOnceFlg)
      {
          if(getClass() != Bomb.class)
             Greenfoot.playSound("break.wav");
          else
             Greenfoot.playSound("bomb-explode.wav");
           BreakOnceFlg = true;
      }
   }
   
   public void SetScoreOnce()
   {
       /*
        if(getClass() == Bomb.class){
             Greenfoot.playSound("bomb-explode.wav");
             Utility.StopBackgroundMusic();
             ScoreAndImage.setFinalView(getWorld(), sccore_strategy);           
        }
       */
      
        if(!has_set_score){
            Greenfoot.playSound("break.wav");
            long current =  System.currentTimeMillis();
            long gap = current - Utility.prevTime;
           // System.out.println("prevTime:" + Utility.prevTime);
          //  System.out.println("current is "+current);
           // System.out.println("gap: "+gap);
            Utility.prevTime=current;
            if(gap < 3000){
                Utility.combonumber++;
                if(Utility.combonumber <= 1 && combostatus == true){
                    sccore_strategy.setStrategy( new SimpleScoreStrategy());
                    combostatus = false;
                }else if(Utility.combonumber > 1 && combostatus == false){
                    sccore_strategy.setStrategy( new ComboScoreStrategy());
                    PaintWorld world = (PaintWorld)getWorld();   
                    world.addObject(world.combo_info, Utility.canvas_width / 2, Utility.canvas_height / 2);
                    combostatus = true;
                }else{}
            }else{
                Utility.combonumber = 0;
                if(combostatus)
                    sccore_strategy.setStrategy( new SimpleScoreStrategy());
                combostatus = false;
            }
            sccore_strategy.setScore(getClass());
            
            PaintWorld world = (PaintWorld)getWorld();   
            world.levelstate.Handle(world);
            world.score_view.RefreshScoreView(sccore_strategy.getScore());

            has_set_score = true;
        }
        /*
        if(!has_set_score)
          {
               Greenfoot.playSound("break.wav");
               sccore_strategy.setScore(getClass());
               has_set_score = true;
          }*/
   }
   /*
   public void SetScoreOnce()
   {
      if(getClass() == Bomb.class)
      {
             Greenfoot.playSound("bomb-explode.wav");
             FinalScoreAndImage.setFinalView(getWorld(), sccore_strategy);
      }

   }*/
   
   private void match_gesture()
   {
        PaintWorld world = (PaintWorld)getWorld();        
        UserData[] users = world.getTrackedUsers();
        for (UserData user : users)
        {
            if ((Math.abs(user.getJoint(Joint.LEFT_HAND).getY()  -getY()) < 50)
                 && (Math.abs(user.getJoint(Joint.LEFT_HAND).getX()  -getX()) < 50))
            {               
                   setImage(brake_img);
                   PlayBreakSoundOnce();   
                   SetScoreOnce();
                   
                  if(getClass() == Bomb.class){
                            PaintWorld my_world = (PaintWorld)getWorld();
                      
                            world.removeObject(world.combo_info);
                    
                            Utility.combonumber = 0;
                            if(combostatus)
                                sccore_strategy.setStrategy( new SimpleScoreStrategy());
                            combostatus = false;

                            if(Utility.bomb_exploded_number <  boomb_num_threshold)
                            {
                                 ++Utility.bomb_exploded_number;
                                 
                                 
                                 if(Utility.bomb_exploded_number == 1)
                                 {
                                     my_world.removeObject(my_world.three_star);
                                     my_world.addObject(my_world.two_star, Utility.canvas_width  - 85, 40);
                                 }
                                 else if(Utility.bomb_exploded_number == 2)
                                 {
                                      my_world.removeObject(my_world.two_star);
                                      my_world.addObject(my_world.one_star, Utility.canvas_width  - 85, 40);
                                 }
                            }
                             else
                             {
                                my_world.removeObject(my_world.one_star);
                                ScoreAndImage.setFinalView(getWorld(), sccore_strategy);         
                                Utility.bomb_exploded_number = 0;
                                Greenfoot.stop();
                             }
                   }
                   
                   explode();
                  // start(); 
            }   
            else if((Math.abs(user.getJoint(Joint.RIGHT_HAND).getY()  -getY()) < 50)
                 && (Math.abs(user.getJoint(Joint.RIGHT_HAND).getX()  -getX()) < 50))
            {              
                   setImage(brake_img);
                   PlayBreakSoundOnce();
                   SetScoreOnce();
                   
                   if(getClass() == Bomb.class){
                            PaintWorld my_world = (PaintWorld)getWorld();
                            world.removeObject(world.combo_info);
                            Utility.combonumber = 0;
                            if(combostatus)
                                sccore_strategy.setStrategy( new SimpleScoreStrategy());
                            combostatus = false;
                            
                            if(Utility.bomb_exploded_number <  boomb_num_threshold)
                            {
                                 ++Utility.bomb_exploded_number;
                                 
                                 
                                 if(Utility.bomb_exploded_number == 1)
                                 {
                                     my_world.removeObject(my_world.three_star);
                                     my_world.addObject(my_world.two_star, Utility.canvas_width  - 85, 40);
                                 }
                                 else if(Utility.bomb_exploded_number == 2)
                                 {
                                      my_world.removeObject(my_world.two_star);
                                      my_world.addObject(my_world.one_star, Utility.canvas_width  - 85, 40);
                                 }
                            }
                             else
                             {
                                my_world.removeObject(my_world.one_star);
                                ScoreAndImage.setFinalView(getWorld(), sccore_strategy);         
                                Utility.bomb_exploded_number = 0;
                                Greenfoot.stop();
                             }
                   }
                   
                   explode();
                  // start();
            }  
            else if((Math.abs(user.getJoint(Joint.LEFT_FOOT).getY()  -getY()) < 50)
                 && (Math.abs(user.getJoint(Joint.LEFT_FOOT).getX()  -getX()) < 50))
            {              
                   if(user.getJoint(Joint.LEFT_FOOT).getY() < user.getJoint(Joint.RIGHT_KNEE).getY())
                   {
                       setImage(brake_img);
                       PlayBreakSoundOnce();
                       SetScoreOnce();
                       
                       if(getClass() == Bomb.class){
                            PaintWorld my_world = (PaintWorld)getWorld();
                            world.removeObject(world.combo_info);
                            Utility.combonumber = 0;
                            if(combostatus)
                                sccore_strategy.setStrategy( new SimpleScoreStrategy());
                            combostatus = false;
                            if(Utility.bomb_exploded_number <  boomb_num_threshold)
                            {
                                 ++Utility.bomb_exploded_number;
                                 
                                 
                                 if(Utility.bomb_exploded_number == 1)
                                 {
                                     my_world.removeObject(my_world.three_star);
                                     my_world.addObject(my_world.two_star, Utility.canvas_width  - 85, 40);
                                 }
                                 else if(Utility.bomb_exploded_number == 2)
                                 {
                                      my_world.removeObject(my_world.two_star);
                                      my_world.addObject(my_world.one_star, Utility.canvas_width  - 85, 40);
                                 }
                            }
                             else
                             {
                                my_world.removeObject(my_world.one_star);
                                ScoreAndImage.setFinalView(getWorld(), sccore_strategy);         
                                Utility.bomb_exploded_number = 0;
                                Greenfoot.stop();
                             }
                       }
                       
                       explode();
                      // start(); 
                   }
                   
                              
            } 
             else if((Math.abs(user.getJoint(Joint.RIGHT_FOOT).getY()  -getY()) < 50)
                 && (Math.abs(user.getJoint(Joint.RIGHT_FOOT).getX()  -getX()) < 50))
            {              
                   if(user.getJoint(Joint.RIGHT_FOOT).getY() < user.getJoint(Joint.LEFT_KNEE).getY())
                   {
                       setImage(brake_img);
                       PlayBreakSoundOnce();
                       SetScoreOnce();
                       
                       if(getClass() == Bomb.class){
                            PaintWorld my_world = (PaintWorld)getWorld();
                            world.removeObject(world.combo_info);
                            Utility.combonumber = 0;
                            if(combostatus)
                                sccore_strategy.setStrategy( new SimpleScoreStrategy());
                            combostatus = false;
                            if(Utility.bomb_exploded_number <  boomb_num_threshold)
                            {
                                 ++Utility.bomb_exploded_number;
                                 
                                 
                                 if(Utility.bomb_exploded_number == 1)
                                 {
                                     my_world.removeObject(my_world.three_star);
                                     my_world.addObject(my_world.two_star, Utility.canvas_width  - 85, 40);
                                 }
                                 else if(Utility.bomb_exploded_number == 2)
                                 {
                                      my_world.removeObject(my_world.two_star);
                                      my_world.addObject(my_world.one_star, Utility.canvas_width  - 85, 40);
                                 }
                            }
                             else
                             {
                                my_world.removeObject(my_world.one_star);
                                ScoreAndImage.setFinalView(getWorld(), sccore_strategy);         
                                Utility.bomb_exploded_number = 0;
                                Greenfoot.stop();
                             }
                       }
                       
                       explode();
                      // start(); 
                   }            
            } 
        }
      
   }

   public void start()
   {
      listener = new TimePrinter();
      Timer t = new Timer(800, listener);
      t.start();
   }

   class TimePrinter implements ActionListener
   {  
      public void actionPerformed(ActionEvent event)
      {  
          if( (!break_already) && (!has_removed_from_world) )
          {
              getWorld().removeObject(Fruit.this);
              has_removed_from_world = true;
              //PlayBreakSoundOnce();
              break_already = true;
          }   
     }
   }

  public void rotate_fruit()
  {
      rotation_angle = getRotation() + 4;
      if(rotation_angle == 360)
        rotation_angle = 0;
      setRotation(rotation_angle);
  }
  
  public void explode()
    {
        if(!has_exploded)
        {
            System.out.println("step 1");
            has_exploded = true;
             //placeDebris(getX(), getY(), NUM_FRAGMENTS);
            placeStains(getX(), getY(), 2);
            System.out.println("step 5");
            getWorld().removeObject(this);
            System.out.println("step 6");          
        }
       
        
    }
    
    private void placeDebris(int x, int y, int numFragments)
    {
      
        while(numFragments >= 0)
        {
           
           if(getClass() == Bomb.class)
           {
           }
           else
              getWorld().addObject( StainFactory.CreateStain(getClass()), x, y);
          
         
            numFragments--;
        }
    }
    
      private void placeStains(int x, int y, int stainNum)
    {
         System.out.println("step 2");
        //remove old stains
        List<Stain> stainList = getWorld().getObjects(Stain.class);
        for(Stain s: stainList)
        {
            getWorld().removeObject(s);
        }
          System.out.println("step 3");
        for(int i = 0; i < stainNum; i++)
        {
            //set stain position
            x = x - 50 + Greenfoot.getRandomNumber(100);
            y = y - 50 + Greenfoot.getRandomNumber(100);
          
            
           if(getClass() == Bomb.class)
           {
           }
           else
           {
              System.out.println("step 4.1");
              getWorld().addObject( StainFactory.CreateStain(getClass()), x, y);
              System.out.println("step 4.2");
            }
         
            
        }
    }
    
    
    public void act_back() 
    {       
        match_gesture();   
        rotate_fruit();
        //start();
        x=x+a;
        y=y+b;
        
        if(outOfBounds(x, y)){
            getWorld().removeObject(this);
            has_removed_from_world = true;
            return;
        }
        
        numAng=0;
        angx=0;
        angy=0;
        for(int f=0;f<360;f=f+10){
            if(!ok((int)(x+5*Math.cos((f/180.0)*Math.PI)),(int)(y+5*Math.sin((f/180.0)*Math.PI)))){
                numAng++;
                angx=angx+Math.cos((f/180.0)*Math.PI);
                angy=angy+Math.sin((f/180.0)*Math.PI);
            }
        }
            
        if(numAng>3){
            int numLoops = 0;
                        
            while(numAng>20 && !Greenfoot.isKeyDown("c") && numLoops < 10){
                numAng=0;
                x=x-.1*angx;
                y=y-.1*angy;
                angx=0;
                angy=0;
                for(int f=0;f<360;f=f+10){
                    if(!ok((int)(x+5*Math.cos((f/180.0)*Math.PI)),(int)(y+5*Math.sin((f/180.0)*Math.PI)))){
                        numAng++;
                        angx=angx+Math.cos((f/180.0)*Math.PI);
                        angy=angy+Math.sin((f/180.0)*Math.PI);
                    }
                }
                numLoops += 1;
            }
            numLoops = 0;
            while(numAng!=0 && !Greenfoot.isKeyDown("c") && numLoops < 10){
                numAng=0;
                y=y-.1*angy;
                x=x-.1*angx;
                for(int f=0;f<360;f=f+5){
                    if(!ok((int)(x+5*Math.cos((f/180.0)*Math.PI)),(int)(y+5*Math.sin((f/180.0)*Math.PI)))){
                        numAng++;
                    }
                }
                numLoops += 1;
            }
            x=x-.1*a;
            y=y-.1*b;
            angN = Math.PI/2.0 + Math.atan2(angy,angx);
            mag = Math.sqrt(a*a + b*b);
            if(mag>.7)mag=mag-.5;
            ang = Math.atan2(b,a);
            if(Math.abs(angN-ang)<.7){
                angN = angN-.05;
                if(angN<ang)angN=angN+.1;
            }
            ang = -(ang-angN)+angN;
            a = mag*Math.cos(ang)*(.3 + .7*Math.abs(Math.cos(angN)));
            b = mag*Math.sin(ang)*(.3 + .7*Math.abs(Math.sin(angN)));
            magG = Math.sin(angN);
            if(angN>Math.PI/2.0 && angN<3*Math.PI/2.0){
                a = a + magG*Math.cos(angN);
                b = b + magG*Math.sin(angN);
            }
            a= (int)(a*40)/40.0;
            b= (int)(b*40)/40.0;
            if(Math.abs(a)<.01)a=Math.random()/10.0-.05;
            

        }else {
            if(Math.sqrt(a*a + (b+.1)*(b+.1)) < 8)b=b+.1;
        }

        setLocation((int)x,(int)y); 
        
    }    
    
    
    public void test()
   {
               
        if(!has_set_score){
            Greenfoot.playSound("break.wav");
            long current =  System.currentTimeMillis();
            long gap = current - Utility.prevTime;
            System.out.println("prevTime:" + Utility.prevTime);
            System.out.println("current is "+current);
            System.out.println("gap: "+gap);
            Utility.prevTime=current;
            if(gap < 1500){
                Utility.combonumber++;
                if(Utility.combonumber <= 1 && combostatus == true){
                    sccore_strategy.setStrategy( new SimpleScoreStrategy());
                    combostatus = false;
                }else if(Utility.combonumber > 1 && combostatus == false){
                    sccore_strategy.setStrategy( new ComboScoreStrategy());
                    combostatus = true;
                }else{}
            }else{
                Utility.combonumber = 0;
                if(combostatus)
                    sccore_strategy.setStrategy( new SimpleScoreStrategy());
                combostatus = false;
            }
            sccore_strategy.setScore(getClass());
            //ScoreAndImage.RefreshScore((PaintWorld)getWorld(), sccore_strategy);
            
            PaintWorld world = (PaintWorld)getWorld();   
            world.levelstate.Handle(world);
            world.score_view.RefreshScoreView(sccore_strategy.getScore());

            has_set_score = true;
        }
        /*
        if(!has_set_score)
          {
               Greenfoot.playSound("break.wav");
               sccore_strategy.setScore(getClass());
               has_set_score = true;
          }*/
   }
    
}

  
