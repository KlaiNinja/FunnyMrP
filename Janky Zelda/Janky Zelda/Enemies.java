import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemies here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemies extends Actor
{
    int health = 5; //hp of enemy
    int atk; //amt of dmg deal to link
    int roomID;
    int xpos; //tracks x 
    int ypos;// tracks y of each enemy
    boolean alive; //alive?
    int dir; //direction the enemy is facing to move towards "link"
public void takeDMG(int dmgtaken){
    if (health > 0){
        health -= dmgtaken;
    }else{
        destroy();
    }
}
public void removeSprite(){
    this.getWorld().removeObject(this);
}
public void destroy(){
    alive = false;
    removeSprite();
}
public void reset(){
    this.health = 5;
    this.alive  = true;
}
}
