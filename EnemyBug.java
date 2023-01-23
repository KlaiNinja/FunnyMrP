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
    int health = 5;
    int startX = 400;
    int startY = 400;
    public EnemyBug(int maxHealth, int atk, int roomID){
        super(maxHealth, atk, roomID);
    }
    public void act()
    {
        Linkcheck();
    }
    //This method moves the enemy like a homing missle as soon as it sees some player
    public void Linkcheck(){
        timer++;
        Actor Link = getWorld().getObjects(Link.class).get(0);
        if (getObjectsInRange(40*4, Link.class).size() > 0 ) {
            turnTowards(Link.getX(), Link.getY());
            slowedmove(2);
        } else {
            this.randomMove();
        }
    }
}
