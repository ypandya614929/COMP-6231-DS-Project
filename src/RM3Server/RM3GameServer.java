package RM3Server;

//References:
/**
 *
 * @author ypandya
 */
public class RM3GameServer {
	
	/**
	 * This is the RM1GameServer class
	 */
	
	public RM3GameServer() {
		
		try {
            
            new RM3EUServer();
			new RM3ASServer();
			new RM3NAServer();
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		
	}
	
}