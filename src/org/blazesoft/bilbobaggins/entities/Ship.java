package org.blazesoft.bilbobaggins.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Timer;
import org.blazesoft.bilbobaggins.Buff;
import org.blazesoft.bilbobaggins.Scene;

/**
 *
 * @author Gage
 */
public class Ship extends Entity
{
    private final List<Bullet> bullets;
    
    private boolean 
            canShoot = true,
            leftDown,
            rightDown,
            spaceDown;
    
    private int
            monstersKilled,
            monstersPassed,
            hitCounter,
            missCounter,
            totalHitCounter,
            totalMissCounter;
    
    private long
            score;
    
    private Timer 
            shootTimer,
            timedBuffTimer;
    
    public Ship()
    {
        super(400, 510, 80, 50, 20, Color.lightGray);
        bullets = Collections.synchronizedList(new ArrayList<Bullet>());
        
        shootTimer = new Timer(300, new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) 
            {
                if(!canShoot)
                    canShoot = true;
            }
        });
        
        shootTimer.setRepeats(true);
        shootTimer.start();
        
        timedBuffTimer = new Timer(3000, new AbstractAction() 
        {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                setBuff(Buff.None);
                timedBuffTimer.stop();
            }
        });
    }

    @Override
    public void run() 
    {
        while(isAlive())
        {
            try
            {
                if(isLeftDown())
                    move(-0.7F);
                else
                if(isRightDown())
                    move(0.7F);

                if(isSpaceDown())
                    shootBullet();
                
                Thread.sleep(40);
            }
            catch(Exception e) { }
        }
    }
    
    @Override
    public void setBuff(Buff buff)
    {
        if(buff == Buff.SmallBullet)
            timedBuffTimer.start();
        
        super.setBuff(buff);
    }
    
    public void move(float notches)
    {
        float movementAdjustment = notches * 20;
        
        if(getX() + movementAdjustment >= 0 && 
           getX() + getWidth() + movementAdjustment <= Scene.SCENE_WIDTH)
            setX((int)(getX() + movementAdjustment));
    }
    
    public void shootBullet()
    {
        if(canShoot || getBuff() == Buff.SmallBullet)
        {
            Bullet bullet = new Bullet(getX(), getBuff());
            bullets.add(bullet);
            new Thread(bullet, "Bullet").start();
            
            if(getBuff() == Buff.ScatterBullet)
            {
                for(int bulletIndex = 50; bulletIndex <= 100; bulletIndex += 50)
                {
                    Bullet bulletLeft = new Bullet(getX() - bulletIndex, getBuff());
                    Bullet bulletRight = new Bullet(getX() + bulletIndex, getBuff());
                    
                    bullets.add(bulletLeft);
                    bullets.add(bulletRight);
                    
                    new Thread(bulletLeft, "Bullet").start();
                    new Thread(bulletRight, "Bullet").start();
                }
            }
            
            if(getBuff() != Buff.SmallBullet)
                setBuff(Buff.None);
            
            canShoot = false;
        }
    }
    
    public void terminateDeadBullets()
    {
        synchronized(bullets)
        {
            for(Bullet bullet : bullets)
            {
                if(bullet.getY() < 0)
                {
                        bullet.setAlive(false);
                        
                        if(bullet.getBuff() != Buff.SmallBullet)
                        {
                            missCounter++;
                            totalMissCounter++;
                        }
                }
            }
            
            for(int index = 0; index < bullets.size(); index++)
            {
                if(!bullets.get(index).isAlive())
                    bullets.remove(index);
            }
        }
    }
    
    public void collision(List<Entity> entities)
    {
        synchronized(bullets)
        {
            for(int bulletIndex = 0; bulletIndex < bullets.size(); bulletIndex++)
            {
                for(int entityIndex = 1; entityIndex < entities.size(); entityIndex++)
                {
                    if(entities.get(entityIndex) instanceof Monster)
                    {
                        if(bulletIndex < bullets.size() && entityIndex < entities.size())
                        {
                            if(bullets.get(bulletIndex).intersects(entities.get(entityIndex))) 
                            {
                                if(entities.get(entityIndex).isAlive())
                                {
                                    entities.get(entityIndex).setAlive(false);

                                    if(bullets.get(bulletIndex).getBuff() != Buff.BigBullet)
                                        bullets.get(bulletIndex).setAlive(false);

                                    monstersKilled++;
                                    hitCounter++;
                                    totalHitCounter++;

                                    if(missCounter >= 5)
                                        score += 10 * missCounter;
                                    else
                                        score += 10 + Math.pow(1.1, hitCounter);

                                    missCounter = 0;
                                    monstersPassed = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public List<Bullet> getBullets()
    {
        return bullets;
    }
    
    public void setScore(long score)
    {
        this.score = score;
    }
    
    public long getScore()
    {
        return score;
    }
    
    public void setHitCounter(int hitCounter)
    {
        this.hitCounter = hitCounter;
    }
    
    public void setMissCounter(int missCounter)
    {
        this.missCounter = missCounter;
    }
    
    public int getMissCounter()
    {
        return missCounter;
    }
    
    public int getMonstersKilled()
    {
        return monstersKilled;
    }
    
    public float getAccuracy()
    {
        float totalShots = totalHitCounter + totalMissCounter;
        float denominator = (totalShots) > 0 ? (totalShots) : 1;

        return  (float)(totalHitCounter / denominator);
    }
    
    public void setMonstersPassed(int monstersPassed)
    {
        this.monstersPassed = monstersPassed;
    }
    
    public int getMonstersPassed()
    {
        return monstersPassed;
    }
    
    public boolean isLeftDown()
    {
        return leftDown;
    }

    public void setLeftDown(boolean leftDown)
    {
        this.leftDown = leftDown;
    }

    public boolean isRightDown() 
    {
        return rightDown;
    }

    public void setRightDown(boolean rightDown) 
    {
        this.rightDown = rightDown;
    }

    public boolean isSpaceDown()
    {
        return spaceDown;
    }

    public void setSpaceDown(boolean spaceDown) 
    {
        this.spaceDown = spaceDown;
    }
    
    @Override
    public void draw(Graphics graphics)
    {
        graphics.drawImage(Scene.SHIP_IMAGE, getX(), getY(), getWidth(), getHeight(), null);
    } 
}
