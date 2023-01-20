import greenfoot.*;
import java.util.List;

public class Link extends Actor
{
    public int speed = 3;//Rate of cells that will be traveled
    int xmove=0;
    int xmove2=0;
    int ymove=0;
    int ymove2=0;
    int up, down , left , right;
    int scroll=0;
    int scrollTimer=0;
    Class[] objects = {Wall.class, Block.class, Lava.class,Water.class};
    static String direction="up";
    GreenfootImage sprite;
    public int spriteW, spriteH;
    int collisionAmount=0;
    int keysCollected = 0;
    Collider collider;
    public Link(int width, int height, int colliderW, int colliderH){
        spriteW = width;
        spriteH = height;
        collider = new Collider(this, colliderW, colliderH);
        sprite = new GreenfootImage("link-sprite.png");
        sprite.scale(spriteW, spriteH);
        setImage(sprite);
    }
    public void act() 
    {
        //Methods
        try{
            ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).setLocation(getX(),getY());
        }catch(IndexOutOfBoundsException e){}
        if (scroll==0){
            basicMoving();
            //graphics();
            //collisionDetection();
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
        if (scroll==0){setLocation(getX()+xmove+xmove2,getY()+ymove+ymove2);}else{scroll(5, 30);}
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
            if (scroll==2 && getX() >= getWorld().getWidth() - spriteW - 10){scroll=0;}
            if (scroll==3 && getY() <= spriteH + 10){scroll=0;}
            if (scroll==4 && getY() >= getWorld().getHeight() - spriteH - 10){scroll=0;}
            if (scroll==0){
                scrollTimer=0; 
                ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).fadeIn();
            }
        }
    }
    public void basicMoving()
    {
        if (scroll!=0)return;
        //Change movement
        /*if (Greenfoot.isKeyDown("a")){setRotation(270);}
        if (Greenfoot.isKeyDown("d")){setRotation(90);}
        if (Greenfoot.isKeyDown("w")){setRotation(0);}
        if (Greenfoot.isKeyDown("s")){setRotation(180);}*/
        left = Greenfoot.isKeyDown("a") ? 1 : 0;
        right = Greenfoot.isKeyDown("d") ? 1 : 0;
        up = Greenfoot.isKeyDown("w") ? 1 : 0;
        down = Greenfoot.isKeyDown("s") ? 1 : 0;
        xmove = speed * (right - left);//overall change in x
        ymove = speed * (down - up);//overall change in y
    }
    //unlocks the doors of the current room that the player is in
    public void clearRoom(){
        Map map = ((RandomlyGeneratingDungeon) getWorld()).map;
        Room currentRoom = map.getCurrentRoom();
        Door[] doorsInRoom = currentRoom.getDoors() != null ? currentRoom.getDoors() : new Door[0];
        currentRoom.unlockRoom(this, true);
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
    public void print(int text){
        System.out.println(text);
    }
    public void graphics()
    {
        
    }
    public void collisionDetection()
    {
        //checks collision with all the objects that can collide with the player
        while (collisionAmount<objects.length){
            //Down check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(i, getImage().getHeight()/2+2,objects[collisionAmount]);
                if (object!=null&&ymove>0){
                    i=-getImage().getWidth()/2+2; ymove=0; setLocation(getX(),object.getY()-object.getImage().getHeight()/2-getImage().getHeight()/2);
                }
            }
            //Up check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(i, -getImage().getHeight()/2-3,objects[collisionAmount]);
                if (object!=null&&ymove<0){
                    i=-getImage().getWidth()/2+2; ymove=0; setLocation(getX(),object.getY()+object.getImage().getHeight()/2+getImage().getHeight()/2);
                }
            }
            //Left check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(0-getImage().getWidth()/2-3, i,objects[collisionAmount]);
                if (object!=null&&xmove<0){
                    i=-getImage().getHeight()/2+2; xmove=0; setLocation(object.getX()+object.getImage().getWidth()/2+getImage().getWidth()/2,getY());
                }
            }
            //Right check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(getImage().getWidth()/2+2, i,objects[collisionAmount]);
                if (object!=null&&xmove>0){
                    i=-getImage().getHeight()/2+2; xmove=0; setLocation(object.getX()-object.getImage().getWidth()/2-getImage().getWidth()/2,getY());
                }
            }
            collisionAmount++;
        }
        collisionAmount=0;
    }
    //checks collision with all the objects that can collide with the player
    public Actor getObjectAtOffset(int dx, int dy, Class object){
        return getOneObjectAtOffset(dx, dy, object);
    }
}
