package server;

import java.io.File;
import java.net.SocketException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import FECorba.FrontendInterface;
import FECorba.FrontendInterfaceHelper;
import frontend.FrontendImplementation;
import sequencer.Sequencer;

//References:
/**
 *
 * @author ypandya
 */
public class GameServer {
	
	/**
	 * This is the GameServer class
	 */
	
	/**
	 * main method to run all the servers
	 * @param args args for main function
	 */
	public static void main(String args[]) {
		
		buildLogDirectory("./logs");
		
		try {
			
			ORB orb = ORB.init(args, null);

			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			FrontendImplementation frontendImplementation = new FrontendImplementation();
			frontendImplementation.setORB(orb);
			
			org.omg.CORBA.Object refFE = rootpoa.servant_to_reference(frontendImplementation);
			FrontendInterface hrefFE = FrontendInterfaceHelper.narrow(refFE);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			NamingContextExt ncRefFE = NamingContextExtHelper.narrow(objRef);
			NameComponent pathFE[] = ncRefFE.to_name("FrontEnd");
			ncRefFE.rebind(pathFE, hrefFE);
			
			System.out.println("FrontEnd Started.");
			
			Runnable t1 = () -> {
				try {
					frontendImplementation.RM1Response();
				} catch (SocketException e) {
					e.printStackTrace();
				}
			};

            Thread thread1 = new Thread(t1);

            thread1.start();
            
            new EUServer();
			new ASServer();
			new NAServer();
			
			new Sequencer().startSequencer();
			
			orb.run();		
		
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		System.out.println("Server(s) are closed");

	}
					
	/**
	 * This method is used to load the initial player data
	 * @param europe europe controller object 
	 * @param northamerica northamerica controller object 
	 * @param asia asia controller object 
	 */
//	static void loadData(Controller europe, Controller northamerica, Controller asia) {
//		
//		BufferedReader reader;
//		try {
//			reader = new BufferedReader(new FileReader("src/data.txt"));
//			String line = reader.readLine();
//			while (line != null) {
//				String[] listParts = line.split(",");
//				String firstName = listParts[0];
//				String lastName = listParts[1];
//				String age = listParts[2];
//				String userName = listParts[3];
//				String password = listParts[4];
//				String ipAddress = listParts[5];
//				
//				if (ipAddress.startsWith("132")) {
//					northamerica.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
//				} else if (ipAddress.startsWith("93")) {
//					europe.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
//				} else if (ipAddress.startsWith("182")) {
//					asia.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
//				}
//				line = reader.readLine();
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
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