package PI;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class responsible of running a game (process)
 * 
 * @author Linus Granath
 * 
 */
public class GameRunner {

	private Process p = null;
	private String path;
	private Controller controller;
	private InputStream is;
	private int bytesRead;
	private byte[] aByte = new byte[1];
	private CheckProcess cp = null;
	private boolean gotInput = false;

	GameRunner(Controller controller, String string) throws IOException {
		path = string;
		cp = new CheckProcess();
		this.controller = controller;
		cp.start();
		System.out.println("RUN GAME " + path);
	}

	// Class added 2014-05-16
	private class ReceiverFromGame extends Thread {

		public ReceiverFromGame() {
			start();
		}

		public void run() {
			ServerSocket servSocket = null;
			try {
				Socket socket = null;
				try {
					servSocket = new ServerSocket(8083);
					servSocket.setSoTimeout(15000);

					socket = servSocket.accept();
					gotInput = true;
				} catch (InterruptedIOException e) {
					new MessagePopUp("Could not load the game properly");
					cp.close();
				}
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				while (true) {

					String inputLine = null;
					inputLine = reader.readLine();
					if (inputLine != null) {
						System.out.println(inputLine);
						if (inputLine.contains("exit")) {
							socket = null;
							System.out.println("connect");
							servSocket.close();
							servSocket = null;
							controller.addSendLocalInput();
							break;
						} 
						
						
						else if (inputLine.equals("layout")) {
							System.out.println("RECEIVED FILE");

							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							is = socket.getInputStream();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (is != null) {
								FileOutputStream fos = null;
								BufferedOutputStream bos = null;
								try {
									fos = new FileOutputStream(
											"C:/temp/layout.txt");
									bos = new BufferedOutputStream(fos);
									bytesRead = is.read(aByte, 0, aByte.length);
									System.out.println(bytesRead);
									do { // Start fetching the bytes, byte for
											// byte
										baos.write(aByte);
										bytesRead = is.read(aByte);
									} while (bytesRead != -1);
									bos.write(baos.toByteArray());
									bos.flush();
									bos.close();
									socket.close();
									servSocket.close();
									controller.sendLayoutToDevice(baos
											.toByteArray());
									controller.addSendLocalInput();
									break;
								} catch (IOException ex) {
									// Do exception handling
								}
							}
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class CheckProcess extends Thread {
		public void run() {
			try {
				new ReceiverFromGame();
				p = Runtime.getRuntime().exec(path);
				// controller.addSendLocalInput(); - borttaget 2014-05-16
				try {
					p.waitFor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("PRocess closed");
				controller.closeProcess();
				p = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void close() {
			p.destroy();
			controller.closeProcess();
			p = null;
		}
	}

	/**
	 * Check if the game is running return true if it does
	 * 
	 * @return result
	 */
	public boolean checkIfProccessRuns() {
		if (p == null)
			return false;
		else
			return true;
	}

	public boolean gotInput() {
		return gotInput;
	}
}
