import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Arrow here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Arrow extends Actor
{
    /**
     * Act - do whatever the Arrow wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private double angle, distance;
    private int speed;
    private int damage;
    private int range;
    private int transparency = 255;
    private Vector2 velocity = new Vector2(0, 0);
    private int lifespan;//the number of seconds it lasts
    private int timer = 0;
    private boolean fadeAway = false;//causes the arrow to slowly fade away
    //objects that will collide with the arrow
    Class[] obstacles = {Enemies.class, Block.class, Wall.class};
    public Arrow(int dmg, int speed, double angle, int lifespan, int range){
        this.speed = speed;
        this.angle = angle;
        this.damage = dmg;
        this.lifespan = lifespan;
        this.range = range;
        transparency = 255;
        //sets the mag and direction of velocity to speed and the angle it is shot from
        velocity.set((double)speed, angle);
        System.out.println(angle);
    }
    public void act()
    {
        // Add your action code here.
        if (fadeAway){
            disappear();
            if (getImage().getTransparency() == 0){
                //when fully transparent, then it removes itself from the world
                getWorld().removeObject(this);
                return;
            }
        }
        moveInDir();
        detectCollision();
    }
    //moves in the direction it's shot from
    public void moveInDir(){
        setLocation(getX() - velocity.intX(), getY() - velocity.intY());
        distance += velocity.mag();
        if (distance >= range){
            stopAndDie();
        }
    }
    //stops the arrow from moving and makes it disappear
    public void stopAndDie(){
        velocity.mult(0);//sets the x and y velocity to zero
        fadeAway = true;
    }
    public void disappear(){
        timer++;
        //until it reach 0, decrease the image's transparency overtime
        if (getImage().getTransparency() >= 0){
            getImage().setTransparency(255 - (255/lifespan)*(timer/100));
        }
    }
    public void detectCollision(){
        for (int n=0; n < obstacles.length; n++){
            if (isTouching(obstacles[n])){
                //if it collides, with an enemy, then it deals damage and knockback to that enemy
                if (obstacles[n] == Enemies.class){
                    Enemies enemy = (Enemies) getOneIntersectingObject(Enemies.class);
                    enemy.takeDMG(damage);
                    getWorld().removeObject(this);
                    return;
                }
                stopAndDie();
            }
        }
    }
}
