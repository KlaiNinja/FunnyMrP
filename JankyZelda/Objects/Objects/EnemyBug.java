import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EnemyBug here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnemyBug extends Enemies
{
    public static enum Dir {up,down,left,right}
    Dir kbDir = Dir.down;
    Dir currentDir = Dir.down;
    private int timer = 0;
    int health = 3;
    public EnemyBug(){
        //Enemies.setHP(5);   
    }

    public void act()
    {
        Linkcheck();
    }
    //This method moves the enemy like a homing missle as soon as it sees some player
    public void Linkcheck()  {
        timer++;
        Actor Link = getWorld().getObjects(Link.class).get(0);

        if (!(getWorld().getObjects(Link.class).isEmpty())){
            if ( getObjectsInRange(40*4, Link.class).size() > 0 ) {
                if (getObjectsInRange(50, Wall.class).size() >= 0 ) {
                    turnTowards(Link.getX(), Link.getY());
                    if (timer%2 == 0){ //increase mod base to slow down the movement
                        move(1);
                    }
                }
            } 
        } else {
            return;
        }
        if (getObjectsInRange(50, sword.class).size() > 0){
            takeKB(); //knockback
        }
    }
    public void takeKB(){
        Actor Link = getWorld().getObjects(Link.class).get(0);
        turnTowards(-Link.getX(), -Link.getY());
        move(10);
    }
}

/*public void dirmove(Dir currentdir, int mvmtSpeed){
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

}
 */