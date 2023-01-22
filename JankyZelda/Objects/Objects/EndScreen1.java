import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndScreen1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndScreen1 extends World
{

    /**
     * Constructor for objects of class EndScreen1.
     * 
     */
    public EndScreen1()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(640, 480, 1); 
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        WinnerText winnerText = new WinnerText();
        addObject(winnerText,319,232);
        winnerText.setLocation(497,263);
        LinkWin linkWin = new LinkWin();
        addObject(linkWin,497,263);
        winnerText.setLocation(179,305);
        Zelda zelda = new Zelda();
        addObject(zelda,179,305);
        winnerText.setLocation(535,69);
        winnerText.setLocation(293,179);
        winnerText.setLocation(325,139);
        winnerText.setLocation(343,136);
        winnerText.setLocation(342,155);
    }
}
