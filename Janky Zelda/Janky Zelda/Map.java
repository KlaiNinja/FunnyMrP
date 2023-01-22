import greenfoot.*;
import java.util.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map
{
    private int[][][] mapLayout;
    public int startingRoomID;
    private Room[][] rooms;
    private int rows, cols;
    private int currentRow, currentCol;
    public int numOfRooms;
    public Room currentRoom;
    /**
     * Constructor for objects of class Map.
     */
    public Map(Room[][] rooms)
    {    
        if (rooms != null){
            this.rooms = rooms;
            this.rows = rooms.length;
            this.cols = rooms[0].length;
            this.numOfRooms = rows * cols;
        }
    }
    public Map(int startingRoom, int[][][] mapLayout){
        this.startingRoomID = startingRoom;
        this.mapLayout = mapLayout;
        this.rows = mapLayout.length;
        this.cols = mapLayout[0].length;
        this.numOfRooms = rows * cols;
        this.rooms = new Room[rows][cols];
        createRooms();
        this.currentRoom = getRoom(startingRoom);
    }
    //Creates each of the rooms based on the given map layout
    void createRooms(){
        for (int row=0; row < rows; row++){
            for (int col=0; col < cols; col++){
                int[] exits = mapLayout[row][col];
                int id = row*rows + col;
                int[] pos = new int[2];
                //if this room is the starting room, set its the position to the origin
                if (id == startingRoomID){
                    pos = new int[]{0, 0};
                }
                else {
                    //finds the position of the room relative to the position of the starting room
                    int startingRow = startingRoomID/rows;
                    int startingCol = startingRoomID%cols;
                    pos = new int[]{col - startingCol, row - startingRow};
                }
                rooms[row][col] = new Room(id, pos, exits[0], exits[1], exits[2], exits[3]);
            }
        }
    }
    //moves to the next room based on the exit name
    void moveToExit(String exitName){
        switch(exitName.toLowerCase()){
            case "north": changeRow(1); break;
            case "south": changeRow(-1); break;
            case "west": changeColumn(-1); break;
            case "east": changeColumn(1); break;
            default: break;
        }
        currentRoom = getRoom(currentRow, currentCol);
        currentRoom.lockDoors();
    }
    void changeColumn(int spaces){
        currentCol += spaces;
        currentCol = constrain(currentCol, cols-1, 0);
    }
    void changeRow(int spaces){
        currentRow -= spaces;
        currentRow = constrain(currentRow, rows-1, 0);
    }
    int constrain(int value, int max, int min){
        int constrainedValue;
        constrainedValue = Math.min(value, max);//constrains it to to the maximum
        constrainedValue = Math.max(value, min);//constrains it to to the minimum
        return constrainedValue;
    }
    public Room getRoom(int id){
        //gets the specifed room based on the ID
        return rooms[id/rows][id%cols];
    }
    //Gets the current room that the player is currently in
    public Room getCurrentRoom(){
        return currentRoom;
    }
    private boolean withinMap(int row, int col){
        return (row >= 0 && row < rows) && (col >= 0 && col < cols);
    }
    public Room getRoom(int row, int col){
        //gets the specifed room based on the row and column
        if (withinMap(row, col)){
            return rooms[row][col];
        }
        return null;//if there is no such room
    }
}
