//References:
//https://www.geeksforgeeks.org/multithreading-in-java/
//https://www.geeksforgeeks.org/synchronized-in-java/
package model;

import java.io.File;
import java.io.FileNotFoundException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import FECorba.FrontendInterface;
import FECorba.FrontendInterfaceHelper;

public class ThreadTesting extends Thread {
	
	public static FrontendInterface conObj;
	public static ORB orb;
	public static NamingContextExt ncRef;
	public static int c = 0;
	
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, InterruptedException, FileNotFoundException {
		
		orb = ORB.init(args, null);
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		ncRef = NamingContextExtHelper.narrow(objRef);
		
		System.out.println("\n--------------------------- Advanced Test Cases ---------------------------\n");
		
		for(int i=0; i<2; i++) {
			new ThreadTesting().start();
		}
					
	}
	
	public static FrontendInterface createAdminObject(String ip) throws NotFound, CannotProceed, InvalidName, org.omg.CosNaming.NamingContextPackage.InvalidName  {
	
			return FrontendInterfaceHelper.narrow(ncRef.resolve_str("FrontEnd"));
	}
	
	public void run() {
		
		try {
			Thread.sleep((long)(Math.random() * 1000));
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : createPlayerAccount() , " + " Username : testuserdata1");
			createAdminObject("182.123.123.123").createPlayerAccount("testdata1", "userdata1", "24", "testuserdata1", "testuserdata1", "182.123.123.123");
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : createPlayerAccount() , " + " Username : testuserdata2");
			createAdminObject("182.123.123.123").createPlayerAccount("testdata2", "userdata2", "24", "testuserdata2", "testuserdata2", "182.123.123.123");
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : createPlayerAccount() , " + " Username : testuserdata3"); 
			createAdminObject("182.123.123.123").createPlayerAccount("testdata3", "userdata3", "24", "testuserdata3", "testuserdata3", "182.123.123.123");
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : suspendAccount(), Username : testuserdata1");
			createAdminObject("182.123.123.123").suspendAccount("Admin", "Admin", "182.123.123.123", "testuserdata1");
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : transferAccount(), Username : testuserdata1");
			createAdminObject("182.123.123.123").transferAccount("testuserdata1", "testuserdata1", "182.123.123.123", "93.123.123.123");
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : transferAccount(), Username : testuserdata2");
			createAdminObject("182.123.123.123").transferAccount("testuserdata2", "testuserdata2", "182.123.123.123", "93.123.123.123");
			c++;
			System.out.println("Req Id " + c + ", Thread " + Thread.currentThread().getId() + " Method : suspendAccount(), Username : testuserdata2");
			createAdminObject("182.123.123.123").suspendAccount("Admin", "Admin", "182.123.123.123", "testuserdata2");
			
		} catch (Exception e) {
			System.out.println("Exception is caught");
		}
	}
	
}