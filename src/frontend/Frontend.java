/**
 * 
 */
package frontend;

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
import sequencer.Sequencer;

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
		    
		    Runnable t2 = () -> {
				try {
					frontendImplementation.RM2Response();
				} catch (SocketException e) {
					e.printStackTrace();
				}
			};
		    Thread thread2 = new Thread(t2);
		    thread2.start();
		    
		    Runnable t3 = () -> {
				try {
					frontendImplementation.RM3Response();
				} catch (SocketException e) {
					e.printStackTrace();
				}
			};
		    Thread thread3 = new Thread(t3);
		    thread3.start();
			
			new Sequencer().startSequencer();
			
			orb.run();		

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
