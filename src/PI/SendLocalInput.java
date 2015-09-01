package PI;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Thread that will forward every input from the remotes to the current game
 * @author LgLinuss
 *
 */
public class SendLocalInput extends Thread {

	private Socket socket;
	private boolean send = false;
	private String input =null;
	private Controller controller;
	/**
	 * Construct the class with a reference to our controller
	 * @param controller
	 */
	public SendLocalInput(Controller controller) {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new MessagePopUp("Accepting input now");
		start();
		this.controller = controller;
	}

	/**
	 * Run the thread, forward any input
	 */
	@Override
	public void run() {
		PrintWriter pw = null;
		while (true) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(controller.getGameRunner()==null){
				break;
			}
			if (send) {
				System.out.println("STARTEDDD");
				try {
					if(socket==null){
						InetAddress address = InetAddress.getLocalHost();

						System.out.println("STARTEDDD");
					socket = new Socket(address.getHostAddress(), 8082);
					pw = new PrintWriter(socket.getOutputStream(),
							true);
					}
					if(pw!=null){
					pw.println(input);
					System.out.println("CONNECTE sendD");
					send = false;}
				
								}
				catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Set send to true, which allows us to send the input
	 * @param input to send
	 */
	public void sendInput(String input) {
		this.input = input;
		send = true;
	}
}
