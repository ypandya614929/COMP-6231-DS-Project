//References:
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
package model;

import java.io.FileNotFoundException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import FECorba.FrontendInterface;
import FECorba.FrontendInterfaceHelper;

public class Testing extends Thread {
	
	public static FrontendInterface conObj;
	public static ORB orb;
	public static NamingContextExt ncRef;
	public static int c = 0;
	
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, InterruptedException, FileNotFoundException {
		
		orb = ORB.init(args, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		ncRef = NamingContextExtHelper.narrow(objRef);

		Testing t = new Testing();
		
		System.out.println("--------------------------- Basic Test Cases ---------------------------\n");
		System.out.println("Test case detail");
		System.out.println("----------------");
		System.out.println("Username : testuserdata1");
		System.out.println("Password : testuserdata1");
		System.out.println("Old IP : 182.123.123.123 (Asian)");
		System.out.println("New IP : 93.123.123.123 (Europe)\n");

		System.out.println("\nTest 1 : Create player account with 182.123.123.123 (Asian) Server");
		t.create();
		System.out.println("\nTest 2 : Sign in player account in 182.123.123.123 (Asian) Server");
		t.signin();
		System.out.println("\nTest 3 : Get player status");
		t.getplayerstatus();
		System.out.println("\nTest 4 : Sign out player account from 182.123.123.123 (Asian) Server");
		t.signout();
		System.out.println("\nTest 5 : Transfer player account 182.123.123.123 (Asian) to 93.123.123.123 (Europe) Server");
		t.transferaccount();
		System.out.println("\nTest 6 : Try Sign in player account into old Server (182.123.123.123 Asian) after transferting");
		t.signin();
		System.out.println("\nTest 7 : Sign in player account into new Server (93.123.123.123 Europe) after transferting");
		t.signinagain();
		System.out.println("\nTest 8 : Get player status");
		t.getplayerstatusagain();
		System.out.println("\nTest 9 : Suspend player account from 93.123.123.123 (Europe) Server");
		t.suspendaccount();
		System.out.println("\nTest 10 : Try Sign in player account after suspended in 93.123.123.123 (Europe) Server");
		t.signinagain();
		System.out.println("\nTest 11 : Get player status");
		t.getplayerstatusagain();
		System.out.println();
					
	}
	
	public void create() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("182.123.123.123");
		System.out.println("Method : createPlayerAccount() , " + conObj.createPlayerAccount("testdata1", "userdata1", "24", "testuserdata1", "testuserdata1", "182.123.123.123"));
	}

	public void signin() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("182.123.123.123");
		System.out.println("Method : playerSignIn() , " + conObj.playerSignIn("testuserdata1", "testuserdata1", "182.123.123.123"));
	}
	
	public void getplayerstatus() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("182.123.123.123");
		System.out.println("Method : getPlayerStatus() , " + conObj.getPlayerStatus("Admin", "Admin", "182.123.123.123"));
	}
	
	public void signout() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("182.123.123.123");
		System.out.println("Method : playerSignOut() , " + conObj.playerSignOut("testuserdata1", "182.123.123.123"));
	}
	
	public void transferaccount() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("182.123.123.123");
		System.out.println("Method : transferAccount() , " + conObj.transferAccount("testuserdata1", "testuserdata1", "182.123.123.123", "93.123.123.123"));
	}
	
	public void signinagain() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("93.123.123.123");
		System.out.println("Method : playerSignIn() , " + conObj.playerSignIn("testuserdata1", "testuserdata1", "93.123.123.123"));
	}
	
	public void getplayerstatusagain() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("93.123.123.123");
		System.out.println("Method : getPlayerStatus() , " + conObj.getPlayerStatus("Admin", "Admin", "93.123.123.123"));
	}
	
	public void suspendaccount() throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName {
		conObj = createAdminObject("93.123.123.123");
		System.out.println("Method : suspendAccount() , " + conObj.suspendAccount("Admin", "Admin", "93.123.123.123", "testuserdata1"));
	}
	
	public static FrontendInterface createAdminObject(String ip) throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName  {
	
			return FrontendInterfaceHelper.narrow(ncRef.resolve_str("FrontEnd"));
	}
	
}