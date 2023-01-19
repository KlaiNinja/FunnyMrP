import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemies here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemies extends Actor
{
   private int health = 5; //hp of enemy
   private int atk; //amt of dmg deal to link
   private int roomID;
   private int xpos; //tracks x 
   private int ypos;// tracks y of each enemy
   private boolean alive; //alive?
   private int dir; //direction the enemy is facing to move towards "link"
private void takeDMG(int dmgtaken){
    if (health > 0){
        health -= dmgtaken;
    }else{
        destroy();
    }
}
private void removeSprite(){
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
public int getHP(){
    return this.health;
}
public int getAtk(){
    return this.atk;
}
public void setAttk(int attk){
    this.atk = attk;
}
public void setHP(int hp){
    this.health = hp;
}
}
