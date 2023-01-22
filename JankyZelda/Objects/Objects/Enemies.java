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
    protected int maxHealth = 5;
    protected int atk; //amt of dmg deal to link
    public int roomID;
    protected int xpos; //tracks x 
    protected int ypos;// tracks y of each enemy
    protected boolean alive = true; //alive?
    public Enemies(int maxHealth, int atk, int roomID){
        this.maxHealth = maxHealth;
        this.atk = atk;
        this.roomID = roomID;
        reset();
    }
    public void takeDMG(int dmgtaken){
        alive = health > 0;
        if (alive){
            health -= dmgtaken;
        }else{
            destroy();
        }
    }

    public void removeSprite(){
        this.getWorld().removeObject(this);
    }

    public void destroy(){
        removeSprite();
    }

    public void reset(){
        this.health = maxHealth;
        this.alive  = true;
    }

    public int getHP(){
        return this.health;
    }

    public int getAtk(){
        return this.atk;
    }

    public void setAttk(int atk){
        this.atk = atk;
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
    public boolean isAlive(){
        return this.alive;
    }
}
