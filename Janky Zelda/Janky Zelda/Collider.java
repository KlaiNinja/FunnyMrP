import greenfoot.*;
/**
 * Write a description of class Collider here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Collider  
{
    //the width and height of the hitbox of collider
    int width, height;
    Link player;
    //type of collision (based on player direction)
    CollisionType collisionType;
    /**
     * Constructor for objects of class Collider
     */
    public Collider(Link player, int width, int height)
    {
        this.player = player;
        this.width = width;
        this.height = height;
    }
    //checks collision between other objects
    public void checkCollision(Class[] objects){
        if (player == null){
            return;
        }
        for (int obj=0; obj < objects.length; obj++){
            //Down check
            for (int i=-width/2+2; i<width/2-2; i+=4){
                Actor object = player.getObjectAtOffset(i, height/2+2,objects[obj]);
                if (object != null && player.ymove > 0){
                    i=-width/2+2; 
                    player.ymove=0; 
                    player.setLocation(player.getX(),object.getY()-object.getImage().getHeight()/2-height/2);
                    collisionType = CollisionType.DOWN;
                }
            }
            //Up check
            for (int i=-width/2+2; i<width/2-2; i+=4){
                Actor object = player.getObjectAtOffset(i, -height/2-3,objects[obj]);
                if (object!=null && player.ymove < 0){
                    i=-width/2+2; 
                    player.ymove=0; 
                    player.setLocation(player.getX(),object.getY() + object.getImage().getHeight()/2+height/2);
                    collisionType = CollisionType.UP;
                }
            }
            //Left check
            for (int i=-height/2+2; i<height/2-2; i+=4){
                Actor object = player.getObjectAtOffset(-width/2-3, i,objects[obj]);
                if (object != null && player.xmove < 0){
                    i=-height/2+2; 
                    player.xmove=0; 
                    player.setLocation(object.getX() + object.getImage().getWidth()/2+width/2,player.getY());
                    collisionType = CollisionType.LEFT;
                }
            }
            //Right check
            for (int i=-height/2+2; i<height/2-2; i+=4){
                Actor object = player.getObjectAtOffset(width/2+2, i,objects[obj]);
                if (object != null && player.xmove > 0){
                    i=-height/2+2; 
                    player.xmove=0; 
                    player.setLocation(object.getX()-object.getImage().getWidth()/2-width/2,player.getY());
                    collisionType = CollisionType.RIGHT;
                }
            }
        }
    }
    public static enum CollisionType{
        UP,
        DOWN,
        LEFT, 
        RIGHT
    }
}
