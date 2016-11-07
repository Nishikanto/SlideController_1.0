package slideviewer;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFrame;

/**
 *
 * @author root
 */
public class ImageViewer extends JFrame {

    // this line is needed to avoid serialization warnings  
    private static final long serialVersionUID = 1L;

    Image screenImage; // downloaded image  
    int w, h; // Display height and width  
    int numOfSlide;
    Frame frame;

    ImageViewer(int numOfSlide) {
        this.numOfSlide = numOfSlide;
        frame = this;
    }

    // Class constructor  
    public void slideShow(String source, int pos) throws MalformedURLException {

        if (pos < numOfSlide && pos >= 0) {
            if (this.isActive()) {
                this.setVisible(false);
                this.dispose();
                this.setUndecorated(true);
            }

            // Exiting program on window close 
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    frame.dispose();
                    //System.exit(0);
                }
            });

            // Exitig program on mouse click 
            addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    frame.dispose();
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            }
            );

            // window should be visible 
            this.setVisible(true);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            gs.setFullScreenWindow(this);
            //this.validate();// otherwise - file 

            screenImage = Toolkit.getDefaultToolkit().getImage(source); // otherwise - file 
        }

    }

    public void paint(Graphics g) {
        if (screenImage != null) // if screenImage is not null (image loaded and ready) 
        {
            g.drawImage(screenImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
        // to draw image at the center of screen 
        // we calculate X position as a half of screen width minus half of image width 
        // Y position as a half of screen height minus half of image height 
    }

    public void close() {
        this.dispose();
    }

    public void exit() {
        System.exit(0);
    }
}
