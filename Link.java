import java.util.*;
import greenfoot.*;

public class Link extends Actor {
    public static enum Dir {up,down,left,right}
    public static enum Attacks {Spin, Bow, Sword}
    Class[] objects = {Wall.class,Block.class,Lava.class,Water.class, Door.class};
    //knockback direction
    Dir kbDir = Dir.down;
    Dir currentDir = Dir.down;
    Dir atkDir = Dir.down;
    //the current active attack
    Attacks activeAttack = Attacks.Spin;
    //Link's weapons
    Weapon[] weapons = new Weapon[3];
    Weapon currentWeapon;
    int speed = 3; //rate of movement
    int xmove=0;
    int ymove=0;
    int scroll=0;
    int scrollTimer=0;
    int angleRotation = 0;
    int roomID = 0;
    int HP = 10; 
    int left, right, up, down;
    public int spriteW, spriteH;
    int keysCollected = 1;
    boolean isKnockback = false;
    boolean attacking = false;
    int knockbackDirection = 0;
    int counter = 0;
    int timer = 0;
    Collider collider;
    public static GameOverScreen gos = new GameOverScreen();
    public static EndScreen endScreen = new EndScreen();
    boolean switchFrame = false;
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
    public Link(int width, int height, int colliderW, int colliderH){
        //sets the collider and sprite it's width and height
        collider = new Collider(this, colliderW, colliderH);
        spriteW = width;
        spriteH = height;
        //sets all the weapons of the player
        weapons = new Weapon[]{new Sword(this, Attacks.Spin, 5, 5, 400, 200), new Sword(this, Attacks.Sword, 7, 1, 20, 100), new Bow(this, 3, 1, 5, 1)};
    }
    public void act() 
    {
        //Methods
        if (HP > 0){
            try{
                ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).setLocation(getX(),getY());
            }catch(IndexOutOfBoundsException e){}
            if (scroll == 0){
                basicMoving();
                chooseAttack();
                clearRoom();
                //enemyHitCollision(30);
                playerAnimation(10);
                if(isKnockback && counter <= 5){
                    dirMove(kbDir, 10);
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
            if (scroll==0){
                setLocation(getX()+xmove,getY()+ymove);
            }else{
                scroll(5, 30);
            }
        } else {
            //death
            //death();
        }
    }

    public void dirMove(Dir dir, int mvmtSpeed){
        int i = 0;
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
        getWorld().removeObject(this);
    }

    public void scroll(int timeLength, int speed){
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
            if (scroll==2 && getX() >= getWorld().getWidth() - spriteW - 20){scroll=0;}
            if (scroll==3 && getY() <= spriteH + 20){scroll=0;}
            if (scroll==4 && getY() >= getWorld().getHeight() - spriteH - 10){scroll=0;}
            if (scroll==0){
                scrollTimer=0; 
                ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).fadeIn();
            }
        }
    }
    public void chooseAttack(){
        //p for spin, o for bow, and i for knife / sword
        if (scroll!=0)return;
        String key = Greenfoot.getKey();
        if (attacking){
            activateAttack();
            return;
        }
        if (key == null)return;
        switch(key){
            case "p":
                attacking = true;
                activeAttack = Attacks.Spin;
                currentWeapon = weapons[0];
                break;
            case "o":
                attacking = true;
                activeAttack = Attacks.Sword;
                currentWeapon = weapons[1];
                break;
            case "i":
                attacking = true;
                activeAttack = Attacks.Bow;
                currentWeapon = weapons[2];
                break;
            default:
                break;
        }
    }
    //activate the current weapon based on the chosen attack
    public void activateAttack(){
        if (activeAttack == Attacks.Spin && !currentWeapon.startCoolDown){
            spinAttack(20);
        }
        //if the current weapon exists, then activate its attack
        if (currentWeapon != null)currentWeapon.activateWeapon();
    }
    //Spin attack: rotates the player a full revolution and gives knockback to nearby enemies
    public void spinAttack(int rotateSpeed){
        List<Enemies> enemies = getObjectsInRange(55, Enemies.class);
        if (angleRotation >= 359){
            stopAttack();
            angleRotation = 0;
            currentWeapon.startCoolDown();
        }else{
            angleRotation += rotateSpeed;
        }
        //all enemies recieve knockback
        for (Enemies enemy : enemies){
            if (enemy != null){
                enemy.takeKB(enemy.currentDir);
            }
        }
        setRotation(angleRotation);
    }
    public void stopAttack(){
        attacking = false;
    }
    public void stopMovement(){
        xmove *= 0;
        ymove *= 0;
    }
    //clears the room and open its doors after the player defeats all enemies
    public void clearRoom(){
        Map map = ((RandomlyGeneratingDungeon) getWorld()).map;
        Room currentRoom = map.getCurrentRoom();
        //if all enemies are killed, then unlock the room and open the doors
        System.out.println(currentRoom.getEnemies().size());
        if (currentRoom.getEnemies() == null)return;
        if (currentRoom.getEnemies().size() <= 0){
            System.out.println("Doors open");
            currentRoom.unlockRoom(this, true);
        }
        else{
            currentRoom.lockDoors();
        }
    }

    public boolean isClose(int maxDist, Actor obj){
        int xDist = getX() - obj.getX();
        int yDist = getY() - obj.getY();
        double distance = Math.sqrt(xDist*xDist + yDist*yDist);
        return distance <= maxDist;
    }

    public void foundKey(){
        keysCollected++;
        System.out.println("Keys: " + keysCollected);
    }

    public int getKeysCollected(){
        return keysCollected;
    }
    public void basicMoving()
    {
        if (scroll!=0 || (attacking && activeAttack == Attacks.Sword && currentWeapon.lockPlayerInPlace))return;
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
    //animates the player movement frame by frame at a specified framerate
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
    //change this when adding boss
    public void enemyHitCollision(int radius){
        List<Enemies> nearbyEnemies = getObjectsInRange(radius, Enemies.class);
        for (Enemies enemy : nearbyEnemies){
            if (isTouching(Enemies.class)){
                initiateKB(enemy, currentDir); //everything does 1hp of dmg
            }
        }
    }
     public void takeDMG(int amt){
         if (HP > 0){
            HP -= amt;
        }
        //do some thing else too
    }
    public void initiateKB(Enemies enemy, Dir currentDirection){
        if (!(isTouching(Wall.class))){
            isKnockback = true;
        }else{
            isKnockback = false;
        }
        enemy.takeDMG(1);
        switch(currentDirection){
            case up:
                if(Greenfoot.isKeyDown("w")){
                    kbDir = Dir.down;
                } else {
                    if (enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case down:
                if(Greenfoot.isKeyDown("s")){
                    kbDir = Dir.up;
                } else {
                    if (enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case left:
                if(Greenfoot.isKeyDown("a")){
                    kbDir = Dir.right;
                }else {
                    if (enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right;
                    } 
                }
                break;
            case right:
                if(Greenfoot.isKeyDown("d")){
                    kbDir = Dir.left;
                }else {
                    if (enemy.currentDir == Enemies.Dir.down){
                        kbDir = Dir.down;
                    } else if (enemy.currentDir == Enemies.Dir.up){
                        kbDir = Dir.up;
                    } else if (enemy.currentDir == Enemies.Dir.left){
                        kbDir = Dir.left;
                    } else if (enemy.currentDir == Enemies.Dir.right){
                        kbDir = Dir.right; 
                    }
                }
                break;
        }
    }
    public Actor getObjectAtOffset(int dx, int dy, Class object){
        return getOneObjectAtOffset(dx, dy, object);
    }
}
