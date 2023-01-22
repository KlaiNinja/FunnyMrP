import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EnemyBug here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemyBug extends Enemies
{
    Collider collider;
    public static enum Dir {up,down,left,right}
    public static Dir kbDir = Dir.down;
    public static Dir currentDir = Dir.down;
    private int timer = 0;
    int health = 3;
    public void act()
    {
        getRot(getRotation());
        Linkcheck();
    }
    //This method moves the enemy like a homing missle as soon as it sees some player
    public void Linkcheck()  {
        timer++;
        Actor Link = getWorld().getObjects(Link.class).get(0);
        if (!(getWorld().getObjects(Link.class).isEmpty())){
            if ( getObjectsInRange(40*4, Link.class).size() > 0 ) {
                if (getObjectsInRange(50, Wall.class).size() >= 0 ) {
                    turnTowards(Link.getX(), Link.getY());
                    if (timer%2 == 0){ //increase mod base to slow down the movement
                        move(1);
                     }
                }
            } 
        } else {
            return;
        }
        if (getObjectsInRange(50, sword.class).size() > 0){
            takeKB(); //knockback
        }
    }
    
    public void getRot(int currentRot){
        //default rotation when spawned is "sprite is looking towards right"
        //left is 180, down is 90, right is 0, up is 270
        
        if (0 < currentRot && currentRot < 90){
            currentDir = Dir.right;
        }
        if (91 < currentRot && currentRot < 180){
            currentDir = Dir.left;
        }
        if (181 < currentRot && currentRot < 270){
            currentDir = Dir.up;
        }
        if (271 < currentRot && currentRot < 360){
            currentDir = Dir.down;
        }
        if (Greenfoot.isKeyDown("space")){
            System.out.println(currentRot);
        }
    }
}
