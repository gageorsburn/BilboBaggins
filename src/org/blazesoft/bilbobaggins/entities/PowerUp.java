package org.blazesoft.bilbobaggins.entities;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Gage
 */
public class PowerUp extends Entity
{
    private Random random = new Random();
    
    public PowerUp()
    {
        super(0, 0, 50, 50, 10, Color.orange);
        
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
                setY(getY() + 10);
                Thread.sleep(getSpeed());
            } catch (Exception e) { }
        }
    }
}
