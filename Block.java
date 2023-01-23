import greenfoot.*;
import java.util.*;

public class Block extends Actor
{
    int xmove=0;
    int ymove=0;
    int width, height;
    int numOfSpaces=20;//determines the number of spaces that a block can move at a time
    int movesLeft;//determines the number of times you can move a block
    int event=0;//Determinds which event activate apon moving.
    int keypos=-1;
    /**
    -1 means the block is infinitely movable
    0 means no event is triggered
    1 means a key is dropped from the ceiling in a random location
    2 means a closed door is opened
    3 means an item appears
    */
    boolean movable=false;//If false, it will not let move from being true
    boolean move=false;//If true, it will allow the block to be pushed
    boolean moving = false;//checks if the block is moving or not
    boolean[] canMoveDir = {true, true, true, true};//determines which direction it can move ([Up, Down, Left, Right])
    GreenfootImage[] arrows = {
        new GreenfootImage("arrowUp.png"),
        new GreenfootImage("arrowDown.png"),
        new GreenfootImage("arrowLeft.png"),
        new GreenfootImage("arrowRight.png"),
    };
    public Block(boolean b, int e, int k){
        movable=b;
        move=b;
        canMoveDir[0] = false;
        canMoveDir[1] = false;
        canMoveDir[2] = false;
        canMoveDir[3] = false;
        event=e;
        keypos=k;
        movesLeft = 5;
        setBlockImage();
        width = getImage().getWidth();
        height = getImage().getHeight();
    }
    public Block(GreenfootImage image, boolean b, int e, int k){
        setImage(image);
        movable=b;
        move=b;
        canMoveDir[0] = movable;
        canMoveDir[1] = movable;
        canMoveDir[2] = movable;
        canMoveDir[3] = movable;
        event=e;
        keypos=k;
        movesLeft = 5;
        width = getImage().getWidth();
        height = getImage().getHeight();
    }
    public Block(int movesLeft, boolean up, boolean down, boolean left, boolean right, int e, int k){
        this.movesLeft = movesLeft;
        canMoveDir[0] = up;
        canMoveDir[1] = down;
        canMoveDir[2] = left;
        canMoveDir[3] = right;
        movable = up || down || left || right;
        move = movable;
        event = e;
        keypos = k;
        if (movable){
            setBlockImage();
            width = getImage().getWidth();
            height = getImage().getHeight();
        }
        else{
            setImage("GreyBlock.png");
        }
    }
    public void setBlockImage(){
        GreenfootImage sprite = new GreenfootImage("GreenBlock.png");
        for (int i=0; i < canMoveDir.length; i++){
            int x = 0, y = 0;
            switch(i){
                case 0: x=7; y=0; break;//Up
                case 1: x=7; y=20; break;//Down
                case 2: x=0; y=10; break;//Left
                case 3: x=15; y=10; break;//Right
            }
            if (canMoveDir[i]){
                sprite.drawImage(arrows[i], x, y);
            }
        }
        setImage(sprite);
    }
    public void act(){
        moveBlock();
    }
    public void moveBlock(){
        if (!movable){
            move = false;
        }
        if (move){
            collisionDetection();
        }
        //numOfSpaces the block as long as their are numOfSpaces left
        if (movesLeft > 0 && moving && numOfSpaces > 0){
            //moves the block a number of spaces
            setLocation(getX()+xmove,getY()+ymove);
            numOfSpaces--;
            move = false;
            //when the block moved N number of spaces, then decrease moves left by 1 and it can move again
            if (numOfSpaces <= 0){
                //unless the block has unlimited spaces, decrease the number of moves by 1 everytime it is pushed
                if (event != -1){
                    movesLeft--;
                }
                if (event == 1){
                    key();
                }
                numOfSpaces = 20;
                move = true;
                moving = false;
            }
        }
        else{
            xmove=0;
            ymove=0;
        }
    }
    public void collisionDetection()
    {
        Class[] pushingObjects = {Link.class, Block.class}; //All classes in this array are able to push this block
        Class[] blockingObjects = {Wall.class,Lava.class,Water.class,Water.class}; //All classes in this array can prevent the block from moving
        int spaces = 2; //How many cells the block travels every movement
        int cells = 13;//range of cells the object has to be within in a givin axis order to push the object
        checkPlayerCollision(pushingObjects, spaces, cells);
        checkObjectCollision(blockingObjects);
        //allows other moving block to be able to push it (if it is pushable)
        checkBlockCollision(pushingObjects, spaces, cells);
    }
    public void checkBlockCollision(Class[] objects, int spaces, int cells){
        //Down check
        Block down = (Block)getOneObjectAtOffset(0, height/2+2,objects[1]);
        //Up check
        Block up = (Block)getOneObjectAtOffset(0, -height/2-2,objects[1]);
        //Left check
        Block left = (Block)getOneObjectAtOffset(-width/2-2, 0,objects[1]);
        //Right check
        Block right = (Block)getOneObjectAtOffset(width/2+2, 0,objects[1]);
        //if the block below this block is moving up, then move up
        if (down != null && down.getX() > getX()-cells && down.getX() < getX()+cells && down.ymove < 0){
            ymove = -spaces;
            moving = true;
            movesLeft++;
        }
        else if (up != null && up.getX() > getX()-cells && up.getX() < getX()+cells && up.ymove > 0){
            ymove = spaces;
            moving = true;
            movesLeft++;
        }
        else if (left != null && left.getY() > getY()-cells && left.getY() < getY()+cells && left.xmove > 0){
            xmove = spaces;
            moving = true;
            movesLeft++;
        }
        else if (right != null && right.getY() > getY()-cells && right.getY() < getY()+cells && right.xmove < 0){
            xmove = -spaces;
            moving = true;
            movesLeft++;
        }
    }
    public void checkPlayerCollision(Class[] objects, int spaces, int cells){
        RandomlyGeneratingDungeon world = (RandomlyGeneratingDungeon)getWorld();
        //Down check
        Actor down = getOneObjectAtOffset(0, world.player.collider.height/2+2,objects[0]);
        //Up check
        Actor up = getOneObjectAtOffset(0, -world.player.collider.height/2-2,objects[0]);
        //Left check
        Actor left = getOneObjectAtOffset(-world.player.collider.width/2-2, 0,objects[0]);
        //Right check
        Actor right = getOneObjectAtOffset(world.player.collider.width/2+2, 0,objects[0]);
        if (down != null && down.getX() > getX()-cells && down.getX() < getX()+cells && Greenfoot.isKeyDown("w") && canMoveDir[0]){
            ymove = -spaces;
            moving = true;
        }
        else if (up != null && up.getX() > getX()-cells && up.getX() < getX()+cells && Greenfoot.isKeyDown("s") && canMoveDir[1]){
            ymove = spaces;
            moving = true;
        }
        else if (left != null && left.getY() > getY()-cells && left.getY() < getY()+cells && Greenfoot.isKeyDown("d") && canMoveDir[3]){
            xmove = spaces;
            moving = true;
        }
        else if (right != null && right.getY() > getY()-cells && right.getY() < getY()+cells && Greenfoot.isKeyDown("a") && canMoveDir[2]){
            xmove = -spaces;
            moving = true;
        }
    }
    public void checkObjectCollision(Class[] objects){
        for (int n=0; n < objects.length; n++){
            //Down check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i++){
                Actor object = getOneObjectAtOffset(i, getImage().getHeight()/2+20,objects[n]);
                if (object!=null && ymove>0){
                    negateMoving();
                }
            }
            //Up check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i++){
                Actor object = getOneObjectAtOffset(i, -getImage().getHeight()/2-20,objects[n]);
                if (object!=null && ymove<0){
                    negateMoving();
                }
            }
            //Left check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i++){
                Actor object = getOneObjectAtOffset(-getImage().getWidth()/2-20, i,objects[n]);
                if (object!=null && xmove<0){
                    negateMoving();
                }
            }
            //Right check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i++){
                Actor object = getOneObjectAtOffset(getImage().getWidth()/2+20, i,objects[n]);
                if (object!=null && xmove>0){
                    negateMoving();
                }
            }
        }
    }
    public void negateMoving(){
        xmove=0;
        ymove=0;
    }
    //spawns the key at a specified location
    public void key(){
        int x=0;
        int y=0;
        if (keypos==0){x=8; y=6;}
        if (keypos==1){x=2; y=9;}
        if (keypos==2){x=14; y=9;}
        if (keypos==3){x=14; y=2;}
        if (keypos==4){x=2; y=2;}
        getWorld().addObject(new Key(),x*40+20,getWorld().getHeight()-y*40-20);
        event=0;
    }
}