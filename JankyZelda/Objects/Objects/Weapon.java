import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Weapon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Weapon extends Actor
{
    /**
     * Act - do whatever the Weapon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    protected int damage;
    protected int knockback;//the amount of knockback received by enemies after being hit
    protected int cooldown;//the lentgh of time disabling the weapon after each use
    protected int duration;//the length of time that the weapon is active
    int timer = 0;
    protected boolean active = false;//determines whether or not the player is using the weapon
    protected boolean enabled = true;//determines if the weapon is enabled to be able to use
    boolean startCoolDown = false;
    public boolean lockPlayerInPlace;//when using the weapon, locks the player in its current position
    protected Link player;
    private GreenfootImage empty = new GreenfootImage("Empty.png");
    private GreenfootImage sprite;
    public Weapon(Link player, GreenfootImage sprite, int damage, int knockback, int duration, int cooldown, boolean lockPlayerInPlace){
        this.player = player;
        this.sprite = sprite;
        this.damage = damage;
        this.knockback = knockback;
        this.duration = duration;
        this.cooldown = cooldown;
        this.lockPlayerInPlace = lockPlayerInPlace;
        if (sprite != null) setImage(sprite);
        timer = 0;
    }
    public void actWeapon()
    {
        // when the weapon is available for use
        if (enabled){
            //when the weapon is used
            if (active){
                timer++;
                if (lockPlayerInPlace)player.stopMovement();
                if (timer >= duration){
                    startCoolDown();
                    player.stopAttack();
                }
                useWeapon();
            }
            else{
                setImage(empty);
            }
            //after weapon use, cooldown starts
            if (startCoolDown){
                timer = 0;
                startCoolDown = false;
                enabled = false;
                setImage(empty);
            }
        }
        //once cooldown ends, re-enable the weapon
        else if(timer >= cooldown){
            enabled = true;
            timer = 0;
        }
        else{
            timer++;
        }
    }
    public void activateWeapon(){
        active = true;
        setImage(sprite);
    }
    public void startCoolDown(){
        startCoolDown = true;
        active = false;
    }
    //this method can be overrided by subclasses of weapons to be able to use them
    public void useWeapon(){
    }
}
