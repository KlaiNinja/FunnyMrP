import greenfoot.*;
/**
 * Write a description of class HealthBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HealthBar extends Actor 
{
    // instance variables - replace the example below with your own
    int hp = 5;
    int hpBarWidth = 80;
    int hpBarHeight = 15;
    int pixelsPerHP = (int)hpBarWidth/hp;
    /**
     * Constructor for objects of class HealthBar
     */
    public HealthBar(int hp)
    {
        this.hp = hp;
        update();
    }
    public void act()
    {
        update();
    }
    public void update()
    {
        setImage(new GreenfootImage(hpBarWidth + 2, hpBarHeight + 2));
        GreenfootImage myImage = getImage();
        myImage.setColor(Color.WHITE);
        myImage.drawRect(500, 60, hpBarWidth + 1, hpBarHeight + 1);
        myImage.setColor(Color.RED);
        myImage.fillRect(1, 1, hp*pixelsPerHP, hpBarHeight);
    }
    public void loseHP()
    {
        hp--;
    }
}
