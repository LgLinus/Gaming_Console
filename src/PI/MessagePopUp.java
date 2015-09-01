package PI;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Class that gives a popup message which dissapears over time
 * @author Linus Granath
 *
 */
public class MessagePopUp {
	
	JOptionPane pane;
	JFrame frame;
	JDialog dialog;
	public MessagePopUp(String message){
		TimeRemove timer = new TimeRemove();
		timer.start();
	   frame = new JFrame("JOptionPane showMessageDialog example");
	    
	    // show a joptionpane dialog using showMessageDialog
	    JOptionPane.showMessageDialog(frame,
	       message);
	}
	
	//Remove the frame after 2 seconds
	private class TimeRemove extends Thread{
		public void run(){
			try {
				Thread.sleep(2000);
				frame.dispose();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
