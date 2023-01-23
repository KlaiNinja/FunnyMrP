import java.util.*;
import greenfoot.*;

public class Link extends Actor {
    public static enum Dir {
        up,down,left,right
    }
    public static Dir kbDir = Dir.down; //the knock back direction of link
    public static Dir currentDir = Dir.down; //current direction of link
    public static Dir stabDir = Dir.down; //the direction of the stabbing attack
    int speed = 3; //rate of movement
    int xmove=0;
    int xmove2=0;
    int ymove=0;
    int ymove2=0;
    int scroll=0;
    int scrollTimer=0;
    int roomID = 0;
    int HP = 10; 
    int keysCollected = 0;
    int left, right, up, down;
    int knockbackDirection = 0;
    int counter = 0;
    int timer = 0;
    int atktimer = 0;
    public int spriteW;
    public int spriteH;
    GreenfootImage sprite;
    boolean isKnockback = false;
    public static boolean attacking = false;
    boolean switchFrame = false;
    boolean GameOver = false;
    Collider collider;
    public GameOverScreen gos = new GameOverScreen();
    public EndScreen endScreen = new EndScreen();
    GreenfootImage[] sprites = {
            new GreenfootImage("1LinkLeft.png"),
            new GreenfootImage("1LinkLeftMoving.png"),
            new GreenfootImage("1LinkDown.png"),
            new GreenfootImage("1LinkDownMoving.png"),
            new GreenfootImage("1LinkRight.png"),
            new GreenfootImage("1LinkRightMoving.png"),
            new GreenfootImage("1LinkUp.png"),
            new GreenfootImage("1LinkUpMoving.png")
        };
    public Link(int width,int height,int  colliderW,int  colliderH){
        collider = new Collider(this, colliderW, colliderH);
        spriteW = width;
        spriteH = height;
    }

    public void act() 
    {
        //Methods
        if (HP > 0){
            try{
                ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).setLocation(getX(),getY());
            }catch(IndexOutOfBoundsException e){}
            if  (scroll == 0) {
                basicMoving();
                enemyHitCollision();
                attack();
                playerAnimation(10);
                if(isKnockback && counter <= 5){
                    dirmove(kbDir, 14);
                    counter++;
                    if(counter == 4){
                        isKnockback = false;
                    }
                } else {
                    timer++;
                    counter = 0;
                }
                if (collider != null){
                    collider.checkCollision(objects);
                }
                if (getX()>=getWorld().getWidth()-20){scroll=1;}
                if (getX()<=20){scroll=2;}
                if (getY()>=getWorld().getHeight()-20){scroll=3;}
                if (getY()<=20){scroll=4;}
            }
            //Press e to clear the current room after the player gets the key
            if (Greenfoot.isKeyDown("E")){
                clearRoom();
            }
            if (scroll==0){
                setLocation(getX()+xmove+xmove2,getY()+ymove+ymove2);
            }else{
                scroll(5, 30);
            }
        } else {
            //gameoverscreen once dead
            Greenfoot.setWorld(new GameOverScreen1());
        }
    }

    public void dirmove(Dir dir,int  mvmtSpeed){
        int  i = 0;
        if (!(getObjectsInRange(50 + 14,Wall.class).size() > 0)){

            switch(dir){
                case up:
                    i = mvmtSpeed;
                    //change kb for specific enemies here by changing i 
                    setLocation (getX(), getY() - i);
                    break;
                case down:
                    i = mvmtSpeed;
                    setLocation (getX(), getY() + i);
                    break;
                case left:
                    i = mvmtSpeed;
                    setLocation (getX() - i, getY());
                    break;
                case right:
                    i = mvmtSpeed;
                    setLocation (getX() + i, getY());
                    break;
            }
        }else {

        }
    }

    public void scroll(int timeLength,int  speed){
        if (scrollTimer==0){((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).fadeOut();}
        scrollTimer++;
        if(scrollTimer==timeLength){
            switch(scroll){
                case 1: 
                    xmove=-speed; 
                    ymove=0; 
                    ((RandomlyGeneratingDungeon)getWorld()).scroll("right");
                    break;
                case 2:
                    xmove=speed; 
                    ymove=0; 
                    ((RandomlyGeneratingDungeon)getWorld()).scroll("left");
                    break;
                case 3:
                    xmove=0; 
                    ymove=-speed; 
                    ((RandomlyGeneratingDungeon)getWorld()).scroll("down");
                    break;
                case 4:
                    xmove=0; 
                    ymove=speed; 
                    ((RandomlyGeneratingDungeon)getWorld()).scroll("up");
                    break;
            }
        }else if(scrollTimer>timeLength){
            setLocation(getX()+xmove,getY()+ymove);
            if (scroll==1 && getX() <= spriteW + 10){scroll=0;}
            if (scroll==2 && getX() >= getWorld().getWidth() - spriteW - 10){scroll=0;}
            if (scroll==3 && getY() <= spriteH + 10){scroll=0;}
            if (scroll==4 && getY() >= getWorld().getHeight() - spriteH - 10){scroll=0;}
            if (scroll==0){
                scrollTimer=0; 
                ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).fadeIn();
            }
        }
    }

    public void attack(){
        //p for spin, o for bow, and i for knife / sword
        if (scroll!=0)
            return;
        if (Greenfoot.isKeyDown("p")){ 
            attacking = true;
        }
        if (Greenfoot.isKeyDown("space")){
            stabAttack(currentDir);
        }
    }

    public void stabAttack(Dir currentDirection){
        switch(currentDirection){
            case up:
                stabDir = Dir.up;
                currentDir = Dir.up;
                attacking = true;
                break;
            case down:
                stabDir = Dir.down;
                currentDir = Dir.down;
                attacking = true;
                break;
            case left:
                stabDir = Dir.left;
                currentDir = Dir.left;
                attacking = true;
                break;
            case right:
                stabDir = Dir.right;
                currentDir = Dir.right;
                attacking = true;
                break;    
        }
    }

    public void clearRoom(){
        Map map = ((RandomlyGeneratingDungeon) getWorld()).map;
        Room currentRoom = map.getCurrentRoom();
        Door[] doorsInRoom = currentRoom.getDoors() != null ? currentRoom.getDoors() : new Door[0];
        currentRoom.unlockRoom(this, true);
    }

    public boolean isClose(int maxDist, Actor obj){
        int  xDist = getX() - obj.getX();
        int  yDist = getY() - obj.getY();
        double distance = Math.sqrt(xDist*xDist + yDist*yDist);
        return distance <= maxDist;
    }

    public void foundKey(){
        keysCollected++;
        System.out.println("Keys: " + keysCollected);
    }

    public int  getKeysCollected(){
        return keysCollected;
    }

    public void basicMoving()
    {

        if (scroll!=0)return;
        left = Greenfoot.isKeyDown("a") ? 1 : 0;
        right = Greenfoot.isKeyDown("d") ? 1 : 0;
        up = Greenfoot.isKeyDown("w") ? 1 : 0;
        down = Greenfoot.isKeyDown("s") ? 1 : 0;
        xmove = speed * (right - left);//overall change in x
        ymove = speed * (down - up);//overall change in y
        if (Greenfoot.isKeyDown("a")&&ymove==0){ 
            kbDir = Dir.right;
            currentDir = Dir.left;
        }
        if (Greenfoot.isKeyDown("d")&&ymove==0){
            kbDir = Dir.left;
            currentDir = Dir.right;
        }
        if (Greenfoot.isKeyDown("w")&&xmove==0){
            kbDir = Dir.down;
            currentDir = Dir.up;
        }
        if (Greenfoot.isKeyDown("s")&&xmove==0){
            kbDir = Dir.up;
            currentDir = Dir.down;
        }
    }

    public void playerAnimation(int frameRate)
    {
        GreenfootImage frame1;
        GreenfootImage frame2;
        switch(currentDir){
            case left:
                frame1 = sprites[0];
                frame2 = sprites[1];
                break;
            case right:
                frame1 = sprites[4];
                frame2 = sprites[5];
                break;
            case up:
                frame1 = sprites[6];
                frame2 = sprites[7];
                break;
            case down:
                frame1 = sprites[2];
                frame2 = sprites[3];
                break;
            default:
                frame1 = sprites[0];
                frame2 = sprites[1];
                break;
        }
        if (xmove == 0 && ymove == 0){
            return;
        }
        if(timer%frameRate == 0){
            switchFrame = !switchFrame;
        } 
        if(switchFrame){
            setImage(frame1);
        } else{
            setImage(frame2);
        } 
    }
    Class[] objects = {Wall.class,Block.class,Lava.class,Water.class, Door.class, Enemies.class};
    int collisionAmount = 0;
    //change this when adding boss !!!!1!11!11111!1!!1
    public void enemyHitCollision(){
        if (getObjectsInRange(30, Enemies.class).size() > 0){
            if (isTouching(Enemies.class)){
                initiateKB(currentDir); //everything does 1hp of dmg
            }
        } 
    }
   public void initiateKB(Enemies Enemy, Dir currentDirection){
        if (!(isTouching(Wall.class))){isKnockback = true;}else{isKnockback = false;}
        if(HP == 4){
            RandomlyGeneratingDungeon.heart5.removeSelf();
        }else if(HP == 3){
            RandomlyGeneratingDungeon.heart4.removeSelf();
        }else if(HP == 2){
            RandomlyGeneratingDungeon.heart3.removeSelf();
        }else if(HP == 1){
            RandomlyGeneratingDungeon.heart2.removeSelf();
        }else if(HP == 0){
            RandomlyGeneratingDungeon.heart1.removeSelf();
            GameOver = true;
        }

        takeDMG(1);
        switch(currentDirection){
            case up:
                if(Greenfoot.isKeyDown("w")){
                    kbDir = Dir.down;
                } else {
                    if (Enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (Enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (Enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (Enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case down:
                if(Greenfoot.isKeyDown("s")){
                    kbDir = Dir.up;
                } else {
                    if (Enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (Enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (Enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (Enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case left:
                if(Greenfoot.isKeyDown("a")){
                    kbDir = Dir.right;
                }else {
                    if (Enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (Enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (Enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (Enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case right:
                if(Greenfoot.isKeyDown("d")){
                    kbDir = Dir.left;
                }else {
                    if (Enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (Enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (Enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (Enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right; 
                    }
                }
                break;
        }
    }

    public void takeDMG(int amt){
        HP = HP - amt; 
        //do some thing else too
    }

    public Actor getObjectAtOffset(int dx, int  dy, Class object){
        return getOneObjectAtOffset(dx, dy, object);
    }
}
