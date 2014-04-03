package org.blazesoft.bilbobaggins.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import org.blazesoft.bilbobaggins.Buff;

/**
 *
 * @author Gage
 */
public class Entity implements Runnable
{
    private int 
            x, 
            y,
            cachedX,
            cachedY,
            width,
            height,
            speed;
    
    private boolean 
            alive;
    
    private Color
            color;
    
    private Buff 
            buff = Buff.None;
    
    
    public Entity(
            int x,
            int y,
            int width,
            int height,
            int speed,
            Color color)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        
        this.alive = true;
        this.color = color; 
    }

    @Override
    public void run()
    {
        
    }
    
    public void draw(Graphics graphics)
    {
        Color previousColor = graphics.getColor();
        
        graphics.setColor(color); 
        graphics.fillRect(x, y, width, height);
        graphics.setColor(previousColor);
    }
     
    public boolean intersects(Entity entity)
    {
        Rectangle rectangleOne = new Rectangle(
                getX(),
                getY(), 
                getWidth(),
                getHeight());
        
        Rectangle rectangleTwo = new Rectangle(
                entity.getX(),
                entity.getY(),
                entity.getWidth(),
                entity.getHeight());
        
        return rectangleOne.intersects(rectangleTwo) || rectangleTwo.intersects(rectangleOne);
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public int getX()
    {
        return x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setSpeed(int speed)
    {
        this.speed = speed;
    }
    
    public int getSpeed()
    {
        return speed;
    }
    
    public void setAlive(boolean alive)
    {
        this.alive = alive;
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public Buff getBuff()
    {
        return buff;
    }

    public void setBuff(Buff buff)
    {
        this.buff = buff;
    }
    
        
    public boolean hasMoved()
    {
        return (x != cachedX || y != cachedY);
    }
    
    public void setCachedPosition()
    {
        if(cachedX != x)
            cachedX = x;
        
        if(cachedY != y)
            cachedY = y;
    }
}
