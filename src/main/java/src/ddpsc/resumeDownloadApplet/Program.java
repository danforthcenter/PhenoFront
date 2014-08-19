package src.ddpsc.resumeDownloadApplet;

import java.awt.Dimension;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * Holy mother of god if you export this to a jar, the Manifest file modifications to
 * specific that Program.class is the main() entry point can be found in /lib/Manifest.txt
 * 
 * The magic line:
 * 		jar -cfm jarfilepath manifestfilepath
 * 
 * And whatever you do, don't delete the empty line at the end of the file.
 * 
 * @author Admin
 *
 */
public class Program
{
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Resume Download Applet");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        //Add the ubiquitous "Hello World" label.
        JApplet applet = new JResumeDownloadApplet();
        frame.getContentPane().add(applet);
        frame.setPreferredSize(new Dimension(625, 400));
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
