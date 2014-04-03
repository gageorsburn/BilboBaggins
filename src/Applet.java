

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.blazesoft.bilbobaggins.Scene;

/**
 *
 * @author Gage
 */
public class Applet extends JApplet
{
    Scene scene;
    
    @Override
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
