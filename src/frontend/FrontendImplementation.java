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
	
	String RM1_response = "";
	String RM2_response = "";
	String RM3_response = "";
	
	int RM1_COUNT = 0;
	int RM2_COUNT = 0;
	int RM3_COUNT = 0;
	
	static Logger logger;

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
		logger.info("waiting for response...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String response = receiveResponse();
		return response;
	}

	@Override
	public String suspendAccount(String AdminUsername, String AdminPassword, String AdminIP, String UsernameToSuspend) {
		String request = "suspendAccount" +  "," + AdminIP + ","+ AdminUsername + "," + AdminPassword + "," + UsernameToSuspend;
		sendRequest(request);
		logger.info("waiting for response...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String response = receiveResponse();
		return response;
	}

	@Override
	public String createPlayerAccount(String firstName, String lastName, String age, String userName, String password,
			String ipAddress) {
		String request = "createPlayerAccount" +  "," + ipAddress + ","+ firstName + "," + lastName + ","+ age + "," + userName + "," + password;
		sendRequest(request);
		logger.info("waiting for response...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String response = receiveResponse();
		return response;
	}

	@Override
	public String playerSignIn(String userName, String password, String ipAddress) {
		String request = "playerSignIn" +  "," + ipAddress + ","+ userName + "," + password;
		sendRequest(request);
		logger.info("waiting for response...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String response = receiveResponse();
		return response;
	}

	@Override
	public String playerSignOut(String userName, String ipAddress) {
		String request = "playerSignOut" +  "," + ipAddress + ","+ userName;
		sendRequest(request.trim());
		logger.info("waiting for response...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String response = receiveResponse();
		return response;
	}

	@Override
	public String transferAccount(String userName, String password, String OldIPAddress, String NewIPAddress) {
		String request = "transferAccount" +  "," + OldIPAddress + ","+ NewIPAddress + "," + userName + "," + password;
		sendRequest(request);
		logger.info("waiting for response...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String response = receiveResponse();
		return response;
	}
	
	public void RM1Response() throws SocketException {
		
		DatagramSocket ds = null;
		try {
			
			ds = new DatagramSocket(Constants.RM1_FRONTEND_PORT);
			while (true) {
				
				byte[] res = new byte[Constants.BYTE_LENGTH];
				DatagramPacket request = new DatagramPacket(res, res.length);
				ds.receive(request);
				RM1_response = new String(request.getData());
				if (!RM1_response.isEmpty()) {
					logger.info("RM 1 : " + RM1_response);
				}
			}
			
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
	
	public void RM2Response() throws SocketException {
		
		DatagramSocket ds = null;
		try {
			
			ds = new DatagramSocket(Constants.RM2_FRONTEND_PORT);
			while (true) {
				
				byte[] res = new byte[Constants.BYTE_LENGTH];
				DatagramPacket request = new DatagramPacket(res, res.length);
				ds.receive(request);
				RM2_response = new String(request.getData());
				if (!RM2_response.isEmpty()) {
					logger.info("RM 2 : " + RM2_response);
				}
			}
			
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
	
	public void RM3Response() throws SocketException {
		
		DatagramSocket ds = null;
		try {
			
			ds = new DatagramSocket(Constants.RM3_FRONTEND_PORT);
			while (true) {
				
				byte[] res = new byte[Constants.BYTE_LENGTH];
				DatagramPacket request = new DatagramPacket(res, res.length);
				ds.receive(request);
				RM3_response = new String(request.getData());
				if (!RM3_response.isEmpty()) {
					logger.info("RM 3 : " + RM3_response);
				}
			}
			
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
	
	public void sendRequest(String message) {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			byte[] msg = message.getBytes();
			InetAddress ia = InetAddress.getByName(Constants.LOCALHOST);
			DatagramPacket request = new DatagramPacket(msg, msg.length, ia, Constants.SEQUENCER_PORT);
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
	
	public String receiveResponse() {
		if (RM1_response.trim().equals(RM2_response.trim())
				&& RM2_response.trim().equals(RM3_response.trim())) {
			return RM1_response;
		} else if (RM1_response.trim().equals(RM2_response.trim())) {
			if (!RM3_response.equals("Server crashed")) {
				RM3_COUNT++;
				System.out.println(" RM3_COUNT " + RM3_COUNT);
				if (RM3_COUNT == 3) {
					logger.info("FRONTEND : RM1 sending to RM3");
					multicastFailtoRM("Server defect", Constants.RM1_ID, Constants.RM3_ID);
					RM3_COUNT = 0;
				}
			}
			return RM1_response;
		} else if (RM1_response.trim().equals(RM3_response.trim())) {
			RM2_COUNT++;
			if (RM2_COUNT == 3) {
				logger.info("FRONTEND : RM1 sending to RM2");
				multicastFailtoRM("Server defect", Constants.RM1_ID, Constants.RM2_ID);
				RM2_COUNT = 0;
			}
			return RM1_response;
		} else if (RM2_response.trim().equals(RM3_response.trim())) {
			RM1_COUNT++;
			if (RM1_COUNT == 3) {
				logger.info("RM2 sending to RM1");
				multicastFailtoRM("Server defect", Constants.RM2_ID, Constants.RM1_ID);
				RM1_COUNT = 0;
			}
			return RM2_response;
		}
		return RM1_response;
	}
	
	public void multicastFailtoRM(String message, String id1, String id2) {
		DatagramSocket ds = null;
		try {
			String data = message + "," + id1 + "," + id2;
			ds = new DatagramSocket();
			byte[] msg = data.getBytes();
			InetAddress ia = InetAddress.getByName(Constants.LOCALHOST);
			DatagramPacket dp = new DatagramPacket(msg, msg.length, ia, Constants.FAULT_PORT);
			ds.send(dp);
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
