import java.util.*;
/**
 * Write a description of class Room here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Room  
{
    // instance variables - replace the example below with your own
    public int roomID;
    protected int[] pos = new int[2];
    protected boolean playerIsInside;
    protected int[] exits = new int[4];//[N, S, W, E] 1 = there is exit, 0 = there is NO exit
    private Door[] doors = new Door[4];
    /**
     * Constructor for objects of class Room
     */
    public Room(int id, int[] pos, int nExit, int sExit, int wExit, int eExit)
    {
        this.roomID = id;
        this.pos[0] = pos[0];
        this.pos[1] = pos[1];
        exits[0] = nExit;
        exits[1] = sExit;
        exits[2] = wExit;
        exits[3] = eExit;
        playerIsInside = false;
    }
    public void drawDoors(){
        for (Door door : doors){
            if (door != null){
                door.drawDoor();
            }
        }
    }
    //get the exit based on its index
    public int getExit(int index){
        return exits[index];
    }
    public Door[] getDoors(){
        return doors;
    }
    //gets the exit based on its name
    public int getExit(String exitName){
        switch(exitName.toLowerCase()){
            case "north": return exits[0];
            case "south": return exits[1];
            case "west": return exits[2];
            case "east": return exits[3];
            default: return -1;//if an invalid exit name is inputed
        }
    }
    public void unlockRoom(Link player, boolean unlock){
        if (unlock){
            for (Door door : doors){
                if (door != null){
                    door.unlockDoor(player);
                }
            }
        }
    }
    public void lockDoors(){
        for (Door door : doors){
            if (door != null){
                door.lockDoor();
            }
        }
    }
    public void setDoor(Door door, int i){
        if (door != null){
            doors[i] = door;
        }
    }
}