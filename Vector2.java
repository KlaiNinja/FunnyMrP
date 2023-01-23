import java.util.*;
/**
 * Write a description of class Vector2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Vector2  
{
    private float x = 0, y = 0;
    private double magnitude, angle;
    /**
     * Constructor for objects of class Vector2
     */
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
        magnitude = Math.sqrt(x*x + y*y);
        angle = Math.atan2(y, x);
    }
    //sets the x and y components of the vector and sets the magnitude and angle accordingly
    public void set(float x, float y){
        this.x = x;
        this.y = y;
        magnitude = Math.sqrt(x*x + y*y);
        angle = rad2Deg(Math.atan2(y, x));
    }
    //sets the magnitude and angle of the vector and sets the x and y components accordingly
    public void set(double magnitude, double angle){
        this.angle = angle;
        this.magnitude = magnitude;
        x = (float)(magnitude * Math.cos(deg2Rad(angle)));
        y = (float)(magnitude * Math.sin(deg2Rad(angle)));
    }
    public double getAngle(){
        return angle;
    }
    public float X(){
        return x;
    }
    public int intX(){
        return (int)x;
    }
    public float Y(){
        return y;
    }
    public int intY(){
        return (int)y;
    }
    public void add(Vector2 vector){
        x += vector.X();
        y += vector.Y();
    }
    public void mult(float scalar){
        x *= scalar;
        y *= scalar;
    }
    //multiplies a vector by a scalar amount and return that value
    public static Vector2 mult(Vector2 vector, float scalar){
        return new Vector2(vector.x * scalar, vector.y * scalar);
    }
    //sets the magnitude to 1 (unit vector) and sets everything else accordingly
    public void normalize(){
        set(1, angle);
    }
    public double mag(){
        return magnitude;
    }
    //converts radians to degrees
    public static double rad2Deg(double radians){
        return radians * 180/Math.PI;
    }
    //converts degrees to radians
    public static double deg2Rad(double degrees){
        return degrees * Math.PI/180;
    }
}
