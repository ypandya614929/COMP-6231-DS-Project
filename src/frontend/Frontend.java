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
						
			orb.run();		

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}
	
}
