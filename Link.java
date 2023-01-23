import java.util.*;
import greenfoot.*;

public class Link extends Actor {
    public static enum Dir {up,down,left,right}
    static Dir kbDir = Dir.down; //the knock back direction of link
    static Dir currentDir = Dir.down; //current direction of link
    static Dir stabDir = Dir.down; //the direction of the stabbing attack
    static int  speed = 3; //rate of movement
    static int  xmove=0;
    static int  xmove2=0;
    static int  ymove=0;
    static int  ymove2=0;
    static int  scroll=0;
    static int  scrollTimer=0;
    static int  roomID = 0;
    static int  HP = 10; 
    static int  keysCollected = 0;
    static int  left, right, up, down;
    static int  knockbackDirection = 0;
    static int  counter = 0;
    static int  timer = 0;
    static int  atktimer = 0;
    public  static int  spriteW;
    public  static int  spriteH;
    GreenfootImage sprite;
    static boolean slowed = false;
    static boolean isKnockback = false;
    static boolean attacking = false;
    static boolean switchFrame = false;
    Collider collider;
    HealthBar linkHP;
    public  GameOverScreen gos = new GameOverScreen();
    public  EndScreen endScreen = new EndScreen();
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
    public Link(int width,int height,int  colliderW,int colliderH){
        collider = new Collider(this, colliderW, colliderH);
        linkHP = new HealthBar(HP);
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
        if ( (scroll == 0) ){
            if (slowed){
                speed = 1;
            } else {
                speed = 3;
            }
            basicMoving();
            enemyHitCollision();
            attack();
            playerAnimation(10);
            if(isKnockback && counter <= 5){
                dirmove(kbDir, 7);
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
            //If you lose all your health, it goes to game over screen
            Greenfoot.setWorld(new GameOverScreen1());
        }
    }

    public void dirmove(Dir dir,int  mvmtSpeed){
        int  i = 0;
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
    }

    public void death(){
        //this will be more
        this.getWorld().removeObject(this);
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
            /*
            List<EnemyBug> Abug;
            while (attacking){
                Abug = getObjectsInRange(55, EnemyBug.class);
                if (Abug.size() > 0){
                    EnemyBug bug = Abug.get(0);
                    bug.takeKB(EnemyBug.currentDir);
                }
                for(int i = 0; i <= 360; i++){
                    if (i%10 == 0){
                        //turn(1);
                    } else if (i > 360){
                        attacking = false;
                    }
                }
            }
            */
        }
        if (Greenfoot.isKeyDown("space")){
            stabAttack(currentDir);
            
            //System.out.println("stabbed at "+ stabDir);
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
    }

    public static int  getKeysCollected(){
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
    Class[] objects = {Wall.class,Block.class,Lava.class,Water.class, Door.class};
    static int  collisionAmount = 0;
    //change this when adding boss !!!!1!11!11111!1!!1
    public void enemyHitCollision(){
        if (getObjectsInRange(40, EnemyBug.class).size() > 0){
            if (isTouching(EnemyBug.class)){
                initiateKB(currentDir); //everything does 1hp of dmg
            }
        }
    }

    public static void initiateKB(Dir currentDirection){
        isKnockback = true;
        HP -= 1;
        switch(currentDirection){
            case up:
                if(Greenfoot.isKeyDown("w")){
                    kbDir = Dir.down;
                } else {
                    if (EnemyBug.currentDir == EnemyBug.Dir.down){
                        kbDir = Dir.down;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.up){
                        kbDir = Dir.up;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.left){
                        kbDir = Dir.left;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case down:
                if(Greenfoot.isKeyDown("s")){
                    kbDir = Dir.up;
                } else {
                    if (EnemyBug.currentDir == EnemyBug.Dir.down){
                        kbDir = Dir.down;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.up){
                        kbDir = Dir.up;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.left){
                        kbDir = Dir.left;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case left:
                if(Greenfoot.isKeyDown("a")){
                    kbDir = Dir.right;
                }else {
                    if (EnemyBug.currentDir == EnemyBug.Dir.down){
                        kbDir = Dir.down;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.up){
                        kbDir = Dir.up;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.left){
                        kbDir = Dir.left;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case right:
                if(Greenfoot.isKeyDown("d")){
                    kbDir = Dir.left;
                }else {
                    if (EnemyBug.currentDir == EnemyBug.Dir.down){
                        kbDir = Dir.down;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.up){
                        kbDir = Dir.up;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.left){
                        kbDir = Dir.left;
                    } else if (EnemyBug.currentDir == EnemyBug.Dir.right){
                        kbDir = Dir.right; 
                    }
                }
                break;
        }
    }

    public Actor getObjectAtOffset(int dx, int  dy, Class object){
        return getOneObjectAtOffset(dx, dy, object);
    }
}
