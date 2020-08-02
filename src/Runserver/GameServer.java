package Runserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import RM1Server.RM1GameServer;
import RM2Server.RM2GameServer;
import RM3Server.RM3GameServer;

/**
 * 
 */

/**
 * @author ypandya
 *
 */
public class GameServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		buildLogDirectory("./logs");
		int option = 1;
		
		try {
            
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				System.out.println("\nChoose Leader Replica");
				System.out.println("================================");
				System.out.println("Replica Options : \n");
				System.out.println("1 : Replica 1");
				System.out.println("2 : Replica 2");
				System.out.println("3 : Replica 3");
				System.out.println("4 : Exit\n");
				System.out.print("Select : ");
				String choice = br.readLine().trim();
				if (choice.equals("1")){
					option = 1;
					break;
				}
				else if (choice.equals("2")){
					option = 2;
					break;
				}
				else if (choice.equals("3")){
					option = 3;
					break;
				}
				else if (choice.equals("4")){
					break;
				}
				else {
					System.out.println("===== Please select valid option =====");
					continue;
				}
			}
			
			switch(option) {
				case 1: 
					new RM2GameServer(false);
					new RM3GameServer(false);
					new RM1GameServer(true);
					break;
				case 2:
					new RM1GameServer(false);
					new RM3GameServer(false);
					new RM2GameServer(true);
					break;
				case 3:
					new RM1GameServer(false);
					new RM2GameServer(false);
					new RM3GameServer(true);
					break;
				default :
		            System.out.println("Invalid choice!");
			}
			
			System.out.println("All Servers are started!");
			
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}
	
	/**
	 * This method is used to create logs directory to store the logs
	 * @param path location of the logs folder
	 */
	public static void buildLogDirectory(String path) {
		File outputDir = new File(path);
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}
	}

}
