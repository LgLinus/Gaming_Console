package PI;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This is the main launch class.
 * @author Ivan Larsson
 *
 */
public class StartAll {
	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(GUI_Pi.class.getName())
					.log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(GUI_Pi.class.getName())
					.log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(GUI_Pi.class.getName())
					.log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(GUI_Pi.class.getName())
			
			
			
			
					.log(Level.SEVERE, null, ex);
		}
		Controller controller = new Controller();		
	}
}
