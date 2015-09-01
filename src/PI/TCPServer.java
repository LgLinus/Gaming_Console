      package PI;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TCPServer listens for a client to send a request. TCPServer then converts a
 * specific file, based on that request, into a byte and sends it to the client
 * 
 * @author Mattias Larsson with help from Thomas Andersson
 * 
 */
class TCPServer {
	private int portSend = 3248, portRecive = 3247;
	private String fileToSend = "";
	private String filePath = "C:/Users/Linus/Desktop/ProjektVT14/Server_Games/"; // 140426 (TA) removed fileType
	private ServerSocket serverSocketSend = null, serverSocketRecive;
	private Socket socketSend = null, socketRecive;
	private Thread clientThread;
	private BufferedOutputStream outToClient;

	private BufferedReader bufferedReader;
	private String inputLine = null;

	
	public TCPServer() {
		Thread connectThread = new Thread(new Connect());
		connectThread.start();
	}
	/**
	 * Inner class used to listen for a client to connect
	 * @author Mattias Larsson
	 *
	 */
	private class Connect implements Runnable {
		public void run() {
			try {
				serverSocketSend = new ServerSocket(portSend);
				serverSocketRecive = new ServerSocket(portRecive);
				while (true) {
					socketRecive = serverSocketRecive.accept();
					bufferedReader = new BufferedReader(new InputStreamReader(socketRecive.getInputStream()));
					inputLine = bufferedReader.readLine();
					System.out.println(inputLine);
					if (inputLine.equals("gamelist")) {	//if the client sends a request of a list of games instead of a filename
						socketSend = serverSocketSend.accept();
						clientThread = new Thread(new ArrayToClient(socketSend));
						clientThread.start();
					} else {
						socketSend = serverSocketSend.accept();
						fileToSend = filePath + inputLine; // 140426 (TA)
						System.out.println(inputLine);
						outToClient = new BufferedOutputStream(socketSend.getOutputStream());

						clientThread = new Thread(new TalkToClient(socketSend,outToClient));
						clientThread.start();
					}
				}
			} catch (IOException e1) {
				System.out.println(e1);
			}
			try {
				serverSocketSend.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Inner class that sends a String array with the filenames of all the files located
	 * on the server
	 * @author Mattias Larsson
	 * 
	 */
	private class ArrayToClient implements Runnable {
		private Socket socket;

		public ArrayToClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				String[] gameList = getFileNames(filePath);
				oos.writeObject(gameList);
				oos.flush();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * This method reads all the filenames in a specified directory and put them in a
		 * string array, then returns that array
		 * @param directoryPath the path where you want the method to retrive the filenames from
		 * @return a string array with all the filenames located in the specified directory
		 */
		public String[] getFileNames(String directoryPath) {

			File dir = new File(directoryPath);
			Collection<String> files = new ArrayList<String>();
			if (dir.isDirectory()) {
				File[] listFiles = dir.listFiles();

				for (File file : listFiles) {
					if (file.isFile()) {
						files.add(file.getName());
					}
				}
			}

			return files.toArray(new String[] {});
		}
	}
	/**
	 * Inner class used for sending a requested file to the client
	 * @author Mattias Larsson
	 *
	 */
	private class TalkToClient implements Runnable {
		private Socket socket;
		private BufferedOutputStream outToClient;

		public TalkToClient(Socket socket, BufferedOutputStream outToClient) {
			this.socket = socket;
			this.outToClient = outToClient;
		}

		public void run() {

			while (socket != null) {

				File file = new File(fileToSend);
				byte[] fileByteArray = new byte[(int) file.length()];
				FileInputStream fileInStream = null;

				try {
					fileInStream = new FileInputStream(file);
				} catch (FileNotFoundException ex) {
					System.out.println(ex);
				}
				BufferedInputStream bis = new BufferedInputStream(fileInStream);

				try {
					System.out.println("Sending " + fileToSend);
					bis.read(fileByteArray, 0, fileByteArray.length);
					outToClient.write(fileByteArray, 0, fileByteArray.length);
					outToClient.flush();
					outToClient.close();
					fileByteArray = null; // 0428 TA
					System.gc(); // 0428 TA
					return;
				} catch (IOException ex) {
					System.out.println(ex);
					break; // added 140426 (TA) to prevent server restart if
							// client app was terminated during transfer
				}
			}
		}
	}

	public static void main(String[] args) {
		new TCPServer();
	}
}