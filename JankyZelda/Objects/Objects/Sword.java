import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Write a description of class Sword here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Sword extends Weapon
{
    /**
     * Act - do whatever the Sword wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public int length;//how far the sword will reach
    public double playerAngle;//the angle the player is facing during a spin attack
    private Vector2 direction;
    int speed;
    int imgW = 10;
    int imgH = 28;
    Vector2 distance;
    Vector2 startingPos;
    Link.Attacks type;
    public Sword(Link player, Link.Attacks type, int dmg, int kb, int dur, int cool){
        super(player, new GreenfootImage("link's_sword.png"), dmg, kb, dur, cool, true);
        this.type = type;
        getImage().scale(2*imgW, 2*imgH);
        length = 15;
        speed = 3;
        startingPos = new Vector2(0, 0);
        direction = new Vector2(0, 0);
        distance = new Vector2(0, 0);
    }
    public void act()
    {
        // Add your action code here.
        if (!active){
            setLocation(player.getX() + direction.intX()*20, player.getY() + direction.intY()*15);
            startingPos.set(getX(), getY());
            aimAttack();
        }
        actWeapon();
    }
    @Override
    public void useWeapon(){
        //if its the sword for the spin attack, then spin at the player's position
        if (type == Link.Attacks.Spin){
            timer = 0;
            playerAngle = Vector2.deg2Rad(player.getRotation());
            int xOffset = (int)(20*Math.cos(playerAngle));
            int yOffset = (int)(20*Math.sin(playerAngle));
            setLocation(player.getX() - xOffset, player.getY() - yOffset);
            setRotation(player.getRotation() - 90);
        }else{
            //otherwise, do a regular melee stab attack
            distance.set(getX() - startingPos.X(), getY() - startingPos.Y());
            if (distance.mag() >= length){
                speed = -speed;
            }
            setLocation(getX() + direction.intX()*speed, getY() + direction.intY()*speed);
        }
        enemyCollision();
    }
    //detects collision with enemies to deal knockback and damage
    public void enemyCollision(){
        List<Enemies> enemies = getIntersectingObjects(Enemies.class);
        if (enemies.size() > 0){
            for (Enemies enemy : enemies){
                enemy.takeDMG(damage);
            }
        }
    }
    public void aimAttack(){
        switch(player.currentDir){
            case up:
                setRotation(270 + 90);
                direction.set(0, -1);
                break;
            case down:
                setRotation(90 + 90);
                direction.set(0, 1);
                break;
            case left:
                setRotation(180 + 90);
                direction.set(-1, 0);
                break;
            case right:
                setRotation(0 + 90);
                direction.set(1, 0);
                break;
        }
    }
}
