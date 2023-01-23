import greenfoot.*;
import java.util.List;

public class RandomlyGeneratingDungeon extends World
{   
    /**
     * Constructor for objects of class RandomlyGeneratingDungeon.
     * 
     */
    public static Class[] all = {Wall.class,Key.class,Block.class,Lava.class,Water.class, Enemies.class};
    public static RandomlyGeneratingDungeon instance;
    //the map of rooms in the randomly generate dungeon
    public Map map;
    public static Room currentRoom;
    private Wall[] walls;
    private Wall[] openWalls;
    private Door[] doors;
    /**
    Tile Sets
    ---------
    0 = Green
    1 = Blue
    2 = Aqua
    3 = Yellow
    4 = Grey
    */
    int tileset = 0;
    public static Link player;
    public RandomlyGeneratingDungeon()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(640, 480, 1, false);
        /*map = new Map(new Room[][]{
            {new Room(0, new int[]{0, 0}, 0, 1, 0, 1), new Room(1, new int[]{1, 0}, 0, 1, 1, 0)},
            {new Room(2, new int[]{0, 1}, 1, 0, 0, 1), new Room(3, new int[]{1, 1}, 1, 0, 1, 0)},
        });*/
        //[N, S, W, E]
        if (instance == null) instance = this;
        map = new Map(0, new int[][][]{
            {{0, 1, 0, 1}, {0, 1, 1, 1}, {0, 0, 1, 1}, {0, 1, 1, 0}},
            {{1, 1, 0, 1}, {1, 0, 1, 1}, {0, 1, 1, 0}, {1, 1, 0, 0}},
            {{1, 1, 0, 1}, {0, 0, 1, 1}, {1, 0, 1, 1}, {1, 1, 1, 0}},
            {{1, 0, 0, 1}, {0, 0, 1, 1}, {0, 0, 1, 1}, {1, 0, 1, 0}},
        });
        //OBJECTS
        player = new Link(50, 50, 35, 35);
        addObject(player, getWidth()/2,getHeight()/2+20);
        //add weapons
        for (Weapon weapon : player.weapons){
            addObject(weapon, player.getX(), player.getY());
        }
        addObject(new FadeOverlay(),getWidth()/2,getHeight()/2);
        //WALLS
        walls = new Wall[]{
            new Wall(getWidth(),40, getWidth()/2,20),//top wall
            new Wall(getWidth(),40, getWidth()/2,getHeight()-20),//bottom wall
            new Wall(40,getHeight(), 20,getHeight()/2),//left wall
            new Wall(40,getHeight(), getWidth() - 20,getHeight()/2)//right wall
        };
        //walls with gaps for the exits
        openWalls = new Wall[]{
            new Wall(getWidth()/2,40, getWidth()/2 - 120,0),//top wall
            new Wall(getWidth()/2,40, getWidth()/2 - 120,0),//bottom wall
            new Wall(40,getHeight()/2, 0,getHeight()/2 - 80),//left wall
            new Wall(40,getHeight()/2, 0,getHeight()/2 - 80)//right wall
        };
        generateDungeon();
        currentRoom = map.getCurrentRoom();
        //add the enemies to that room
        generateDungeonEnemies(currentRoom, Greenfoot.getRandomNumber(3) + 3, getWidth()*currentRoom.pos[0], getHeight()*currentRoom.pos[1]);
        //testDungeon();
        paintOrder();
    }
    //when the player goes offscreen, the whole scene scrolls at a specific direction
    public void scroll(String direction){
        int v=0;
        int h=0;
        int a=0;
        switch(direction){
            case "up": 
                v = getHeight();
                map.moveToExit("north");
                break;
            case "down": 
                v = -getHeight();
                map.moveToExit("south");
                break;
            case "left": 
                h = getWidth();
                map.moveToExit("west");
                break;
            case "right": 
                h = -getWidth();
                map.moveToExit("east");
                break;
        }
        while (a<all.length){
            List object = getObjects(all[a]);
            if (! object.isEmpty()){
                for (int i=0; i<object.size(); i++){
                    Actor Object = (Actor) object.get(i);
                    Object.setLocation(Object.getX()+h,Object.getY()+v);
                }
            }
            a++;
        }
        //add the enemies to that room
        generateDungeonEnemies(currentRoom, Greenfoot.getRandomNumber(3) + 3, getWidth()*currentRoom.pos[0], getHeight()*currentRoom.pos[1]);
    }
    public void paintOrder(){
        setPaintOrder(Link.class,FadeOverlay.class,Wall.class,Key.class,Door.class,Block.class,Lava.class,Water.class);
    }
    public void act(){
        paintOrder();
        if (tileset==0)setBackground(new GreenfootImage("GreenTile.png"));
        if (tileset==1)setBackground(new GreenfootImage("BlueTile.png"));
        if (tileset==2)setBackground(new GreenfootImage("AquaTile.png"));
        if (tileset==3)setBackground(new GreenfootImage("YellowTile.png"));
        if (tileset==4)setBackground(new GreenfootImage("GreyTile.png"));
    }
    //Dungeon Manipulation methods
    void generateRoom(int id){
        Room room = map.getRoom(id);
        char[] doorExits = new char[]{'N', 'S', 'W', 'E'};
        //System.out.println("ID: " + id + "Pos: (" + room.pos[0] + ", " + room.pos[1] + ")");
        for (int i=0; i < walls.length; i++){
            Wall wall = walls[i];
            int newXPos = wall.xPos + getWidth()*room.pos[0];
            int newYPos = wall.yPos + getHeight()*room.pos[1];
            //if there is no exit on this side, then add a wall
            if (room.getExit(i) == 0){
                //places the walls to the correct room based on its x and y coordinates
                //System.out.println(i);
                addObject(new Wall(wall.width, wall.height, newXPos, newYPos), newXPos, newYPos);
            }
            else{
                //if there is an exit, then add two half walls to leave an opening for a door
                Wall halfWall = openWalls[i];
                addObject(new Wall(halfWall.width, halfWall.height, newXPos - halfWall.xPos, newYPos - halfWall.yPos), newXPos - halfWall.xPos, newYPos - halfWall.yPos);
                addObject(new Wall(halfWall.width, halfWall.height, newXPos + halfWall.xPos, newYPos + halfWall.yPos), newXPos + halfWall.xPos, newYPos + halfWall.yPos);
                Door door = new Door(id, true, doorExits[i], 1);
                room.setDoor(door, i);
                addObject(door, newXPos, newYPos);
            }
        }
        //draw the doors
        room.drawDoors();
    }
    public void nextRoom(){
        
    }
    public void generateDungeon(){
        for (int i=0; i < map.numOfRooms; i++){
            generateRoom(i);
        }
    }
    public void clearDungeonRoom(){
        Class[] objects = {Block.class,Wall.class}; //List of objects that will be cleared
        int object = 0;
        int i = 0;
        while (object<objects.length){
            List Object = getObjects(objects[object]);
            if (! Object.isEmpty() && (Actor) Object.get(0)!=null){
                while (i<Object.size()){
                    removeObject((Actor) Object.get(i));
                    i++;
                }
            }
            object++;
            i=0;
        }
    }
    //randomly generates enemies in the dungeon
    public void generateDungeonEnemies(Room room, int amount, int xPos, int yPos){
        for (int i=0; i < amount; i++){
            int[] max = {getWidth() - 100, getHeight() - 100};
            int[] min = {300, 300};
            int[] randomPos = {Greenfoot.getRandomNumber(max[0] - min[0]) + min[0], Greenfoot.getRandomNumber(max[1] - min[1]) + min[1]};
            int randomHP = Greenfoot.getRandomNumber(5) + 3;
            int randomAtk = Greenfoot.getRandomNumber(1) + 1;
            Enemies enemy = new EnemyBug(randomHP, randomAtk, map.currentRoom.roomID);
            room.addEnemy(enemy);
            addObject(enemy, xPos + randomPos[0], yPos + randomPos[1]);
        }
    }
    public void testDungeon(){
        block(0,0);
        block(4,6,true,-1);
        block(3,6,true,1,2);
        block(5,6);
        block(4,8);
        block(4,4);
        
        lava(12,9);
        lava(15,9);
        lava(14,10);
        lava(13,10);
        lava(13,8);
        lava(14,8);
        
        block(15,4);
        block(14,4);
        block(13,4);
        block(12,4,true,1,4);
        block(12,3,true);
        block(12,2,true, -1);
        block(12,1,true);
        block(16,1);
        block(16,2);
        block(16,3);
        block(16,4);
        
        water(1,1);
        water(1,2);
        water(2,1);
        water(3,1);
        water(3,2);
        water(3,3);
        water(2,3);
        water(1,3);
    }
    
    public void changeTileSet(int i){
        tileset=i;
    }
    //Dungeon Tile Methods
    public void block(int x, int y, int movesLeft, boolean up, boolean down, boolean left, boolean right, int event, int keypos){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Block(movesLeft, up, down , left, right, event, keypos),x*40+20,getHeight()-y*40-20);
    }
    public void block(int x, int y, boolean up, boolean down, boolean left, boolean right, int event, int keypos){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Block(5, up, down , left, right, event, keypos),x*40+20,getHeight()-y*40-20);
    }
    public void block(int x, int y, boolean movable, int event, int keypos){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Block(movable,event,keypos),x*40+20,getHeight()-y*40-20);
    }
    public void block(int x, int y, boolean movable, int event){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Block(movable,event,-1),x*40+20,getHeight()-y*40-20);
    }
    public void block(int x, int y, boolean movable){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Block(movable,0,-1),x*40+20,getHeight()-y*40-20);
    }
    public void block(int x, int y){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Block(false,0,-1),x*40+20,getHeight()-y*40-20);
    }
    //Liquids
    public void lava(int x, int y){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Lava(),x*40+20,getHeight()-y*40-20);
    }
    public void water(int x, int y){
        if (!checkObjectInRange(x, y, 40, 40)) return;
        addObject(new Water(),x*40+20,getHeight()-y*40-20);
    }
    public boolean checkObjectInRange(int x, int y, int w, int h){
        if (x*w+(w/2) < 0 || x*w+(w/2) > getWidth() || getHeight()-y*h-(h/2) < 0 || getHeight()-y*h-(h/2)>getHeight()){
            System.out.println("ERROR: OBJECT AT ("+x+","+y+") IS OUT OF WORLD RANGE"); 
            return false;
        }
        return true;
    }
}