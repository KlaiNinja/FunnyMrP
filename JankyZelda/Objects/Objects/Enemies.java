import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Enemies here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemies extends Actor
{
    public enum Dir { //enum to make directions easier to work with
        up,down,left,right
    }
    Dir kbDir = Dir.down; 
    Dir currentDir = Dir.down; 
    protected int maxHealth =  5; //hp of enemy
    protected int atk; //amt of dmg deal to link
    protected int roomID;
    protected int xpos; //tracks x 
    public int timer = 0;
    protected int ypos;// tracks y of each enemy
    protected boolean alive; //alive?
    protected boolean isKnockback;
    protected int counter = 0;
    public Enemies(int maxHealth, int atk, int roomID){
        this.maxHealth = maxHealth;
        this.atk = atk;
        this.roomID = roomID;
        this.reset();
    }

    public void act(){
        if(this.isKnockback && counter <= 5){
            dirmove(kbDir, 15); //smooth kb not implemented yet
            counter++;
            if(counter == 4){
                this.isKnockback = false;
            } else {
                counter = 0;
            }
        }
        counter = 0;
    }

    public void DirToNEWS(Dir Direction){ 
        switch(Direction){
            case up:
                this.setRotation(270);
                break;
            case down:
                this.setRotation(90);
                break;
            case left:
                this.setRotation(180);
                break;
            case right:
                this.setRotation(0);
                break;
        }
    }

    public void getDir(int currentRot){
        //default rotation when spawned is "sprite is looking towards right"
        //left is 180, down is 90, right is 0, up is 270
        if (currentRot < 45 || 315 < currentRot){
            this.currentDir = Dir.right;
        }
        if (45 < currentRot && currentRot < 135){
            this.currentDir = Dir.down;
        }
        if (135 < currentRot && currentRot < 225){
            this.currentDir = Dir.left;
        }
        if (225 < currentRot && currentRot < 315){
            this.currentDir = Dir.up;
        }
    }

    public void currentDirToNEWS(){ ///left is 180, down is 90, right is 0, up is 270
        switch(currentDir){
            case up:
                this.setRotation(270);
                break;
            case down:
                this.setRotation(90);
                break;
            case left:
                this.setRotation(180);
                break;
            case right:
                this.setRotation(0);
                break;
        }
    }

    public void slowedmove(int mod){ //move slowly
        if (timer%mod == 0){ //increase mod base to slow down the movement
            this.move(1);
        }
    }

    public void randomMove(){ 
        Random generator = new Random(); //random library
        int ranNum = generator.nextInt(222); //gets random number from 0-222
        if(ranNum == 0){
            this.currentDir = Dir.down;
        }
        if (ranNum == 1){
            this.currentDir = Dir.left;
        }
        if(ranNum == 2){
            this.currentDir = Dir.right;
        }
        if(ranNum == 3){
            this.currentDir = Dir.up;
        }
    }

    public void takeDMG(int dmgtaken){
        if (this.maxHealth > 0){
            this.maxHealth -= dmgtaken;
        }else{
            this.destroy();
        }
    }

    public void takeKB(Dir currentDirection){ // takes kb in stab direction
        switch(currentDirection){
            case up:
                if(currentDir == Dir.up){
                    this.kbDir = Dir.down;
                } else {
                    goInPlayerDir(); 
                }
                break;
            case down:
                if(currentDir == Dir.down){
                    this.kbDir = Dir.up;
                } else {
                    goInPlayerDir(); 
                }
                break;
            case left:
                if(currentDir == Dir.left){
                    this.kbDir = Dir.right;
                } else {
                    goInPlayerDir();
                }
                break;
            case right:
                if(currentDir == Dir.right){
                    this.kbDir = Dir.left;
                } else {
                     goInPlayerDir();
                }
                break;
        }

    }
    public void goInPlayerDir(){
    if (Link.currentDir == Link.Dir.down){
                        this.kbDir = Dir.down;
                    } else if (Link.currentDir == Link.Dir.up){
                        this.kbDir = Dir.up;
                    } else if (Link.currentDir == Link.Dir.left){
                        this.kbDir = Dir.left;
                    } else if (Link.currentDir == Link.Dir.right){
                        this.kbDir = Dir.right;
                    }
    }

    public void dirmove(Dir knockbackDirection, int speed){
        System.out.println("checking");
        int i = 0; 
        switch(knockbackDirection){
            case up:
                i = speed;
                //change kb for specific weapons of link here by changing i 
                this.setLocation (this.getX(), this.getY() - i);
                break;
            case down:
                i = speed;
                this.setLocation (this.getX(), this.getY() + i);
                break;
            case left:
                i = speed;
                this.setLocation (this.getX() - i, this.getY());
                break;
            case right:
                i = speed;
                this.setLocation (this.getX() + i, this.getY());
                break;
        }
    }

    public void removeSprite(){
        this.getWorld().removeObject(this);
    }

    public void destroy(){
        this.alive = false;
        this.removeSprite();
    }

    public void reset(){
        this.maxHealth = 5;
        this.alive  = true;
    }

    public int getHP(){
        return this.maxHealth;
    }

    public int getAtk(){
        return this.atk;
    }

    public void setAttk(int attk){
        this.atk = attk;
    }

    public void setHP(int hp){
        this.maxHealth = hp;
        if (maxHealth <= 0){
            this.destroy();
        }
    }
}
