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
    public EnemyBug(int x, int y){
        this.alive = true;
        xpos = x;
        ypos = y;
    }

    public void act()
    {
        Linkcheck();
    }
    //This method moves the enemy like a homing missle as soon as it sees some player, BUT doesnt move in a different room.
    public void Linkcheck(){
        Actor Link = getWorld().getObjects(Link.class).get(0);
        if (!(getWorld().getObjects(Link.class).isEmpty())){
                turnTowards(Link.getX(), Link.getY());
                //Greenfoot.wait(1); //this to slow it down.
                move(1);
        } else {
            return;
        }
    }
}
