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
        System.out.println("Col: " + currentCol + "/ Row: " + currentRow);
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
        System.out.println(cols);
        return rooms[(int)id/rows][id%cols];
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
