import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Key here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Key extends Actor{
  public void act() 
    {
        // Add your action code here.
        //if it is touching the player, then it can be picked up
        if (isTouching(Link.class)){
            playerInteraction(getWorld().getObjects(Link.class).get(0));
        }
    }
    public void playerInteraction(Link player){
        //when the player presses a button, it will pick up the key and remove it
        if (Greenfoot.isKeyDown("E")){
            player.foundKey();
            getWorld().removeObject(this);
        }
    }
}
