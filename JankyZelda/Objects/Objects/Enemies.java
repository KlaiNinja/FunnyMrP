import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemies here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemies extends Actor
{
    protected int health = 5; //hp of enemy
    protected int atk; //amt of dmg deal to link
    protected int roomID;
    protected int xpos; //tracks x 
    protected int ypos;// tracks y of each enemy
    protected  boolean alive; //alive?

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
        if (health <= 0){
            destroy();
        }
    }

    public void takeKB(){
        Actor Link = getWorld().getObjects(Link.class).get(0);
        turnTowards(-Link.getX(), -Link.getY());
        move(10);
    }
}
