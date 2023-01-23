import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class EnemyBug here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemyBug extends Enemies
{
    public EnemyBug(int maxHealth, int atk, int roomID){
        super(maxHealth, atk, roomID);
    }
    int health = 5;
    int startX = 500;
    int startY = 90;
    public void act()
    {
        getDir( getRotation() );
        currentDirToNEWS(); //NEWS north east west south
        Linkcheck();
        isHit();
    }
    //This method moves the enemy like a homing missle as soon as it sees some player
    public void Linkcheck(){
        timer++;
        Actor Link = getWorld().getObjects(Link.class).get(0);
        if (!(isTouching(Wall.class))){ 
            if (getObjectsInRange(40*4, Link.class).size() > 0 ) {
                turnTowards(Link.getX(), Link.getY());
                slowedmove(2);
            } else {
                if (Math.abs(getX() - startX) < 10){
                    if (Math.abs(getY() - startY) < 10){
                        this.randomMove();
                    }
                } 
            }
            if (getObjectsInRange(35, Link.class).size() > 0){
                move(-1);
            }
        } 
    }
}
