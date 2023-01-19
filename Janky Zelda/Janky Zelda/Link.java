import java.util.*;

import greenfoot.*;

public class Link extends Actor
{
    int xmove=0;
    int xmove2=0;
    int ymove=0;
    int ymove2=0;
    int scroll=0;
    int scrollTimer=0;
    int roomID = 0;
    int HP = 10; 
    boolean isKnockback = false;
    int knockbackDirection = 0;
    int counter = 0;
    public void act() 
    {
        //Methods
        if (HP > 0){

            try{
                ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).setLocation(getX(),getY());
            }catch(IndexOutOfBoundsException e){}
            if (scroll==0){
                basicMoving();
                enemyHitCollision();
                collisionDetection();
                attack();
                if(isKnockback && counter <= 5){
                    turn(knockbackDirection);
                    move(10);
                    counter++;
                    if(counter == 4){
                        isKnockback = false;
                    }
                }
                if (getX()>=getWorld().getWidth()-1){scroll=1;}
                if (getX()<=0){scroll=2;}
                if (getY()>=getWorld().getHeight()-1){scroll=3;}
                if (getY()<=0){scroll=4;}
            }
            if (scroll==0){
                setLocation(getX()+xmove+xmove2,getY()+ymove+ymove2);
            }else{
                scroll();
            }
        } else {
            //death
            //death();
        }
    }

    public void death(){
        //this will be more
        getWorld().removeObject(this);
    }

    public void scroll(){
        if (scrollTimer==0){((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).fadeOut();}
        scrollTimer++;
        if(scrollTimer==30){
            if (scroll==1){xmove=-9; ymove=0; ((RandomlyGeneratingDungeon)getWorld()).scroll("right");}
            if (scroll==2){xmove=9; ymove=0; ((RandomlyGeneratingDungeon)getWorld()).scroll("left");}
            if (scroll==3){xmove=0; ymove=-9; ((RandomlyGeneratingDungeon)getWorld()).scroll("down");}
            if (scroll==4){xmove=0; ymove=9; ((RandomlyGeneratingDungeon)getWorld()).scroll("up");}
        }else if(scrollTimer>30){
            setLocation(getX()+xmove,getY()+ymove);
            if (scroll==1&&getX()<=30){scroll=0;}
            if (scroll==2&&getX()>=getWorld().getWidth()-30){scroll=0;}
            if (scroll==3&&getY()<=30){scroll=0;}
            if (scroll==4&&getY()>=getWorld().getHeight()-30){scroll=0;}
            if (scroll==0){scrollTimer=0; ((FadeOverlay)getWorld().getObjects(FadeOverlay.class).get(0)).fadeIn();}
        }
    }

    public void attack(){
        
        if (Greenfoot.isKeyDown("o") || Greenfoot.isKeyDown("p") ) {
            String input = Greenfoot.getKey();
            if (input == null) {
                return;
            } else {
                switch (input){
                    case "p":
                        //make a sword spin atk 
                        //this is a normal stab
                        
                        break;
                    case "o":
                        //bow and arrow atk
                        getWorld().addObject(new bow(), getX(), getY());

                        break;
                }
            }
        }
    }


    public void basicMoving()
    {
        if (scroll!=0)return;
        int m=3; //Rate of cells that will be traveled
        //Change movement
        if (Greenfoot.isKeyDown("a")&&ymove==0){
            xmove=-m; 
            setRotation(270);
        }
        if (Greenfoot.isKeyDown("d")&&ymove==0){
            xmove=m; 
            setRotation(90);
        }
        if (Greenfoot.isKeyDown("w")&&xmove==0){
            ymove=-m; 
            setRotation(0);
        }
        if (Greenfoot.isKeyDown("s")&&xmove==0){
            ymove=m; 
            setRotation(180);
        }
        if (! Greenfoot.isKeyDown("a")&&! Greenfoot.isKeyDown("d")){
            xmove=0;
        }
        if (! Greenfoot.isKeyDown("w")&&! Greenfoot.isKeyDown("s")){
            ymove=0;
        }
    }
    static String direction="up";
    public void graphics()
    {

    }
    Class[] objects = {Wall.class,Block.class,Lava.class,Water.class, Door.class, Key.class, Enemies.class};
    int collisionAmount=0;
    //change this when adding boss !!!!1!11!11111!1!!1
    //Actor enemy = getWorld().getObjects(Enemies.class).get(0);
    public void enemyHitCollision(){
        if( getObjectsInRange(45, Enemies.class).size() > 0 ) {
            //old v new/if ( (enemy.getX() + 35) > (getX()) && (enemy.getX() - 35) < (getX() ) )//if ( (enemy.getY() + 35) > (getY()) && (enemy.getX() - 35) < (getY() ) ){
            if (isTouching(Enemies.class) ){
                initiateKB(); //everything does 0.5hp of dmg
            }
        }
    }

    public void initiateKB(){
        Actor enemy = getWorld().getObjects(Enemies.class).get(0);
        HP -= 1;
        turnTowards(enemy.getX(), enemy.getY());
        move(-20);
    }

    public void collisionDetection()
    {
        while (collisionAmount<objects.length){
            //Down check
            for (int i= -getImage().getWidth()/2+2;  i<getImage().getWidth()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(i, getImage().getHeight()/2+2,objects[collisionAmount]);
                if (object!=null&&ymove>0){
                    i=-getImage().getWidth()/2+2; 
                    ymove=0; 
                    setLocation(getX(),object.getY()-object.getImage().getHeight()/2-getImage().getHeight()/2);
                }
            }
            //Up check
            for (int i=-getImage().getWidth()/2+2; i<getImage().getWidth()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(i, -getImage().getHeight()/2-3,objects[collisionAmount]);
                if (object!=null&&ymove<0){
                    i=-getImage().getWidth()/2+2; 
                    ymove=0; 
                    setLocation(getX(),object.getY()+object.getImage().getHeight()/2+getImage().getHeight()/2);}
            }
            //Left check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(0-getImage().getWidth()/2-3, i,objects[collisionAmount]);
                if (object!=null&&xmove<0){
                    i=-getImage().getHeight()/2+2; 
                    xmove=0; 
                    setLocation(object.getX()+object.getImage().getWidth()/2+getImage().getWidth()/2,getY());}
            }
            //Right check
            for (int i=-getImage().getHeight()/2+2; i<getImage().getHeight()/2-2; i+=4){
                Actor object = getOneObjectAtOffset(getImage().getWidth()/2+2, i,objects[collisionAmount]);
                if (object!=null&&xmove>0){
                    i=-getImage().getHeight()/2+2; 
                    xmove=0; 
                    setLocation(object.getX()-object.getImage().getWidth()/2-getImage().getWidth()/2,getY());}
            }
            collisionAmount++;
        }
        collisionAmount=0;
    }
}
