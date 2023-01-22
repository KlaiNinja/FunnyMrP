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
    public static enum Dir {down,left,right,up}
    static Dir kbDir = Dir.down; 
    static Dir currentDir = Dir.down; 
    protected int health = 5; //hp of enemy
    protected int atk; //amt of dmg deal to link
    protected int roomID;
    protected int xpos; //tracks x 
    public int timer = 0;
    protected int ypos;// tracks y of each enemy
    protected boolean alive; //alive?
    protected boolean isKnockback;
    protected int counter = 0;
    public void act(){
        if(isKnockback && counter <= 5){
            dirmove(kbDir, 15); //smooth kb not implemented yet
            counter++;
            if(counter == 4){
                isKnockback = false;
            } else {
                counter = 0;
            }
        }
        counter = 0;
    }

    public void slowedmove(int mod){ //move slowly
        if (timer%mod == 0){ //increase mod base to slow down the movement
            move(1);
        }
    }

    public void randomMove(){ //not implemented yet
        Random generator = new Random(); //random number generator
        int ranNum = generator.nextInt(250); //gets random number from 0-249
        if(ranNum == 0){
            currentDir = Dir.down;
            
        }
        if (ranNum == 1){
            currentDir = Dir.left;
            
        }
        if(ranNum == 2){
            currentDir = Dir.right;
            
        }
        if(ranNum == 3){
            currentDir = Dir.up;
            
        }
    }

    public void takeDMG(int dmgtaken){
        if (health > 0){
            health -= dmgtaken;
        }else{
            destroy();
        }
    }

    public void takeKB(Dir currentDirection){ // takes kb in stab direction
        switch(currentDirection){
            case up:
                if(currentDir == Dir.up){
                    kbDir = Dir.down;
                } else {
                    if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } else if (Link.stabDir == Link.Dir.up){
                        kbDir = Dir.up;
                    } else if (Link.stabDir == Link.Dir.left){
                        kbDir = Dir.left;
                    } else if (Link.stabDir == Link.Dir.right){
                        kbDir = Dir.right;
                    } else if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } 
                }
                break;
            case down:
                if(currentDir == Dir.down){
                    kbDir = Dir.up;
                } else {
                    if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } else if (Link.stabDir == Link.Dir.up){
                        kbDir = Dir.up;
                    } else if (Link.stabDir == Link.Dir.left){
                        kbDir = Dir.left;
                    } else if (Link.stabDir == Link.Dir.right){
                        kbDir = Dir.right;
                    } else if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } 
                }
                break;
            case left:
                if(currentDir == Dir.left){
                    kbDir = Dir.right;
                } else {
                    if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } else if (Link.stabDir == Link.Dir.up){
                        kbDir = Dir.up;
                    } else if (Link.stabDir == Link.Dir.left){
                        kbDir = Dir.left;
                    } else if (Link.stabDir == Link.Dir.right){
                        kbDir = Dir.right;
                    } else if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } 
                }
                break;
            case right:
                if(currentDir == Dir.right){
                    kbDir = Dir.left;
                } else {
                    if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } else if (Link.stabDir == Link.Dir.up){
                        kbDir = Dir.up;
                    } else if (Link.stabDir == Link.Dir.left){
                        kbDir = Dir.left;
                    } else if (Link.stabDir == Link.Dir.right){
                        kbDir = Dir.right;
                    } else if (Link.stabDir == Link.Dir.down){
                        kbDir = Dir.down;
                    } 
                }
                break;
        }

    }

    public void dirmove(Dir knockbackDirection, int speed){
        System.out.println("checking");
        int i = 0; 
        switch(knockbackDirection){
            case up:
                i = speed;
                //change kb for specific weapons of link here by changing i 
                setLocation (getX(), getY() - i);
                break;
            case down:
                i = speed;
                setLocation (getX(), getY() + i);
                break;
            case left:
                i = speed;
                setLocation (getX() - i, getY());
                break;
            case right:
                i = speed;
                setLocation (getX() + i, getY());
                break;
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
    }

    public void isHit(){ // this is old try to fix it
        Actor link = getOneIntersectingObject(Link.class);
        if(link != null){
            if(Link.attacking == true){
                if (getObjectsInRange(15, sword.class).size() > 0){
                    this.takeDMG(1);
                    this.takeKB(currentDir);
                }
            }
        }       
    }
}
