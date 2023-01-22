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
    Collider collider;
    int health = 3;
    int startX = 400;
    int startY = 400;
    public void act()
    {
        getRot( getRotation() );
        rotate();
        Linkcheck();
        isHit();
    }

    public void rotate(){ ///left is 180, down is 90, right is 0, up is 270
        switch(currentDir){
            case up:
                setRotation(270);
                break;
            case down:
                setRotation(90);
                break;
            case left:
                setRotation(180);
                break;
            case right:
                setRotation(0);
                break;
        }
    }
    //This method moves the enemy like a homing missle as soon as it sees some player
    public void Linkcheck()  {
        timer++;
        Actor Link = getWorld().getObjects(Link.class).get(0);
        if (!(getWorld().getObjects(Link.class).isEmpty() ) ){
            if (getObjectsInRange(40*4, Link.class).size() > 0 ) {
                turnTowards(Link.getX(), Link.getY());
                slowedmove(2);
            } else {
                randomMove();
            }
        } else {
            return;
        }
    }


}
