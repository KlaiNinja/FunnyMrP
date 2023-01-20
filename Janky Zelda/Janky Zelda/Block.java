import greenfoot.*;
import java.util.*;
public class Block extends Actor
{
    int xmove=0;
    int ymove=0;
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
        event=e;
        keypos=k;
        movesLeft = 5;
        setBlockImage();
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
        setBlockImage();
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
        //makes the block move for an unlimited number of spaces
        if (event==-1 && numOfSpaces==0){
            move=true;
        }
        //This will activate a key event if one has been specified
        if (numOfSpaces <= 0 && event == 1){
            
        }
        //numOfSpaces the block as long as their are numOfSpaces left
        if (movesLeft > 0 && moving && numOfSpaces > 0){
            //moves the block a number of spaces
            setLocation(getX()+xmove,getY()+ymove);
            numOfSpaces--;
            move = false;
            //when the block moved N number of spaces, then decrease moves left by 1 and it can move again
            if (numOfSpaces <= 0){
                movesLeft--;
                //if it has a key event, then activate it
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
        Class[] objects = {Link.class}; //All classes in this array are able to push this block
        int collisionAmount=0;
        int m=2; //How many cells the block travels every movement
        while (collisionAmount<objects.length){
            int o=13;//range of cells the object has to be within in a givin axis order to push the object
            //Down check
            Actor down = getOneObjectAtOffset(0, getImage().getHeight()/2+2,objects[collisionAmount]);
            //Up check
            Actor up = getOneObjectAtOffset(0, -getImage().getHeight()/2-2,objects[collisionAmount]);
            //Left check
            Actor left = getOneObjectAtOffset(-getImage().getWidth()/2-2, 0,objects[collisionAmount]);
            //Right check
            Actor right = getOneObjectAtOffset(getImage().getWidth()/2+2, 0,objects[collisionAmount]);
            if (down != null && down.getX() > getX()-o && down.getX() < getX()+o && Greenfoot.isKeyDown("w") && canMoveDir[0]){
                ymove=-m;
                moving = true;
            }
            else if (up != null && up.getX() > getX()-o && up.getX() < getX()+o && Greenfoot.isKeyDown("s") && canMoveDir[1]){
                ymove=m;
                moving = true;
            }
            else if (left != null && left.getY() > getY()-o && left.getY() < getY()+o && Greenfoot.isKeyDown("d") && canMoveDir[3]){
                xmove=m;
                moving = true;
            }
            else if (right != null && right.getY() > getY()-o && right.getY() < getY()+o && Greenfoot.isKeyDown("a") && canMoveDir[2]){
                xmove=-m;
                moving = true;
            }
            collisionAmount++;
        }
        collisionAmount=0;
        
        /**This checks if there is an object in the blocks way when it numOfSpaces*/
        
        Class[] blocks = {Wall.class,Block.class,Lava.class,Water.class,Water.class}; //All classes in this array can prevent the block from moving
        while (collisionAmount<blocks.length){
            //Down check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i++){
                Actor object = getOneObjectAtOffset(i, getImage().getHeight()/2+20,blocks[collisionAmount]);
                if (object!=null&&ymove>0){negateMoving();}
            }
            //Up check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i++){
                Actor object = getOneObjectAtOffset(i, -getImage().getHeight()/2-20,blocks[collisionAmount]);
                if (object!=null&&ymove<0){negateMoving();}
            }
            //Left check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i++){
                Actor object = getOneObjectAtOffset(-getImage().getWidth()/2-20, i,blocks[collisionAmount]);
                if (object!=null&&xmove<0){negateMoving();}
            }
            //Right check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i++){
                Actor object = getOneObjectAtOffset(getImage().getWidth()/2+20, i,blocks[collisionAmount]);
                if (object!=null&&xmove>0){negateMoving();}
            }
            collisionAmount++;
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
