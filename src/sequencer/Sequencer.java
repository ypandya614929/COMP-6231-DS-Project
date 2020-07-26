/**
 * 
 */
package sequencer;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import constants.Constants;

import FECorba.FrontendInterface;
/**
 * @author ypandya
 *
 */
public class Sequencer {
	
	static long count = 1;
	static FrontendInterface sequencerObj;
	static Logger logger;
	
	public void startSequencer() {
		
		DatagramSocket ds = null;
		
		try {
			
			addLog("logs/sequencer.txt", "Sequencer");
			ds = new DatagramSocket(Constants.SEQUENCER_PORT);

			logger.info("Sequencer Started");
			while (true) {
				
				byte[] buffer = new byte[Constants.BYTE_LENGTH];
				
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				byte[] data = dp.getData();
				String[] data1 = new String(data).split(",");
				String ip = data1[1];
				int port = getServerPort(ip);
				String dpData = new String(data).trim();
				logger.info("Sequencer Data : " + dpData);
				count++;
				InetAddress ia = InetAddress.getByName(Constants.MULTICAST_IP);
				byte[] msg = dpData.getBytes();
				new DatagramSocket().send(new DatagramPacket(msg, msg.length, ia, port));
			}
			
		} catch (SocketException e) {
			logger.info(e.getMessage());
		} catch (UnknownHostException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method is used to return port based on ip
	 * @param ip new ip of transfer account
	 * @return ip
	 */
	public static int getServerPort(String ip) {
		if (ip.startsWith("132")) {
			return Constants.NA_SERVER_PORT;
		}
		else if (ip.startsWith("93")) {
			return Constants.EU_SERVER_PORT;
		}
		else if (ip.startsWith("182")) {
			return Constants.AS_SERVER_PORT;
		}
		return 0;	
	}
	
	/**
	 * This method is used to set/update logger
	 * @param path
	 * @param key
	 */
	static void addLog(String path, String key) {
		try {
			File f = new File(path);
			String data = "";
			logger = Logger.getLogger(key);
			if(f.exists() && !f.isDirectory()) { 
				data = new String(Files.readAllBytes(Paths.get(path)));
			}
			if (logger.getHandlers().length < 1)
			{	
				try {
					f.delete();
				} catch (Exception e) {}
				logger = Logger.getLogger(key);
				FileHandler fh = new FileHandler(path, true);
				SimpleFormatter ft = new SimpleFormatter();
				fh.setFormatter(ft);
				logger.addHandler(fh);
				logger.setUseParentHandlers(false);
				logger.info(data);
				logger.setUseParentHandlers(true);
				
			}
		} catch (Exception err) {
			logger.info("Unable to create file, please check file permission.");
		}
	}
	
}
