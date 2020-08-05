package RM2Server;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import RM2Server.GenerateRequest;
import RM2Server.MyRMThreads;
import constants.Constants;

//References:
/**
 *
 * @author ypandya
 */
public class RM2GameServer {
	
	static long count = 0;
	static Logger logger;
	static final int Threads = 3;
	
	static String response = "";
	
	static String RM1_response = "";
	static String RM2_response = "";
	static String RM3_response = "";
	
	static int RM3_COUNT = 0;
	
	public RM2EUServer rm2_eu_obj;
	public RM2ASServer rm2_as_obj;
	public RM2NAServer rm2_na_obj;
	
	static boolean is_send_response = false;
	static boolean is_request_receive = true;
	
	static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	public static int request_queue_id = 1;
	public static ArrayList<GenerateRequest> request_queue_list = new ArrayList<GenerateRequest>(); 
	public static PriorityQueue<GenerateRequest> request_queue = new PriorityQueue<GenerateRequest>(100, new RequestComparator()); 
	
	PrintWriter writer;
	/**
	 * This is the RM2GameServer class
	 */
	
	public RM2GameServer(boolean is_leader) {
		
//		try {
//			writer = new PrintWriter("logs/Testing.txt", "UTF-8");
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		try {
			
			rm2_eu_obj = new RM2EUServer();
			rm2_as_obj = new RM2ASServer();
			rm2_na_obj = new RM2NAServer();
			
			if (is_leader) {
				
				Runnable t1 = () -> {
					startLeader();
				};
			    Thread thread1 = new Thread(t1);
			    thread1.start();

			    Runnable t2 = () -> {
			    	try {
						RM1Response();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			    Thread thread2 = new Thread(t2);
			    thread2.start();
			    
			    Runnable t3 = () -> {
			    	try {
						RM2Response();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			    Thread thread3 = new Thread(t3);
			    thread3.start();
			    
			    Runnable t4 = () -> {
			    	try {
						RM3Response();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			    Thread thread4 = new Thread(t4);
			    thread4.start();
			    
			    Runnable t5 = () -> {
			    	try {
						sendResponse();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			    Thread thread5 = new Thread(t5);
			    thread5.start();
			    
			    Runnable t6 = () -> {
			    	try {
				    	sendRequest();
			    	} catch (Exception e) {
			    		e.printStackTrace();
			    	}
				};
			    Thread thread6 = new Thread(t6);
			    thread6.start();
			    
			}				   
		    
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	public void RM1Response() throws SocketException {
		DatagramSocket ds = null;
		try {
			
			ds = new DatagramSocket(Constants.RM1_PORT);
			while (true) {
				
				byte[] res = new byte[Constants.BYTE_LENGTH];
				DatagramPacket request = new DatagramPacket(res, res.length);
				ds.receive(request);
				RM1_response = new String(request.getData());
				if (!RM1_response.isEmpty()) {
					logger.info("RM 1 : " + RM1_response.split("#")[0]);
				}
				if(!isNullOrEmpty(RM1_response)) {
					synchronized (this) {
						is_send_response = true;
						this.notifyAll();
					}
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
			
			ds = new DatagramSocket(Constants.RM2_PORT);
			while (true) {
				
				byte[] res = new byte[Constants.BYTE_LENGTH];
				DatagramPacket request = new DatagramPacket(res, res.length);
				ds.receive(request);
				RM2_response = new String(request.getData());
				if (!RM2_response.isEmpty()) {
					logger.info("RM 2 : " + RM2_response.split("#")[0]);
				}
				if(!isNullOrEmpty(RM1_response) && !isNullOrEmpty(RM2_response) && !isNullOrEmpty(RM3_response)) {
					synchronized (this) {
						is_send_response = true;
						this.notifyAll();
					}
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
			
			ds = new DatagramSocket(Constants.RM3_PORT);
			while (true) {
				
				byte[] res = new byte[Constants.BYTE_LENGTH];
				DatagramPacket request = new DatagramPacket(res, res.length);
				ds.receive(request);
				RM3_response = new String(request.getData());
				if (!RM3_response.isEmpty()) {
					logger.info("RM 3 : " + RM3_response.split("#")[0]);
				}
				if(!isNullOrEmpty(RM1_response) && !isNullOrEmpty(RM2_response) && !isNullOrEmpty(RM3_response)) {
					synchronized (this) {
						is_send_response = true;
						this.notifyAll();
					}
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
	
	/**
	 * This method is used to check the string is empty/null or not
	 * @param str
	 * @return boolean true if string is null, false otherwise
	 */
	public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }
	
	
	public void sendResponse() throws IOException, InterruptedException {
		while(true) {
			synchronized (this) {
	            while (!is_send_response) {
	                try {
	                    this.wait();
	                } catch (InterruptedException e) {}
	            }
	            is_send_response = false;
	            Thread.sleep(1500);
	            response = generateResponse();
	            if (!isNullOrEmpty(response)) {
					InetAddress ia = InetAddress.getByName(Constants.LOCALHOST);
					String no = response.split("#")[1];
					response = response.split("#")[0].trim();
					logger.info("##### " + no);
					logger.info("Leader Response No : " + no + ", Data : " + response);
//					writer.println("Leader Response No : " + no + ", Data : " + response);
					new DatagramSocket().send(new DatagramPacket(response.getBytes(), response.length(), ia, Constants.FRONTEND_RESPONSE_PORT));
					response = "";
					RM1_response = "";
					RM2_response = "";
					RM3_response = "";
					synchronized (this) {
						is_request_receive = true;
						this.notifyAll();
					}
	            }
	        }				
		}
	}	
	
	public String generateResponse() {
		if (RM1_response.trim().equals(RM2_response.trim())
				&& RM2_response.trim().equals(RM3_response.trim())) {
			return RM1_response;
		} else if (RM1_response.trim().equals(RM2_response.trim())) {
			if (!isNullOrEmpty(RM3_response)) {
				RM3_COUNT++;
				if (RM3_COUNT == 3) {
					synchronized (this) {
						logger.info("RM3 : == Byzantine error == ");
						sendFailuretoRM("Data recovery");
						this.notifyAll();
					}
				}
			}
			return RM1_response;
		}
		return RM1_response;
	}
	
	public static void sendFailuretoRM(String message) {
		byte[] msg = message.getBytes();
		ExecutorService executor = Executors.newFixedThreadPool(Threads);
		int rnd = ((int) (Math.random()*(2 - 1))) + 1;
		ArrayList<Integer> port_list = new ArrayList<Integer>();
		if (rnd == 1) {
			logger.info("== RM1 sending data to RM3 ==");
			port_list.add(Constants.RM1_EU_SEND_DATA_PORT);
			port_list.add(Constants.RM1_NA_SEND_DATA_PORT);
			port_list.add(Constants.RM1_AS_SEND_DATA_PORT);
		}
		if (rnd == 2) {
			logger.info("== RM2 sending data to RM3 ==");
			port_list.add(Constants.RM2_EU_SEND_DATA_PORT);
			port_list.add(Constants.RM2_NA_SEND_DATA_PORT);
			port_list.add(Constants.RM2_AS_SEND_DATA_PORT);
		}
		for (int i = 0; i < Threads; i++) {
			int port = port_list.get(i);
			Runnable task = new MyRMThreads(msg, port);
			executor.execute(task);
		}
		executor.shutdown();
	}
	
	public static void startLeader() {
		
		
		DatagramSocket ds = null;
		
		try {
			
			addLog("logs/Leader.txt", "Leader");
			ds = new DatagramSocket(Constants.LEADER_PORT);
			
			logger.info("Leader is up!");
			
			while (true) {
				
				byte[] buffer = new byte[Constants.BYTE_LENGTH];
				
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				byte[] data = dp.getData();
				count++;
				String dpData = new String(data).trim();				
				GenerateRequest request = new GenerateRequest(dpData, (int) count);
				lock.writeLock().lock();
				request_queue.add(request);
				lock.writeLock().unlock();

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
	
	synchronized public void sendRequest() throws IOException, InterruptedException{
		while(true) {
			synchronized (this) {
	            while (!is_request_receive) {
	                try {
	                    this.wait();
	                } catch (InterruptedException e) {}
	            }
	            if (is_request_receive) {
	            	if (RM3_COUNT==3) {
	            		Thread.sleep(2500);
	            	}
	            	lock.readLock().lock();
	            	Iterator<GenerateRequest> request_itr = request_queue.iterator();
	        		while (request_itr.hasNext()) {
        				GenerateRequest request = request_itr.next();
        				if (request != null) {
        					try {
    		        			if(request.getCount() == request_queue_id) {
    		        	            is_request_receive = false;
    		        				request_queue_id++;
    		        				String req = request.getReq();
    		        				try {
    			        				request_queue_list.add(request);
    		        				} catch (Exception e) {}
    		        				req = req.concat(","+request.getCount());
    		        				String[] data1 = new String(req).split(",");
    		        				String ip = data1[1];
    		        				byte[] msg = req.getBytes();
    		        				logger.info("##### " + request.getCount());
    		        				logger.info("Leader Request No : " + request.getCount() + ", Data : " + request.getReq());
    		        				ExecutorService executor = Executors.newFixedThreadPool(Threads);
    		        				for (int i = 1; i <= Threads; i++) {
    		        					int port = getServerPort(ip, i);
    		        					Runnable task = new MyRMThreads(msg, port);
    		        					executor.execute(task);
    		        				}
    		        				executor.shutdown();
    		        			}
    	        			} catch (Exception e) {
    		        			e.printStackTrace();
    	        			}
        				}
	        		}
	        		lock.readLock().unlock();
	            }
	        }
		}
	}
	
	/**
	 * This method is used to return port based on ip
	 * @param ip new ip of transfer account
	 * @return ip
	 */
	public static int getServerPort(String ip, int rm_num) {
		if (ip.startsWith("132")) {
			if (rm_num == 1) {
				return Constants.RM1_NA_SERVER_PORT;
			}
			if (rm_num == 2) {
				return Constants.RM2_NA_SERVER_PORT;
			}
			if (rm_num == 3) {
				return Constants.RM3_NA_SERVER_PORT;
			}
		}
		else if (ip.startsWith("93")) {
			if (rm_num == 1) {
				return Constants.RM1_EU_SERVER_PORT;
			}
			if (rm_num == 2) {
				return Constants.RM2_EU_SERVER_PORT;
			}
			if (rm_num == 3) {
				return Constants.RM3_EU_SERVER_PORT;
			}
		}
		else if (ip.startsWith("182")) {
			if (rm_num == 1) {
				return Constants.RM1_AS_SERVER_PORT;
			}
			if (rm_num == 2) {
				return Constants.RM2_AS_SERVER_PORT;
			}
			if (rm_num == 3) {
				return Constants.RM3_AS_SERVER_PORT;
			}
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

class GenerateRequest {
    public String req; 
    public int count; 
          
    public GenerateRequest(String req, int count) { 
      
        this.req = req; 
        this.count = count; 
    }

	/**
	 * @return the req
	 */
	public String getReq() {
		return req;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

}

class MyRMThreads implements Runnable {
	
	private final byte[] msg;
	private final int port;
 
	MyRMThreads(byte[] msg, int port) {
		this.msg = msg;
		this.port = port;
	}
 
	@Override
	public void run() {
 
		try {
			InetAddress ia = InetAddress.getByName(Constants.LOCALHOST);
			new DatagramSocket().send(new DatagramPacket(this.msg, this.msg.length, ia, this.port));
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
}

class RequestComparator implements Comparator<GenerateRequest> { 

	/**
	 *
	 */
	@Override
	public int compare(GenerateRequest prev, GenerateRequest current) {
		
		if (prev.count < current.count) 
			return 1; 
		else if (prev.count > current.count) 
			return -1; 
		return 0;
		
	} 
} 