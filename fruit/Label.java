import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Label extends Actor
{
    public Label(String text)
    {
        setText(text);
    }
    
    public Label(String text, int size)
    {
        setText(text, size);
    }
    
    public void setText(String text)
    {
        setText(text, 20);
    }
    
    public void setText(String text, int size)
    {
        setImage(new GreenfootImage(text, size, java.awt.Color.BLACK, new java.awt.Color(0,0,0,0)));
    }

    public void act() 
    {
    }    
}
