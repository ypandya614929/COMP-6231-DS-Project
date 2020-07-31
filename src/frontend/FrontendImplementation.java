/**
 * 
 */
package frontend;

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

import org.omg.CORBA.ORB;

import constants.Constants;

import FECorba.FrontendInterfacePOA;
/**
 * @author ypandya
 *
 */
public class FrontendImplementation extends FrontendInterfacePOA {
	
	private ORB orb;
	static Logger logger;
	static String fe_response;
	static boolean is_response_received = false;

	public FrontendImplementation() {

		addLog("logs/FrontEnd.txt", "FrontEnd");
	
	}

	public void setORB(ORB orb_val) {
		orb = orb_val;
	}

	public ORB getOrb() {
		return orb;
	}

	@Override
	public String getPlayerStatus(String userName, String password, String ipAddress) {
		String request = "getPlayerStatus" +  "," + ipAddress + ","+ userName + "," + password;
		sendRequest(request);
		logger.info("FE Request : " + request);
		logger.info("waiting for response...");
		String reply = getResponse();
		logger.info("FE Response : " + reply);
		return reply;
	}

	@Override
	public String suspendAccount(String AdminUsername, String AdminPassword, String AdminIP, String UsernameToSuspend) {
		String request = "suspendAccount" +  "," + AdminIP + ","+ AdminUsername + "," + AdminPassword + "," + UsernameToSuspend;
		sendRequest(request);
		logger.info("FE Request : " + request);
		logger.info("waiting for response...");
		String reply = getResponse();
		logger.info("FE Response : " + reply);
		return reply;
	}

	@Override
	public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password,
			String ipAddress) {
		String request = "createPlayerAccount" +  "," + ipAddress + ","+ firstName + "," + lastName + ","+ age + "," + userName + "," + password;
		sendRequest(request);
		logger.info("FE Request : " + request);
		logger.info("waiting for response...");
		String reply = getResponse();
		logger.info("FE Response : " + reply);
		return reply;
	}

	@Override
	public String playerSignIn(String userName, String password, String ipAddress) {
		String request = "playerSignIn" +  "," + ipAddress + ","+ userName + "," + password;
		sendRequest(request);
		logger.info("FE Request : " + request);
		logger.info("waiting for response...");
		String reply = getResponse();
		logger.info("FE Response : " + reply);
		return reply;
	}

	@Override
	public String playerSignOut(String userName, String ipAddress) {
		String request = "playerSignOut" +  "," + ipAddress + ","+ userName;
		sendRequest(request.trim());
		logger.info("FE Request : " + request);
		logger.info("waiting for response...");
		String reply = getResponse();
		logger.info("FE Response : " + reply);
		return reply;
	}

	@Override
	public String transferAccount(String userName, String password, String OldIPAddress, String NewIPAddress) {
		String request = "transferAccount" +  "," + OldIPAddress + ","+ NewIPAddress + "," + userName + "," + password;
		sendRequest(request);
		logger.info("FE Request : " + request);
		logger.info("waiting for response...");
		String reply = getResponse();
		logger.info("FE Response : " + reply);
		return reply;
	}
	
	public void sendRequest(String message) {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			byte[] msg = message.getBytes();
			InetAddress ia = InetAddress.getByName(Constants.LOCALHOST);
			DatagramPacket request = new DatagramPacket(msg, msg.length, ia, Constants.LEADER_PORT);
			ds.send(request);
		} catch (SocketException e) {
			logger.info(e.getMessage());
		} catch (UnknownHostException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void serverConnection(int port) {
		
		logger.info("FrontEnd Started!");
		DatagramSocket ds = null;

		while (true) {
			try {
								
				ds = new DatagramSocket(port);
				byte[] receive = new byte[Constants.BYTE_LENGTH];
				DatagramPacket dp = new DatagramPacket(receive, receive.length);
				ds.receive(dp);
				byte[] data = dp.getData();
				
				fe_response = new String(data);
				synchronized (this) {
		            is_response_received = true;
		            this.notifyAll();
		        }
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ds.close();
			}
		}
	}
	
	synchronized public String getResponse() {
		synchronized (this) {
            while (!is_response_received) {
                try {
                    this.wait();
                } catch (InterruptedException e) {}
            }
        }
		String reply = new String(fe_response);
		is_response_received = false;
		fe_response = "";
		return reply;
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

