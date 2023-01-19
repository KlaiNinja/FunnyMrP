import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EnemyBug here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemyBug extends Enemies
{
    int dir = 0;
    int health = 3;
    public EnemyBug(){
        //Enemies.setHP(5);   
    }

    public void act()
    {
        Linkcheck();
    }
    //This method moves the enemy like a homing missle as soon as it sees some player
    public void Linkcheck()  {
        Actor Link = getWorld().getObjects(Link.class).get(0);
        if (!(getWorld().getObjects(Link.class).isEmpty())){
            if ( getObjectsInRange(40*4, Link.class).size() > 0 ) {
                 if (getObjectsInRange(45, Wall.class).size() <= 0 ) {
                turnTowards(Link.getX(), Link.getY());
                move(1);
                }
            } 
        } else {
            return;
        }
    }  
    public void kb(){
        
    }
}
