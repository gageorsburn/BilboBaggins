package org.blazesoft.bilbobaggins.entities;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Gage
 */
public class Stalker extends Entity
{
    private Random random = new Random();
    private Ship ship;
    
    public Stalker(Ship ship)
    {
        super(0, 0, 30, 30, 100, Color.cyan);
        
        this.ship = ship;
        
        setX(random.nextInt(300) + 100);
        setY(-20);
    }

    @Override
    public void run() 
    {
        while(isAlive())
        {
            try
            {
                if(getX() > ship.getX())
                    setX(getX() - 10);
                else
                if(getX() < ship.getX())
                    setX(getX() + 10);
                
                setY(getY() + 10);
                
                Thread.sleep(getSpeed());
            } catch (Exception e) { }
        }
    }
}
