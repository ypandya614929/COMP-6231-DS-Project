package client;
//References:
//https://systembash.com/a-simple-java-udp-server-and-udp-client/
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
//https://objectcomputing.com/resources/publications/sett/january-2002-corba-and-java-by-don-busch-principal-software-engineer
//http://www.ejbtutorial.com/corba/tutorial-for-corba-hello-world-using-java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CORBA.ORB;

import DPSSCorba.DPSSInterface;
import DPSSCorba.DPSSInterfaceHelper;

/**
 *
 * @author ypandya
 */
public class PlayerClient {

	/**
	 * This is the player user class containing player user operations
	 */

	static DPSSInterface playerObj;
	static BufferedReader br;
	private static Logger logger;
	static NamingContextExt ncRef;
	private static String str2;

	/**
	 * main method to run the player user operations
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
						
		try {
			
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			ncRef = NamingContextExtHelper.narrow(objRef);
			
			br = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				playerObj = null;
				System.out.println("\nDistributed Player Status System");
				System.out.println("================================");
				System.out.println("Player Options : \n");
				System.out.println("1 : Create Player Account");
				System.out.println("2 : Sign in");
				System.out.println("3 : Sign out");
				System.out.println("4 : Transfer Account");
				System.out.println("5 : Exit\n");
				System.out.print("Select : ");
				String choice = br.readLine().trim();
				if (choice.equals("1")){
					createAccount();
				}
				else if (choice.equals("2")){
					signIn();
				}
				else if (choice.equals("3")){
					signOut();
				}
				else if (choice.equals("4")){
					transferAccount();
				}
				else if (choice.equals("5")){
					break;
				}
				else {
					System.out.println("===== Please select valid option =====");
					continue;
				}
			}
		} catch (Exception e) {
			System.out.println("Something went wrong while starting player");
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
	
	/**
	 * This method is used to check user wants to re-enter missing input or wants to quit
	 * @return boolean true if user wants to exit, false otherwise
	 * @throws IOException
	 */
	public static boolean exitCheck() throws IOException {
		boolean is_return = false;
		while (true) {
			System.out.println("Please select below options : ");
			System.out.println("1 : Do you want to re-enter");
			System.out.println("2 : exit");
			System.out.print("Select : ");
			String str = br.readLine().trim();
			if (str.equals("1")) {
				is_return = false;
				break;
			}
			else if (str.equals("exit") || str.equals("2")) {
				is_return = true;
				break;
			}
			else {
				System.out.println("===== Please select valid option =====");
				is_return = false;
				continue;
			}
		}
		return is_return;
	}
	
	/**
	 * This method is used to check the age is integer or not
	 * @param str
	 * @return boolean true if string is integer, false otherwise
	 */
	public static boolean isNumeric(String str) { 
		str2 = str;
		if(str2 == null || str2.trim().isEmpty()) {
            return false;
		}
		try {  
		    Integer.parseInt(str2);
		    return true;
		} catch(NumberFormatException e){  
		    return false;  
		}
	}
	
	/**
	 * This method is used to check the username validations
	 * @param str
	 * @return boolean true if username is valid, false otherwise
	 */
	public static boolean isUserNameCheck(String str) { 
		str2 = str;
		if(str2 == null || str2.trim().isEmpty()) {
            return false;
		}
		else {
			if (str2.length()<6 || str2.length()>15) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * This method is used to check the password validations
	 * @param str
	 * @return boolean true if password is valid, false otherwise
	 */
	public static boolean isPasswordCheck(String str) { 
		str2 = str;
		if(str2 == null || str2.trim().isEmpty()) {
            return false;
		}
		else {
			if (str2.length()<6) {
				return false;
			}
			return true;
		}
	}
	
	/**
	 * This method is used to check the ip is valid or not
	 * @param ip
	 * @return boolean true if ip is valid, false otherwise
	 */
	public static boolean ipCheck(String ip) {
		if (ip == null || ip.equals("")) {
			return false;
		}
		if (ip.startsWith("132") || ip.startsWith("182")) {
			if (ip.length()==15) {
				if ((ip.substring(3,4).equals(".")) && (ip.substring(7,8).equals(".")) && (ip.substring(11, 12).equals("."))) {
					return true;
				}
			}
		}
		else if (ip.startsWith("93")) {
			if (ip.length()==14) {
				if ((ip.substring(2,3).equals(".")) && (ip.substring(6,7).equals(".")) && (ip.substring(10, 11).equals("."))) { 
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method is used to get input to create player account
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static void createAccount() throws IOException, InterruptedException, NotFound, CannotProceed, InvalidName{
		boolean is_info_collected = false;
		String firstname = "";
		String lastname = "";
		String age = "";
		String username = "";
		String password = "";
		String ip = "";
		boolean returnMenu = false;
		while (!returnMenu) {
			if (!is_info_collected) {
				while (isNullOrEmpty(firstname) && !is_info_collected) {
					System.out.print("Enter firstname : ");
					firstname = br.readLine().trim();
					if (isNullOrEmpty(firstname)) {
						System.out.println("===== The firstname can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (isNullOrEmpty(lastname) && !is_info_collected) {
					System.out.print("Enter lastname : ");
					lastname = br.readLine().trim();
					if (isNullOrEmpty(lastname)) {
						System.out.println("===== The lastname can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!isNumeric(age) && !is_info_collected) {
					System.out.print("Enter age : ");
					age = br.readLine().trim();
					if (!isNumeric(age)) {
						System.out.println("===== The age must be an integer. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!isUserNameCheck(username) && !is_info_collected) {
					System.out.print("Enter username : ");
					username = br.readLine().trim();
					if (!isUserNameCheck(username)) {
						System.out.println("===== The username must be within 6 to 15 characters. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!isPasswordCheck(password) && !is_info_collected) {
					System.out.print("Enter password : ");
					password = br.readLine().trim();
					if (!isPasswordCheck(password)) {
						System.out.println("===== The password must be more than 6 characters. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!ipCheck(ip) && !is_info_collected) {
					System.out.print("Enter ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
					ip = br.readLine().trim();
					if (ipCheck(ip)) {
						returnMenu = true;
						break;
					}
					else {
						System.out.println("===== The ip must be in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX. =====");
						if (exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!isNullOrEmpty(username) && !isNullOrEmpty(password) && ipCheck(ip) && !isNullOrEmpty(age) && !isNullOrEmpty(firstname) && !isNullOrEmpty(lastname)) {
				is_info_collected = true;
			}
		}
		if (is_info_collected && ip!=null) {
			addLog("logs/" + username + ".txt", username);
			logger.info("IP : " + ip + ", username : " + username + ", start createPlayerAccount() operation.");
			playerObj = createPlayerObject();
			String response = playerObj.createPlayerAccount(firstname, lastname, age, username, password, ip);
			logger.info("IP : " + ip + ", username : " + username + ", Result createPlayerAccount() : " + response);
			System.out.println(response);
		}
	}
	
	/**
	 * This method is used to get input to sign in the player
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static void signIn() throws IOException, InterruptedException, NotFound, CannotProceed, InvalidName{
		boolean is_info_collected = false;
		String username = "";
		String password = "";
		String ip = "";
		boolean returnMenu = false;
		while (!returnMenu) {
			if (!returnMenu) {
				while (isNullOrEmpty(username)) {
					System.out.print("Enter username : ");
					username = br.readLine().trim();
					if (isNullOrEmpty(username)) {
						System.out.println("===== The username can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (isNullOrEmpty(password)) {
					System.out.print("Enter password : ");
					password = br.readLine().trim();
					if (isNullOrEmpty(password)) {
						System.out.println("===== The password can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!ipCheck(ip) && !is_info_collected) {
					System.out.print("Enter ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
					ip = br.readLine().trim();
					if (ipCheck(ip)) {
						returnMenu = true;
						break;
					}
					else {
						System.out.print("Enter ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
						if (exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!isNullOrEmpty(username) && !isNullOrEmpty(password) && ipCheck(ip)) {
				is_info_collected = true;
			}
		}
		if (is_info_collected && ip!=null) {
			addLog("logs/" + username + ".txt", username);
			logger.info("IP : " + ip + ", username : " + username + ", start playerSignIn() operation.");
			playerObj = createPlayerObject();
			String response = playerObj.playerSignIn(username, password, ip);
			logger.info("IP : " + ip + ", username : " + username + ", Result playerSignIn() : " + response);
			System.out.println(response);
		}
	}
	
	/**
	 * This method is used to get input to sign out the player
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static void signOut() throws IOException, InterruptedException, NotFound, CannotProceed, InvalidName{
		boolean is_info_collected = false;
		String username = "";
		String ip = "";
		boolean returnMenu = false;
		while (!returnMenu) {
			if (!returnMenu) {
				while (isNullOrEmpty(username)) {
					System.out.print("Enter username : ");
					username = br.readLine().trim();
					if (isNullOrEmpty(username)) {
						System.out.println("===== The username can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!ipCheck(ip) && !is_info_collected) {
					System.out.print("Enter ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
					ip = br.readLine().trim();
					if (ipCheck(ip)) {
						returnMenu = true;
						break;
					}
					else {
						System.out.print("Enter ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
						if (exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!isNullOrEmpty(username) && ipCheck(ip)) {
				is_info_collected = true;
			}
		}
		if (is_info_collected && ip!=null) {
			addLog("logs/" + username + ".txt", username);
			logger.info("IP : " + ip + ", username : " + username + ", start playerSignOut() operation.");
			playerObj = createPlayerObject();
			String response = playerObj.playerSignOut(username, ip);
			logger.info("IP : " + ip + ", username : " + username + ", Result playerSignOut() : " + response);
			System.out.println(response);
		}
	}
	
	/**
	 * This method is used to get input to transfer player account
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static void transferAccount() throws IOException, InterruptedException, NotFound, CannotProceed, InvalidName{
		boolean is_info_collected = false;
		String username = "";
		String password = "";
		String ip = "";
		String newip = "";
		boolean returnMenu = false;
		while (!returnMenu) {
			if (!returnMenu) {
				while (isNullOrEmpty(username)) {
					System.out.print("Enter username : ");
					username = br.readLine().trim();
					if (isNullOrEmpty(username)) {
						System.out.println("===== The username can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (isNullOrEmpty(password)) {
					System.out.print("Enter password : ");
					password = br.readLine().trim();
					if (isNullOrEmpty(password)) {
						System.out.println("===== The password can't be empty. =====");
						if(exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!ipCheck(ip) && !is_info_collected) {
					System.out.print("Enter Old ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
					ip = br.readLine().trim();
					if (ipCheck(ip)) {
						break;
					}
					else {
						System.out.print("Enter Old ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
						if (exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!returnMenu) {
				while (!ipCheck(newip) && !is_info_collected) {
					System.out.print("Enter New ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
					newip = br.readLine().trim();
					if (ipCheck(newip)) {
						returnMenu = true;
						break;
					}
					else {
						System.out.print("Enter New ip-address in following format 132.XXX.XXX.XXX or 93.XXX.XXX.XXX or 182.XXX.XXX.XXX : ");
						if (exitCheck()) {
							returnMenu = true;
							break;
						}
					}
				}
			}
			if (!isNullOrEmpty(username) && !isNullOrEmpty(password) && ipCheck(ip) && ipCheck(newip)) {
				if(ip.substring(0, 2).equals(newip.substring(0, 2))) {
					System.out.println("===== The player can't be transfered within same server. =====");
				} else {
					is_info_collected = true;
				}
			}
		}
		if (is_info_collected && ip!=null) {
			addLog("logs/" + username + ".txt", username);
			logger.info("IP : " + ip + ", username : " + username + ", start transferAccount() operation.");
			playerObj = createPlayerObject();
			String response = playerObj.transferAccount(username, password, ip, newip);
			logger.info("IP : " + ip + ", username : " + username + ", Result transferAccount() : " + response);
			System.out.println(response);
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

	
	/**
	 * This method is used to set the server object based on the ip
	 * @param ip ip address
	 * @throws InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public static DPSSInterface createPlayerObject() throws NotFound, CannotProceed, InvalidName {
		return DPSSInterfaceHelper.narrow(ncRef.resolve_str("FrontEnd"));
	}
}
	
