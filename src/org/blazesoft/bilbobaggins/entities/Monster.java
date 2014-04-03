package org.blazesoft.bilbobaggins.entities;

import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Gage
 */
public class Monster extends Entity
{
    private Random random = new Random();
    
    public Monster()
    {
        super(0, 0, 30, 30, 30, Color.red);
        
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
                Thread.sleep(getSpeed() >= 15 ? getSpeed() : 15);
            } catch (Exception e) { }
        }
    }
}
