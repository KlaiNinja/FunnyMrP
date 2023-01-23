import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Bow here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bow extends Weapon
{
    /**
     * Act - do whatever the Bow wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    int range = 5;//the distance that can be traveled by arrows
    int fireRate = 1;//number of arrows shot per second
    int angle = 0;
    int arrowSpeed = 5;
    private Vector2 direction;
    public Bow(Link player, int dmg, int kb, int range, int fireRate){
        super(player, new GreenfootImage("link's_bow.png"),  dmg, kb, 500, 100, false);
        this.range = range;
        this.fireRate = fireRate;
        this.direction = new Vector2(0, 0);
    }
    public void act()
    {
        // Add your action code here.
        setRotation(angle);
        actWeapon();
    }
    @Override
    public void useWeapon(){
        aimTowardsMouse(new Vector2(player.getX(), player.getY()), Greenfoot.getMouseInfo());
        int xOffset = (int)(20*Math.cos(Vector2.deg2Rad(direction.getAngle())));
        int yOffset = (int)(20*Math.sin(Vector2.deg2Rad(direction.getAngle())));
        setLocation(player.getX() - xOffset, player.getY() - yOffset);
        shootArrow();
    }
    //shoots arrows when the mouse button is pressedawd
    public void shootArrow(){
        if (Greenfoot.mousePressed(null)){
            Arrow arrow = new Arrow(damage, arrowSpeed, direction.getAngle(), 3, range*100);
            getWorld().addObject(arrow, getX(), getY());
            arrow.setRotation((int)angle);
        }
    }
    //aims the bow towards the mouse position
    public void aimTowardsMouse(Vector2 pos, MouseInfo mouse){
        if (mouse == null) return;
        int mX = mouse.getX();
        int mY = mouse.getY();
        direction.set(pos.X() - mX, pos.Y() - mY);
        direction.normalize();
        angle = (int)direction.getAngle() - 45;
    }
}
