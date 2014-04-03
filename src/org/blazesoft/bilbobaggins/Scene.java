package org.blazesoft.bilbobaggins;

import org.blazesoft.bilbobaggins.entities.Bullet;
import org.blazesoft.bilbobaggins.entities.PowerUp;
import org.blazesoft.bilbobaggins.entities.Entity;
import org.blazesoft.bilbobaggins.entities.Ship;
import org.blazesoft.bilbobaggins.entities.Monster;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import org.blazesoft.bilbobaggins.entities.Stalker;

/**
 *
 * @author Gage
 */
public final class Scene extends JPanel implements MouseWheelListener, Runnable
{
    public static int
            SCENE_WIDTH = 652,
            SCENE_HEIGHT = 600;
    
    public static Image 
            SHIP_IMAGE = null;
    
    final List<Entity> entities;

    boolean 
            gameOver = false,
            gameWon = true,
            debug = true,
            monsterSpawning = true;
    
    int
            lastFrames = 0,
            currentFrames = 0,
            
            totalMonstersPassed = 0;
    
    long
            nextSecond = System.currentTimeMillis() + 1000;

    public Scene()
    {
        entities = Collections.synchronizedList(new ArrayList<Entity>());
        
        try
        {
            InputStream inputStream = Scene.class.getResourceAsStream("/org/blazesoft/bilbobaggins/images/ship.png");
            Scene.SHIP_IMAGE = ImageIO.read(inputStream);
        }
        catch(Exception e) { }
        
        load();
        init();
    }
    
    public void init()
    {
        setupTimers();
        
        Action reloadAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { if(gameOver) reload(); }};
        Action shootEnabledAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { getShip().setSpaceDown(true); }};
        Action shootDisabledAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { getShip().setSpaceDown(false); }};
        Action moveLeftEnabledAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { getShip().setLeftDown(true); }};
        Action moveLeftDisabledAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { getShip().setLeftDown(false); }}; 
        Action moveRightEnabledAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { getShip().setRightDown(true); }}; 
        Action moveRightDisabledAction = new AbstractAction()
        {@Override public void actionPerformed(ActionEvent actionEvent) { getShip().setRightDown(false); }}; 
        
        getInputMap().put(KeyStroke.getKeyStroke("pressed R"), "preload");
        getInputMap().put(KeyStroke.getKeyStroke("pressed SPACE"), "pspace");
        getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "rspace");
        getInputMap().put(KeyStroke.getKeyStroke("pressed LEFT"), "pleft");
        getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "rleft");
        getInputMap().put(KeyStroke.getKeyStroke("pressed RIGHT"), "pright");
        getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "rright");
        
        getActionMap().put("preload", reloadAction);
        getActionMap().put("pspace", shootEnabledAction);
        getActionMap().put("rspace", shootDisabledAction);
        
        getActionMap().put("pleft", moveLeftEnabledAction);
        getActionMap().put("rleft", moveLeftDisabledAction);
        getActionMap().put("pright", moveRightEnabledAction);
        getActionMap().put("rright", moveRightDisabledAction);
    }
    
    public void load()
    {
        setBackground(Color.black);
       
        entities.add(new Ship());
        
        new Thread(this, "Update").start();
        new Thread(getShip(), "Movement").start();
    }
    
    public void reload()
    {
        entities.clear();
        
        gameOver = false;
        gameWon = true;
        debug = true;
        monsterSpawning = true;
        
        lastFrames = 0;
        currentFrames = 0;
        totalMonstersPassed = 0;
        
        load();
    }
    
    public void setupTimers()
    {
        //final Ship ship = getShip();
        
        Timer repaintTimer = new Timer(1000/60, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) 
            {
                //while(!gameOver)
                {
                    checkFPS();
                    repaint();
                }
            }
        });

        repaintTimer.setRepeats(true);
        repaintTimer.start();
        
        Timer monsterSpawnTimer = new Timer(500, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) 
            {
                if(!gameOver && (debug == false || (debug == true && monsterSpawning)))
                {
                    Monster monster = new Monster();
                    monster.setSpeed(70 - getShip().getMonstersKilled());

                    entities.add(monster);
                    new Thread(monster, "Monster").start();
                }
            }
        });
        
        monsterSpawnTimer.setRepeats(true);
        monsterSpawnTimer.start();
        
        Timer powerUpSpawnTimer = new Timer(4000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) 
            {
                if(!gameOver)
                {
                    PowerUp powerUp = new PowerUp();
                    entities.add(powerUp);
                    new Thread(powerUp, "Power Up").start();
                }
            }
        });
        
        powerUpSpawnTimer.setRepeats(true);
        powerUpSpawnTimer.start();
        
        Timer stalkerSpawnTimer = new Timer(8000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) 
            {
                if(!gameOver)
                {
                    Stalker stalker = new Stalker(getShip());
                    entities.add(stalker);
                    new Thread(stalker, "Stalker").start();
                }
            }
        });
        
        stalkerSpawnTimer.setRepeats(true);
        stalkerSpawnTimer.start();
    }
    
    public void checkFPS()
    {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime > nextSecond)
        {
            nextSecond += 1000;
            lastFrames = currentFrames;
            currentFrames = 0;
        }
        
        currentFrames++;
    }
    
    public Ship getShip()
    {
        return entities.size() > 0 ? (Ship)entities.get(0) : null;
    }
    
    public void collision()
    {
        Ship ship = getShip();
        
        if(ship != null)
        {
            synchronized(entities)
            {
                for(int index = 1; index < entities.size(); index++)
                {
                    if(ship.intersects(entities.get(index)))
                    {
                        if(entities.get(index) instanceof Monster || entities.get(index) instanceof Stalker)
                        {
                            gameOver = true;
                        }
                        else
                        if(entities.get(index) instanceof PowerUp)
                        {
                            entities.get(index).setAlive(false);
                            ship.setBuff(Buff.values()[new Random().nextInt(Buff.values().length - 1) + 1]);
                        }
                    }
                } 
            }

            ship.collision(entities);
        }
    }
    
    public final void terminateDeadObjects()
    {
        Ship ship = getShip();
        
        synchronized(entities)
        {
            for(Entity entity : entities)
            {
                if(entity.getY() + entity.getHeight() >= 600)
                {
                    if(entity.isAlive())
                    {
                        entity.setAlive(false);
                        
                        if(entity instanceof Monster)
                        {
                            ship.setMonstersPassed(ship.getMonstersPassed() + 1);
                            totalMonstersPassed++;
                            ship.setScore(ship.getScore() - 10 * (long)Math.pow(1.25, ship.getMonstersPassed()));
                        }
                    }
                }
            }
            
            for(int index = 0; index < entities.size(); index++)
                if(!entities.get(index).isAlive())
                    entities.remove(index);
        }
        
        ship.terminateDeadBullets();
    }
    
    public void terminateAllObjects()
    {
        Ship ship = getShip();
        
        synchronized(ship.getBullets())
        {
            for(Bullet bullet : ship.getBullets())
                bullet.setAlive(false);
        }
        
        synchronized(entities)
        {
            for(Entity entity : entities)
                entity.setAlive(false);
        }
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) 
    {
        getShip().move(mouseWheelEvent.getWheelRotation());
    }
    
    @Override
    public void run() 
    {
        Ship ship = getShip();
        
        while(!gameOver)
        {
            collision();
            terminateDeadObjects();
            checkForMovement();
            
            if(ship.getScore() <= -999)
            {
                gameOver = true;
                gameWon = false;
            }
            
            try { Thread.sleep(10); } catch(Exception e) { }
        }
        
        if(gameWon)
        {
            ship.setScore(ship.getScore() + (long)(ship.getScore() * ship.getAccuracy()));
            
            /*
            String name = JOptionPane.showInputDialog(this, "What is your name?");
            String url = "http://bsx3.cloudapp.net/?insert=true&name=" + name + "&score=" + ship.getScore();
            
            try
            {
                URL httpObject = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) httpObject.openConnection();
                
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                
                int responseCode = connection.getResponseCode();
                
            
            }
            catch(Exception e)
            {
                
            }*/
        }
        else
            ship.setScore(-999);
        
        terminateAllObjects();
    }
    
    public void checkForMovement()
    {
        Ship ship = getShip();
        
        synchronized(entities)
        {
            for(Entity entity : entities)
                entity.setCachedPosition();
        }
        
        synchronized(ship.getBullets())
        {
            for(Bullet bullet : ship.getBullets())
                bullet.setCachedPosition();
        }
    }
    
    @Override
    public void paintComponent(Graphics graphics) 
    {
        Graphics2D graphics2D = (Graphics2D) graphics;
        
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,
          RenderingHints.VALUE_RENDER_QUALITY);
        
        super.paintComponent(graphics2D);
        
        FontMetrics fontMetrics = getFontMetrics(getFont());
        
        Ship ship = getShip();
        
        graphics.setColor(Color.lightGray);
        graphics.drawString("Monsters killed: " + ship.getMonstersKilled(), 500, 20);
        graphics.drawString("Monsters passed: " + totalMonstersPassed, 500, 40);
        graphics.drawString(String.format("Accuracy: %.2f%%", ship.getAccuracy() * 100), 500, 60);
        graphics.drawString("Score: " + ship.getScore(), 500, 80);
        graphics.drawString("Buff active: " + ship.getBuff(), 500, 100);
        
        if(!gameOver)
        {
            synchronized(entities)
            {
                for(Entity entity : entities)
                        entity.draw(graphics2D);
            }
            
            synchronized(ship.getBullets())
            {
                for(Bullet bullet : ship.getBullets())
                    bullet.draw(graphics2D);
            }
        }
        else
        {
            String losingMessage = "Nice try!";
            String scoreMessage =  "You finished with a score of "  + ship.getScore() + ".";
            String tryMessage = "Press the R key to try again.";
            
            graphics.drawString(losingMessage, (SCENE_WIDTH - fontMetrics.stringWidth(losingMessage)) / 2, 280);
            graphics.drawString(scoreMessage, (SCENE_WIDTH - fontMetrics.stringWidth(scoreMessage)) / 2, 300);
            graphics.drawString(tryMessage, (SCENE_WIDTH - fontMetrics.stringWidth(tryMessage)) / 2, 320);
        }

        if(debug)
           graphics.drawString("FPS: " + lastFrames, 10, 20); 
    }
}
