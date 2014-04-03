package org.blazesoft.bilbobaggins;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Gage
 * @version 0.0.0
 */

public class Program extends JFrame
{
    Scene scene;
    
    public Program()
    {
        super("BilboBaggins");
        
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args)
    {
        final Program program = new Program();
        program.init();
    }
    
    public void init()
    {
        try
        {
            SwingUtilities.invokeAndWait(new Runnable() 
            {
                @Override
                public void run() 
                {
                    setSize(652, 600);
                    scene = new Scene();
                    
                    add(scene);
                    
                    scene.setVisible(true);
                    setVisible(true);
                }
            });
        }
        catch(Exception e) { JOptionPane.showMessageDialog(scene, e.getStackTrace());}
    }
}
