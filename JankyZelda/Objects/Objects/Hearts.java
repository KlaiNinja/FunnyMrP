import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Hearts here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hearts extends UI
{
    /**
     * Act - do whatever the Hearts wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        //just here to add and remove the images to signify heart loss / gain
    }
    public void removeSelf(){
        getWorld().removeObject(this);
    }
}
