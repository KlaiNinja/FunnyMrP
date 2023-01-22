import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;
import java.util.*;

/**
 * Write a description of class Door here.
 * 
 * @author Akram Klai
 * @version 1.5
 */
public class Door extends Actor
{
    /**
     * Act - do whatever the Door wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    String name;
    boolean isLocked;
    char exit;
    int keysRequired = 1;//the number of keys required to open it
    int doorID;//same id as the id for the room its in
    public static GreenfootImage ogImage = new GreenfootImage("YellowBlock.png");
    public static GreenfootImage doorImage[] = {new GreenfootImage("leftDoor.png"), new GreenfootImage("rightDoor.png")};//the original image to use to make the sprite
    private Block[][] blocks;//the block objects that make up the entire door
    public Door(int doorID, boolean isLocked, char exit, int keysRequired){
        this.doorID = doorID;
        this.name = "Door " + doorID + " " + exit;
        this.isLocked = isLocked;
        this.exit = exit;
        this.keysRequired = keysRequired;
        //if the exit is on the west or east side of the room, then shape the door accordingly
        if (exit == 'W' || exit == 'E'){
            createDoor(1, 2);
        }
        else{
            createDoor(2, 1);
        }
    }
    public void act()
    {
        // Add your action code here.
    }
    /*public void createDoorImage(int blockW, int blockH){
        int spriteW = ogImage.getWidth();
        int spriteH = ogImage.getHeight();
        GreenfootImage image = new GreenfootImage(blockW * spriteW, blockH * spriteH);
        //draws an array of blocks in the image based on the given width and height
        for (int row = 0; row < blockH; row++){
            for (int block = 0; block < blockW; block++){
                //draw a block as one tile for the whole block
                System.out.println("BLOCK: " + block + ", " + row);
                image.drawImage(ogImage, block * spriteW, row * spriteH);
            }
        }
        setImage(image);//sets the new image as the current image for the door
    }*/
    public void createDoor(int blockW, int blockH){
        blocks = new Block[blockH][blockW];
        //draws an array of blocks in the image based on the given width and height
        for (int row = 0; row < blockH; row++){
            for (int col = 0; col < blockW; col++){
                //adds a immovable block object to the array and the world
                blocks[row][col] = new Block(ogImage, false, 0, 0);
            }
        }
    }
    public void drawDoor(){
        int width = ogImage.getWidth();
        int height = ogImage.getHeight();
        if (getWorld() == null){ 
            System.out.println("Not working :("); 
            return;
        }
        for (int row=0; row < blocks.length; row++){
            for (int col=0; col < blocks[0].length; col++){
                if (blocks[row][col] != null){
                    if (exit == 'W' || exit == 'E'){
                        getWorld().addObject(blocks[row][col], getX() + col*width, getY() - 20 + row*height);
                    }
                    else{
                        getWorld().addObject(blocks[row][col], getX() - 20 + col*width, getY() + row*height);
                    }
                }
            }
        }
    }
    private void openDoorAnim(int maxDist, int speed){
        int xMove = 0, yMove = 0;
        int xDist = 0, yDist = 0;
        Actor block1;  
        Actor block2;
        if (exit == 'W' || exit == 'E'){
            yMove = speed;
            block1 = (Actor) blocks[0][0];
            block2 = (Actor) blocks[1][0];
        }
        else{
            xMove = speed;
            block1 = (Actor) blocks[0][0];
            block2 = (Actor) blocks[0][1];
        }
        for (int i=0; i < maxDist; i += speed){
            block1.setLocation(block1.getX() - xMove, block1.getY() - yMove);
            block2.setLocation(block2.getX() + xMove, block2.getY() + yMove);
            xDist = block1.getX() - block2.getX();
            yDist = block1.getY() - block2.getY();
        }
    }
    public void lockDoor(){
        isLocked = true;
    }
    public void unlockDoor(Link player){
        System.out.println("Door " + doorID + " opening");
        if (player.keysCollected >= keysRequired){
            openDoorAnim(7, 2);
            isLocked = false;
        }
    }
}
