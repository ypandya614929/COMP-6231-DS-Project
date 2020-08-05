/**
 * 
 */
package frontend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import FECorba.FrontendInterface;
import FECorba.FrontendInterfaceHelper;
import constants.Constants;

/**
 * @author ypandya
 *
 */
public class Frontend {
	
	/**
	 * main method to run all the servers
	 * @param args args for main function
	 */
	public static void main(String args[]) {
		
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
						
			Runnable t1 = () -> {
				frontendImplementation.serverConnection(Constants.FRONTEND_RESPONSE_PORT);
			};
		    Thread thread1 = new Thread(t1);
		    thread1.start();
						
//		    loadData(args);
		    
			orb.run();		

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}
	
	/**
	 * This method is used to load the initial player data
	 * @throws InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws org.omg.CORBA.ORBPackage.InvalidName 
	 */
	static void loadData(String args[]) throws NotFound, CannotProceed, InvalidName, org.omg.CORBA.ORBPackage.InvalidName {
		
		BufferedReader reader;
		try {
			
			FrontendInterface feObj;
			NamingContextExt ncRef;
			
			ORB orb = ORB.init(args, null);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			ncRef = NamingContextExtHelper.narrow(objRef);
			
			feObj = FrontendInterfaceHelper.narrow(ncRef.resolve_str("FrontEnd"));
			
			reader = new BufferedReader(new FileReader(Constants.DATA_FILE_PATH));
			String line = reader.readLine();
			while (line != null) {
				String[] listParts = line.split(",");
				String firstName = listParts[0];
				String lastName = listParts[1];
				String age = listParts[2];
				String userName = listParts[3];
				String password = listParts[4];
				String ipAddress = listParts[5];
				
				
				feObj.createPlayerAccount(firstName, lastName, age, userName, password, ipAddress);
				
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
