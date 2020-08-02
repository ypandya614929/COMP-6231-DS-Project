//References:
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
package model;

import java.util.Random;

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
	
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, InterruptedException {
		
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
		System.out.println("New IP : 93.123.123.123 (RM1EUServer)\n");

		System.out.println("\nTest 1 : Create player account with 182.123.123.123 (Asian) Server");
		t.create();
		System.out.println("\nTest 2 : Sign in player account in 182.123.123.123 (Asian) Server");
		t.signin();
		System.out.println("\nTest 3 : Get player status");
		t.getplayerstatus();
		System.out.println("\nTest 4 : Sign out player account from 182.123.123.123 (Asian) Server");
		t.signout();
		System.out.println("\nTest 5 : Transfer player account 182.123.123.123 (Asian) to 93.123.123.123 (RM1EUServer) Server");
		t.transferaccount();
		System.out.println("\nTest 6 : Try Sign in player account into old RM1Server (182.123.123.123 Asian) after transferting");
		t.signin();
		System.out.println("\nTest 7 : Sign in player account into new RM1Server (93.123.123.123 RM1EUServer) after transferting");
		t.signinagain();
		System.out.println("\nTest 8 : Get player status");
		t.getplayerstatusagain();
		System.out.println("\nTest 9 : Suspend player account from 93.123.123.123 (RM1EUServer) Server");
		t.suspendaccount();
		System.out.println("\nTest 10 : Try Sign in player account after suspended in 93.123.123.123 (RM1EUServer) Server");
		t.signinagain();
		System.out.println("\nTest 11 : Get player status");
		t.getplayerstatusagain();
		System.out.println();
		
		System.out.println("\n--------------------------- Advanced Test Cases ---------------------------\n");
		System.out.println("Test case detail");
		System.out.println("----------------");
		System.out.println("- I have made 3 threads that run on Asian RM1Server and transfer operations are performed on RM1EUServer RM1Server");
		System.out.println("\n- Each threads try to create 3 different players with username testuserdata1, testuserdata2 and testuserdata3");
		System.out.println("\n- Out of 9 thread calls only 3 will be successful and rest of them contains an error");
		System.out.println("\n- Each threads try to suspend testuserdata1 player account only 1 out of 3 will be successful");
		System.out.println("\n- Each threads try to transfer testuserdata1 player account if its already suspended than none of thread "
				+ "\n  will be successful otherwise only 1 out of 3 will be successful");
		System.out.println("\n- Each threads try to transfer testuserdata2 player account only 1 out of 3 will be successful");
		System.out.println("\n- Each threads try to suspend testuserdata2 player account if its already transfered than none of thread "
				+ "\n  will be successful otherwise only 1 out of 3 will be successful");
		System.out.println("\n- I have made 3 threads that run on Asian RM1Server\n");
		
		
		for (int i=0; i<3; i++) {
			Testing t1 = new Testing();
			t1.start();
		}
		
		Thread.sleep(1200);
		
		System.out.println("\n- testuserdata1 and testuserdata2 is either suspended or transfered depending on operation result,"
				+ "\n however testuserdata3 is always created and offline. result of get player status after all thread execution as below\n");
		System.out.println("\nMethod : getPlayerStatus() , " + createAdminObject("182.123.123.123").getPlayerStatus("Admin", "Admin", "182.123.123.123"));
				
		// removing players after test execution to avoid from errors
		// and no need to restart RM1Server again to perform other actions.
		createAdminObject("182.123.123.123").suspendAccount("Admin", "Admin", "182.123.123.123", "testuserdata3");
		createAdminObject("93.123.123.123").suspendAccount("Admin", "Admin", "93.123.123.123", "testuserdata2");
				
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
	
	public void run() {
		
		try {
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : createPlayerAccount() , " + " Username : testuserdata1 , " + createAdminObject("182.123.123.123").createPlayerAccount("testdata1", "userdata1", "24", "testuserdata1", "testuserdata1", "182.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : createPlayerAccount() , " + " Username : testuserdata2 , " + createAdminObject("182.123.123.123").createPlayerAccount("testdata2", "userdata2", "24", "testuserdata2", "testuserdata2", "182.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : createPlayerAccount() , " + " Username : testuserdata3 , " + createAdminObject("182.123.123.123").createPlayerAccount("testdata3", "userdata3", "24", "testuserdata3", "testuserdata3", "182.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : suspendAccount() , " + createAdminObject("182.123.123.123").suspendAccount("Admin", "Admin", "182.123.123.123", "testuserdata1"));
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : transferAccount() , " + createAdminObject("182.123.123.123").transferAccount("testuserdata1", "testuserdata1", "182.123.123.123", "93.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : transferAccount() , " + createAdminObject("182.123.123.123").transferAccount("testuserdata2", "testuserdata2", "182.123.123.123", "93.123.123.123"));
			System.out.println("Thread " + Thread.currentThread().getId() + " Method : suspendAccount() , " + createAdminObject("182.123.123.123").suspendAccount("Admin", "Admin", "182.123.123.123", "testuserdata2"));
			
		} catch (Exception e) {
			System.out.println("Exception is caught");
		}
	}
	
}