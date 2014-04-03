package org.blazesoft.bilbobaggins.entities;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.blazesoft.bilbobaggins.Buff;

/**
 *
 * @author Gage
 */
public class Bullet extends Entity
{
    public Bullet(int x, Buff buff)
    {
        super(x+28, 480, 20, 50, 10, Color.green);
        
        setBuff(buff);
        
        switch(buff)
        {
            case SmallBullet: 
                setHeight(10);
                setY(getY() + 30);
            break;
            case BigBullet: 
                setX(x - 85);
                setY(getY() - 50);
                setWidth(200);
                setHeight(100);
            break;
            case ScatterBullet:
                setHeight(10);
            break;
        }
    }

    @Override
    public void run() 
    {
        while(isAlive())
        {
            try
            {
                setY(getY() - 10);
                Thread.sleep(getSpeed());
            } catch (Exception e) { }
        }
    }

    @Override
    public void draw(Graphics graphics) 
    {
        Color previousColor = graphics.getColor();
        
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gradientPaint = new GradientPaint
        (
                (float)getX(),
                (float)getY(),
                Color.lightGray,
                (float)getX(),
                (float)getY()  + getWidth(),
                getColor()
        );
        
        graphics2D.setPaint(gradientPaint);
        graphics2D.fillRect(getX(), getY(), getWidth(), getHeight());
        
        graphics.setColor(previousColor);
    }
}
