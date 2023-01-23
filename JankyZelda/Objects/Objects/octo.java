import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class octo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class octo extends Enemies
{
    public octo(int maxHealth, int atk, int roomID){
        super(maxHealth, atk, roomID);
    }

    /**
     * Act - do whatever the octo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */       
    public void act()
    {
        timer++;
        getDir(getRotation() );
        currentDirToNEWS();
        shoot();
        isHit();
    }

    public void shoot(){
        //looks at link's general direction
        Actor Link = getWorld().getObjects(Link.class).get(0);
        turnTowards(Link.getX(), Link.getY());
        getDir( getRotation() );
        currentDirToNEWS();
        //shoot projectile
        if (timer%150 == 0){
            getWorld().addObject(new rocky(currentDir), getX(), getY());
        }
    }    
}
