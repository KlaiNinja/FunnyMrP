import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class rock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class rocky extends octo
{
    /**
     * Act - do whatever the rock wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    public rocky(Dir direction){
        super(5,1,0);
        this.currentDir = direction;
    }

    public void act()
    {
        int speed = 2;
        switch(currentDir){
            case up:
                setLocation(getX(), getY() - speed);
                break;
            case down:
                setLocation(getX(), getY() + speed);
                break;
            case left:
                setLocation(getX() - speed, getY());
                break;
            case right:
                setLocation(getX() + speed, getY());
                break;
        }
        checkRemove();
    }

    public void checkRemove(){
        if (isTouching(Wall.class)){
            getWorld().removeObject(this);
        } else {
            if (isTouching(Block.class)){
                getWorld().removeObject(this);
            } else {
                if (isTouching(Link.class)){
                    timer++;
                    if (timer%2 == 0){ //only hits link once
                        getWorld().removeObject(this);
                    }
                }
            }
        }

        // if (isAtEdge()){
        // getWorld().removeObject(this);
        // }
    }
}
